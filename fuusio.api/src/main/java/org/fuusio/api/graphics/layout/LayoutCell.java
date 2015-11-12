// ============================================================================
// Floxp.com : Java Class Source File
// ============================================================================
//
// Class: LayoutCell
// Package: FloXP.com Android APIs (com.floxp.api) -
// Graphics API (com.floxp.api.graphics) -
// Layout Managers (org.fuusio.api.graphics.layout)
//
// Author: Marko Salmela
//
// Copyright (C) Marko Salmela, 2000-2011. All Rights Reserved.
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

package org.fuusio.api.graphics.layout;

import java.util.List;

import org.fuusio.api.util.Dimension;
import org.fuusio.api.util.Insets;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * {@link LayoutCell} is an abstract base class for the layout cell objects that consist a
 * {@link CellLayout}.
 * 
 * @author Marko Salmela
 */
public abstract class LayoutCell {

    /**
     * The fixed size of this cell.
     */
    protected final Dimension mFixedSize;

    /**
     * Vertical resizing policy for the height of this {@link LayoutCell}.
     */
    protected ResizePolicy mHeightResizePolicy;

    /**
     * Top, left, bottom, and right margins of this {@link LayoutCell}.
     */
    protected final Insets mInsets;

    /**
     * The {@link CellLayout} that owns this {@link LayoutCell}.
     */
    protected CellLayout mLayout;

    /**
     * Current location of this {@link LayoutCell} inside its container.
     */
    protected final Point mLocation;

    /**
     * Calculated maximum size of this {@link LayoutCell}.
     */
    protected final Dimension mMaximumSize;

    /**
     * Calculated minimum size of this {@link LayoutCell}.
     */
    protected final Dimension mMinimumSize;

    /**
     * Calculated preferred size of this {@link LayoutCell}.
     */
    protected final Dimension mPreferredSize;

    /**
     * A boolean flag indicating whether the bounds of this {@link LayoutCell} should be
     * re-calculated.
     */
    protected boolean mRecalculateBounds;

    /**
     * Current size of this {@link LayoutCell}.
     */
    protected final Dimension mSize;

    /**
     * A {@code boolean} flag indicating if this {@link LayoutCell} is touch sensitive.
     */
    protected boolean mTouchSensitive;

    /**
     * Vertical resizing policy for the width of the specified {@link LayoutCell}.
     */
    protected ResizePolicy mWidthResizePolicy;

    /**
     * Constructs a new instance of {@link LayoutCell} for the given {@link CellLayout}.
     * 
     * @param layout A {@link CellLayout}.
     */
    protected LayoutCell(final CellLayout layout) {
        mLayout = layout;
        mFixedSize = new Dimension();
        mHeightResizePolicy = ResizePolicy.FIXED;
        mInsets = new Insets(0, 0, 0, 0);
        mRecalculateBounds = true;
        mLocation = new Point();
        mMaximumSize = new Dimension();
        mMinimumSize = new Dimension();
        mPreferredSize = new Dimension();
        mSize = new Dimension();
        mTouchSensitive = false;
        mWidthResizePolicy = ResizePolicy.FIXED;
    }

    /**
     * Constructs a new instance of {@link LayoutCell} for the given {@link CellLayout}.
     * TODO
     * 
     * @param layout A {@link CellLayout}.
     * @param pWidthResizePolicy The horizontal {@link ResizePolicy}.
     * @param pHeightResizePolicy The vertical {@link ResizePolicy}.
     */
    protected LayoutCell(final CellLayout layout, final ResizePolicy pWidthResizePolicy,
            final ResizePolicy pHeightResizePolicy) {
        this(layout);
        mWidthResizePolicy = pWidthResizePolicy;
        mHeightResizePolicy = pHeightResizePolicy;
    }

    /**
     * Constructs a new instance of {@link LayoutCell} for the given {@link CellLayout}.
     * TODO
     * 
     * @param layout A {@link CellLayout}.
     * @param pWidth The width of the {@link LayoutCell}.
     * @param pHeight The height of the {@link LayoutCell}.
     */
    protected LayoutCell(final CellLayout layout, final int pWidth, final int pHeight) {
        this(layout);
        mSize.mWidth = pWidth;
        mSize.mHeight = pHeight;
    }

    /**
     * Constructs a new instance of {@link LayoutCell} for the given {@link CellLayout}.
     * TODO
     * 
     * @param layout A {@link CellLayout}.
     * @param pWidthResizePolicy The horizontal {@link ResizePolicy}.
     * @param pHeight The height of the {@link LayoutCell}.
     */
    protected LayoutCell(final CellLayout layout, final ResizePolicy pWidthResizePolicy,
            final int pHeight) {
        this(layout);
        mWidthResizePolicy = pWidthResizePolicy;
        mSize.mHeight = pHeight;
    }

    /**
     * Constructs a new instance of {@link LayoutCell} for the given {@link CellLayout}.
     * TODO
     * 
     * @param layout A {@link CellLayout}.
     * @param pWidth The width of the {@link LayoutCell}.
     * @param pHeightResizePolicy The vertical {@link ResizePolicy} to be added.
     */
    protected LayoutCell(final CellLayout layout, final int pWidth,
            final ResizePolicy pHeightResizePolicy) {
        this(layout);
        mSize.mWidth = pWidth;
        mHeightResizePolicy = pHeightResizePolicy;
    }

    /**
     * Constructs a copy instance of the given source {@link LayoutCell}. for the given
     * {@link CellLayout}.
     * 
     * @param layout A {@link CellLayout}.
     * @param pSource The source {@link LayoutCell}.
     */
    protected LayoutCell(final CellLayout layout, final LayoutCell pSource) {
        mLayout = layout;
        mFixedSize = new Dimension(pSource.mFixedSize);
        mHeightResizePolicy = pSource.mHeightResizePolicy;
        mInsets = new Insets(pSource.mInsets);
        mRecalculateBounds = true;
        mLocation = new Point();
        mMaximumSize = new Dimension();
        mMinimumSize = new Dimension();
        mPreferredSize = new Dimension();
        mSize = new Dimension();
        mWidthResizePolicy = pSource.mWidthResizePolicy;
    }

    /**
     * Gets the bounds of this {@link LayoutCell}.
     * 
     * @return The bounds as a {link Rect}.
     */
    public Rect getBounds() {
        return new Rect(mLocation.x, mLocation.y, mLocation.x + mSize.mWidth, mLocation.y
                + mSize.mHeight);
    }

    public boolean isTouchSensitive() {
        return mTouchSensitive;
    }

    public void setTouchSensitive(final boolean pTouchSensitive) {
        mTouchSensitive = pTouchSensitive;
    }

    /**
     * Gets the height of this {@link LayoutCell}.
     * 
     * @return The height as an {@code int}. Returns -1 is the absolute height is not applied.
     */
    public final int getHeight() {
        return mSize.mHeight;
    }

    /**
     * Gets the {@link ResizePolicy} for zone height.
     * 
     * @return A {@link ResizePolicy}.
     */
    public final ResizePolicy getHeightResizePolicy() {
        return mHeightResizePolicy;
    }

    /**
     * Gets the {@link CellLayout} that contains this {@link LayoutCell}.
     * 
     * @return A {@link CellLayout}.
     */
    public final CellLayout getLayout() {
        return mLayout;
    }

    /**
     * Sets the {@link CellLayout} defined to this {@link LayoutCell}.
     * 
     * @param layout A {@link CellLayout}.
     */
    protected void setLayout(final CellLayout layout) {
        if (mLayout != null) {
            throw new IllegalStateException("LayoutCell is already assigned to a CellLayout.");
        }

        mLayout = layout;
    }

    /**
     * Gets the current location calculated for this {@link LayoutCell}.
     * 
     * @return The location as a {@code Point}.
     */
    public Point getLocation() {
        return mLocation;
    }

    /**
     * Sets the current location calculated for this {@link LayoutCell}.
     * 
     * @param pX The x-coordinate of the new location as an {@code int}.
     * @param pY The y-coordinate of the new location as an {@code int}.
     */
    public void setLocation(final int pX, final int pY) {
        mLocation.x = pX;
        mLocation.y = pY;
    }

    /**
     * Gets the maximum size calculated for this {@link LayoutCell}.
     * 
     * @return The maximum size as a {@link Dimension}.
     */
    public Dimension getMaximumSize() {
        return mMaximumSize;
    }

    /**
     * Gets the minimum size calculated for this {@link LayoutCell}.
     * 
     * @return The minimum size as a {@link Dimension}.
     */
    public Dimension getMinimumSize() {
        return mMinimumSize;
    }

    /**
     * Gets the preferred size calculated for this {@link LayoutCell}.
     * 
     * @return The preferred size as a {@link Dimension}.
     */
    public Dimension getPreferredSize() {
        return mPreferredSize;
    }

    /**
     * Gets the current size calculated for this {@link LayoutCell}.
     * 
     * @return The size as a {@link Dimension}.
     */
    public Dimension getSize() {
        return mSize;
    }

    /**
     * Gets the current size calculated for this {@link LayoutCell}.
     * 
     * @param pWidth The width of the new size as an {@code int}.
     * @param pHeight The height of the new size as an {@code int}.
     */
    public void setSize(final int pWidth, final int pHeight) {
        mSize.mWidth = pWidth;
        mSize.mHeight = pHeight;
    }

    /**
     * Gets the width of this {@link LayoutCell}.
     * 
     * @return The width as an {@code int}. Returns -1 if the absolute width is not applied.
     */
    public final int getWidth() {
        return mSize.mWidth;
    }

    /**
     * Gets the {@link ResizePolicy} for zone height.
     * 
     * @return A {@link ResizePolicy}.
     */
    public final ResizePolicy getWidthResizePolicy() {
        return mWidthResizePolicy;
    }

    /**
     * Calculates the minimum, maximum, and preferred sizes of this {@link LayoutCell}.
     */
    protected abstract void measureSizes();

    /**
     * Invalidates this {@link LayoutCell}.
     */
    protected void invalidate() {
        mRecalculateBounds = true;
    }

    /**
     * Reset the minimum, maximum, and preferred sizes of this {@link LayoutCell}.
     */
    protected void resetSizes() {
        mMaximumSize.mWidth = 0;
        mMaximumSize.mHeight = 0;
        mMinimumSize.mWidth = 0;
        mMinimumSize.mHeight = 0;
        mPreferredSize.mWidth = 0;
        mPreferredSize.mHeight = 0;
    }

    /**
     * Tests whether the layout cell defined by this {@link LayoutCell} is visible or not.
     * 
     * @return A {@code boolean} value.
     */
    public boolean isVisible() {
        return true;
    }

    public abstract void collectDrawables(List<Drawable> pDrawables);

    public void collectViews(final List<View> pViews) {
    }
}
