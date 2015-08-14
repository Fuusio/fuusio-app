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

public class URLBuilder {

    private final StringBuilder mBuilder;

    private boolean mFirstParameter;

    public URLBuilder(final String pUrl) {
        mBuilder = new StringBuilder(pUrl);
        mFirstParameter = true;
    }

    public URLBuilder(final String pFormattedUrl, final Object... pArgs) {
        mBuilder = new StringBuilder(StringToolkit.formatString(pFormattedUrl, pArgs));
        mFirstParameter = true;
    }

    public URLBuilder p(final String pKey, final int pValue) {
        return addParam(pKey, Integer.toString(pValue));
    }

    public URLBuilder p(final String pKey, final boolean pValue) {
        return addParam(pKey, Boolean.toString(pValue));
    }

    public URLBuilder p(final String pKey, final float pValue) {
        return addParam(pKey, Float.toString(pValue));
    }

    public URLBuilder p(final String pKey, final double pValue) {
        return addParam(pKey, Double.toString(pValue));
    }

    public URLBuilder p(final String pKey, final long pValue) {
        return addParam(pKey, Long.toString(pValue));
    }

    public URLBuilder p(final String pKey, final String pValue) {
        return addParam(pKey, pValue);
    }

    public URLBuilder addParam(final String pKey, final String pValue) {

        if (mFirstParameter) {
            mBuilder.append('?');
            mFirstParameter = false;
        } else {
            mBuilder.append('&');
        }

        mBuilder.append(pKey);
        mBuilder.append('=');
        mBuilder.append(pValue);

        return this;
    }

    @Override
    public String toString() {
        return mBuilder.toString();
    }
}
