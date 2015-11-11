/*
 * Copyright (C) 2001-2015 Marko Salmela.
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
package org.fuusio.api.util;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link AbstractListenable} is an abstract base class for concrete implementations of
 * {@link Listenable} interface.
 */
public abstract class AbstractListenable<T_Listener> implements Listenable<T_Listener> {

    protected final ArrayList<T_Listener> mListeners;

    protected AbstractListenable() {
        mListeners = new ArrayList<>();
    }

    @Override
    public List<T_Listener> getListeners() {
        return mListeners;
    }

    @Override
    public boolean addListener(final T_Listener listener) {
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeListener(final T_Listener listener) {
        return mListeners.remove(listener);
    }

    @Override
    public void removeAllListeners() {
        mListeners.clear();
    }

    @Override
    public boolean hasListeners() {
        return !mListeners.isEmpty();
    }
}
