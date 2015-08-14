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

/**
 * {@link AbstractPresentationModel} provides an abstract base class for implementing {@link PresentationModel}s
 * @param <T_Presenter> The parametrised type of {@link Presenter}.
 */
public class AbstractPresentationModel<T_Presenter extends Presenter, T_EventType> extends AbstractModel<T_EventType>
        implements PresentationModel<T_Presenter, T_EventType> {

        protected T_Presenter mPresenter;

        @Override
        public T_Presenter getPresenter() {
                return mPresenter;
        }
}
