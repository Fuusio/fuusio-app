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
    public static void setGlobalInjector(final PluginInjector injector) {
        sGlobalInjector = injector;
    }

    @SuppressWarnings("unused")
    public <T extends Plugin> List<T> getPlugins(final Class<? extends PluginInterface> pluginInterface, final String plugName) {
        PluginInvocationHandler handler = null;

        final HashMap<String, PluginInvocationHandler> handlers = mInvocationHandlers.get(pluginInterface);

        if (handlers != null) {
            handler = handlers.get(plugName);
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

    public static <T extends PluginInterface> T getPlug(final Class<T> pluginInterface) {
        return sInstance.doGetPlug(pluginInterface, Plugin.DEFAULT_PLUG_NAME);
    }

    public static <T extends PluginInterface> T getPlug(final Class<T> pluginInterface, final String plugName) {
        return sInstance.doGetPlug(pluginInterface, plugName);
    }

    @SuppressWarnings("unchecked")
    public <T extends PluginInterface> T doGetPlug(final Class<T> pluginInterface, final String plugName) {

        final ClassLoader classLoader = pluginInterface.getClassLoader();
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

        return (T) proxy;
    }

    public static void plug(final Plugin plugin) {
        sInstance.doPlug(plugin, true);
    }

    public static void plug(final Plugin plugin, final boolean useAnnotations) {
        sInstance.doPlug(plugin, useAnnotations);
    }

    private void doPlug(final Plugin plugin, final boolean useAnnotations) {

        if (mPlugins.contains(plugin)) {
            return;
        }

        final Class<? extends Plugin> pluginClass = plugin.getClass();
        final ClassLoader classLoader = pluginClass.getClassLoader();
        final PluginInjector injector = (sGlobalInjector != null) ? sGlobalInjector : getInjector(plugin);

        if (injector != null) {
            injector.onPlug(plugin);
        }

        final List<Class<? extends PluginInterface>> pluginInterfaces = getPluginInterfaces(plugin);

        if (useAnnotations) {
            final List<PlugDescriptor> descriptors = getPlugDescriptors(plugin);

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
                    field.set(plugin, proxy);
                } catch (final Exception e) {
                    throw new RuntimeException("Failed to assign to plugin field: " + field.getName());
                }
            }
        }

        for (final Class<? extends PluginInterface> pluginInterface : pluginInterfaces) {
            final String plugName = plugin.getPlugName();
            final PlugDescriptor descriptor = new PlugDescriptor(null, plugName, pluginInterface);
            descriptor.setCreated(false);

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
            }

            handler.plug(plugin);
        }

        mPlugins.add(plugin);
        plugin.onPlugged(this);
    }

    private PluginInjector getInjector(final Plugin plugin) {

        if (plugin instanceof InjectorProvider) {
            return ((InjectorProvider) plugin).getInjector();
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private List<PlugDescriptor> getPlugDescriptors(final Plugin plugin) {
        final Class<?> pluginClass = plugin.getClass();
        final List<PlugDescriptor> fields = new ArrayList<>();

        collectPlugFields(pluginClass, fields);

        return fields;
    }

    @SuppressWarnings("unchecked")
    private void collectPlugFields(final Class<?> plugClass, final List<PlugDescriptor> descriptors) {

        if (Plugin.class.isAssignableFrom(plugClass)) {
            final Field[] fields = plugClass.getDeclaredFields();

            for (final Field field : fields) {
                final Plugin.Plug plug = field.getAnnotation(Plugin.Plug.class);

                if (plug != null) {
                    final String plugName = plug.name();
                    final Class<? extends PluginInterface> fieldType = (Class<? extends PluginInterface>) field.getType();
                    final PlugDescriptor descriptor = new PlugDescriptor(field, plugName, fieldType);
                    descriptor.setCreated(plug.create());
                    descriptors.add(descriptor);
                }
            }

            collectPlugFields(plugClass.getSuperclass(), descriptors);
        }
    }

    public static void unplug(final Plugin plugin) {
        sInstance.doUnplug(plugin);
    }

    @SuppressWarnings("static-access")
    private void doUnplug(final Plugin plugin) {

        if (!mPlugins.contains(plugin)) {
            return;
        }

        final PluginInjector injector = (sGlobalInjector != null) ? sGlobalInjector : getInjector(plugin);

        if (injector != null) {
            injector.onUnplug(plugin);
        }

        final List<Class<? extends PluginInterface>> pluginInterfaces = getPluginInterfaces(plugin);
        final List<PlugDescriptor> descriptors = getPlugDescriptors(plugin);
        final List<PluginInvocationHandler> removedHandlers = new ArrayList<>();

        for (final Class<? extends PluginInterface> pluginInterface : pluginInterfaces) {

            final Collection<PluginInvocationHandler> handlers = mInvocationHandlers.get(pluginInterface).values();

            for (final PluginInvocationHandler handler : handlers) {
                handler.unplug(plugin);

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
                field.set(plugin, null);
            } catch (final Exception pException) {
                throw new RuntimeException("Failed to assign to plugin field.");
            }
        }

        for (final PluginInvocationHandler handler : removedHandlers) {
            removeInvocationHandler(handler);
        }

        mPlugins.remove(plugin);
        plugin.onUnplugged(this);
    }

    private List<Class<? extends PluginInterface>> getPluginInterfaces(final Plugin plugin) {
        final Class<?> pluginClass = plugin.getClass();
        final List<Class<? extends PluginInterface>> pluginInterfaces = new ArrayList<>();
        collectPluginInterfaces(pluginClass, pluginInterfaces);
        return pluginInterfaces;
    }

    @SuppressWarnings("unchecked")
    private void collectPluginInterfaces(final Class<?> pluginClass, List<Class<? extends PluginInterface>> pluginInterfaces) {

        final Class<?>[] implementedInterfaces = pluginClass.getInterfaces();

        for (final Class<?> implementedInterface : implementedInterfaces) {
            if (PluginInterface.class.isAssignableFrom(implementedInterface)) {
                if (!pluginInterfaces.contains(implementedInterface)) {
                    pluginInterfaces.add((Class<? extends PluginInterface>) implementedInterface);
                }
            }
        }

        final Class<?> superClass = pluginClass.getSuperclass();

        if (!Object.class.equals(superClass)) {
            collectPluginInterfaces(superClass, pluginInterfaces);
        }
    }

    public void removeInvocationHandler(final PluginInvocationHandler removedHandler) {
        final HashMap<String, PluginInvocationHandler> handlers = mInvocationHandlers.get(removedHandler.getPluginInterface());

        for (final String key : handlers.keySet()) {
            final PluginInvocationHandler handler = handlers.get(key);

            if (handler == removedHandler) {
                handlers.remove(key);
                break;
            }
        }
    }

}
