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

import org.fuusio.api.util.AbstractListenable;

public class AbstractModel<T_EventType, T_Listener extends Model.Listener> extends AbstractListenable<T_Listener> implements Model<T_EventType, T_Listener> {

    protected ModelEvent createEvent(final T_EventType pType) {
        return null;
    }

    protected void notifyModelChanged(final T_EventType pType) {
        final ModelEvent event = createEvent(pType);

        if (event != null) {
            for (final Listener listener : mListeners) {
                listener.onModelChanged(event);
            }
        }
    }
}
