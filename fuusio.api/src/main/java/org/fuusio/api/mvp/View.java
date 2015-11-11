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

import org.fuusio.api.component.Component;

/**
 * {@link View} is the interface to be implemented by View components of a MVP
 * architectural pattern implementation.
 * @param <T_Presenter> The parametrised type extending {@link Presenter}.
 */
public interface View<T_Presenter extends Presenter> extends Component {

    /**
     * Gets the {@link Presenter} assigned for this {@link View}.
     * @return A {@link Presenter}.
     */
    T_Presenter getPresenter();


    /**
     * Gets the {@link ViewState} of this {@link View}.
     * @return An {@link ViewState}.
     */
    ViewState getState();

    /**
     * Test if this {@link View} has been paused.
     * @return A {@code boolean}.
     */
    boolean isPaused();

    /**
     * Test if this {@link View} has been restarted.
     * @return A {@code boolean}.
     */
    boolean isRestarted();

    interface Listener {

        /**
         * Invoked by a {@link View} implementation when it is created,
         * e.g. on {@link ViewFragment#onViewCreated(android.view.View, Bundle)}.
         *
         * @param view A {@link View}
         * @param inState {@lin Bundle} containing the initial state.
         */
        void onViewCreated(View view, Bundle inState);

        /**
         * Invoked by a {@link View} implementation when it is resumed,
         * e.g. on {@link ViewFragment#onResume()}.
         *
         * @param view A {@link View}
         */
        void onViewResume(View view);

        /**
         * Invoked by a {@link View} implementation when it is paused,
         * e.g. on {@link ViewFragment#onPause()}.
         *
         * @param view A {@link View}
         */
        void onViewPause(View view);

        /**
         * Invoked by a {@link View} implementation when it is started,
         * e.g. on {@link ViewFragment#onStart()}.
         *
         * @param view A {@link View}
         */
        void onViewStart(View view);

        /**
         * Invoked by a {@link View} implementation when it is stopped,
         * e.g. on {@link ViewFragment#onStop()}.
         *
         * @param view A {@link View}
         */
        void onViewStop(View view);
    }
}
