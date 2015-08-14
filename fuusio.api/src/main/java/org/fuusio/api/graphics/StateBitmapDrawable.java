// ============================================================================
// Fuusio.org : Java Class Source File
// ============================================================================
//
// Class: StateImagePainter
// Package: Fuusio Graphics API (org.fuusio.api.graphics) -
// Painter Nodes (org.fuusio.api.graphics.painter)
//
// Author: Marko Salmela
//
// Copyright (C) Marko Salmela, 2000-2008. All Rights Reserved.
//
// This software is the proprietary information of Marko Salmela.
// Use is subject to license terms. This software is protected by
// copyright and distributed under licenses restricting its use,
// copying, distribution, and decompilation. No part of this software
// or associated documentation may be reproduced in any form by any
// means without prior written authorization of Marko Salmela.
//
// Disclaimer:
// -----------
//
// This software is provided by the author 'as is' and any express or implied
// warranties, including, but not limited to, the implied warranties of
// merchantability and fitness for a particular purpose are disclaimed.
// In no event shall the author be liable for any direct, indirect,
// incidental, special, exemplary, or consequential damages (including, but
// not limited to, procurement of substitute goods or services, loss of use,
// data, or profits; or business interruption) however caused and on any
// theory of liability, whether in contract, strict liability, or tort
// (including negligence or otherwise) arising in any way out of the use of
// this software, even if advised of the possibility of such damage.
// ============================================================================

package org.fuusio.api.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;

/**
 * {@code StateImagePainter} extends {@code ImagePainter} to implement a {@code PainterNode} thatcan
 * be used to render state depended mBitmaps.
 * 
 * @author Marko Salmela
 */

public class StateBitmapDrawable extends Drawable {

    protected Bitmap mBitmap;

    /**
     * An {@code Bitmap} array containing the state mBitmaps.
     */
    protected Bitmap[] mStateBitmaps;

    /**
     * The current state of this {@code StateSkinImagePainter}.
     */
    protected int mState;

    /**
     * Constructs a new instance of {@code StateImagePainter}.
     */

    public StateBitmapDrawable() {
    }

    /**
     * Constructs a new instance of {@code StateImagePainter} with the give state bitmaps.
     * 
     * @param pStateImages The state mBitmaps as an array of {@link Bitmap}s.
     */

    public StateBitmapDrawable(final Bitmap[] pStateImages) {
        setStateBitmaps(pStateImages);
    }

    /**
     * Gets the mBitmap of this {@code ImagePainter}.
     * 
     * @return The mBitmap as an {@code Bitmap}.
     */

    public Bitmap getImage() {
        if (mState >= 0 && mState < mStateBitmaps.length) {
            mBitmap = mStateBitmaps[mState];
        } else {
            mBitmap = null;
        }

        return mBitmap;
    }

    /**
     * Sets the mBitmap of this {@code ImagePainter}.
     * 
     * @param mBitmap The mBitmap as an {@code Bitmap}.
     */

    public void setImage(Bitmap mBitmap) {
        if (mBitmap != null) {
            for (int i = 0; i < mStateBitmaps.length; i++) {
                if (mBitmap == mStateBitmaps[i]) {
                    this.mBitmap = mBitmap;
                    setDrawableState(i);
                    break;
                }
            }
        } else {
            setDrawableState(-1);
            mBitmap = null;
        }
    }

    /**
     * Gets the current state.
     * 
     * @return The state as an {@code int}.
     */

    public int getDrawableState() {
        return mState;
    }

    /**
     * Sets the current state.
     * 
     * @param state The state as an {@code int}.
     */

    public void setDrawableState(final int state) {
        if (mState != state) {
            mState = state;
            // renderScene(); // TODO PROPERTY
        }
    }

    /**
     * Gets the state mBitmaps of this {@code StateImagePainter}.
     * 
     * @return The state mBitmaps as an array of {@code Images}.
     */

    public Bitmap[] getStateBitmaps() {
        return mStateBitmaps;
    }

    /**
     * Sets the state mBitmaps of this {@code StateImagePainter}.
     * 
     * @param pStateBitmaps The state mBitmaps as an array of {@code Images}.
     */
    public void setStateBitmaps(final Bitmap[] pStateBitmaps) {
        mStateBitmaps = pStateBitmaps;
        mState = 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.graphics.drawable.Drawable#draw(android.graphics.Canvas)
     */
    @Override
    public void draw(final Canvas pCanvas) {
        if (mState >= 0 && mState < mStateBitmaps.length) {
            mBitmap = mStateBitmaps[mState];
            // TODO super.paintNode(context);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.graphics.drawable.Drawable#getOpacity()
     */
    @Override
    public int getOpacity() {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.graphics.drawable.Drawable#setAlpha(int)
     */
    @Override
    public void setAlpha(final int pAlpha) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see android.graphics.drawable.Drawable#setColorFilter(android.graphics.ColorFilter)
     */
    @Override
    public void setColorFilter(final ColorFilter pCf) {
        // TODO Auto-generated method stub

    }
}
