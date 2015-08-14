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

import org.fuusio.api.component.Component;

/**
 * {@link Presenter} is interface for Presenter components in a MVP architectural pattern
 * implementation.
 * @param <T_View> The parametrised type extending {@link View}.
 */
public interface Presenter<T_View extends View> extends Component, View.Listener {

    /**
     * Return the {@link View} of this {@link Presenter}.
     * @return A {@link View}.
     */
    T_View getView();

    /**
     * Set the {@link View} of this {@link Presenter}.
     * @param pView A {@link View}.
     */
    void setView(T_View pView);

    /**
     * Invoked to start this {@link Presenter}.
     */
    void start();

    /**
     * Invoked to pause this {@link Presenter}.
     */
    void pause();

    /**
     * Invoked to resume this {@link Presenter}.
     */
    void resume();

    /**
     * Invoked to stop this {@link Presenter}.
     */
    void stop();

    /**
     * Add the given {@link Presenter.Listener} to receive events from this {@link Presenter}.
     * @param pListener A {@link Presenter.Listener}.
     * @return A {@code boolean} value {@code true} if adding succeeded; otherwise {@code false}.
     */
    boolean addListener(Presenter.Listener pListener);

    /**
     * Remove the given {@link Presenter.Listener} from receiving events from this {@link Presenter}.
     * @param pListener A {@link Presenter.Listener} to be removed.
     * @return A {@code boolean} value {@code true} if removing succeeded; otherwise {@code false}.
     */
    boolean removeListener(Presenter.Listener pListener);

    interface Listener {

        void onPresenterStarted(Presenter pPresenter);

        void onPresenterResumed(Presenter pPresenter);

        void onPresenterPaused(Presenter pPresenter);

        void onPresenterStopped(Presenter pPresenter);
    }
}
