//  ============================================================================
//  Floxp.com : Java Class Source File
//  ============================================================================
//
//  Class:      Insets
//  Package:    FloXP Utilites (org.fuusio.api.util)
//
//  Author:     Marko Salmela
//
//  Copyright (C) Marko Salmela, 2009-2011. All Rights Reserved.
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

package org.fuusio.api.util;

public class Insets {

    public int mBottom;
    public int mLeft;
    public int mRight;
    public int mTop;

    public Insets() {
        this(0, 0, 0, 0);
    }

    public Insets(final int pTop, final int pLeft, final int pBottom, final int pRight) {
        mTop = pTop;
        mLeft = pLeft;
        mBottom = pBottom;
        mRight = pRight;
    }

    public Insets(final Insets pSource) {
        mTop = pSource.mTop;
        mLeft = pSource.mLeft;
        mBottom = pSource.mBottom;
        mRight = pSource.mRight;
    }

    public final int getBottom() {
        return mBottom;
    }

    public void setBottom(final int pBottom) {
        mBottom = pBottom;
    }

    public final int getLeft() {
        return mLeft;
    }

    public void setLeft(final int pLeft) {
        mLeft = pLeft;
    }

    public int getRight() {
        return mRight;
    }

    public void setRight(final int pRight) {
        mRight = pRight;
    }

    public final int getTop() {
        return mTop;
    }

    public void setTop(final int pTop) {
        mTop = pTop;
    }

    public void set(final int pTop, final int pLeft, final int pBottom, final int pRight) {
        mTop = pTop;
        mLeft = pLeft;
        mBottom = pBottom;
        mRight = pRight;
    }

    public void set(final Insets pInsets) {
        mTop = pInsets.mTop;
        mLeft = pInsets.mLeft;
        mBottom = pInsets.mBottom;
        mRight = pInsets.mRight;
    }
}
