// ============================================================================
// Floxp.com : Java Class Source File
// ============================================================================
//
// Class: CompositeCell
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

import android.graphics.drawable.Drawable;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code CompositeCell} implements {@code LayoutCell} that may contain a horizontally or vertically
 * oriented group of multiple {@link LayoutCell}s.
 *
 * @author Marko Salmela
 */
public abstract class ContainerCell extends LayoutCell {

    /**
     * A list of contained component {@link LayoutCell}s.
     */
    protected final ArrayList<LayoutCell> mComponentCells;

    /**
     * Constructs a new instance of {@code CompositeCell} for the given {@link CellLayout}.
     *
     * @param layout A {@link CellLayout}.
     */
    protected ContainerCell(final CellLayout layout) {
        super(layout);
        mComponentCells = new ArrayList<>();
    }

    /**
     * Constructs a new instance of {@code CompositeCell} for the given {@link CellLayout}.
     * The instance is resizeable according to the specified resize mode.
     *
     * @param layout             A {@link CellLayout}.
     * @param widthResizePolicy  The horizontal {@code ResizePolicy}.
     * @param heightResizePolicy The vertical {@code ResizePolicy}.
     */
    protected ContainerCell(final CellLayout layout,
                            final ResizePolicy widthResizePolicy, final ResizePolicy heightResizePolicy) {
        this(layout);
        mWidthResizePolicy = widthResizePolicy;
        mHeightResizePolicy = heightResizePolicy;
    }

    /**
     * Constructs a new instance of {@code CompositeCell} for the given {@link CellLayout}.
     * The instance defines a layout cell with fixed width and height.
     *
     * @param layout A {@link CellLayout}.
     * @param width  The width of the {@code CompositeCell}.
     * @param height The height of the {@code CompositeCell}.
     */
    protected ContainerCell(final CellLayout layout, final int width, final int height) {
        this(layout);
        mFixedSize.mWidth = width;
        mFixedSize.mHeight = height;
    }

    /**
     * Constructs a new instance of {@code CompositeCell} for the given {@link CellLayout}.
     * The instance defines a layout cell with fixed height and resizeable width.
     *
     * @param layout            A {@link CellLayout}.
     * @param widthResizePolicy The horizontal {@code ResizePolicy}.
     * @param height            The height of the {@code CompositeCell}.
     */
    protected ContainerCell(final CellLayout layout, final ResizePolicy widthResizePolicy, final int height) {
        this(layout);
        mWidthResizePolicy = widthResizePolicy;
        mFixedSize.mHeight = height;
    }

    /**
     * Constructs a new instance of {@code CompositeCell} for the given {@link CellLayout}.
     * The instance defines a layout cell with fixed width and resizeable height.
     *
     * @param layout             A {@link CellLayout}.
     * @param width              The width of the {@code CompositeCell}.
     * @param heightResizePolicy The vertical {@code ResizePolicy} to be added.
     */
    protected ContainerCell(final CellLayout layout, final int width, final ResizePolicy heightResizePolicy) {
        this(layout);
        mFixedSize.mWidth = width;
        mHeightResizePolicy = heightResizePolicy;
    }

    @Override
    public void collectDrawables(final List<Drawable> drawables) {
        for (final LayoutCell cell : mComponentCells) {
            cell.collectDrawables(drawables);
        }
    }

    @Override
    public void collectViews(final List<View> views) {
        for (final LayoutCell cell : mComponentCells) {
            cell.collectViews(views);
        }
    }

    /**
     * Adds the given {@link LayoutCell} to this {@code CompositeCell}.
     *
     * @param cell The {@link LayoutCell} to be added.
     * @return The added {@link LayoutCell} if adding succeeds, otherwise {@code null}.
     */
    public LayoutCell addCell(final LayoutCell cell) {
        if (!mComponentCells.contains(cell)) {
            mComponentCells.add(cell);
            return cell;
        }

        return null;
    }

    /**
     * Tests whether the layout cell defined by this {@link LayoutCell} is visible or not.
     *
     * @return A {@code boolean} value.
     */
    @Override
    public boolean isVisible() {
        final int cellCount = mComponentCells.size();

        for (int i = 0; i < cellCount; i++) {
            final LayoutCell cell = mComponentCells.get(i);
            if (cell.isVisible()) {
                return true;
            }
        }
        return false;
    }
}
