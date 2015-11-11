/*
 * Copyright (C) 2014 - 2015 Marko Salmela, http://fuusio.org
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
package org.fuusio.api.mvp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * {@link AbstractListItemView} provides an abstract base class for implementing {@link ListItemView}s.
 */
public abstract class AbstractListItemView implements ListItemView {

    private View mInflatedView;

    protected AbstractListItemView(final Context context, final int layoutResId) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        mInflatedView = inflater.inflate(layoutResId, null, false);
        mInflatedView.setTag(this);
    }

    public final View getInflatedView() {
        return mInflatedView;
    }

    protected <T extends View> T getView(final int viewId) {
        return (T) mInflatedView.findViewById(viewId);
    }
}
