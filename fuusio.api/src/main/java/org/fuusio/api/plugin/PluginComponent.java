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

/**
 * {@link PluginComponent} provides an abstract base class for implementing {@link Plugin}
 * components.
 */
public abstract class PluginComponent implements Plugin, InjectorProvider {

    protected String mPlugName;

    protected PluginComponent() {
        this(Plugin.DEFAULT_PLUG_NAME);
    }

    protected PluginComponent(final String pPlugName) {
        mPlugName = pPlugName;
    }

    public String getPlugName() {
        return mPlugName;
    }

    public PluginInjector getInjector() {
        return null;
    }

    public void onPlugged(final PluginBus pBus) {
    }

    public void onUnplugged(final PluginBus pBus) {
    }
}
