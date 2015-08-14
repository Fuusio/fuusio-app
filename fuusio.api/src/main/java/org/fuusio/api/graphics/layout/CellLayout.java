// ============================================================================
// Floxp.com : Java Class Source File
// ============================================================================
//
// Class: DrawableCellLayout
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

import org.fuusio.api.util.Dimension;
import org.fuusio.api.util.Insets;

import android.view.View;

/**
 * {@link CellLayout} implements a layout manager for defining and managing the layout of
 * {@link android.graphics.drawable.Drawable}s . The layout managing is based on a number of
 * {@link LayoutCell} instances that each define constraints for setting the layout for given set of
 * {@link android.graphics.drawable.Drawable}s.
 * 
 * @author Marko Salmela
 */
public class CellLayout {
    /**
     * The set size of the managed {@link View}.
     */
    protected Dimension mContainerSize;

    /**
     * Top, left, bottom, and right margins of this {@link CellLayout}.
     */
    protected Insets mInsets;

    /**
     * A {@code boolean} flag specifying if the size of manager View can be dynamically changed
     * based on the calculated preferred sizes of the components.
     */
    protected boolean mDynamicallyResized;

    /**
     * A boolean flag indicating whether the layout needs to be re-calculated.
     */
    protected boolean mInvalidated;

    /**
     * The {@link View} whose layout is managed by this {@link CellLayout}.
     */
    protected View mLayoutOwner;

    /**
     * Calculated maximum size of this {@link CellLayout}.
     */
    protected Dimension mMaximumLayoutSize;

    /**
     * Calculated minimum size of this {@link CellLayout}.
     */
    protected Dimension mMinimumLayoutSize;

    /**
     * Calculated preferred size of this {@link CellLayout}.
     */
    protected Dimension mPreferredLayoutSize;

    /**
     * The top level {@link ContainerCell} that hierarchically contains all the other cells.
     */
    protected ContainerCell mRootCell;

    /**
     * Constructs a new instance of {@link CellLayout}.
     */
    public CellLayout() {
        this(null);
    }

    /**
     * Constructs a new instance of {@link CellLayout} for managing the layout of the given
     * {@link View}.
     * 
     * @param pView A {@link View}.
     */
    public CellLayout(final View pView) {
        mLayoutOwner = pView;
        mInsets = new Insets(0, 0, 0, 0);
        mDynamicallyResized = false;
        mInvalidated = false;
        mMaximumLayoutSize = null;
        mMinimumLayoutSize = null;
        mPreferredLayoutSize = null;
    }

    /**
     * Gets the managed {@link View}.
     * 
     * @return A {@link View}.
     */
    protected View getView() {
        return mLayoutOwner;
    }

    /**
     * Gets the default margins for {@link LayoutCell}s capsulating
     * {@link android.graphics.drawable.Drawable}s.
     * 
     * @return The margins as an {@link Insets}.
     */
    public Insets getDefaultComponentInsets() {
        return DrawableCell.getDefaultDrawableInsets();
    }

    /**
     * Sets the default margins for {@link LayoutCell}.
     * 
     * @param pTop The top insets.
     * @param pLeft The left insets.
     * @param pBottom The bottom insets.
     * @param pRight The right insets.
     */
    public void setDefaultComponentInsets(final int pTop, final int pLeft, final int pBottom,
            final int pRight) {
        DrawableCell.setDefaultDrawableInsets(pTop, pLeft, pBottom, pRight);
    }

    /**
     * Sets the layout insets that are used for defining top, left, bottom, and right margins.
     * 
     * @param pInsets An {@link Insets}.
     */
    public void setLayoutInsets(final Insets pInsets) {
        mInsets.mTop = pInsets.mTop;
        mInsets.mLeft = pInsets.mLeft;
        mInsets.mBottom = pInsets.mBottom;
        mInsets.mRight = pInsets.mRight;
    }

    /**
     * Sets this {@link CellLayout} to be dynamically resized depending on the given boolean value.
     * The size of the dynamically resized {@link View} is determined by the preferred sizes of the
     * contained components.
     * 
     * @param pDynamicallyResized A {@code boolean} flag specifying whether this {@link CellLayout}
     *        is dynamically resized.
     */
    public void setDynamicallyResized(final boolean pDynamicallyResized) {
        mDynamicallyResized = pDynamicallyResized;
    }

    /**
     * Gets the root {@link ContainerCell} of the build {@link CellLayout}.
     * 
     * @return A {@link ContainerCell}.
     */
    public ContainerCell getRootCell() {
        return mRootCell;
    }

    /**
     * Sets the root {@link ContainerCell} of the build {@link CellLayout}.
     * 
     * @param pCell A {@link ContainerCell}.
     */
    public void setRootCell(final ContainerCell pCell) {
        mRootCell = pCell;

    }

    /**
     * Creates a new instance of {@link CellLayout} for managing the layout of the given
     * {@link View}.
     * 
     * @param pView A {@link View}.
     * @return The created {@link CellLayout}.
     */
    public static CellLayout create(final View pView) {
        return create(pView, false);
    }

    /**
     * Creates a new instance of {@link CellLayout} for managing the layout of the given
     * {@link View}. The given {@code boolean} flag specifies whether the {@link View} is
     * dynamically resized according to calculated preferred layout size.
     * 
     * @param pView A {@link View}.
     * @param pDynamicallyResized A {@code boolean} flag specifying whether the {@link View} is
     *        dynamically resized.
     * @return The created {@link CellLayout}.
     */
    public static CellLayout create(final View pView, final boolean pDynamicallyResized) {
        final CellLayout layout = new CellLayout(pView);
        layout.setDynamicallyResized(pDynamicallyResized);
        return layout;
    }

    /**
     * Tests whether the assigned {@link View} is to be dynamically resized according to calculated
     * preferred layout size.
     * 
     * @return A {@code boolean} flag specifying whether the {@link View} is dynamically resized.
     */
    public final boolean isDynamicallyResized() {
        return mDynamicallyResized;
    }

    /**
     * Invalidates the layout for the given {@link View}, indicating that if this
     * {@link CellLayout} has cached information it should be discarded.
     * 
     * @param pView The {@link View} to be isInvalidated.
     */
    public void invalidateLayout(final View pView) {
        if (pView != mLayoutOwner) {
            return;
        }

        mInvalidated = true;
        mMaximumLayoutSize = null;
        mMinimumLayoutSize = null;
        mPreferredLayoutSize = null;

        if (mRootCell != null) {
            mRootCell.invalidate();
        }
    }

    /**
     * Lays out the given {@link View}.
     * 
     * @param pView The {@link View} to be laid out.
     */
    public void layoutView(final View pView) {
        if (pView != mLayoutOwner) {
            return;
        }

        if (mRootCell == null) {
            return;
        }

        // Calculate cell bounds

        if (mRootCell.isVisible()) {
            mRootCell.measureSizes();
            mMaximumLayoutSize = mRootCell.getMaximumSize();
            mMinimumLayoutSize = mRootCell.getMinimumSize();
            mPreferredLayoutSize = mRootCell.getPreferredSize();

            if (isDynamicallyResized()) {
                mRootCell.setSize(mPreferredLayoutSize.mWidth, mPreferredLayoutSize.mHeight);
                // TODO view.setSize( preferredLayoutSize );
            } else {
                int width = pView.getMeasuredWidth();
                int height = pView.getMeasuredHeight();

                if (width == 0) {
                    width = 320;
                }
                if (height == 0) {
                    height = 32; // TODO
                }

                mRootCell.setSize(width, height);
            }
        }
    }

    public void layout(final int pWidth, final int pHeight) {

        if (mRootCell == null) {
            return;
        }

        if (mRootCell.isVisible()) {
            mRootCell.setSize(pWidth, pHeight);
        }
    }

    /**
     * Calculates the maximum size dimensions for the specified View, given the components it
     * contains.
     * 
     * @param pView The {@link View} to be laid out.
     * @return The maximum layout size as a {@link Dimension}.
     */
    public Dimension maximumLayoutSize(final View pView) {
        if (pView == mLayoutOwner) {
            if (mMaximumLayoutSize == null) {
                layoutView(pView);
            }

            return mMaximumLayoutSize;
        }

        return null;
    }

    /**
     * Calculates the minimum size dimensions for the specified {@link View}, given the components
     * it contains.
     * 
     * @param pView The {@link View} to be laid out.
     * @return The minimum layout size as a {@link Dimension}.
     */
    public Dimension minimumLayoutSize(final View pView) {
        if (pView == mLayoutOwner) {
            if (mMinimumLayoutSize == null) {
                layoutView(pView);
            }

            return mMinimumLayoutSize;
        }

        return mMinimumLayoutSize;
    }

    public Dimension measure() {
        if (mRootCell.isVisible()) {
            mRootCell.measureSizes();
            mPreferredLayoutSize = mRootCell.getPreferredSize();
        } else {
            mPreferredLayoutSize = new Dimension();
        }

        return mPreferredLayoutSize;
    }
}
