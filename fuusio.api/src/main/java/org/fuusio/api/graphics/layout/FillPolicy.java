//  ============================================================================
//  Floxp.com : Java Enum Source File
//  ============================================================================
//
//  Enum:       FillPolicy
//  Package:    FloXP.com Android APIs (com.floxp.api) -
//              Graphics API (com.floxp.api.graphics) -
// 				Layout Managers (org.fuusio.api.graphics.layout)
//
//  Author:     Marko Salmela
//
//  Copyright (C) Marko Salmela, 2000-2011. All Rights Reserved.
//
//  This software is the proprietary information of Marko Salmela.
//  Use is subject to license terms. This software is protected by
//  copyright and distributed under licenses restricting its use,
//  copying, distribution, and decompilation. No part of this software
//  or associated documentation may be reproduced in any form by any
//  means without prior written authorization of Marko Salmela.
//
//  Disclaimer:
//  -----------
//
//  This software is provided by the author 'as is' and any express or implied
//  warranties, including, but not limited to, the implied warranties of
//  merchantability and fitness for a particular purpose are disclaimed.
//  In no event shall the author be liable for any direct, indirect,
//  incidental, special, exemplary, or consequential damages (including, but
//  not limited to, procurement of substitute goods or services, loss of use,
//  data, or profits; or business interruption) however caused and on any
//  theory of liability, whether in contract, strict liability, or tort
//  (including negligence or otherwise) arising in any way out of the use of
//  this software, even if advised of the possibility of such damage.
//  ============================================================================

package org.fuusio.api.graphics.layout;

/**
 * {@code FillPolicy} defines an enumerated type for representing
 * the various fill policies for {@link android.graphics.drawable.Drawable}s
 * contained by {@link DrawableCell} instances in a {@link CellLayout}.
 *
 * @author Marko Salmela
 */
public enum FillPolicy {

    /**
     * In None policy, the component is not stretched to fill entire cell are.
     */
    NONE("None"),

    /**
     * In Horizontal policy, the component is stretched to horizontally fill
     * the entire cell area.
     */
    HORIZONTAL("Horizontal"),

    /**
     * In Horizontal policy, the component is stretched to horizontally fill
     * the entire cell area.
     */
    VERTICAL("Vertical"),

    /**
     * In Horizontal policy, the component is stretched to fill both horizontally
     * and vertically the entire cell area.
     */
    BOTH("Both");

    /**
     * The displayable label of {@code FillPolicy} item values.
     */
    private String mLabel;

    /**
     * Constructs a new instance of {@link FillPolicy} with the given
     * displayable label.
     *
     * @param label The displayable label as a {@link String};
     */
    FillPolicy(final String label) {
        mLabel = label;
    }

    /**
     * Gets the displayable label for this {@code FillPolicy}.
     *
     * @return A {@link String} for displayable label.
     */
    public final String getLabel() {
        return mLabel;
    }

    /**
     * Returns a {@link String} representation of this
     * {@code FillPolicy}.
     *
     * @return A {@link String} representation of this
     * {@link FillPolicy}.
     */
    @Override
    public String toString() {
        return mLabel;
    }
}
