/**
 * Fuusio.org
 * <p/>
 * Copyright (C) Marko Salmela 2013. All rights reserved.
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

    public CellLayoutBuilder(final CellLayout layout) {
        mLayout = layout;
        mCurrentContainer = mLayout.getRootCell();
        // assert (mCurrentContainer != null);
    }

    public final ContainerCell getCurrentContainer() {
        if (mCurrentContainer == null && mLayout != null) {
            mCurrentContainer = mLayout.getRootCell();
        }
        return mCurrentContainer;
    }

    public void setCurrentContainer(final ContainerCell currentContainer) {
        mCurrentContainer = currentContainer;
    }

    public ColumnCell addColumn(final ResizePolicy widthResizePolicy,
                                final ResizePolicy heightResizePolicy) {
        final ColumnCell column = new ColumnCell(mLayout, widthResizePolicy, heightResizePolicy);
        mCurrentContainer.addCell(column);
        return column;
    }

    public ColumnCell addColumn(final int width, final int height) {
        final ColumnCell column = new ColumnCell(mLayout, width, height);
        getCurrentContainer().addCell(column);
        return column;
    }

    public ColumnCell addColumn(final ResizePolicy widthResizePolicy, final int height) {
        final ColumnCell column = new ColumnCell(mLayout, widthResizePolicy, height);
        getCurrentContainer().addCell(column);
        return column;
    }

    public ColumnCell addColumn(final int width, final ResizePolicy heightResizePolicy) {
        final ColumnCell column = new ColumnCell(mLayout, width, heightResizePolicy);
        getCurrentContainer().addCell(column);
        return column;
    }

    public RowCell addRow(final ResizePolicy widthResizePolicy,
                          final ResizePolicy heightResizePolicy) {
        final RowCell row = new RowCell(mLayout, widthResizePolicy, heightResizePolicy);
        getCurrentContainer().addCell(row);
        return row;
    }

    public RowCell addRow(final int width, final int height) {
        final RowCell row = new RowCell(mLayout, width, height);
        getCurrentContainer().addCell(row);
        return row;
    }

    public RowCell addRow(final ResizePolicy widthResizePolicy, final int height) {
        final RowCell row = new RowCell(mLayout, widthResizePolicy, height);
        getCurrentContainer().addCell(row);
        return row;
    }

    public RowCell addRow(final int width, final ResizePolicy heightResizePolicy) {
        final RowCell row = new RowCell(mLayout, width, heightResizePolicy);
        getCurrentContainer().addCell(row);
        return row;
    }

    public Spacer addSpacer() {
        final Spacer spacer = new Spacer(mLayout);
        getCurrentContainer().addCell(spacer);
        return spacer;
    }

    public Spacer addSpacer(final FillPolicy fillMode, final int fixedWidth,
                            final int fixedHeight) {
        final Spacer spacer = new Spacer(mLayout, fillMode, fixedWidth, fixedHeight);
        getCurrentContainer().addCell(spacer);
        return spacer;
    }

    public Spacer addSpacer(final Spacer source) {
        final Spacer spacer = new Spacer(mLayout, source);
        getCurrentContainer().addCell(spacer);
        return spacer;
    }

    public DrawableCell addDrawable(final CellLayout layout, final Drawable foregroundDrawable) {
        final DrawableCell drawableCell = new DrawableCell(mLayout, foregroundDrawable);
        getCurrentContainer().addCell(drawableCell);
        return drawableCell;
    }

    public DrawableCell addDrawable(final Drawable foregroundDrawable,
                                    final ResizePolicy widthResizePolicy, final ResizePolicy heightResizePolicy) {
        final DrawableCell drawableCell = new DrawableCell(mLayout, foregroundDrawable);
        getCurrentContainer().addCell(drawableCell);
        return drawableCell;
    }

    public DrawableCell addDrawable(final Drawable foregroundDrawable, final int width,
                                    final int height) {
        final DrawableCell drawableCell = new DrawableCell(mLayout, foregroundDrawable, width, height);
        getCurrentContainer().addCell(drawableCell);
        return drawableCell;
    }

    public DrawableCell addDrawable(final Drawable foregroundDrawable,
                                    final ResizePolicy widthResizePolicy, final int height) {
        final DrawableCell drawableCell = new DrawableCell(mLayout, foregroundDrawable,
                widthResizePolicy, height);
        getCurrentContainer().addCell(drawableCell);
        return drawableCell;
    }

    public DrawableCell addDrawable(final Drawable foregroundDrawable, final int width,
                                    final ResizePolicy heightResizePolicy) {
        final DrawableCell drawableCell = new DrawableCell(mLayout, foregroundDrawable, width,
                heightResizePolicy);
        getCurrentContainer().addCell(drawableCell);
        return drawableCell;
    }

    public ViewCell addView(final CellLayout layout, final View view) {
        final ViewCell viewCell = new ViewCell(mLayout, view);
        getCurrentContainer().addCell(viewCell);
        return viewCell;
    }

    public ViewCell addView(final View view, final ResizePolicy widthResizePolicy,
                            final ResizePolicy heightResizePolicy) {
        final ViewCell viewCell = new ViewCell(mLayout, view);
        getCurrentContainer().addCell(viewCell);
        return viewCell;
    }

    public ViewCell addView(final View view, final int width, final int height) {
        final ViewCell cell = new ViewCell(mLayout, view, width, height);
        getCurrentContainer().addCell(cell);
        return cell;
    }

    public ViewCell addView(final View view, final ResizePolicy widthResizePolicy,
                            final int height) {
        final ViewCell viewCell = new ViewCell(mLayout, view, widthResizePolicy, height);
        getCurrentContainer().addCell(viewCell);
        return viewCell;
    }

    public ViewCell addView(final View view, final int width,
                            final ResizePolicy heightResizePolicy) {
        final ViewCell viewCell = new ViewCell(mLayout, view, width, heightResizePolicy);
        getCurrentContainer().addCell(viewCell);
        return viewCell;
    }
}
