// ============================================================================
// Floxp.com : Java Class Source File
// ============================================================================
//
// Class: Spacer
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

import android.graphics.drawable.Drawable;

/**
 * {@code Spacer} implements {@link LayoutCell} that is used to just fill space horizontally,
 * vertically, or both directions.
 * 
 * @author Marko Salmela
 */
public class Spacer extends LayoutCell {

    /**
     * {@code FillPolicy} specifies how the {@code Spacer} fills this cell.
     */
    protected FillPolicy mFillMode;

    /**
     * The fixed height of this {@code Spacer}. Value is set to be -1 if this {@code Spacer} if this
     * spacer does not have fixed height.
     */
    protected int mFixedHeight;

    /**
     * The fixed width of this {@code Spacer}. Value is set to be -1 if this {@code Spacer} if this
     * spacer does not have width height.
     */
    protected int mFixedWidth;

    /**
     * Constructs a new instance of {@code Spacer} for the given {@code CellLayout}. By default,
     * this instance fills space both horizontally and vertically.
     * 
     * @param pLayout A {@code CellLayout}.
     */
    public Spacer(final CellLayout pLayout) {
        this(pLayout, FillPolicy.BOTH, -1, -1);
    }

    /**
     * Constructs a new instance of {@code Spacer} for the given {@code CellLayout}. The instance
     * fills space according to the given {@code FillPolicy}.
     * 
     * @param pLayout A {@code CellLayout}.
     * @param pFillMode A {@code FillPolicy}.
     * @param pFixedWidth The fixed width of this {@code Spacer}. Value is -1 if this {@code Spacer}
     *        if this spacer does not have fixed width.
     * @param pFixedHeight The fixed height of this {@code Spacer}. Value is -1 if this
     *        {@code Spacer} if this spacer does not have fixed height.
     */
    public Spacer(final CellLayout pLayout, final FillPolicy pFillMode, final int pFixedWidth,
            final int pFixedHeight) {
        super(pLayout);
        mFillMode = pFillMode;
        mFixedHeight = pFixedHeight;
        mFixedWidth = pFixedWidth;
    }

    /**
     * Constructs a copy instance of the given {@code Spacer} for the given {@code CellLayout}.
     * 
     * @param pLayout A {@code CellLayout}.
     * @param pSource A {@code Spacer}.
     */
    public Spacer(final CellLayout pLayout, final Spacer pSource) {
        super(pLayout, pSource);
        mFillMode = pSource.mFillMode;
        mFixedHeight = pSource.mFixedHeight;
        mFixedWidth = pSource.mFixedWidth;
    }


    /**
     * Gets the fill mode defined to this {@code Spacer}. Fill mode
     * is mode is used to specify how the component fills up the area calculated 
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
     * Calculates the minimum, maximum, and preferred sizes of this {@code LayoutCell}.
     */
    @Override
    protected void measureSizes() {
        if (mFixedWidth >= 0) {
            mMaximumSize.mWidth = mFixedWidth;
            mMinimumSize.mWidth = mFixedWidth;
            mPreferredSize.mWidth = mFixedWidth;
        } else {
            mMaximumSize.mWidth = Integer.MAX_VALUE;
            mMinimumSize.mWidth = 0;
            mPreferredSize.mWidth = mMaximumSize.mWidth;
        }

        if (mFixedHeight >= 0) {
            mMaximumSize.mHeight = mFixedHeight;
            mMinimumSize.mHeight = mFixedHeight;
            mPreferredSize.mHeight = mFixedHeight;
        } else {
            mMaximumSize.mHeight = Integer.MAX_VALUE;
            mMinimumSize.mHeight = 0;
            mPreferredSize.mHeight = mMaximumSize.mHeight;
        }
    }

    @Override
    public void collectDrawables(final List<Drawable> pDrawables) {
    }
}
