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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class IntentAction extends Action {

    protected Class<? extends Activity> mActivityClass;

    public IntentAction(final int textResId, final Class<? extends Activity> activityClass) {
        this(0, textResId, activityClass);
    }

    public IntentAction(final int iconResId, final int textResId,
            final Class<? extends Activity> activityClass) {
        super(iconResId, textResId);
        setActivityClass(activityClass);
    }

    public IntentAction(final int iconResId, final int textResId) {
        super(iconResId, textResId);
        // TODO Auto-generated constructor stub
    }

    public Class<? extends Activity> getActivityClass() {
        return mActivityClass;
    }

    public void setActivityClass(final Class<? extends Activity> activityClass) {
        mActivityClass = activityClass;
    }

    @Override
    public boolean execute(final ActionContext actionContext) {
        final Context context = actionContext.getContext();
        final Intent intent = new Intent(context, mActivityClass);
        context.startActivity(intent);
        return true;
    }
}
