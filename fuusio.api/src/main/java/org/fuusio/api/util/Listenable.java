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

import java.util.List;

/**
 * {@link Listenable} provides an interface for classes that have listeners.
 */
public interface Listenable<T_Listener> {

    List<T_Listener> getListeners();

    boolean addListener(T_Listener pListener);

    boolean removeListener(T_Listener pListener);

    void removeAllListeners();
}