/*
 * Copyright (C) 2014 - 2015 Marko Salmela, http://fuusio.org
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

import org.fuusio.api.mvp.AbstractPresenter;
import org.fuusio.api.mvp.Presenter;
import org.fuusio.api.mvp.View;

/**
 * {@link PluginPresenter} extends {@link AbstractPresenter} to provide an abstract base class
 * for concrete {@link Presenter} implementations that can be used as {@link Plugin}s.
 */
public abstract class PluginPresenter<T_View extends View, T_Listener extends Presenter.Listener> extends AbstractPresenter<T_View, T_Listener>
        implements Plugin, InjectorProvider {

    protected boolean mPlugged;

    protected PluginPresenter() {
    }

    @Override
    public PluginInjector getInjector() {
        return null;
    }

    @Override
    public String getPlugName() {
        return Plugin.DEFAULT_PLUG_NAME;
    }

    @Override
    public void start() {
        super.start();

        if (!mPlugged) {
            PluginBus.plug(this);
            mPlugged = true;
        }
    }

    @Override
    public void stop() {

        if (!mStopped) {
            super.stop();

            if (mPlugged) {
                PluginBus.unplug(this);
                mPlugged = false;
            }
        }
    }

    @Override
    public void onPlugged(final PluginBus bus) {
        // Do nothing by default
    }

    @Override
    public void onUnplugged(final PluginBus bus) {
        // Do nothing by default
    }
}
