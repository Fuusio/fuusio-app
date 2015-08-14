// ============================================================================
// Fuusio.org : Java Class Source File
// ============================================================================
//
// Class: SkinImagePainter
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

import java.util.List;

import org.fuusio.api.util.Dimension;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * {@link StretchableBitmapDrawable} extends {@link CompositeDrawable} to implement a
 * {@link Drawable} that utilizes {@link StretchableBitmap}s for painting.
 * 
 * @author Marko Salmela
 */
public class StretchableBitmapDrawable extends CompositeDrawable {
    // -------------------------------------------------------------------------
    // Constant Fields
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    // Static Fields
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    // Instance Fields
    // -------------------------------------------------------------------------

    /**
     * The number of columns in the assigned {@link StretchableBitmap}.
     */
    protected int mColumns;

    /**
     * The number of rows in the assigned {@link StretchableBitmap}.
     */
    protected int mRows;

    protected Dimension mSize;

    /**
     * The rendered {@link StretchableBitmap}.
     */
    protected StretchableBitmap mStretchableBitmap;

    // Transient Fields

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Constructs a new instance of {@link StretchableBitmapDrawable}.
     */
    public StretchableBitmapDrawable() {
        mSize = new Dimension();
    }

    public StretchableBitmapDrawable(final StretchableBitmap pStretchableBitmap) {
        this();
        setStretchableBitmap(pStretchableBitmap);
    }

    // -------------------------------------------------------------------------
    // Getter & Setter Methods
    // -------------------------------------------------------------------------

    /**
     * Gets the assigned {@link StretchableBitmap}
     * 
     * @return A {@link StretchableBitmap}.
     */

    public StretchableBitmap getStretchableBitmap() {
        return mStretchableBitmap;
    }

    /**
     * Sets the assigned {@link StretchableBitmap}.
     * 
     * @param pStretchableBitmap A {@link StretchableBitmap}.
     */

    public void setStretchableBitmap(final StretchableBitmap pStretchableBitmap) {
        if (pStretchableBitmap != mStretchableBitmap) {
            boolean isLayoutingNeeded = false;

            if (mStretchableBitmap != null) {
                removeAllDrawables();
                isLayoutingNeeded = true;
            }

            if (pStretchableBitmap != null) {
                Rect[][] sourceRect = pStretchableBitmap.getSourceRects();
                mColumns = sourceRect.length;
                mRows = sourceRect[0].length;

                for (int column = 0; column < mColumns; column++) {
                    for (int row = 0; row < mRows; row++) {
                        final StretchableBitmapCellDrawable drawable = new StretchableBitmapCellDrawable(
                                pStretchableBitmap);
                        drawable.setSourceRect(sourceRect[column][row]);
                        addDrawable(drawable);
                    }
                }
            }

            if (isLayoutingNeeded) {
                doLayout();
            }

            mStretchableBitmap = pStretchableBitmap;
        }
    }

    // -------------------------------------------------------------------------
    // Create Methods
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    // Modifier Methods
    // -------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see android.graphics.drawable.Drawable#setBounds(int, int, int, int)
     */
    @Override
    public void setBounds(final int pLeft, final int pTop, final int pRight, final int pBottom) {
        mSize.mWidth = pRight - pLeft;
        mSize.mHeight = pBottom - pTop;
        doLayout();
        super.setBounds(pLeft, pTop, pRight, pBottom);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.graphics.drawable.Drawable#setBounds(android.graphics.Rect)
     */
    @Override
    public void setBounds(final Rect pBounds) {
        mSize.mWidth = pBounds.width();
        mSize.mHeight = pBounds.height();
        doLayout();
        super.setBounds(pBounds);
    }

    /**
     * Invoked when the bounds of this {@link Drawable} have changed.
     */

    public void doLayout() {
        if (mStretchableBitmap != null) {
            mStretchableBitmap.doLayout(mSize);

            final Rect[][] dstRects = mStretchableBitmap.getDestinationBounds();
            final List<Drawable> drawables = getDrawables();
            int index = 0;

            for (int column = 0; column < mColumns; column++) {
                for (int row = 0; row < mRows; row++) {
                    StretchableBitmapCellDrawable drawable = (StretchableBitmapCellDrawable) drawables
                            .get(index++);
                    drawable.setBounds(dstRects[column][row]);
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // Event Handler Methods
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    // Paint Methods
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    // Test Methods
    // -------------------------------------------------------------------------

    /**
     * Tests whether this {@link Drawable} is currently enabled or not.
     * 
     * @return A {@link boolean}.
     */
    @Override
    public boolean isEnabled() {
        if (mStretchableBitmap == null) {
            return false;
        }

        return super.isEnabled();
    }

    // -------------------------------------------------------------------------
    // Utility Methods
    // -------------------------------------------------------------------------
}
