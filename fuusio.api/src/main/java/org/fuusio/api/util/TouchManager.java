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

import android.view.MotionEvent;

public class TouchManager {

    private final int mMaxTouchPoints;
    private final Vector2D[] mPoints;
    private final Vector2D[] mPreviousPoints;

    public TouchManager(final int pMaxTouchPoints) {
        mMaxTouchPoints = pMaxTouchPoints;
        mPoints = new Vector2D[pMaxTouchPoints];
        mPreviousPoints = new Vector2D[pMaxTouchPoints];
    }

    public boolean isPressed(final int pIndex) {
        return (mPoints[pIndex] != null);
    }

    public int getPressCount() {
        int count = 0;

        for (final Vector2D point : mPoints) {
            if (point != null) {
                ++count;
            }
        }
        return count;
    }

    public Vector2D moveDelta(final int pIndex) {

        if (isPressed(pIndex)) {
            final Vector2D previous = mPreviousPoints[pIndex] != null ? mPreviousPoints[pIndex]
                    : mPoints[pIndex];
            return Vector2D.subtract(mPoints[pIndex], previous);
        } else {
            return new Vector2D();
        }
    }

    private static Vector2D getVector(final Vector2D pVector1, final Vector2D pVector2) {
        if (pVector1 == null || pVector2 == null) {
            throw new RuntimeException("Can't do this on nulls");
        }

        return Vector2D.subtract(pVector2, pVector1);
    }

    public Vector2D getPoint(final int pIndex) {
        return mPoints[pIndex] != null ? mPoints[pIndex] : new Vector2D();
    }

    public Vector2D getPreviousPoint(final int pIndex) {
        return mPreviousPoints[pIndex] != null ? mPreviousPoints[pIndex] : new Vector2D();
    }

    public Vector2D getVector(final int pIndexA, final int pIndexB) {
        return getVector(mPoints[pIndexA], mPoints[pIndexB]);
    }

    public Vector2D getPreviousVector(final int pIndexA, final int pIndexB) {
        if (mPreviousPoints[pIndexA] == null || mPreviousPoints[pIndexB] == null) {
            return getVector(mPoints[pIndexA], mPoints[pIndexB]);
        } else {
            return getVector(mPreviousPoints[pIndexA], mPreviousPoints[pIndexB]);
        }
    }

    public void update(final MotionEvent pEvent) {

        final int action = pEvent.getAction() & MotionEvent.ACTION_MASK;

        if (action == MotionEvent.ACTION_POINTER_UP || action == MotionEvent.ACTION_UP) {

            final int index = pEvent.getAction() >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;

            if (index < mMaxTouchPoints) {
                mPreviousPoints[index] = null;
                mPoints[index] = null;
                // System.out.println("CLEARED: " + index);
            }
        } else {

            final int pointerCount = pEvent.getPointerCount();

            for (int i = 0; i < mMaxTouchPoints; ++i) {

                if (i < pointerCount) {
                    final int index = pEvent.getPointerId(i);
                    final Vector2D newPoint = new Vector2D(pEvent.getX(i), pEvent.getY(i));

                    if (mPoints[index] == null) {
                        mPoints[index] = newPoint;
                        // System.out.println("ADDED: " + index);
                    } else {
                        if (mPreviousPoints[index] != null) {
                            mPreviousPoints[index].set(mPoints[index]);
                        } else {
                            mPreviousPoints[index] = new Vector2D(newPoint);
                        }

                        if (Vector2D.subtract(mPoints[index], newPoint).getLength() < 64) {
                            mPoints[index].set(newPoint);
                        }

                        // System.out.println("UPDATED: " + index);
                    }
                } else {
                    mPreviousPoints[i] = null;
                    mPoints[i] = null;
                }
            }
        }
    }
}
