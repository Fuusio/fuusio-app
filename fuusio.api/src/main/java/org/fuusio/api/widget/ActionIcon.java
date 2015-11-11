/*
 * Copyright (C) 2000-2015 Marko Salmela.
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
package org.fuusio.api.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import org.fuusio.api.R;
import org.fuusio.api.ui.action.Action;

public class ActionIcon extends ImageView {

    private final Action mAction;
    private Drawable mIconDrawable;
    private int mNormalBackground;
    private int mSelectedBackground;

    public ActionIcon(final Context context, final Action action) {
        super(context);
        mAction = action;

        final int resId = action.getDrawableResId();

        if (resId > 0) {
            mIconDrawable = getResources().getDrawable(resId);
            setImageDrawable(mIconDrawable);
        }

        setNormalBackground(R.color.White);
        setSelectedBackground(R.color.Blue);
    }

    public final Action getAction() {
        return mAction;
    }

    public void setNormalBackground(final int colorResId) {
        mNormalBackground = getResources().getColor(colorResId);
    }

    public void setSelectedBackground(final int colorResId) {
        mSelectedBackground = getResources().getColor(colorResId);
    }

    @Override
    public void setSelected(final boolean selected) {

        super.setSelected(selected);
        setBackgroundColor(selected ? mSelectedBackground : mNormalBackground);
    }
}
