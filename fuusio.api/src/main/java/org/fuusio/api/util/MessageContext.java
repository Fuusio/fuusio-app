// ============================================================================
// Floxp.com : Java Class Source File
// ============================================================================
//
// Class: MessageContext
// Package: FloXP Utilities (org.fuusio.api.util)
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

package org.fuusio.api.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;

public class MessageContext {

    private final Context mContext;

    protected final ArrayList<Object> mArgs;

    protected String mMessage;
    protected int mMessageId;

    public MessageContext(final Context pContext) {
        mContext = pContext;
        mArgs = new ArrayList<Object>();
        clear();
    }

    public String getFormattedMessage() {

        final Resources resources = mContext.getResources();

        if (mMessage != null) {
            return StringToolkit.formatString(mMessage, mArgs);
        } else {
            final int length = mArgs.size();

            if (length > 0) {
                return resources.getString(mMessageId, mArgs);
            } else {
                return resources.getString(mMessageId);
            }
        }
    }


    public void setMessage(final String pMessage) {
        mMessage = pMessage;
    }

    public void setMessage(final int pStringResId) {
        mMessageId = pStringResId;
    }

    public void setMessage(final int pStringResId, final  Object... pArgs) {
        mMessageId = pStringResId;
        setMessageArgs(pArgs);
    }


    public void setMessageArgs(final  Object... pArgs) {
        mArgs.clear();

        for (final Object pArg : pArgs) {
            mArgs.add(pArg);
        }
    }

    public void setMessageArgs(final List<Object> pArgs) {
        mArgs.clear();
        mArgs.addAll(pArgs);
    }

    public MessageContext addMessageArg(final boolean pArg) {
        mArgs.add(pArg);
        return this;
    }

    public MessageContext addMessageArg(final byte pArg) {
        mArgs.add(pArg);
        return this;
    }

    public MessageContext addMessageArg(final char pArg) {
        mArgs.add(pArg);
        return this;
    }

    public MessageContext addMessageArg(final double pArg) {
        mArgs.add(pArg);
        return this;
    }

    public MessageContext addMessageArg(final float pArg) {
        mArgs.add(pArg);
        return this;
    }

    public MessageContext addMessageArg(final int pArg) {
        mArgs.add(pArg);
        return this;
    }

    public MessageContext addMessageArg(final Object pArg) {
        mArgs.add(pArg);
        return this;
    }

    public MessageContext addMessageArg(final String pArg) {
        mArgs.add(pArg);
        return this;
    }

    public void clear() {
        mArgs.clear();
        mMessage = null;
        mMessageId = 0;
    }

    public boolean hasContent() {
        return (mMessage != null || mMessageId > 0);
    }
}
