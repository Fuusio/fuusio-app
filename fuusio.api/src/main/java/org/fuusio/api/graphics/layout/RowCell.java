// ============================================================================
// Floxp.com : Java Class Source File
// ============================================================================
//
// Class: RowCell
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
 * {@code RowCell} implements {@link ContainerCell} that may contain a horizontally oriented group
 * of multiple {@link LayoutCell}s.
 * 
 * @author Marko Salmela
 */
public class RowCell extends ContainerCell {

    /**
     * Constructs a new instance of {@code RowCell} for the given {@link CellLayout}. The instance
     * is resizeable according to the specified resize mode.
     * 
     * @param pLayout A {@link CellLayout}.
     * @param pWidthResizePolicy The horizontal {@link ResizePolicy}.
     * @param pHeightResizePolicy The vertical {@link ResizePolicy}.
     */
    public RowCell(final CellLayout pLayout, final ResizePolicy pWidthResizePolicy,
            final ResizePolicy pHeightResizePolicy) {
        super(pLayout, pWidthResizePolicy, pHeightResizePolicy);
    }

    /**
     * Constructs a new instance of {@code RowCell} for the given {@link CellLayout}. The instance
     * defines a layout cell with fixed width and height.
     * 
     * @param pLayout A {@link CellLayout}.
     * @param pWidth The width of the {@code RowCell}.
     * @param pHeight The height of the {@code RowCell}.
     */
    public RowCell(final CellLayout pLayout, final int pWidth, final int pHeight) {
        super(pLayout, pWidth, pHeight);
    }

    /**
     * Constructs a new instance of {@code RowCell} for the given {@link CellLayout}. The instance
     * defines a layout cell with fixed height and resizeable width.
     * 
     * @param pLayout A {@link CellLayout}.
     * @param pWidthResizePolicy The horizontal {@link ResizePolicy}.
     * @param pHeight The height of the {@code RowCell}.
     */
    public RowCell(final CellLayout pLayout, final ResizePolicy pWidthResizePolicy,
            final int pHeight) {
        super(pLayout, pWidthResizePolicy, pHeight);
    }

    /**
     * Constructs a new instance of {@code RowCell} for the given {@link CellLayout}. The instance
     * defines a layout cell with fixed width and resizeable height.
     * 
     * @param pLayout A {@link CellLayout}.
     * @param pWidth The width of the {@code RowCell}.
     * @param pHeightResizePolicy The vertical {@link ResizePolicy} to be added.
     */
    public RowCell(final CellLayout pLayout, final int pWidth,
            final ResizePolicy pHeightResizePolicy) {
        super(pLayout, pWidth, pHeightResizePolicy);
    }

    /**
     * Gets the current size calculated for this {@code LayoutCell}.
     * 
     * @param width The width of the new size as an {@code int}.
     * @param height The height of the new size as an {@code int}.
     */
    @Override
    public void setSize(final int width, final int height) {
        super.setSize(width, height);

        final int cellCount = mComponentCells.size();
        int x = mLocation.x;
        int y = mLocation.y;

        if (mLayout.isDynamicallyResized()) {
            int prefRowWidth = 0;
            int resizeableCount = 0;
            int setCellHeight = height;

            for (int i = 0; i < cellCount; i++) {
                final LayoutCell cell = mComponentCells.get(i);
                final Dimension prefSize = cell.getPreferredSize();
                prefRowWidth += prefSize.mWidth;

                if (cell.getWidthResizePolicy() == ResizePolicy.PREFERRED) {
                    resizeableCount++;
                }

                if (prefSize.mHeight > setCellHeight) {
                    setCellHeight = prefSize.mHeight;
                }
            }

            final int offset = (resizeableCount == 0) ? 0
                    : ((width - prefRowWidth) / resizeableCount);
            final int lastOffset = offset + (width - prefRowWidth - resizeableCount * offset);

            for (int i = 0; i < cellCount; i++) {
                final LayoutCell cell = mComponentCells.get(i);
                final Dimension prefSize = cell.getPreferredSize();
                int cellWidth = prefSize.mWidth;
                int cellHeight = setCellHeight; // (prefSize.height > height) ? prefSize.height :
                                                // height;

                if (cell.getWidthResizePolicy() == ResizePolicy.PREFERRED) {
                    cellWidth += (i < cellCount - 1) ? offset : lastOffset;
                }

                cell.setLocation(x, y);
                cell.setSize(cellWidth, cellHeight);
                x += cellWidth;
            }
        } else {
            int resizeableCount = 0;
            int extraWidth = width;

            for (int i = 0; i < cellCount; i++) {
                final LayoutCell cell = mComponentCells.get(i);
                final Dimension minSize = cell.getMinimumSize();
                final Dimension prefSize = cell.getPreferredSize();

                if (cell.mWidthResizePolicy != ResizePolicy.FIXED) {
                    resizeableCount++;
                    extraWidth -= minSize.mWidth;
                } else {
                    extraWidth -= prefSize.mWidth;
                }
            }

            int widthOffset = 0;

            if (resizeableCount > 0) {
                widthOffset = extraWidth / resizeableCount;
            }

            int offset = extraWidth - resizeableCount * widthOffset;

            for (int i = 0; i < cellCount; i++) {
                final LayoutCell cell = mComponentCells.get(i);
                final Dimension minSize = cell.getMinimumSize();
                final Dimension prefSize = cell.getPreferredSize();
                int cellWidth = minSize.mWidth;
                int cellHeight = (prefSize.mHeight > height) ? prefSize.mHeight : height;

                if (cell.mWidthResizePolicy != ResizePolicy.FIXED) {
                    cellWidth += widthOffset + offset;
                    offset = 0;
                }

                cell.setLocation(x, y);
                cell.setSize(cellWidth, cellHeight);
                x += cellWidth;
            }
        }
    }

    /**
     * Adds the given {@code LayoutCell} to this {@code RowCell}.
     * 
     * @param cell The {@code LayoutCell} to be added.
     * @return The reference for {@code RowCell} for method invocation linking.
     */

    @Override
    public RowCell addCell(final LayoutCell cell) {
        if (!mComponentCells.contains(cell)) {
            mComponentCells.add(cell);
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

        int cellCount = mComponentCells.size();
        int maxWidth = Integer.MAX_VALUE;
        int maxHeight = Integer.MAX_VALUE;
        int minWidth = 0;
        int minHeight = 0;
        int prefWidth = 0;
        int prefHeight = 0;

        for (int i = 0; i < cellCount; i++) {
            LayoutCell cell = mComponentCells.get(i);
            cell.measureSizes();

            Dimension maxCellSize = cell.getMaximumSize();
            Dimension minCellSize = cell.getMinimumSize();
            Dimension prefCellSize = cell.getPreferredSize();

            maxWidth += maxCellSize.mWidth;
            maxHeight = (maxCellSize.mHeight < maxHeight) ? maxCellSize.mHeight : maxHeight;

            minWidth += minCellSize.mWidth;
            minHeight = (minCellSize.mHeight > minHeight) ? minCellSize.mHeight : minHeight;

            prefWidth += prefCellSize.mWidth;
            prefHeight = (prefCellSize.mHeight > prefHeight) ? prefCellSize.mHeight : prefHeight;
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

    public ColumnCell addColumn(final int pWidth) {
        final ColumnCell column = new ColumnCell(mLayout, pWidth, ResizePolicy.PREFERRED);
        return column;
    }

    public DrawableCell addDrawable(final Drawable pDrawable, final int pWidth) {
        final DrawableCell drawable = new DrawableCell(mLayout, pDrawable, pWidth,
                ResizePolicy.PREFERRED);
        return drawable;
    }

    public DrawableCell addDrawable(final Drawable pDrawable) {
        final DrawableCell drawable = new DrawableCell(mLayout, pDrawable, ResizePolicy.PREFERRED,
                ResizePolicy.PREFERRED);
        return drawable;
    }

    public ViewCell addView(final View pView) {
        final ViewCell view = new ViewCell(mLayout, pView, ResizePolicy.PREFERRED,
                ResizePolicy.PREFERRED);
        return view;
    }

    public ViewCell addView(final View pView, final int pWidth) {
        final ViewCell view = new ViewCell(mLayout, pView, pWidth, ResizePolicy.PREFERRED);
        return view;
    }
}
