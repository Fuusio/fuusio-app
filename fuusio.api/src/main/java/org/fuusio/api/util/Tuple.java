/*
 * Copyright (C) 2001 - 2015 Marko Salmela, http://fuusio.org
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

public class Tuple<T_Value> {

    public T_Value mValue1;
    public T_Value mValue2;

    public Tuple(final T_Value pValue1, final T_Value pValue2) {
        mValue1 = pValue1;
        mValue2 = pValue2;
    }

    public final T_Value getValue1() {
        return mValue1;
    }

    public void setValue1(final T_Value pValue) {
        mValue1 = pValue;
    }

    public final T_Value getValue2() {
        return mValue2;
    }

    public void setValue2(final T_Value pValue) {
        mValue2 = pValue;
    }
}
