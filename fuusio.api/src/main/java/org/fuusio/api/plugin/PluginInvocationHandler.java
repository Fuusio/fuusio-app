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

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link PluginInvocationHandler} implements an {@link InvocationHandler} for delegating invocations
 * on an interface derived from {@link PluginInterface} to number of {@link Plugin}s implementing
 * the interface.
 */
public class PluginInvocationHandler implements InvocationHandler {

    protected final Looper mMainLooper;
    protected final ArrayList<Plugin> mPlugins;
    protected final PluginBus mPluginBus;
    protected final Class<? extends PluginInterface> mPluginInterface;

    protected Handler mHandler;
    protected int mPluginCount;
    protected Proxy mProxy;

    public PluginInvocationHandler(final Class<? extends PluginInterface> pluginInterface, final PluginBus pluginBus) {
        mMainLooper = Looper.getMainLooper();
        mPluginInterface = pluginInterface;
        mPluginBus = pluginBus;
        mPlugins = new ArrayList<>();
        mPluginCount = 0;
    }

    public final int getPluginCount() {
        return mPluginCount;
    }

    public final Class<? extends PluginInterface> getPluginInterface() {
        return mPluginInterface;
    }

    @SuppressWarnings("unchecked")
    public final <T extends Plugin> List<T> getPlugins() {
        final List<T> plugins = new ArrayList<>();

        for (final Plugin plugin : plugins) {
            plugins.add((T) plugin);
        }
        return plugins;
    }

    public final Proxy getProxy() {
        return mProxy;
    }

    public void setProxy(final Proxy proxy) {
        mProxy = proxy;
    }

    @Override
    public Object invoke(final Object proxy, final Method pMethod, final Object[] args)
            throws Throwable {

        Object returnValue = null;

        if (Looper.myLooper() == mMainLooper) {
            for (int i = mPluginCount - 1; i >= 0; i--) {
                try {
                    returnValue = pMethod.invoke(mPlugins.get(i), args);
                } catch (final Exception e) {
                    throw new RuntimeException("Failed to invoke method: " + pMethod.getName() + ". Reason: " + e.getMessage());
                }
            }
        } else {
            if (mHandler == null) {
                mHandler = new Handler(mMainLooper);
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    for (int i = mPluginCount - 1; i >= 0; i--) {
                        try {
                            pMethod.invoke(mPlugins.get(i), args);
                        } catch (final Exception e) {
                            throw new RuntimeException("Failed to invoke method: " + pMethod.getName() + ". Reason: " + e.getMessage());
                        }
                    }
                }
            });
        }
        return returnValue;
    }

    public void plug(final Plugin plugin) {
        if (!mPlugins.contains(plugin)) {
            mPlugins.add(plugin);
            mPluginCount = mPlugins.size();
        }
    }

    public void unplug(final Plugin plugin) {
        if (mPlugins.contains(plugin)) {
            mPlugins.remove(plugin);
            mPluginCount = mPlugins.size();
        }
    }
}
