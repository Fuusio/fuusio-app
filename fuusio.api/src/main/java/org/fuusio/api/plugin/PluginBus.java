/*
 * Copyright (C) 2000-2014 Marko Salmela.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fuusio.api.plugin;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class PluginBus {

	private static PluginBus sInstance = null;
    private static PluginInjector sGlobalInjector = null;

	private final HashMap<Class<? extends PluginInterface>, HashMap<String, PluginInvocationHandler>> mInvocationHandlers;
    // OPTION private final HashMap<Class<? extends PluginInterface>, HashMap<String, PlugInvocationHandler>> mSyncedInvocationHandlers;
    private final List<Plugin> mPlugins;

	private PluginBus() {
		mInvocationHandlers = new HashMap<>();
        // OPTION mSyncedInvocationHandlers = new HashMap<>();
        mPlugins = new ArrayList<>();
	}

    @SuppressWarnings("unused")
    public static void setGlobalInjector(final PluginInjector pInjector) {
        sGlobalInjector = pInjector;
    }

    @SuppressWarnings("unused")
    public <T extends Plugin> List<T> getPlugins(final Class<? extends PluginInterface> pPluginInterface, final String pPlugName) {
        PluginInvocationHandler handler = null;

        final HashMap<String,PluginInvocationHandler> handlers = mInvocationHandlers.get(pPluginInterface);

        if (handlers != null) {
            handler = handlers.get(pPlugName);
        }

        if (handler != null) {
            return handler.getPlugins();
        }

        return new ArrayList<>();
    }

	public static PluginBus getInstance() {

        if (sInstance == null) {
            sInstance = new PluginBus();
        }
		return sInstance;
	}

    public static <T extends PluginInterface> T getPlug(final Class<T> pPluginInterface) {
        return sInstance.doGetPlug(pPluginInterface, Plugin.DEFAULT_PLUG_NAME);
    }

    public static <T extends PluginInterface> T getPlug(final Class<T> pPluginInterface, final String pPlugName) {
        return sInstance.doGetPlug(pPluginInterface, pPlugName);
    }

    @SuppressWarnings("unchecked")
    public <T extends PluginInterface> T doGetPlug(final Class<T> pPluginInterface, final String pPlugName) {

        final ClassLoader classLoader = pPluginInterface.getClassLoader();
        HashMap<String,PluginInvocationHandler> handlers = mInvocationHandlers.get(pPluginInterface);

        if (handlers == null) {
            handlers = new HashMap<>();
            mInvocationHandlers.put(pPluginInterface, handlers);
        }

        PluginInvocationHandler handler = handlers.get(pPlugName);
        Proxy proxy;

        if (handler == null) {
            handler = new PluginInvocationHandler(pPluginInterface, this);
            handlers.put(pPlugName, handler);

            final Class<?>[] interfaceClasses = {pPluginInterface};
            proxy = (Proxy)Proxy.newProxyInstance(classLoader, interfaceClasses, handler);
            handler.setProxy(proxy);
        } else {
            proxy = handler.getProxy();
        }

        return (T)proxy;
    }

    public static void plug(final Plugin pPlugin) {
        sInstance.doPlug(pPlugin, true);
    }

    public static void plug(final Plugin pPlugin, final boolean pUseAnnotations) {
        sInstance.doPlug(pPlugin, pUseAnnotations);
    }

	private void doPlug(final Plugin pPlugin, final boolean pUseAnnotations) {

        if (mPlugins.contains(pPlugin)) {
            return;
        }

        final Class<? extends Plugin> pluginClass = pPlugin.getClass();
		final ClassLoader classLoader = pluginClass.getClassLoader();
        final PluginInjector injector = (sGlobalInjector != null) ? sGlobalInjector : getInjector(pPlugin);

        if (injector != null) {
            injector.onPlug(pPlugin);
        }

		final List<Class<? extends PluginInterface>> pluginInterfaces = getPluginInterfaces(pPlugin);

        if (pUseAnnotations) {
            final List<PlugDescriptor> descriptors = getPlugDescriptors(pPlugin);

            for (final PlugDescriptor descriptor : descriptors) {
                final String plugName = descriptor.getName();
                final Class<? extends PluginInterface> pluginInterface = descriptor.getPluginInterface();

                HashMap<String, PluginInvocationHandler> handlers = mInvocationHandlers.get(pluginInterface);

                if (handlers == null) {
                    handlers = new HashMap<>();
                    mInvocationHandlers.put(pluginInterface, handlers);
                }

                PluginInvocationHandler handler = handlers.get(plugName);
                Proxy proxy;

                if (handler == null) {
                    handler = new PluginInvocationHandler(pluginInterface, this);
                    handlers.put(plugName, handler);

                    final Class<?>[] interfaceClasses = {pluginInterface};
                    proxy = (Proxy) Proxy.newProxyInstance(classLoader, interfaceClasses, handler);
                    handler.setProxy(proxy);
                } else {
                    proxy = handler.getProxy();
                }

                final Field field = descriptor.getField();

                try {
                    field.setAccessible(true);
                    field.set(pPlugin, proxy);
                } catch (final Exception e) {
                    throw new RuntimeException("Failed to assign to plugin field: " + field.getName());
                }
            }
        }
		
		for (final Class<? extends PluginInterface> pluginInterface : pluginInterfaces) {
			final String plugName = pPlugin.getPlugName();
			final PlugDescriptor descriptor = new PlugDescriptor(null, plugName, pluginInterface);
            descriptor.setCreated(false);

			HashMap<String,PluginInvocationHandler> handlers = mInvocationHandlers.get(pluginInterface);

            if (handlers == null) {
                handlers = new HashMap<>();
                mInvocationHandlers.put(pluginInterface, handlers);
            }

            PluginInvocationHandler handler = handlers.get(plugName);
            Proxy proxy;

            if (handler == null) {
				handler = new PluginInvocationHandler(pluginInterface, this);
                handlers.put(plugName, handler);

				final Class<?>[] interfaceClasses = {pluginInterface};
				proxy = (Proxy)Proxy.newProxyInstance(classLoader, interfaceClasses, handler);
                handler.setProxy(proxy);
			}
			
			handler.plug(pPlugin);
		}

        mPlugins.add(pPlugin);
        pPlugin.onPlugged(this);
	}

    private PluginInjector getInjector(final Plugin pPlugin) {

        if (pPlugin instanceof InjectorProvider) {
            return ((InjectorProvider)pPlugin).getInjector();
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
	private List<PlugDescriptor> getPlugDescriptors(final Plugin pPlugin) {
		final Class<?> pluginClass = pPlugin.getClass();
		final List<PlugDescriptor> fields = new ArrayList<>();

        collectPlugFields(pluginClass, fields);

		return fields;
	}

    @SuppressWarnings("unchecked")
    private void collectPlugFields(final Class<?> pClass, final List<PlugDescriptor> pDescriptors) {

        if (Plugin.class.isAssignableFrom(pClass)) {
            final Field[] fields = pClass.getDeclaredFields();

            for (final Field field : fields) {
                final Plugin.Plug plug = field.getAnnotation(Plugin.Plug.class);

                if (plug != null) {
                    final String plugName = plug.name();
                    final Class<? extends PluginInterface> fieldType = (Class<? extends PluginInterface>) field.getType();
                    final PlugDescriptor descriptor = new PlugDescriptor(field, plugName, fieldType);
                    descriptor.setCreated(plug.create());
                    pDescriptors.add(descriptor);
                }
            }

            collectPlugFields(pClass.getSuperclass(), pDescriptors);
        }
    }

    public static void unplug(final Plugin pPlugin) {
        sInstance.doUnplug(pPlugin);
    }

	@SuppressWarnings("static-access")
	private void doUnplug(final Plugin pPlugin) {

        if (!mPlugins.contains(pPlugin)) {
            return;
        }

        final PluginInjector injector = (sGlobalInjector != null) ? sGlobalInjector : getInjector(pPlugin);

        if (injector != null) {
            injector.onUnplug(pPlugin);
        }

		final List<Class<? extends PluginInterface>> pluginInterfaces = getPluginInterfaces(pPlugin);
		final List<PlugDescriptor> descriptors = getPlugDescriptors(pPlugin);
		final List<PluginInvocationHandler> removedHandlers = new ArrayList<>();

		for (final Class<? extends PluginInterface> pluginInterface : pluginInterfaces) {

            final Collection<PluginInvocationHandler> handlers = mInvocationHandlers.get(pluginInterface).values();

			for (final PluginInvocationHandler handler : handlers) {
				handler.unplug(pPlugin);

                if (handler.getPluginCount() == 0) {
                    removedHandlers.add(handler);
                }
			}
		}
		
		for (final PlugDescriptor descriptor : descriptors) {
            Field field;

            try {
				field = descriptor.getField();
				field.setAccessible(true);
				field.set(pPlugin, null);
			} catch (final Exception pException) {
                throw new RuntimeException("Failed to assign to plugin field.");
			}
		}

        for (final PluginInvocationHandler handler : removedHandlers) {
            removeInvocationHandler(handler);
        }

        mPlugins.remove(pPlugin);
        pPlugin.onUnplugged(this);
	}

	private List<Class<? extends PluginInterface>> getPluginInterfaces(final Plugin pPlugin) {
		final Class<?> pluginClass = pPlugin.getClass();
		final List<Class<? extends PluginInterface>> pluginInterfaces = new ArrayList<>();
        collectPluginInterfaces(pluginClass, pluginInterfaces);
		return pluginInterfaces;
	}
	
	@SuppressWarnings("unchecked")
	private void collectPluginInterfaces(final Class<?> pPluginClass, List<Class<? extends PluginInterface>> pPluginInterfaces) {
		
		final Class<?>[] implementedInterfaces = pPluginClass.getInterfaces();

		for (final Class<?> implementedInterface : implementedInterfaces) {
			if (PluginInterface.class.isAssignableFrom(implementedInterface)) {
				if (!pPluginInterfaces.contains(implementedInterface)) {
					pPluginInterfaces.add((Class<? extends PluginInterface>)implementedInterface);
				}
			}
		}
		
		final Class<?> superClass = pPluginClass.getSuperclass();
		
		if (!Object.class.equals(superClass)) {
			collectPluginInterfaces(superClass, pPluginInterfaces);
		}
	}
	
	public void removeInvocationHandler(final PluginInvocationHandler pHandler) {
		final HashMap<String,PluginInvocationHandler> handlers = mInvocationHandlers.get(pHandler.getPluginInterface());

        for (final String key : handlers.keySet()) {
            final PluginInvocationHandler handler = handlers.get(key);

            if (handler == pHandler) {
                handlers.remove(key);
                break;
            }
        }
	}
	
}
