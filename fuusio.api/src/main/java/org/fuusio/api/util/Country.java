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

public class Country {
    
    private final String mCode;
    private final String mIso;
    private final String mName;

    protected Country(final String pIso, final String pCode, final String pName) {
        mIso = pIso;
        mCode = pCode;
        mName = pName;
    }

    public final String getCode() {
        return mCode;
    }

    public final String getIso() {
        return mIso;
    }

    public final String getName() {
        return mName;
    }   
}
