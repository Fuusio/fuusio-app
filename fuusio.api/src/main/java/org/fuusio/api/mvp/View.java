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
 * {@link View} is interface for View components in a MVP architectural pattern implementation.
 */
public interface View<T_Presenter extends Presenter> extends Component {

    T_Presenter getPresenter();

    interface Listener {

        void onViewCreated(View pView, Bundle pInState);

        void onViewStart(View pView);

        void onViewResume(View pView);

        void onViewPause(View pView);

        void onViewStop(View pView);
    }
}
