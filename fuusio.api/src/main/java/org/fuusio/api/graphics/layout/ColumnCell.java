// ============================================================================
// Floxp.com : Java Class Source File
// ============================================================================
//
// Class: ColumnCell
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

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * {@code ColumnCell} implements {@link ContainerCell} that may contain a vertically oriented group
 * of multiple {@link LayoutCell}s.
 * 
 * @author Marko Salmela
 */
public class ColumnCell extends ContainerCell {

    /**
     * Constructs a new instance of {@code ColumnCell} for the given {@code CellLayout}. The
     * instance is resizeable according to the specified resize mode.
     * 
     * @param pLayout A {@code CellLayout}.
     * @param pWidthResizePolicy The horizontal {@code ResizePolicy}.
     * @param pHeightResizePolicy The vertical {@code ResizePolicy}.
     */
    public ColumnCell(final CellLayout pLayout, final ResizePolicy pWidthResizePolicy,
            final ResizePolicy pHeightResizePolicy) {
        super(pLayout, pWidthResizePolicy, pHeightResizePolicy);
    }

    /**
     * Constructs a new instance of {@code ColumnCell} for the given {@code CellLayout}. The
     * instance defines a layout cell with fixed width and height.
     * 
     * @param pLayout A {@code CellLayout}.
     * @param pWidth The width of the {@code CompositeCell}.
     * @param pHeight The height of the {@code CompositeCell}.
     */
    public ColumnCell(final CellLayout pLayout, final int pWidth, final int pHeight) {
        super(pLayout, pWidth, pHeight);
    }

    /**
     * Constructs a new instance of {@code ColumnCell} for the given {@code CellLayout}. The
     * instance defines a layout cell with fixed height and resizeable width.
     * 
     * @param pLayout A {@code CellLayout}.
     * @param pWidthResizePolicy The horizontal {@code ResizePolicy}.
     * @param pHeight The height of the {@code CompositeCell}.
     */
    public ColumnCell(final CellLayout pLayout, final ResizePolicy pWidthResizePolicy,
            final int pHeight) {
        super(pLayout, pWidthResizePolicy, pHeight);
    }

    /**
     * Constructs a new instance of {@code ColumnCell} for the given {@code CellLayout}. The
     * instance defines a layout cell with fixed width and resizeable height.
     * 
     * @param pLayout A {@code CellLayout}.
     * @param pWidth The width of the {@code CompositeCell}.
     * @param pHeightResizePolicy The vertical {@code ResizePolicy} to be added.
     */
    public ColumnCell(final CellLayout pLayout, final int pWidth,
            final ResizePolicy pHeightResizePolicy) {
        super(pLayout, pWidth, pHeightResizePolicy);
    }

    /**
     * Gets the current size calculated for this {@code LayoutCell}.
     * 
     * @param pWidth The width of the new size as an {@code int}.
     * @param pHeight The height of the new size as an {@code int}.
     */
    @Override
    public void setSize(final int pWidth, final int pHeight) {
        super.setSize(pWidth, pHeight);

        final int cellCount = mComponentCells.size();
        int x = mLocation.x;
        int y = mLocation.y;

        if (mLayout.isDynamicallyResized()) {
            int prefColumHeight = 0;
            int setCellWidth = pWidth;
            int resizeableCount = 0;

            for (int i = 0; i < cellCount; i++) {
                final LayoutCell cell = mComponentCells.get(i);
                final Dimension prefSize = cell.getPreferredSize();
                prefColumHeight += prefSize.mHeight;

                if (cell.getHeightResizePolicy() == ResizePolicy.PREFERRED) {
                    resizeableCount++;
                }

                if (prefSize.mWidth > setCellWidth) {
                    setCellWidth = prefSize.mWidth;
                }
            }

            int offset = (resizeableCount == 0) ? 0
                    : ((pHeight - prefColumHeight) / resizeableCount);
            int lastOffset = offset + (pHeight - prefColumHeight - resizeableCount * offset);
            final int count = mComponentCells.size();

            for (int i = 0; i < count; i++) {
                final LayoutCell cell = mComponentCells.get(i);
                final Dimension prefSize = cell.getPreferredSize();
                int cellWidth = setCellWidth; // (prefSize.width > width) ? prefSize.width : width;
                int cellHeight = prefSize.mHeight;

                if (cell.getHeightResizePolicy() == ResizePolicy.PREFERRED) {
                    cellHeight += (i < count - 1) ? offset : lastOffset;
                }

                cell.setLocation(x, y);
                cell.setSize(cellWidth, cellHeight);
                y += cellHeight;
            }
        } else {
            int resizeableCount = 0;
            int extraHeight = pHeight;

            for (int i = 0; i < cellCount; i++) {
                final LayoutCell cell = mComponentCells.get(i);
                final Dimension minSize = cell.getMinimumSize();
                final Dimension prefSize = cell.getPreferredSize();

                if (cell.mHeightResizePolicy != ResizePolicy.FIXED) {
                    resizeableCount++;
                    extraHeight -= minSize.mHeight;
                } else {
                    extraHeight -= prefSize.mHeight;
                }
            }

            int heightOffset = 0;

            if (resizeableCount > 0) {
                heightOffset = extraHeight / resizeableCount;
            }

            int offset = extraHeight - resizeableCount * heightOffset;

            for (LayoutCell cell : mComponentCells) {
                final Dimension minSize = cell.getMinimumSize();
                final Dimension prefSize = cell.getPreferredSize();
                int cellWidth = (prefSize.mWidth > pWidth) ? prefSize.mWidth : pWidth;
                int cellHeight = minSize.mHeight;

                if (cell.mHeightResizePolicy != ResizePolicy.FIXED) {
                    cellHeight += heightOffset + offset;
                    offset = 0;
                }

                cell.setLocation(x, y);
                cell.setSize(cellWidth, cellHeight);
                y += cellHeight;
            }
        }
    }

    /**
     * Adds the given {@code LayoutCell} to this {@code ColumnCell}.
     * 
     * @param pCell The {@code LayoutCell} to be added.
     * @return The reference for {@code ColumnCell} for method invocation linking.
     */

    @Override
    public ColumnCell addCell(final LayoutCell pCell) {
        if (!mComponentCells.contains(pCell)) {
            mComponentCells.add(pCell);
        }

        return this;
    }

    /**
     * Calculates the minimum, maximum, and preferred sizes of this {@code LayoutCell}.
     */

    @Override
    protected void measureSizes() {
        if (!isVisible()) {
            if (mWidthResizePolicy == ResizePolicy.FIXED) {
                mMinimumSize.mWidth = mFixedSize.mWidth;
                mMaximumSize.mWidth = mFixedSize.mWidth;
                mPreferredSize.mWidth = mFixedSize.mWidth;
            } else {
                mMinimumSize.mWidth = 0;
                mMaximumSize.mWidth = Integer.MAX_VALUE;
                mPreferredSize.mWidth = 0;
            }

            if (mHeightResizePolicy != ResizePolicy.FIXED) {
                mMinimumSize.mHeight = mFixedSize.mHeight;
                mMaximumSize.mHeight = mFixedSize.mHeight;
                mPreferredSize.mHeight = mFixedSize.mHeight;
            } else {
                mMinimumSize.mHeight = 0;
                mMaximumSize.mHeight = Integer.MAX_VALUE;
                mPreferredSize.mHeight = 0;
            }

            return;
        }

        resetSizes();

        final int cellCount = mComponentCells.size();
        int maxWidth = 0;
        int maxHeight = 0;
        int minWidth = 0;
        int minHeight = 0;
        int prefWidth = 0;
        int prefHeight = 0;

        for (int i = 0; i < cellCount; i++) {
            final LayoutCell cell = mComponentCells.get(i);
            cell.measureSizes();

            Dimension maxCellSize = cell.getMaximumSize();
            Dimension minCellSize = cell.getMinimumSize();
            Dimension prefCellSize = cell.getPreferredSize();

            maxWidth = (maxCellSize.mWidth > maxWidth) ? maxCellSize.mWidth : maxWidth;
            maxHeight += maxCellSize.mHeight;

            minWidth = (minCellSize.mWidth > minWidth) ? minCellSize.mWidth : minWidth;
            minHeight += minCellSize.mHeight;

            prefWidth = (prefCellSize.mWidth > prefWidth) ? prefCellSize.mWidth : prefWidth;
            prefHeight += prefCellSize.mHeight;
        }

        mMaximumSize.mWidth = maxWidth;
        mMaximumSize.mHeight = maxHeight;

        mMinimumSize.mWidth = minWidth;
        mMinimumSize.mHeight = minHeight;

        if (mWidthResizePolicy == ResizePolicy.MINIMUM) {
            mPreferredSize.mWidth = minWidth;
        } else if (mWidthResizePolicy == ResizePolicy.PREFERRED) {
            mPreferredSize.mWidth = prefWidth;
        } else if (mWidthResizePolicy == ResizePolicy.FIXED) {
            mMinimumSize.mWidth = mFixedSize.mWidth;
            mMaximumSize.mWidth = mFixedSize.mWidth;
            mPreferredSize.mWidth = mFixedSize.mWidth;
        }

        if (mHeightResizePolicy == ResizePolicy.MINIMUM) {
            mPreferredSize.mHeight = minHeight;
        } else if (mHeightResizePolicy == ResizePolicy.PREFERRED) {
            mPreferredSize.mHeight = prefHeight;
        } else if (mHeightResizePolicy == ResizePolicy.FIXED) {
            mMinimumSize.mHeight = mFixedSize.mHeight;
            mMaximumSize.mHeight = mFixedSize.mHeight;
            mPreferredSize.mHeight = mFixedSize.mHeight;
        }
    }

    public RowCell addRow(final int pHeight) {
        final RowCell row = new RowCell(mLayout, ResizePolicy.PREFERRED, pHeight);
        return row;
    }

    public DrawableCell addDrawable(final Drawable pDrawable) {
        final DrawableCell drawable = new DrawableCell(mLayout, pDrawable, ResizePolicy.PREFERRED,
                ResizePolicy.PREFERRED);
        return drawable;
    }

    public DrawableCell addDrawable(final Drawable pDrawable, final int pHeight) {
        final DrawableCell drawable = new DrawableCell(mLayout, pDrawable, ResizePolicy.PREFERRED,
                pHeight);
        return drawable;
    }

    public ViewCell addView(final View pView) {
        final ViewCell view = new ViewCell(mLayout, pView, ResizePolicy.PREFERRED,
                ResizePolicy.PREFERRED);
        return view;
    }

    public ViewCell addView(final View pView, final int pHeight) {
        final ViewCell view = new ViewCell(mLayout, pView, ResizePolicy.PREFERRED, pHeight);
        return view;
    }
}
