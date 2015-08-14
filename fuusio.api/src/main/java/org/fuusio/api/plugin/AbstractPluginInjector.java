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

public abstract class AbstractPluginInjector implements PluginInjector {

    private Plugin[] mPlugins;

    protected abstract Plugin[] getPlugins();

    @Override
    public void onPlug(final Plugin pPlugin) {

        mPlugins = getPlugins();

        if (mPlugins != null) {
            final int count = mPlugins.length;

            for (int i = count - 1; i >= 0; i--) {
                PluginBus.plug(mPlugins[i]);
            }
        }
    }

    @Override
    public void onUnplug(final Plugin pPlugin) {

        if (mPlugins != null) {
            final int count = mPlugins.length;

            for (int i = count - 1; i >= 0; i--) {
                PluginBus.unplug(mPlugins[i]);
            }
        }
    }
}
