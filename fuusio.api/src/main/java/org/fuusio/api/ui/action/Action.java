/*
 * Copyright (C) 2001 - 2015 Marko Salmela, http://fuusio.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fuusio.api.ui.action;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.fuusio.api.dependency.D;

public class Action {

    public static final Action DUMMY = new Action(0); // TODO REMOVE

    protected ActionDelegate mDelegate;
    protected boolean mEnabled;
    protected int mDrawableResId;
    protected int mId;
    protected ActionListener mListener;
    protected MenuItem mMenuItem;
    protected int mOrder;
    protected int mTextResId;

    public Action(final int pDrawableResId) {
        this(null, -1, pDrawableResId, 0);
    }

    public Action(final int pDrawableResId, final int pTextResId) {
        this(null, -1, pDrawableResId, pTextResId);
    }

    public Action(final ActionListener pListener, final int pDrawableResId) {
        this(pListener, -1, pDrawableResId, 0);
    }

    public Action(final ActionListener pListener, final int pId, final int pDrawableResId) {
        this(pListener, pId, pDrawableResId, 0);
    }

    public Action(final ActionListener pListener, final int pId, final int pDrawableResId,
            final int pTextResId) {
        mId = pId;
        mDrawableResId = pDrawableResId;
        mTextResId = pTextResId;
        mEnabled = true;
        setListener(pListener);
    }

    public final ActionDelegate getDelegate() {
        return mDelegate;
    }

    public void setDelegate(final ActionDelegate pDelegate) {
        mDelegate = pDelegate;
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public void setEnabled(final boolean pEnabled) {
        mEnabled = pEnabled;
    }

    public int getDrawableResId() {
        return mDrawableResId;
    }

    public void setDrawableResId(final int pResId) {
        mDrawableResId = pResId;
    }

    public int getId() {
        return mId;
    }

    public void setId(final int pId) {
        mId = pId;
    }

    public final int getOrder() {
        return mOrder;
    }

    public void setOrder(final int pOrder) {
        mOrder = pOrder;
    }

    public final ActionListener getListener() {
        return mListener;
    }

    public void setListener(final ActionListener pListener) {
        mListener = pListener;
    }

    public String getText(final Context pContext) {
        final int textResId = getTextResId();

        if (textResId > 0) {
            return pContext.getString(textResId);
        }
        return null;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(final int resId) {
        mTextResId = resId;
    }

    public boolean execute(final ActionContext pActionContext) {

        boolean executed = false;

        if (mDelegate != null) {
            executed = mDelegate.executeAction(this);
        }

        if (executed && mListener != null) {
            mListener.actionPerformed(this, pActionContext);
        }

        return executed;
    }

    public void onClick(final View pView) {
        final ActionManager actionManager = D.get(ActionManager.class);
        execute(actionManager.getActiveActionContext());
    }

    public boolean isUndoable() {
        return false;
    }

    public boolean undo(final ActionContext pActionContext) {
        assert (false);
        return false;
    }

    public MenuItem createMenuItem(final Menu pMenu) {
        mMenuItem = pMenu.add(Menu.NONE, getId(), mOrder, mTextResId);
        mMenuItem.setIcon(mDrawableResId);
        return mMenuItem;
    }

    public final MenuItem getMenuItem() {
        return mMenuItem;
    }
}
