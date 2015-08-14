/**
 * Fuusio.org
 * 
 * Copyright (C) Marko Salmela 2013. All rights reserved.
 * 
 */

package org.fuusio.api.graphics.layout;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * 
 */
public class CellLayoutBuilder {

    private final CellLayout mLayout;
    private ContainerCell mCurrentContainer;

    public CellLayoutBuilder(final CellLayout pLayout) {
        mLayout = pLayout;
        mCurrentContainer = mLayout.getRootCell();
        // assert (mCurrentContainer != null);
    }

    public final ContainerCell getCurrentContainer() {
        if (mCurrentContainer == null && mLayout != null) {
            mCurrentContainer = mLayout.getRootCell();
        }
        return mCurrentContainer;
    }

    public void setCurrentContainer(final ContainerCell pCurrentContainer) {
        mCurrentContainer = pCurrentContainer;
    }

    public ColumnCell addColumn(final ResizePolicy pWidthResizePolicy,
            final ResizePolicy pHeightResizePolicy) {
        final ColumnCell column = new ColumnCell(mLayout, pWidthResizePolicy, pHeightResizePolicy);
        mCurrentContainer.addCell(column);
        return column;
    }

    public ColumnCell addColumn(final int pWidth, final int pHeight) {
        final ColumnCell column = new ColumnCell(mLayout, pWidth, pHeight);
        getCurrentContainer().addCell(column);
        return column;
    }

    public ColumnCell addColumn(final ResizePolicy pWidthResizePolicy, final int pHeight) {
        final ColumnCell column = new ColumnCell(mLayout, pWidthResizePolicy, pHeight);
        getCurrentContainer().addCell(column);
        return column;
    }

    public ColumnCell addColumn(final int pWidth, final ResizePolicy pHeightResizePolicy) {
        final ColumnCell column = new ColumnCell(mLayout, pWidth, pHeightResizePolicy);
        getCurrentContainer().addCell(column);
        return column;
    }

    public RowCell addRow(final ResizePolicy pWidthResizePolicy,
            final ResizePolicy pHeightResizePolicy) {
        final RowCell row = new RowCell(mLayout, pWidthResizePolicy, pHeightResizePolicy);
        getCurrentContainer().addCell(row);
        return row;
    }

    public RowCell addRow(final int pWidth, final int pHeight) {
        final RowCell row = new RowCell(mLayout, pWidth, pHeight);
        getCurrentContainer().addCell(row);
        return row;
    }

    public RowCell addRow(final ResizePolicy pWidthResizePolicy, final int pHeight) {
        final RowCell row = new RowCell(mLayout, pWidthResizePolicy, pHeight);
        getCurrentContainer().addCell(row);
        return row;
    }

    public RowCell addRow(final int pWidth, final ResizePolicy pHeightResizePolicy) {
        final RowCell row = new RowCell(mLayout, pWidth, pHeightResizePolicy);
        getCurrentContainer().addCell(row);
        return row;
    }

    public Spacer addSpacer() {
        final Spacer spacer = new Spacer(mLayout);
        getCurrentContainer().addCell(spacer);
        return spacer;
    }

    public Spacer addSpacer(final FillPolicy pFillMode, final int pFixedWidth,
            final int pFixedHeight) {
        final Spacer spacer = new Spacer(mLayout, pFillMode, pFixedWidth, pFixedHeight);
        getCurrentContainer().addCell(spacer);
        return spacer;
    }

    public Spacer addSpacer(final Spacer pSource) {
        final Spacer spacer = new Spacer(mLayout, pSource);
        getCurrentContainer().addCell(spacer);
        return spacer;
    }

    public DrawableCell addDrawable(final CellLayout pLayout, final Drawable pForegroundDrawable) {
        final DrawableCell cell = new DrawableCell(mLayout, pForegroundDrawable);
        getCurrentContainer().addCell(cell);
        return cell;
    }

    public DrawableCell addDrawable(final Drawable pForegroundDrawable,
            final ResizePolicy pWidthResizePolicy, final ResizePolicy pHeightResizePolicy) {
        final DrawableCell cell = new DrawableCell(mLayout, pForegroundDrawable);
        getCurrentContainer().addCell(cell);
        return cell;
    }

    public DrawableCell addDrawable(final Drawable pForegroundDrawable, final int pWidth,
            final int pHeight) {
        final DrawableCell cell = new DrawableCell(mLayout, pForegroundDrawable, pWidth, pHeight);
        getCurrentContainer().addCell(cell);
        return cell;
    }

    public DrawableCell addDrawable(final Drawable pForegroundDrawable,
            final ResizePolicy pWidthResizePolicy, final int pHeight) {
        final DrawableCell cell = new DrawableCell(mLayout, pForegroundDrawable,
                pWidthResizePolicy, pHeight);
        getCurrentContainer().addCell(cell);
        return cell;
    }

    public DrawableCell addDrawable(final Drawable pForegroundDrawable, final int pWidth,
            final ResizePolicy pHeightResizePolicy) {
        final DrawableCell cell = new DrawableCell(mLayout, pForegroundDrawable, pWidth,
                pHeightResizePolicy);
        getCurrentContainer().addCell(cell);
        return cell;
    }

    public ViewCell addView(final CellLayout pLayout, final View pView) {
        final ViewCell cell = new ViewCell(mLayout, pView);
        getCurrentContainer().addCell(cell);
        return cell;
    }

    public ViewCell addView(final View pView, final ResizePolicy pWidthResizePolicy,
            final ResizePolicy pHeightResizePolicy) {
        final ViewCell cell = new ViewCell(mLayout, pView);
        getCurrentContainer().addCell(cell);
        return cell;
    }

    public ViewCell addView(final View pView, final int pWidth, final int pHeight) {
        final ViewCell cell = new ViewCell(mLayout, pView, pWidth, pHeight);
        getCurrentContainer().addCell(cell);
        return cell;
    }

    public ViewCell addView(final View pView, final ResizePolicy pWidthResizePolicy,
            final int pHeight) {
        final ViewCell cell = new ViewCell(mLayout, pView, pWidthResizePolicy, pHeight);
        getCurrentContainer().addCell(cell);
        return cell;
    }

    public ViewCell addView(final View pView, final int pWidth,
            final ResizePolicy pHeightResizePolicy) {
        final ViewCell cell = new ViewCell(mLayout, pView, pWidth, pHeightResizePolicy);
        getCurrentContainer().addCell(cell);
        return cell;
    }
}
