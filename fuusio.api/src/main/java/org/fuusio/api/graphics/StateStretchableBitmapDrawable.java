// ============================================================================
// Fuusio.org : Java Class Source File
// ============================================================================
//
// Class: StateSkinImagePainter
// Package: Fuusio Graphics API (org.fuusio.api.graphics) -
// Painter Nodes (org.fuusio.api.graphics.painter)
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

/**
 * {@code StateStretchableBitmapPainter} extends {@code StretchableBitmapDrawable} to implement {@code Drawable}
 * that can be used to render state depended skin images.
 *
 * @author Marko Salmela
 */

public class StateStretchableBitmapDrawable extends StretchableBitmapDrawable {

    /**
     * The current state of this {@code StateSkinImagePainter}.
     */
    protected int mState;

    /**
     * Constructs a new instance of {@code StateSkinImagePainter}.
     */
    public StateStretchableBitmapDrawable() {
    }

    /**
     * Gets the current state.
     *
     * @return The state as an {@code int}.
     */

    public int getDrawableState() {
        return mState;
    }

    /**
     * Sets the current state.
     *
     * @param state The state as an {@code int}.
     */

    public void setDrawableState(final int state) {
        if (mState != state) {
            mState = state;

            if (mStretchableBitmap != null) {
                mStretchableBitmap.setState(state);
            }

            // TODO
        }
    }
}
