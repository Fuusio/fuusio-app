// ============================================================================
// Floxp.com : Java Enum Source File
// ============================================================================
//
// Enum: ResizePolicy
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

/**
 * {@code ResizePolicy} defines an enumerated type for representing the various resize policies for
 * {@link LayoutCell} instances defined in a {@link CellLayout}.
 * 
 * @author Marko Salmela
 */
public enum ResizePolicy {

    /**
     * In Fixed policy, the width or height of a {@link LayoutCell} is not resizeable but has a
     * fixed width or height.
     */
    FIXED("Fixed"),

    /**
     * In Minimum policy, the width or height of a {@link LayoutCell} is the minimum width or
     * height.
     */
    MINIMUM("Minimum"),

    /**
     * In Preferred policy, the width or height of a {@link LayoutCell} is the preferred width or
     * height.
     */
    PREFERRED("Preferred");

    // -------------------------------------------------------------------------
    // Enumeration Fields
    // -------------------------------------------------------------------------

    /**
     * The displayable label of {@code ResizePolicy} item values.
     */
    private final String mLabel;

    /**
     * Constructs a new instance of {@code ResizePolicy} with the given displayable label.
     * 
     * @param pLabel A {@link String} for the displayable label.
     */
    ResizePolicy(final String pLabel) {
        mLabel = pLabel;
    }

    /**
     * Gets the displayable label for this {@code ResizePolicy}.
     * 
     * @return A {@link String} for displayable label.
     */
    public String getLabel() {
        return mLabel;
    }

    /**
     * Returns a {@link String} representation of this {@code ResizePolicy}.
     * 
     * @return A {@link String} representation of this @code ResizePolicy}.
     */
    @Override
    public String toString() {
        return mLabel;
    }
}
