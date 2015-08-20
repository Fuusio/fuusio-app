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
package org.fuusio.api.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.fuusio.api.dependency.ApplicationScope;
import org.fuusio.api.util.AppToolkit;

public abstract class FuusioApplication extends Application {

    private static FuusioApplication sInstance = null;

    protected final ApplicationScope mDependencyScope;

    protected FuusioApplication() {
        sInstance = this;
        mDependencyScope = createDependencyScope();
    }

    @SuppressWarnings("unchecked")
    public static <T extends FuusioApplication> T getInstance() {
        return (T)sInstance;
    }

    protected abstract ApplicationScope createDependencyScope();

    /**
     * Gets the Google Analytics Property ID.
     *
     * @return The property ID as an {@code int} value.
     */
    public int getPropertyId() {
        return -1;
    }

    /**
     * Gets the {@link SharedPreferences}.
     * @return A {@link SharedPreferences}.
     */
    public SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }

    /**
     * Read the application preferences.
     */
    protected final void readPreferences() {
        final SharedPreferences preferences = getPreferences();
        onReadPreferences(preferences);
    }

    /**
     * Invoked by {@link FuusioApplication#readPreferences()}. This method should be overridden
     * in extended classes.
     * @param pPreferences A {@link SharedPreferences} for reading the preferences.
     */
    protected void onReadPreferences(final SharedPreferences pPreferences) {
        // Do nothing by default
    }

    /**
     * Write the application preferences.
     */
    protected final void writePreferences() {
        final SharedPreferences preferences = getPreferences();
        final SharedPreferences.Editor editor = preferences.edit();
        onWritePreferences(preferences);
        editor.commit();
    }

    /**
     * Invoked by {@link FuusioApplication#writePreferences()}. This method should be overridden
     * in extended classes.
     * @param pPreferences A {@link SharedPreferences} for reading the preferences.
     */
    protected void onWritePreferences(final SharedPreferences pPreferences) {
        // Do nothing by default
    }

    @SuppressWarnings("unchecked")
    public static <T extends FuusioApplication> T getApplication(final Activity pActivity) {
        return (T)pActivity.getApplicationContext();
    }

    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        super.onCreate();
        AppToolkit.setApplication(this);
        readPreferences();
    }



    /*
    public void handleNetworkNotAvailable() {
        final Activity activity = getForegroundActivity();
        handleNetworkNotAvailable(activity);
    }

    public void handleNetworkNotAvailable(final Context pActivity) {
        if (pActivity == null) {
            // TODO context = getApplicationContext().getOrCreate;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(pActivity);
        // TODO builder.setTitle(R.string.title_server_connection_error);
        // TODO builder.setIcon(R.drawable.ic_dialog_alert);
        // TODO builder.setPositiveButton(R.string.button_ok, this);
        // TODO builder.setMessage(R.string.text_network_not_available);
        // final AlertDialog dialog = builder.show();
        // mAlertDialog = dialog;

        pActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                builder.show();
            }
        });
    }*/

}
