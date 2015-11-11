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

import org.fuusio.api.util.AbstractListenable;

/**
 * {@link AbstractPresenter} extends {@link AbstractListenable} to provide an abstract base class
 * for concrete implementations of {@link Presenter}s.
 * @param <T_View> The parametrised type extended  from {@link View}.
 * @param <T_Listener> The parametrised type extended  from {@link Presenter.Listener}.
 */
public class AbstractPresenter<T_View extends View, T_Listener extends Presenter.Listener> extends AbstractListenable<T_Listener>
        implements Presenter<T_View, T_Listener> {

    protected boolean mStopped;
    protected T_View mView;

    protected AbstractPresenter() {
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
     * @param view A {@link View}.
     */
    public void setView(final T_View view) {
        mView = view;
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


    @SuppressWarnings("unchecked")
    @Override
    public void onViewCreated(final View view, final Bundle inState) {
        setView((T_View)view);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewResume(final View view) {
        setView((T_View)view);
        resume();
    }

    @Override
    public void onViewPause(final View view) {
        pause();
    }

    @Override
    public void onViewStart(final View view) {
        start();
    }

    @Override
    public void onViewStop(final View view) {
        stop();
    }
}
