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
package org.fuusio.api.util;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Build;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class AppToolkit {

    private static Application sApplication = null;

    private static final String PREFIX_EDIT = "edit_";
    private static final int PROPERTY_NAME_BEGIN_INDEX = PREFIX_EDIT.length();

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getApplicationContext() {
        return sApplication.getApplicationContext();
    }

    public static void setApplication(final Application pApplication) {
        sApplication = pApplication;
        UIToolkit.setApplication(pApplication);
    }

    public static String getStringResource(final int pStringResId) {
        return sApplication.getString(pStringResId);
    }

    public static String getPropertyName(final View pView) {
        final Resources resources = pView.getResources();
        final String resourceName = resources.getResourceEntryName(pView.getId());

        if (resourceName != null && resourceName.startsWith(PREFIX_EDIT)) {
            return resourceName.substring(PROPERTY_NAME_BEGIN_INDEX);
        }
        return null;
    }

    public static boolean getBoolean(final int pResId) {
        return sApplication.getResources().getBoolean(pResId);
    }

    public static int getColor(final int pResId) {
        return sApplication.getResources().getColor(pResId);
    }

    public static int getInteger(final int pResId) {
        return sApplication.getResources().getInteger(pResId);
    }

    public static LocationManager getLocationManager() {
        return (LocationManager) sApplication.getSystemService(Context.LOCATION_SERVICE);
    }

    public static DisplayMetrics getDisplayMetrics() {
        return sApplication.getResources().getDisplayMetrics();
    }

    public static String getString(final int pResId) {
        return sApplication.getString(pResId);
    }

    public static String getString(final int pResId, final Object... pFormatArgs) {
        return sApplication.getString(pResId, pFormatArgs);
    }

    public static String[] getStringArray(final int pResId) {
        return sApplication.getResources().getStringArray(pResId);
    }

    public static Bitmap getBitmap(final int pResId) {
        return getBitmap(pResId, true);
    }

    public static Bitmap getBitmap(final int pResId, final boolean pUseCache) {
        Bitmap bitmap = null;

        if (pUseCache) {
            bitmap = getCachedBitmap(pResId);
        }

        if (bitmap == null) {
            final InputStream inputStream = sApplication.getResources().openRawResource(pResId);
            bitmap = BitmapFactory.decodeStream(inputStream);

            if (pUseCache) {
                BitmapCache.addBitmap(pResId, bitmap);
            }
        }

        return bitmap;
    }

    public static Bitmap loadBitmap(final int pResId, final boolean pUseCache) {
        Bitmap bitmap = null;

        if (pUseCache) {
            bitmap = getCachedBitmap(pResId);
        }

        final Context context = sApplication.getApplicationContext();

        if (bitmap == null) {
            final InputStream inputStream = context.getResources().openRawResource(pResId);
            bitmap = BitmapFactory.decodeStream(inputStream);

            if (pUseCache) {
                BitmapCache.addBitmap(pResId, bitmap);
            }
        }

        return bitmap;
    }

    public static Bitmap loadBitmap(final String pFilepath) {
        Bitmap bitmap = null;

        final File file = new File(pFilepath);

        if (file.exists() && file.canRead()) {

            FileInputStream inputStream = null;

            try {
                inputStream = new FileInputStream(file);
            } catch (final Exception e) {
                e.printStackTrace();
            }

            if (inputStream != null) {
                bitmap = BitmapFactory.decodeStream(inputStream);
            }
        }

        return bitmap;
    }

    public static Bitmap getCachedBitmap(final int pResId) {
        return BitmapCache.getBitmap(pResId);
    }

    public static File getApplicationDirectory() {
        final String path = getApplicationDirectoryPath();
        final File directory = new File(path);

        assert (directory != null);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        assert (directory.exists() && directory.canRead());

        return directory;
    }

    public static String getApplicationDirectoryPath() {
        final PackageManager manager = sApplication.getPackageManager();
        final String packageName = sApplication.getPackageName();

        try {
            final PackageInfo info = manager.getPackageInfo(packageName, 0);
            return info.applicationInfo.dataDir;
        } catch (final NameNotFoundException e) {
            L.w(sApplication, "getApplicationDirectoryPath", "Error Package name not found ");
        }
        return null;
    }

    public static Vibrator getVibrator() {
        return (Vibrator) sApplication.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public static Resources getResources() {
        return sApplication.getResources();
    }


    public static boolean hasNfc() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            final NfcManager manager = (NfcManager) getApplicationContext().getSystemService(
                    Context.NFC_SERVICE);
            final NfcAdapter adapter = manager.getDefaultAdapter();
            return (adapter != null && adapter.isEnabled());
        }
        return false;
    }

    public static boolean isNetworkAvailable() {
        final Context context = getApplicationContext();
        final ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (manager == null) {
            L.wtf(AppToolkit.class, "isNetworkAvailable()", "Network access not allowed");
        } else {
            final NetworkInfo[] info = manager.getAllNetworkInfo();

            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isPackageInstalled(final String pPackageName) {
        final Context context = getApplicationContext();
        final PackageManager manager = context.getPackageManager();
        final List<ApplicationInfo> infos = manager.getInstalledApplications(PackageManager.GET_META_DATA);

        for (final ApplicationInfo info : infos) {
            if (pPackageName.equalsIgnoreCase(info.packageName)) {
                return true;
            }
        }
        return false;
    }
}
