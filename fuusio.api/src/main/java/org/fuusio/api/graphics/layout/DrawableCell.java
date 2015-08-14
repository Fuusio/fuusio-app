// ============================================================================
// Floxp.com : Java Class Source File
// ============================================================================
//
// Class: DrawableCell
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

import org.fuusio.api.util.Insets;

import android.graphics.Point;
import android.graphics.drawable.Drawable;

/**
 * {@code DrawableCell} implements {@code LayoutCell} that is used to define a layout cell in a
 * {@code CellLayout} that may contain a {@link Drawable}.
 * 
 * @author Marko Salmela
 */
public class DrawableCell extends LayoutCell {

    /**
     * Default layout cell insets for a {@link Drawable}.
     */
    protected static Insets sDefaultDrawableInsets = new Insets(0, 0, 0, 0);

    /**
     * Anchor specifies the location of the component in the area specified by this cell.
     */
    protected Anchor mAnchor;

    /**
     * The background {@link Drawable} managed by this {@link LayoutCell}.
     */
    protected Drawable mBackgroundDrawable;

    protected Insets mBackgroundInsets;

    /**
     * The foreground {@link Drawable} managed by this {@link LayoutCell}.
     */
    protected Drawable mForegroundDrawable;

    /**
     * {@link FillPolicy} specifies how the container {@link Drawable} fills the area of this cell.
     */
    protected FillPolicy mFillMode;

    /**
     * Constructs a new instance of {@code DrawableCell} for adding the given foreground
     * {@link Drawable} into the {@code Container} whose layout is managed by the given
     * {@code CellLayout}.
     * 
     * @param pLayout A {@code CellLayout}.
     * @param pForegroundDrawable The {@link Drawable} to be added.
     */
    protected DrawableCell(final CellLayout pLayout, final Drawable pForegroundDrawable) {
        super(pLayout);
        mBackgroundInsets = new Insets(0, 0, 0, 0);
        mForegroundDrawable = pForegroundDrawable;
        mAnchor = Anchor.CENTER;
        mFillMode = FillPolicy.NONE;
    }

    /**
     * Constructs a new instance of {@code DrawableCell} for adding the given foreground
     * {@link Drawable} into the {@code Container} whose layout is managed by the given
     * {@code CellLayout}.
     * 
     * @param pLayout A {@code CellLayout}.
     * @param pForegroundDrawable The {@link Drawable} to be added.
     * @param pWidthResizePolicy The horizontal {@code ResizePolicy}.
     * @param pHeightResizePolicy The vertical {@code ResizePolicy}.
     */
    public DrawableCell(final CellLayout pLayout, final Drawable pForegroundDrawable,
            final ResizePolicy pWidthResizePolicy, final ResizePolicy pHeightResizePolicy) {
        this(pLayout, pForegroundDrawable);
        mWidthResizePolicy = pWidthResizePolicy;
        mHeightResizePolicy = pHeightResizePolicy;
    }

    /**
     * Constructs a new instance of {@code DrawableCell} for adding the given foreground
     * {@link Drawable} into the {@code Container} whose layout is managed by the given
     * {@code CellLayout}.
     * 
     * @param pLayout A {@code CellLayout}.
     * @param pForegroundDrawable The {@link Drawable} to be added.
     * @param pWidth The width of the {@code DrawableCell}.
     * @param pHeight The height of the {@code DrawableCell}.
     */
    public DrawableCell(final CellLayout pLayout, final Drawable pForegroundDrawable,
            final int pWidth, final int pHeight) {
        this(pLayout, pForegroundDrawable);
        mFixedSize.mWidth = pWidth;
        mFixedSize.mHeight = pHeight;
    }

    /**
     * Constructs a new instance of {@code DrawableCell} for adding the given foreground
     * {@link Drawable} into the {@code Container} whose layout is managed by the given
     * {@code CellLayout}.
     * 
     * @param pLayout A {@code CellLayout}.
     * @param pForegroundDrawable The {@link Drawable} to be added.
     * @param pWidthResizePolicy The horizontal {@code ResizePolicy}.
     * @param pHeight The height of the {@code DrawableCell}.
     */
    public DrawableCell(final CellLayout pLayout, final Drawable pForegroundDrawable,
            final ResizePolicy pWidthResizePolicy, final int pHeight) {
        this(pLayout, pForegroundDrawable);
        mWidthResizePolicy = pWidthResizePolicy;
        mFixedSize.mHeight = pHeight;
    }

    /**
     * Constructs a new instance of {@code DrawableCell} for adding the given foreground
     * {@link Drawable} into the {@code Container} whose layout is managed by the given
     * {@code CellLayout}.
     * 
     * @param pLayout A {@code CellLayout}.
     * @param pForegroundDrawable The {@link Drawable} to be added.
     * @param pWidth The width of the {@code DrawableCell}.
     * @param pHeightResizePolicy The vertical {@code ResizePolicy}.
     */
    public DrawableCell(final CellLayout pLayout, final Drawable pForegroundDrawable,
            final int pWidth, final ResizePolicy pHeightResizePolicy) {
        this(pLayout, pForegroundDrawable);
        mFixedSize.mWidth = pWidth;
        mHeightResizePolicy = pHeightResizePolicy;
    }

    @Override
    public void collectDrawables(final List<Drawable> pDrawables) {
        if (mBackgroundDrawable != null) {
            pDrawables.add(mBackgroundDrawable);
        }

        if (mForegroundDrawable != null) {
            pDrawables.add(mForegroundDrawable);
        }
    }

    /**
     * Gets the anchor defined to this {@code DrawableCell}. The anchor is 
     * used when the drawable is smaller than area calculated for its container
     * {@code LayoutCell}. It determines where, within the cell area, to place 
     * the drawable. The possible anchor values are: {@code CENTER}, 
     * {@code NORTH}, {@code NORTHEAST}, {@code EAST}, 
     * {@code SOUTHEAST}, {@code SOUTH}, {@code SOUTHWEST}, 
     * {@code WEST}, and {@code NORTHWEST}. The default value is 
     * {@code CENTER</code. 
     *  
     * @return An {@code Anchor}.
     */
    public final Anchor getAnchor() {
        return mAnchor;
    }

    /**
     * Sets the anchor defined to this {@code DrawableCell}. The anchor is 
     * used when the drawable is smaller than area calculated for its container
     * {@code LayoutCell}. It determines where, within the cell area, to place 
     * the drawable. The possible anchor values are: {@code CENTER}, 
     * {@code NORTH}, {@code NORTHEAST}, {@code EAST}, 
     * {@code SOUTHEAST}, {@code SOUTH}, {@code SOUTHWEST}, 
     * {@code WEST}, and {@code NORTHWEST}. The default value is 
     * {@code CENTER</code. 
     * 
     * @param pAnchor An {@code Direction} specifying the anchor.
     * @return This instance as a {@code DrawableCell}.
     */
    public final DrawableCell setAnchor(final Anchor pAnchor) {
        mAnchor = pAnchor;
        return this;
    }

    /**
     * Gets the location for the specified anchor.
     * 
     * @return A location as a {@code Point}.
     */
    protected Point getAnchorLocation() {
        int x = mLocation.x + mSize.mWidth / 2;
        int y = mLocation.y + mSize.mHeight / 2;

        if (mForegroundDrawable != null && mForegroundDrawable.isVisible()) {
            switch (mAnchor) {
                case NORTH_WEST: {
                    x = mLocation.x;
                    y = mLocation.y;
                    break;
                }
                case NORTH: {
                    x = mLocation.x + mSize.mWidth / 2;
                    y = mLocation.y;
                    break;
                }
                case NORTH_EAST: {
                    x = mLocation.x + mSize.mWidth;
                    y = mLocation.y;
                    break;
                }
                case EAST: {
                    x = mLocation.x + mSize.mWidth;
                    y = mLocation.y + mSize.mHeight / 2;
                    break;
                }
                case SOUTH_EAST: {
                    x = mLocation.x + mSize.mWidth;
                    y = mLocation.y + mSize.mHeight;
                    break;
                }
                case SOUTH: {
                    x = mLocation.x + mSize.mWidth / 2;
                    y = mLocation.y + mSize.mHeight;
                    break;
                }
                case SOUTH_WEST: {
                    x = mLocation.x;
                    y = mLocation.y + mSize.mHeight;
                    break;
                }
                case WEST: {
                    x = mLocation.x;
                    y = mLocation.y + mSize.mHeight / 2;
                    break;
                }
                case CENTER: {
                    x = mLocation.x + mSize.mWidth / 2;
                    y = mLocation.y + mSize.mHeight / 2;
                    break;
                }
            }
        }

        return new Point(x, y);
    }

    /**
     * Gets the background {@link Drawable} assigned to this {@code DrawableCell}.
     * 
     * @return A {@link Drawable}.
     */
    public final Drawable getBackgroundDrawable() {
        return mBackgroundDrawable;
    }

    /**
     * Gets the foreground {@link Drawable} assigned to this {@code DrawableCell}.
     * 
     * @return A {@link Drawable}.
     */
    public final Drawable getForegroundDrawable() {
        return mForegroundDrawable;
    }

    /**
     * Gets the default margins for {@code DrawableCell}s.
     * 
     * @return The margins as an {@link Insets}.
     */
    public static Insets getDefaultDrawableInsets() {
        return sDefaultDrawableInsets;
    }

    /**
     * Sets the default margins for {@code ComponentCells}.
     * 
     * @param pTop The top insets.
     * @param pLeft The left insets.
     * @param pBottom The bottom insets.
     * @param pRight The right insets.
     */
    public static void setDefaultDrawableInsets(final int pTop, final int pLeft, final int pBottom,
            final int pRight) {
        sDefaultDrawableInsets = new Insets(pTop, pLeft, pBottom, pRight);
    }

/**
     * Gets the fill mode defined to this {@code DrawableCell}. Fill mode
     * is mode is used to specify how the drawable fills up the area calculated 
     * for its container {@code LayoutCell}.  The possible fill mode values are: 
     * {@code NONE}, {@code HORIZONTAL}, {@code VERTICAL}, 
     * and {@code BOTH}. The default value is {@code NONE</code. 
     *  
     * @return A {@link FillPolicy}.
     */
    public final FillPolicy getFillMode() {
        return mFillMode;
    }

/**
     * Sets the fill mode defined to this {@code DrawableCell}. Fill mode
     * is mode is used to specify how the drawable fills up the area calculated 
     * for its container {@code LayoutCell}.  The possible fill mode values are: 
     * {@code NONE}, {@code HORIZONTAL}, {@code VERTICAL}, 
     * and {@code BOTH}. The default value is {@code NONE</code. 
     * 
     * @param pFillMode A {@link FillPolicy}.
     * @return This instance as a {@code DrawableCell} for method invocation
     *      linking.
     */
    public final DrawableCell setFillPolicy(final FillPolicy pFillMode) {
        mFillMode = pFillMode;
        return this;
    }

    /**
     * Gets the background insets of this {@code DrawableCell}.
     * 
     * @return An {@link Insets}.
     */
    public final Insets getBackgroundInsets() {
        return mBackgroundInsets;
    }

    /**
     * Sets the background insets of this {@code DrawableCell}.
     * 
     * @param pInsets An {@link Insets}.
     * @return This instance for method invocation linking.
     */
    public final DrawableCell setBackgroundInsets(final Insets pInsets) {
        return setBackgroundInsets(pInsets.mTop, pInsets.mLeft, pInsets.mBottom, pInsets.mRight);
    }

    /**
     * Sets the background insets of this {@code DrawableCell}.
     * 
     * @param pTop The top insets.
     * @param pLeft The left insets.
     * @param pBottom The bottom insets.
     * @param pRight The right insets.
     * @return This instance for method invocation linking.
     */
    public final DrawableCell setBackgroundInsets(final int pTop, final int pLeft,
            final int pBottom, final int pRight) {
        mBackgroundInsets.mTop = pTop;
        mBackgroundInsets.mLeft = pLeft;
        mBackgroundInsets.mBottom = pBottom;
        mBackgroundInsets.mRight = pRight;
        return this;
    }

    /**
     * Gets the insets of this {@code DrawableCell}.
     * 
     * @return An {@link Insets}.
     */
    public final Insets getInsets() {
        return mInsets;
    }

    /**
     * Sets the insets of this {@code DrawableCell}.
     * 
     * @param pInsets An {@link Insets}.
     * @return This instance for method invocation linking.
     */
    public final DrawableCell setInsets(final Insets pInsets) {
        return setInsets(pInsets.mTop, pInsets.mLeft, pInsets.mBottom, pInsets.mRight);
    }

    /**
     * Sets the insets of this {@code DrawableCell}.
     * 
     * @param pTop The top insets.
     * @param pLeft The left insets.
     * @param pBottom The bottom insets.
     * @param pRight The right insets.
     * @return This instance for method invocation linking.
     */
    public final DrawableCell setInsets(final int pTop, final int pLeft, final int pBottom,
            final int pRight) {
        mInsets.mTop = pTop;
        mInsets.mLeft = pLeft;
        mInsets.mBottom = pBottom;
        mInsets.mRight = pRight;
        return this;
    }

    /**
     * Sets the current size measure for this {@code LayoutCell}.
     * 
     * @param pWidth The width of the new size as an {@code int}.
     * @param pHeight The height of the new size as an {@code int}.
     */
    @Override
    public void setSize(final int pWidth, final int pHeight) {
        super.setSize(pWidth, pHeight);

        if (mBackgroundDrawable != null) {
            layoutDrawable(mBackgroundDrawable, pWidth, pHeight, mBackgroundInsets);
        }

        if (mForegroundDrawable != null) {
            layoutDrawable(mForegroundDrawable, pWidth, pHeight, mInsets);
        }
    }

    protected void layoutDrawable(final Drawable pDrawable, final int pWidth, final int pHeight,
            final Insets pInsets) {

        if (mFillMode == FillPolicy.BOTH) {
            final int drawableWidth = pWidth - pInsets.mLeft - pInsets.mRight;
            final int drawableHeight = pHeight - pInsets.mTop - pInsets.mBottom;
            int x = mLocation.x + pInsets.mLeft;
            int y = mLocation.y + pInsets.mTop;
            pDrawable.setBounds(x, y, x + drawableWidth, y + drawableHeight);
            return;
        }

        int x = pInsets.mLeft;
        int y = pInsets.mTop;

        if (mFillMode == FillPolicy.HORIZONTAL) {
            final int drawableWidth = pWidth - pInsets.mLeft - pInsets.mRight;
            int drawableHeight = pDrawable.getMinimumHeight();
            int maxHeight = pHeight - pInsets.mTop - pInsets.mBottom;
            drawableHeight = (drawableHeight > maxHeight) ? maxHeight : drawableHeight;

            switch (mAnchor) {
                case NORTH:
                case NORTH_WEST:
                case NORTH_EAST: {
                    y = mLocation.y;
                    break;
                }
                case SOUTH:
                case SOUTH_WEST:
                case SOUTH_EAST: {
                    y = mLocation.y + pHeight - drawableHeight;
                    break;
                }
                case WEST:
                case EAST:
                case CENTER: {
                    y = mLocation.y + (pHeight - drawableHeight) / 2;
                    break;
                }
            }

            pDrawable.setBounds(x, y, x + drawableWidth, y + drawableHeight);
        } else if (mFillMode == FillPolicy.VERTICAL) {
            int drawableWidth = pDrawable.getMinimumWidth();
            final int maxWidth = pWidth - pInsets.mLeft - pInsets.mRight;
            drawableWidth = (drawableWidth > maxWidth) ? maxWidth : drawableWidth;
            final int drawableHeight = pHeight - pInsets.mTop - pInsets.mBottom;

            switch (mAnchor) {
                case WEST:
                case SOUTH_WEST:
                case NORTH_WEST: {
                    x = mLocation.x;
                    break;
                }
                case EAST:
                case NORTH_EAST:
                case SOUTH_EAST: {
                    x = mLocation.x + pWidth - drawableWidth;
                    break;
                }
                case NORTH:
                case SOUTH:
                case CENTER: {
                    x = mLocation.x + (pWidth - drawableWidth) / 2;
                    break;
                }
            }

            pDrawable.setBounds(x, y, x + drawableWidth, y + drawableHeight);
        } else {
            int drawableWidth = pDrawable.getMinimumWidth();
            final int maxWidth = pWidth - pInsets.mLeft - pInsets.mRight;
            drawableWidth = (drawableWidth > maxWidth) ? maxWidth : drawableWidth;
            int drawableHeight = pDrawable.getMinimumHeight();
            final int maxHeight = pHeight - pInsets.mTop - pInsets.mBottom;
            drawableHeight = (drawableHeight > maxHeight) ? maxHeight : drawableHeight;

            switch (mAnchor) {
                case NORTH: {
                    x = mLocation.x + (pWidth - drawableWidth) / 2;
                    y = mLocation.y;
                    break;
                }
                case NORTH_EAST: {
                    x = mLocation.x + pWidth - drawableWidth;
                    y = mLocation.y;
                    break;
                }
                case EAST: {
                    x = mLocation.x + pWidth - drawableWidth;
                    y = mLocation.y + (pHeight - drawableHeight) / 2;
                    break;
                }
                case SOUTH_EAST: {
                    x = mLocation.x + pWidth - drawableWidth;
                    y = mLocation.y + pHeight - drawableHeight;
                    break;
                }
                case SOUTH: {
                    x = mLocation.x + (pWidth - drawableWidth) / 2;
                    y = mLocation.y + pHeight - drawableHeight;
                    break;
                }
                case SOUTH_WEST: {
                    x = mLocation.x;
                    y = mLocation.y + pHeight - drawableHeight;
                    break;
                }
                case WEST: {
                    x = mLocation.x;
                    y = mLocation.y + (pHeight - drawableHeight) / 2;
                    break;
                }
                case NORTH_WEST: {
                    x = mLocation.x;
                    y = mLocation.y;
                    break;
                }
                case CENTER: {
                    x = mLocation.x + (pWidth - drawableWidth) / 2;
                    y = mLocation.y + (pHeight - drawableHeight) / 2;
                    break;
                }
            }

            pDrawable.setBounds(x, y, x + drawableWidth, y + drawableHeight);
        }
    }

    /**
     * Calculates the minimum, maximum, and preferred sizes of this {@code LayoutCell}.
     */

    @Override
    protected void measureSizes() {
        // Drawable is not assigned for this DrawableCell or the Drawable
        // is not visible

        mMaximumSize.mWidth = Integer.MAX_VALUE;
        mMaximumSize.mHeight = Integer.MAX_VALUE;

        if (!isVisible()) {
            if (mWidthResizePolicy == ResizePolicy.FIXED) {
                mMinimumSize.mWidth = mFixedSize.mWidth;
                mMaximumSize.mWidth = mFixedSize.mWidth;
                mPreferredSize.mWidth = mFixedSize.mWidth;
            } else {
                mMinimumSize.mWidth = 0;
                mPreferredSize.mWidth = 0;
            }

            if (mHeightResizePolicy != ResizePolicy.FIXED) {
                mMinimumSize.mHeight = mFixedSize.mHeight;
                mMaximumSize.mHeight = mFixedSize.mHeight;
                mPreferredSize.mHeight = mFixedSize.mHeight;
            } else {
                mMinimumSize.mHeight = 0;
                mPreferredSize.mHeight = 0;
            }

            mMaximumSize.mWidth += mInsets.mLeft + mInsets.mRight;
            mMaximumSize.mHeight += mInsets.mTop + mInsets.mBottom;
            mMinimumSize.mWidth += mInsets.mLeft + mInsets.mRight;
            mMinimumSize.mHeight += mInsets.mTop + mInsets.mBottom;
            mPreferredSize.mWidth += mInsets.mLeft + mInsets.mRight;
            mPreferredSize.mHeight += mInsets.mTop + mInsets.mBottom;
            return;
        }

        if (mForegroundDrawable != null) {
            mMinimumSize.mWidth = mForegroundDrawable.getMinimumWidth();
            mMinimumSize.mHeight = mForegroundDrawable.getMinimumHeight();
        } else if (mBackgroundDrawable != null) {
            mMinimumSize.mWidth = mBackgroundDrawable.getMinimumWidth();
            mMinimumSize.mHeight = mBackgroundDrawable.getMinimumHeight();
        }

        switch (mWidthResizePolicy) {
            case FIXED: {
                mMaximumSize.mWidth = mFixedSize.mWidth;
                mMinimumSize.mWidth = mFixedSize.mWidth;
                mPreferredSize.mWidth = mFixedSize.mWidth;
                break;
            }
            case MINIMUM: {
                mPreferredSize.mWidth = mMinimumSize.mWidth;
                break;
            }
            case PREFERRED: {
                if (mForegroundDrawable != null) {
                    mPreferredSize.mWidth = mForegroundDrawable.getIntrinsicWidth();
                } else if (mBackgroundDrawable != null) {
                    mPreferredSize.mWidth = mBackgroundDrawable.getIntrinsicWidth();
                }
                break;
            }
        }

        switch (mHeightResizePolicy) {
            case FIXED: {
                mMaximumSize.mHeight = mFixedSize.mHeight;
                mMinimumSize.mHeight = mFixedSize.mHeight;
                mPreferredSize.mHeight = mFixedSize.mHeight;
                break;
            }
            case MINIMUM: {
                mPreferredSize.mHeight = mMinimumSize.mHeight;
                break;
            }
            case PREFERRED: {
                if (mForegroundDrawable != null) {
                    mPreferredSize.mWidth = mForegroundDrawable.getIntrinsicHeight();
                } else if (mBackgroundDrawable != null) {
                    mPreferredSize.mWidth = mBackgroundDrawable.getIntrinsicHeight();
                }
                break;
            }
        }

        mMaximumSize.mWidth += mInsets.mLeft + mInsets.mRight;
        mMaximumSize.mHeight += mInsets.mTop + mInsets.mBottom;
        mMinimumSize.mWidth += mInsets.mLeft + mInsets.mRight;
        mMinimumSize.mHeight += mInsets.mTop + mInsets.mBottom;
        mPreferredSize.mWidth += mInsets.mLeft + mInsets.mRight;
        mPreferredSize.mHeight += mInsets.mTop + mInsets.mBottom;
    }

    /**
     * Tests whether the layout cell defined by this {@code LayoutCell} is visible or not.
     * 
     * @return A {@code boolean} value.
     */
    @Override
    public boolean isVisible() {
        boolean backgroundVisible = false;
        boolean foregroundVisible = false;

        if (mBackgroundDrawable != null) {
            backgroundVisible = mBackgroundDrawable.isVisible();
        }

        if (mForegroundDrawable != null) {
            foregroundVisible = mForegroundDrawable.isVisible();
        }

        return (backgroundVisible || foregroundVisible);
    }

    /**
     * Sets the anchor defined to this {@code DrawableCell}. The anchor is 
     * used when the drawable is smaller than area calculated for its container
     * {@code LayoutCell}. It determines where, within the cell area, to place 
     * the drawable. The possible anchor values are: {@code CENTER}, 
     * {@code NORTH}, {@code NORTHEAST}, {@code EAST}, 
     * {@code SOUTHEAST}, {@code SOUTH}, {@code SOUTHWEST}, 
     * {@code WEST}, and {@code NORTHWEST}. The default value is 
     * {@code CENTER</code. 
     * 
     * @param pAnchor An {@code Direction} specifying the anchor.
     * @return This instance as a {@code DrawableCell} for method invocation
     *      linking.
     */
    public final DrawableCell anchor(final Anchor pAnchor) {
        return setAnchor(pAnchor);
    }

    /**
     * Sets the fill mode defined to this {@code DrawableCell}. Fill mode
     * is mode is used to specify how the drawable fills up the area calculated 
     * for its container {@code LayoutCell}.  The possible fill mode values are: 
     * {@code NONE}, {@code HORIZONTAL}, {@code VERTICAL}, 
     * and {@code BOTH}. The default value is {@code NONE</code. 
     * 
     * @param pFillMode A {@link FillPolicy}.
     * @return This instance as a {@code DrawableCell}.
     */
    public final DrawableCell fill(final FillPolicy pFillMode) {
        return setFillPolicy(pFillMode);
    }

    /**
     * Sets the insets of this {@code DrawableCell}.
     * 
     * @param pTop The top insets.
     * @param pLeft The left insets.
     * @param pBottom The bottom insets.
     * @param pRight The right insets.
     * @return This instance for method invocation linking.
     */
    public final DrawableCell insets(final int pTop, final int pLeft, final int pBottom,
            final int pRight) {
        return setInsets(pTop, pLeft, pBottom, pRight);
    }
}
