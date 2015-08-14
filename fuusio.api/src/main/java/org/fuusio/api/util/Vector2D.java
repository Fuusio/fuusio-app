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

import android.annotation.SuppressLint;

public class Vector2D {

    public float mX;
    public float mY;

    public Vector2D() {
    }

    public Vector2D(final Vector2D pOther) {
        mX = pOther.mX;
        mY = pOther.mY;
    }

    public Vector2D(final float pX, final float pY) {
        mX = pX;
        mY = pY;
    }

    public final float getX() {
        return mX;
    }

    public final float getY() {
        return mY;
    }

    public final float getLength() {
        return (float) Math.sqrt(mX * mX + mY * mY);
    }

    public Vector2D set(final Vector2D pOther) {
        mX = pOther.getX();
        mY = pOther.getY();
        return this;
    }

    public Vector2D set(final float pX, final float pY) {
        mX = pX;
        mY = pY;
        return this;
    }

    public Vector2D add(final Vector2D pValue) {
        mX += pValue.mX;
        mY += pValue.mY;
        return this;
    }

    public static Vector2D subtract(final Vector2D pVector1, final Vector2D pVector2) {
        return new Vector2D(pVector1.mX - pVector2.mX, pVector1.mY - pVector2.mY);
    }

    public static float getDistance(final Vector2D lhs, final Vector2D pVector2) {
        final Vector2D delta = Vector2D.subtract(lhs, pVector2);
        return delta.getLength();
    }

    public static float getSignedAngleBetween(final Vector2D pVector1, final Vector2D pVector2) {
        final Vector2D normalized1 = getNormalized(pVector1);
        final Vector2D normalized2 = getNormalized(pVector2);

        return (float) (Math.atan2(normalized2.mY, normalized2.mX) - Math.atan2(normalized1.mY,
                normalized1.mX));
    }

    public static Vector2D getNormalized(final Vector2D pVector) {
        final float length = pVector.getLength();

        if (length == 0) {
            return new Vector2D();
        } else {
            return new Vector2D(pVector.mX / length, pVector.mY / length);
        }

    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        return String.format("(%.4f, %.4f)", mX, mY);
    }
}
