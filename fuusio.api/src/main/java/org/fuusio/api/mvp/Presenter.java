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
 */
public interface Presenter<T_View extends View> extends Component, View.Listener {

    T_View getView();

    void setView(T_View pView);

    void start();

    void pause();

    void resume();

    void stop();

    boolean addListener(Presenter.Listener pListener);

    boolean removeListener(Presenter.Listener pListener);

    interface Listener {

        void onPresenterStarted(Presenter pPresenter);

        void onPresenterResumed(Presenter pPresenter);

        void onPresenterPaused(Presenter pPresenter);

        void onPresenterStopped(Presenter pPresenter);
    }
}
