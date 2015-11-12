// ============================================================================
// Fuusio.org : Java Class Source File
// ============================================================================
//
// Class: SkinCellPainter
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
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * {@code StretchableBitmapCellDrawable} extends {@code Drawable} to implement a {@code Drawable}
 * that can be used to render a single cell in a {@code StretchableBitmap}.
 */

public class StretchableBitmapCellDrawable extends Drawable {

    protected StretchableBitmap mSkinImage;

    protected Rect mSourceRect;

    /**
     * Constructs a new instance of {@code SkinCellPainter} for the given {@code SkinImage}.
     * 
     * @param skinImage A {@code SkinImage}.
     */

    public StretchableBitmapCellDrawable(final StretchableBitmap skinImage) {
        mSkinImage = skinImage;
        mSourceRect = new Rect();
    }

    /**
     * Sets the source bounding box.
     * 
     * @param rect The bounding box as a {@code Rect}.
     */
    public void setSourceRect(final Rect rect) {
        mSourceRect.left = rect.left;
        mSourceRect.top = rect.top;
        mSourceRect.right = rect.right;
        mSourceRect.bottom = rect.bottom;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.graphics.drawable.Drawable#draw(android.graphics.Canvas)
     */
    @Override
    public void draw(final Canvas canvas) {
        final Bitmap cellBitmap = mSkinImage.getActiveStateImage();

        if (cellBitmap != null) {
            final Rect targetRect = getBounds();
            canvas.drawBitmap(cellBitmap, mSourceRect, targetRect, null);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.graphics.drawable.Drawable#getOpacity()
     */
    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT; // TODO
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.graphics.drawable.Drawable#setAlpha(int)
     */
    @Override
    public void setAlpha(final int alpha) {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.graphics.drawable.Drawable#setColorFilter(android.graphics.ColorFilter)
     */
    @Override
    public void setColorFilter(final ColorFilter filter) {
        // TODO Auto-generated method stub
    }
}
