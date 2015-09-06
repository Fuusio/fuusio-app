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

public class Dimension {

    public int mHeight;
    public int mWidth;

    public Dimension() {
        this(0, 0);
    }

    public Dimension(final int pWidth, final int pHeight) {
        mWidth = pWidth;
        mHeight = pHeight;
    }

    public Dimension(final Dimension pSource) {
        mWidth = pSource.mWidth;
        mHeight = pSource.mHeight;
    }

    public final int getHeight() {
        return mHeight;
    }


    public void setHeight(final int pHeight) {
        mHeight = pHeight;
    }

    public final int getWidth() {
        return mWidth;
    }

    public void setWidth(final int pWidth) {
        mWidth = pWidth;
    }

    public void set(final int pWidth, final int pHeight) {
        mWidth = pWidth;
        mHeight = pHeight;
    }

    public void set(final Dimension pValue) {
        mWidth = pValue.mWidth;
        mHeight = pValue.mHeight;
    }
}
