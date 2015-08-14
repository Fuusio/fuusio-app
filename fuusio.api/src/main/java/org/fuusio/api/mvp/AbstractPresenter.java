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
package org.fuusio.api.mvp;

import android.os.Bundle;

import org.fuusio.api.plugin.InjectorProvider;
import org.fuusio.api.plugin.Plugin;
import org.fuusio.api.plugin.PluginBus;
import org.fuusio.api.plugin.PluginComponent;
import org.fuusio.api.plugin.PluginInjector;

import java.util.ArrayList;

public class AbstractPresenter<T_View extends View> implements Presenter<T_View> {

    protected final ArrayList<Presenter.Listener> mListeners;

    protected boolean mStopped;
    protected T_View mView;

    protected AbstractPresenter() {
        this(null);
    }

    protected AbstractPresenter(final T_View pView) {
        setView(pView);
        mListeners = new ArrayList<>();
        mStopped = false;
    }

    /**
     * Return the {@link View} of this {@link Presenter}.
     * @return A {@link View}.
     */
    public final T_View getView() {
        return mView;
    }

    /**
     * Set the {@link View} of this {@link Presenter}.
     * @param pView A {@link View}.
     */
    public void setView(final T_View pView) {
        mView = pView;
    }

    /**
     * Invoked to pause this {@link Presenter}.
     */
    @Override
    public void pause() {

        for (final Presenter.Listener listener : mListeners) {
            listener.onPresenterPaused(this);
        }
    }

    /**
     * Invoked to resume this {@link Presenter}.
     */
    @Override
    public void resume() {

        for (final Presenter.Listener listener : mListeners) {
            listener.onPresenterResumed(this);
        }
    }

    /**
     * Invoked to start this {@link Presenter}.
     */
    @Override
    public void start() {

        mStopped = false;

        for (final Presenter.Listener listener : mListeners) {
            listener.onPresenterStarted(this);
        }
    }

    /**
     * Invoked to stop this {@link Presenter}.
     */
    @Override
    public void stop() {

        if (!mStopped) {

            mStopped = true;

            for (final Presenter.Listener listener : mListeners) {
                listener.onPresenterStopped(this);
            }
        }
    }

    /**
     * Add the given {@link Presenter.Listener} to receive events from this {@link Presenter}.
     * @param pListener A {@link Presenter.Listener} to be added.
     * @return A {@code boolean} value {@code true} if adding succeeded; otherwise {@code false}.
     */
    @Override
    public boolean addListener(final Presenter.Listener pListener) {
        if (!mListeners.contains(pListener)) {
            mListeners.add(pListener);
            return true;
        }
        return false;
    }

    /**
     * Remove the given {@link Presenter.Listener} from receiving events from this {@link Presenter}.
     * @param pListener A {@link Presenter.Listener} to be removed.
     * @return A {@code boolean} value {@code true} if removing succeeded; otherwise {@code false}.
     */
    @Override
    public boolean removeListener(final Presenter.Listener pListener) {
        if (mListeners.contains(pListener)) {
            mListeners.remove(pListener);
            return true;
        }
        return false;
    }

    @Override
    public void onViewCreated(final View pView, final Bundle pInState) {
        // Do nothing by default
    }

    @Override
    public void onViewResume(final View pView) {
        if (pView == mView) {
            resume();
        }
    }

    @Override
    public void onViewPause(final View pView) {
        if (pView == mView) {
            pause();
        }
    }

    @Override
    public void onViewStart(final View pView) {
        if (pView == mView) {
            start();
        }
    }

    @Override
    public void onViewStop(final View pView) {
        if (pView == mView) {
            stop();
        }
    }
}
