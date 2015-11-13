// ============================================================================
// Floxp.com : Java Enum Source File
// ============================================================================
//
// Enum: Anchor
// Package: FloXP.com Android APIs (com.floxp.api) -
// Graphics API (org.fuusio.api.graphics) -
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
 * {@code Anchor} defines an enumerated type for representing for anchoring directions (up, left,
 * down, and right) for {@link LayoutCell}s used in {@link CellLayout}.
 *
 * @author Marko Salmela
 */
public enum Anchor {

    /**
     * The central position in the area of a {@link LayoutCell}.
     */
    CENTER("Center"),

    /**
     * The top-middle position in the area of a {@link LayoutCell}.
     */
    NORTH("North"),

    /**
     * The top-right corner position in the area of a {@link LayoutCell}.
     */
    NORTH_EAST("NorthEast"),

    /**
     * The right-middle position in the area of a {@link LayoutCell}.
     */
    EAST("East"),

    /**
     * The bottom-right corner position in the area of a {@link LayoutCell}.
     */
    SOUTH_EAST("SouthEast"),

    /**
     * The bottom-middle position in the area of a {@link LayoutCell}.
     */
    SOUTH("SOUTH"),

    /**
     * The bottom-left corner position in the area of a {@link LayoutCell}.
     */
    SOUTH_WEST("SouthWest"),

    /**
     * The left-middle position in the area of a cell.
     */
    WEST("West"),

    /**
     * The top-left corner position in the area of a {@link LayoutCell}.
     */
    NORTH_WEST("NorthWest");

    /**
     * The displayable label of {@code Anchor} item values.
     */
    private final String mLabel;

    /**
     * Constructs a new instance of {@code Anchor} with the given displayable label.
     *
     * @param label A {@link String} for displayable label.
     */
    Anchor(final String label) {
        mLabel = label;
    }

    /**
     * Gets the displayable label for this {@code Anchor}.
     *
     * @return A {@link String} for displayable label.
     */
    public final String getLabel() {
        return mLabel;
    }

    /**
     * Returns a {@link String} representation of this {@code Anchor}.
     *
     * @return A {@link String}.
     */
    @Override
    public String toString() {
        return mLabel;
    }
}
