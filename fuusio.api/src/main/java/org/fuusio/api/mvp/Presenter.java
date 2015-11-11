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
import org.fuusio.api.util.Listenable;

/**
 * {@link Presenter} is the interface to be implemented by Presenter components of a MVP
 * architectural pattern implementation. By default, a {@link Presenter} implementation is also
 * a listener for {@link View} events via the {@link View.Listener} interface.
 * @param <T_View> The parametrised type extending {@link View}.
 */
public interface Presenter<T_View extends View, T_Listener extends Presenter.Listener> extends Component, Listenable<T_Listener>, View.Listener {

    /**
     * Return the {@link View} of this {@link Presenter}.
     * @return A {@link View}.
     */
    T_View getView();

    /**
     * Set the {@link View} of this {@link Presenter}.
     * @param view A {@link View}.
     */
    void setView(T_View view);

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

    interface Listener {

        /**
         * Invoked by a {@link Presenter} when it is started. Typically a {@link Presenter} is
         * started when its {@link View} is started.
         * @param presenter The started {@link Presenter}.
         */
        void onPresenterStarted(Presenter presenter);

        /**
         * Invoked by a {@link Presenter} when it is resumed. Typically a {@link Presenter} is
         * resumed when its {@link View} is resumed.
         * @param presenter The resumed {@link Presenter}.
         */
        void onPresenterResumed(Presenter presenter);

        /**
         * Invoked by a {@link Presenter} when it is paused. Typically a {@link Presenter} is
         * paused when its {@link View} is paused.
         * @param presenter The paused {@link Presenter}.
         */
        void onPresenterPaused(Presenter presenter);

        /**
         * Invoked by a {@link Presenter} when it is stopped. Typically a {@link Presenter} is
         * stopped when its {@link View} is stopped.
         * @param presenter The stopped {@link Presenter}.
         */
        void onPresenterStopped(Presenter presenter);
    }
}
