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

    public Action(final int drawableResId) {
        this(null, -1, drawableResId, 0);
    }

    public Action(final int drawableResId, final int textResId) {
        this(null, -1, drawableResId, textResId);
    }

    public Action(final ActionListener listener, final int drawableResId) {
        this(listener, -1, drawableResId, 0);
    }

    public Action(final ActionListener listener, final int id, final int drawableResId) {
        this(listener, id, drawableResId, 0);
    }

    public Action(final ActionListener listener, final int pId, final int drawableResId,
                  final int textResId) {
        mId = pId;
        mDrawableResId = drawableResId;
        mTextResId = textResId;
        mEnabled = true;
        setListener(listener);
    }

    public final ActionDelegate getDelegate() {
        return mDelegate;
    }

    public void setDelegate(final ActionDelegate delegate) {
        mDelegate = delegate;
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public void setEnabled(final boolean enabled) {
        mEnabled = enabled;
    }

    public int getDrawableResId() {
        return mDrawableResId;
    }

    public void setDrawableResId(final int resId) {
        mDrawableResId = resId;
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

    public void setOrder(final int order) {
        mOrder = order;
    }

    public final ActionListener getListener() {
        return mListener;
    }

    public void setListener(final ActionListener listener) {
        mListener = listener;
    }

    public String getText(final Context context) {
        final int textResId = getTextResId();

        if (textResId > 0) {
            return context.getString(textResId);
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

    public void onClick(final View view) {
        final ActionManager actionManager = D.get(ActionManager.class);
        execute(actionManager.getActiveActionContext());
    }

    public boolean isUndoable() {
        return false;
    }

    public boolean undo(final ActionContext actionContext) {
        assert (false);
        return false;
    }

    public MenuItem createMenuItem(final Menu menu) {
        mMenuItem = menu.add(Menu.NONE, getId(), mOrder, mTextResId);
        mMenuItem.setIcon(mDrawableResId);
        return mMenuItem;
    }

    public final MenuItem getMenuItem() {
        return mMenuItem;
    }
}
