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

public class Insets {

    public int mBottom;
    public int mLeft;
    public int mRight;
    public int mTop;

    public Insets() {
        this(0, 0, 0, 0);
    }

    public Insets(final int pTop, final int pLeft, final int pBottom, final int pRight) {
        mTop = pTop;
        mLeft = pLeft;
        mBottom = pBottom;
        mRight = pRight;
    }

    public Insets(final Insets pSource) {
        mTop = pSource.mTop;
        mLeft = pSource.mLeft;
        mBottom = pSource.mBottom;
        mRight = pSource.mRight;
    }

    public final int getBottom() {
        return mBottom;
    }

    public void setBottom(final int pBottom) {
        mBottom = pBottom;
    }

    public final int getLeft() {
        return mLeft;
    }

    public void setLeft(final int pLeft) {
        mLeft = pLeft;
    }

    public int getRight() {
        return mRight;
    }

    public void setRight(final int pRight) {
        mRight = pRight;
    }

    public final int getTop() {
        return mTop;
    }

    public void setTop(final int pTop) {
        mTop = pTop;
    }

    public void set(final int pTop, final int pLeft, final int pBottom, final int pRight) {
        mTop = pTop;
        mLeft = pLeft;
        mBottom = pBottom;
        mRight = pRight;
    }

    public void set(final Insets pInsets) {
        mTop = pInsets.mTop;
        mLeft = pInsets.mLeft;
        mBottom = pInsets.mBottom;
        mRight = pInsets.mRight;
    }
}
