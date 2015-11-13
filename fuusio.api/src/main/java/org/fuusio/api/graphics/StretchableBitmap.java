// ============================================================================
// Fuusio.org : Java Class Source File
// ============================================================================
//
// Class: SkinImage
// Package: Fuusio Graphics API (org.fuusio.api.graphics)
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
import android.graphics.Point;
import android.graphics.Rect;
import android.util.SparseArray;

import org.fuusio.api.util.Dimension;
import org.fuusio.api.util.ExceptionToolkit;

import java.util.StringTokenizer;

/**
 * {@link StretchableBitmap} implements a skinnable image based on the {@link Bitmap}
 * class.
 */

public class StretchableBitmap {

    protected static final int DEFAULT_STATE = 0;
    protected static final int FILL_NONE = 0;
    protected static final int FILL_HORIZONTAL = 1;
    protected static final int FILL_VERTICAL = 2;
    protected static final int FILL_BOTH = 3;

    protected static final String INVALID_COLUMN_SPECIFIER = "Invalid column specifier: '{0}' in '{1}'";
    protected static final String INVALID_ROW_SPECIFIER = "Invalid row specifier: '{0}' in '{1}'";

    /**
     * A 2-dimensional array containing the current tile cell sizes.
     */
    protected Dimension[][] mCellSizes;

    /**
     * Number of column tiles in the defined skin image.
     */
    protected int mColumns;

    /**
     * A 2-dimensional array containing the destination bounding boxes for rendering the individual
     * tile images.
     */
    protected Rect[][] mDstRects;

    /**
     * A 2-dimensional array containing the fill constraints for each tile cell.
     */
    protected int[][] mFillConstraints;

    /**
     * A @link Dimension} defining the fill offset TODO.
     */
    protected Dimension mFillOffset;

    /**
     * The current size of the defined skin image.
     */
    protected Dimension mImageSize;

    /**
     * A 2-dimensional array containing the maximum size for each tile cell.
     */
    protected Dimension[][] mMaximumCellSizes;

    /**
     * The maximum size of the defined skin image.
     */
    protected Dimension mMaximumSize;

    /**
     * A 2-dimensional array containing the minimum size for each tile cell.
     */
    protected Dimension[][] mMinimumCellSizes;

    /**
     * The minimum size of the defined skin image.
     */
    protected Dimension mMinimumSize;

    /**
     * Number of resizable columns tiles in the defined skin image.
     */
    protected int mResizableColumns;

    /**
     * Number of resizable row tiles in the defined skin image.
     */
    protected int mResizableRows;

    /**
     * Number of column tiles in the defined skin image.
     */
    protected int mRows;

    /**
     * An {@link SparseArray} containing the state specific images.
     */
    protected SparseArray<Bitmap> mStateBitmaps;

    /**
     * A 2-dimensional array containing the source bounding boxes for rendering the individual tile
     * images.
     */
    protected Rect[][] mSrcRects;

    /**
     * The current state.
     */
    protected int mState;

    /**
     * Constructs a new instance of {@link StretchableBitmap}.
     */
    protected StretchableBitmap() {
        this((Bitmap) null, null, null);
    }

    /**
     * Constructs a new instance of {@link StretchableBitmap} with the given skin {@link Bitmap} and
     * layout specification.
     *
     * @param bitmap      An {@link Bitmap}.
     * @param columnsSpec A {@link String} specifying the layout columns.
     * @param rowsSpec    A {@link String} specifying the layout rows.
     */
    public StretchableBitmap(final Bitmap bitmap, final String columnsSpec, final String rowsSpec) {
        mImageSize = new Dimension();
        mMaximumSize = new Dimension();
        mMinimumSize = new Dimension();
        mFillOffset = new Dimension();
        mStateBitmaps = new SparseArray<Bitmap>();
        setStateImage(bitmap, DEFAULT_STATE);
        setupLayout(columnsSpec, rowsSpec);
    }

    /**
     * Gets the currently active skin image.
     *
     * @return The default skin image as an {@link Bitmap}.
     */
    public final Bitmap getActiveStateImage() {
        return getStateImage(mState, true);
    }

    /**
     * Gets the default skin image.
     *
     * @return The default skin image as an {@link Bitmap}.
     */
    public Bitmap getDefaultStateImage() {
        return getStateImage(DEFAULT_STATE, true);
    }

    /**
     * Gets the destination rectangles.
     *
     * @return An two-dimensional array of Rectangles containing the destination rectangles
     */
    public final Rect[][] getDestinationBounds() {
        return mDstRects;
    }

    /**
     * Gets the initial size of this {@link StretchableBitmap}.
     *
     * @return The initial size a {@link Dimension}.
     */
    public Dimension getInitialSize() {
        return mImageSize;
    }

    /**
     * Gets the maximum size of this {@link StretchableBitmap}.
     *
     * @return The maximum size a {@link Dimension}.
     */
    public Dimension getMaximumSize() {
        return mMaximumSize;
    }

    /**
     * Gets the minimum size of this {@link StretchableBitmap}.
     *
     * @return The minimum size a {@link Dimension}.
     */
    public Dimension getMinimumSize() {
        return mMinimumSize;
    }

    /**
     * Gets the skin Bitmap for the specified state.
     *
     * @param state           An {@link int} index specifying the state.
     * @param isReturnDefault A {@link boolean} value indicating whether the default skin image is
     *                        returned if the state specific skin Bitmap is not defined.
     * @return An {@link Bitmap}.
     */
    public Bitmap getStateImage(final int state, final boolean isReturnDefault) {
        Bitmap image = mStateBitmaps.get(state);

        if (image == null && isReturnDefault) {
            image = mStateBitmaps.get(DEFAULT_STATE);
        }

        return image;
    }

    /**
     * Sets the skin image for the specified state.
     *
     * @param bitmap The skin image as an {@link Bitmap}. May be {@link null}. If null the
     *               specified state is set not to have a skin image specific for the given state.
     * @param state  An {@link int} index specifying the state.
     */
    public void setStateImage(final Bitmap bitmap, final int state) {
        if (bitmap != null) {
            mStateBitmaps.put(state, bitmap);

            if (mImageSize.mWidth == 0 || mImageSize.mHeight == 0) {
                mImageSize.mWidth = bitmap.getWidth();
                mImageSize.mHeight = bitmap.getHeight();
            }
        } else {
            mStateBitmaps.remove(state);
        }
    }

    /**
     * Gets the source rectangles.
     *
     * @return An two-dimensional array of {@link Rect}s containing the source rectangles.
     */
    public final Rect[][] getSourceRects() {
        return mSrcRects;
    }

    /**
     * Gets the current state.
     *
     * @return The state as an {@link int}.
     */
    public final int getState() {
        return mState;
    }

    /**
     * Sets the current state.
     *
     * @param state The state as an {@link int}.
     */
    public final void setState(final int state) {
        mState = state;
    }

    /**
     * Layouts this {@link StretchableBitmap} for the given size.
     *
     * @param size A {@link Dimension} specifying the size.
     */
    public void doLayout(final Dimension size) {
        int width = size.mWidth - mMinimumSize.mWidth;
        int height = size.mHeight - mMinimumSize.mHeight;

        if (width < 0) {
            width = 0;
        }

        if (height < 0) {
            height = 0;
        }

        if (mResizableColumns > 0) {
            mFillOffset.mWidth = width / mResizableColumns;
        } else {
            mFillOffset.mWidth = 0;
        }

        if (mResizableRows > 0) {
            mFillOffset.mHeight = height / mResizableRows;
        } else {
            mFillOffset.mHeight = 0;
        }

        final Point offset = new Point();
        offset.x = size.mWidth - mMinimumSize.mWidth - mResizableColumns * mFillOffset.mWidth;
        offset.y = size.mHeight - mMinimumSize.mHeight - mResizableRows * mFillOffset.mHeight;

        int x = 0;

        for (int column = 0; column < mColumns; column++) {
            int xOffset = mFillOffset.mWidth;

            if (xOffset < mMinimumCellSizes[column][0].mWidth) {
                xOffset = mMinimumCellSizes[column][0].mWidth;
            } else if (xOffset > mMaximumCellSizes[column][0].mWidth) {
                xOffset = mMaximumCellSizes[column][0].mWidth;
            }

            int y = 0;

            for (int row = 0; row < mRows; row++) {
                mDstRects[column][row].top = y;
                mDstRects[column][row].left = x;

                int yOffset = mFillOffset.mHeight;

                if (yOffset < mMinimumCellSizes[column][row].mHeight) {
                    yOffset = mMinimumCellSizes[column][row].mHeight;
                } else if (yOffset > mMaximumCellSizes[column][row].mHeight) {
                    yOffset = mMaximumCellSizes[column][row].mHeight;
                }

                switch (mFillConstraints[column][row]) {
                    case FILL_NONE: {
                        y += mSrcRects[column][row].height();
                        mDstRects[column][row].bottom = y;
                        mDstRects[column][row].right = x + mSrcRects[column][0].width();
                        break;
                    }
                    case FILL_HORIZONTAL: {
                        mDstRects[column][row].right = x + xOffset
                                + mMinimumCellSizes[column][row].mWidth;
                        y += mSrcRects[column][row].height();
                        mDstRects[column][row].bottom = y;
                        break;
                    }
                    case FILL_VERTICAL: {
                        mDstRects[column][row].bottom = y + yOffset
                                + mMinimumCellSizes[column][row].mHeight;
                        y += yOffset + mMinimumCellSizes[column][row].mHeight;
                        mDstRects[column][row].right = x + mSrcRects[column][0].width();
                        break;
                    }
                    case FILL_BOTH: {
                        mDstRects[column][row].right = x + xOffset
                                + mMinimumCellSizes[column][row].mWidth;
                        mDstRects[column][row].bottom = y + yOffset
                                + mMinimumCellSizes[column][row].mHeight;
                        y += yOffset + mMinimumCellSizes[column][row].mHeight;
                        break;
                    }
                }
            }

            switch (mFillConstraints[column][0]) {
                case FILL_NONE: {
                    x += mSrcRects[column][0].width();
                    break;
                }
                case FILL_HORIZONTAL: {
                    x += xOffset + mMinimumCellSizes[column][0].mWidth;
                    break;
                }
                case FILL_VERTICAL: {
                    x += mSrcRects[column][0].width();
                    break;
                }
                case FILL_BOTH: {
                    x += xOffset + mMinimumCellSizes[column][0].mWidth;
                    break;
                }
            }
        }
    }

    /**
     * Sets up the layout for this {@link StretchableBitmap} according to the given rows and columns
     * specifications.
     *
     * @param columnsSpec A {@link String} containing the specifications for rows. The
     *                    specification consists number of tokens separated by spaces.
     * @param rowsSpec    A {@link String} containing the specifications for rows. The specification
     *                    consists number of tokens separated by spaces.
     */
    protected void setupLayout(final String columnsSpec, final String rowsSpec) {
        final StringTokenizer columnTokenizer = new StringTokenizer(columnsSpec, " ");
        final StringTokenizer rowTokenizer = new StringTokenizer(rowsSpec, " ");
        mRows = rowTokenizer.countTokens();
        mColumns = columnTokenizer.countTokens();

        final String[] rowTokens = new String[mRows];

        for (int i = 0; rowTokenizer.hasMoreTokens(); i++) {
            rowTokens[i] = rowTokenizer.nextToken();
        }

        final String[] columnTokens = new String[mColumns];

        for (int i = 0; columnTokenizer.hasMoreTokens(); i++) {
            columnTokens[i] = columnTokenizer.nextToken();
        }

        int x = 0;

        mMaximumSize.mWidth = 0;
        mMaximumSize.mHeight = 0;
        mMinimumSize.mWidth = 0;
        mMinimumSize.mHeight = 0;

        mCellSizes = new Dimension[mColumns][mRows];
        mFillConstraints = new int[mColumns][mRows];
        mDstRects = new Rect[mColumns][mRows];
        mMaximumCellSizes = new Dimension[mColumns][mRows];
        mMinimumCellSizes = new Dimension[mColumns][mRows];
        mSrcRects = new Rect[mColumns][mRows];

        mResizableColumns = 0;
        mResizableRows = 0;

        for (int column = 0; column < mColumns; column++) {
            String token = columnTokens[column];
            boolean isResizableColumn = false;
            int index = token.indexOf('|');
            int initialWidth = 0;
            int maximumWidth = 0;
            int minimumWidth = 0;

            if (index < 0) {
                if (token.equalsIgnoreCase("*")) {
                    initialWidth = 2;
                    minimumWidth = 0;
                    maximumWidth = 2000;
                } else {
                    try {
                        initialWidth = Integer.parseInt(token);
                        minimumWidth = initialWidth;
                        maximumWidth = initialWidth;
                    } catch (Exception e) {
                        Object[] args = {token, columnsSpec};
                        ExceptionToolkit.throwIllegalArgumentException(INVALID_COLUMN_SPECIFIER,
                                args);
                    }
                }
            } else {
                final StringTokenizer t = new StringTokenizer(token, "|");

                if (t.countTokens() == 3) {
                    String valueString = t.nextToken();

                    try {
                        minimumWidth = Integer.parseInt(valueString);
                    } catch (Exception e) {
                        final Object[] args = {token, columnsSpec};
                        ExceptionToolkit.throwIllegalArgumentException(INVALID_COLUMN_SPECIFIER,
                                args);
                    }

                    valueString = t.nextToken();

                    try {
                        initialWidth = Integer.parseInt(valueString);
                    } catch (Exception e) {
                        final Object[] args = {token, columnsSpec};
                        ExceptionToolkit.throwIllegalArgumentException(INVALID_COLUMN_SPECIFIER,
                                args);
                    }

                    valueString = t.nextToken();

                    try {
                        maximumWidth = Integer.parseInt(valueString);
                    } catch (Exception e) {
                        final Object[] args = {token, columnsSpec};
                        ExceptionToolkit.throwIllegalArgumentException(INVALID_COLUMN_SPECIFIER,
                                args);
                    }
                } else {
                    final Object[] args = {token, columnsSpec};
                    ExceptionToolkit.throwIllegalArgumentException(INVALID_COLUMN_SPECIFIER, args);
                }

                mResizableColumns++;
                isResizableColumn = true;
            }

            int y = 0;

            for (int row = 0; row < mRows; row++) {
                token = rowTokens[row];
                boolean isResizableRow = false;
                index = token.indexOf('|');
                int initialHeight = 0;
                int maximumHeight = 0;
                int minimumHeight = 0;

                if (index < 0) {
                    if (token.equalsIgnoreCase("*")) {
                        initialWidth = 2;
                        minimumWidth = 0;
                        maximumWidth = 2000;
                    } else {
                        try {
                            initialHeight = Integer.parseInt(token);
                            minimumHeight = initialHeight;
                            maximumHeight = initialHeight;
                        } catch (Exception e) {
                            final Object[] args = {token, rowsSpec};
                            ExceptionToolkit.throwIllegalArgumentException(INVALID_ROW_SPECIFIER,
                                    args);
                        }
                    }
                } else {
                    final StringTokenizer t = new StringTokenizer(token, "|");

                    if (t.countTokens() == 3) {
                        String valueString = t.nextToken();

                        try {
                            minimumHeight = Integer.parseInt(valueString);
                        } catch (Exception e) {
                            final Object[] args = {token, rowsSpec};
                            ExceptionToolkit.throwIllegalArgumentException(INVALID_ROW_SPECIFIER,
                                    args);
                        }

                        valueString = t.nextToken();

                        try {
                            initialHeight = Integer.parseInt(valueString);
                        } catch (Exception e) {
                            final Object[] args = {token, rowsSpec};
                            ExceptionToolkit.throwIllegalArgumentException(INVALID_ROW_SPECIFIER,
                                    args);
                        }

                        valueString = t.nextToken();

                        try {
                            maximumHeight = Integer.parseInt(valueString);
                        } catch (Exception e) {
                            final Object[] args = {token, rowsSpec};
                            ExceptionToolkit.throwIllegalArgumentException(
                                    INVALID_COLUMN_SPECIFIER, args);
                        }
                    } else {
                        final Object[] args = {token, rowsSpec};
                        ExceptionToolkit.throwIllegalArgumentException(INVALID_ROW_SPECIFIER, args);
                    }

                    mResizableRows++;
                    isResizableRow = true;
                }

                mDstRects[column][row] = new Rect(x, y, x + initialWidth, y + initialHeight);
                mSrcRects[column][row] = new Rect(x, y, x + initialWidth, y + initialHeight);

                y += initialHeight;

                mCellSizes[column][row] = new Dimension(initialWidth, initialHeight);
                mMaximumCellSizes[column][row] = new Dimension(maximumWidth, maximumHeight);
                mMinimumCellSizes[column][row] = new Dimension(minimumWidth, minimumHeight);

                if (column == 0) {
                    mMaximumSize.mHeight += mMaximumCellSizes[0][row].mHeight;
                    mMinimumSize.mHeight += mMinimumCellSizes[0][row].mHeight;
                }

                if (row == 0) {
                    mMaximumSize.mWidth += mMaximumCellSizes[column][0].mWidth;
                    mMinimumSize.mWidth += mMinimumCellSizes[column][0].mWidth;
                }

                if (isResizableColumn && isResizableRow) {
                    mFillConstraints[column][row] = FILL_BOTH;
                } else if (!isResizableColumn && isResizableRow) {
                    mFillConstraints[column][row] = FILL_VERTICAL;
                } else if (isResizableColumn && !isResizableRow) {
                    mFillConstraints[column][row] = FILL_HORIZONTAL;
                } else {
                    mFillConstraints[column][row] = FILL_NONE;
                }
            }

            x += initialWidth;
        }

        mResizableRows = mResizableRows / mColumns; // TODO
    }
}
