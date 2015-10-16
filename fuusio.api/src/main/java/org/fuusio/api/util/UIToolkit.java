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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.fuusio.api.graphics.BitmapManager;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.util.Xml;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.GridLayoutAnimationController;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class UIToolkit {

    public static final int COLOR_BLACK = 0xff000000;
    public static final int COLOR_BLUE = 0xff0000ff;
    public static final int COLOR_GREEN = 0xff00ff00;
    public static final int COLOR_RED = 0xffff0000;
    public static final int COLOR_YELLOW = 0xff00ffff;
    public static final int COLOR_WHITE = 0xffffffff;

    private static int sConfirmDialogIconRes = 0;
    
    private static Application sApplication = null;
    private static DisplayMetrics sDisplayMetrics = null;
    private static Class<? extends Activity> sSettingsActivityClass = null;

    private static final String PREFIX_EDIT = "edit_";
    private static final int PROPERTY_NAME_BEGIN_INDEX = PREFIX_EDIT.length();

    private static int sDensity = 0;

    // px = dp * (dpi / 160) 480 x 800 pixels and 218 dpi => 1.3625

    private final Activity mActivity;
    private final View mContentView;
    private Fragment mFragment;

    public UIToolkit(final Activity pActivity, final int pLayoutResId) {
        this(pActivity, setContentView(pActivity, pLayoutResId));
    }

    public UIToolkit(final Activity pActivity, final View pContentView) {
        mActivity = pActivity;
        mContentView = pContentView;
    }

    public UIToolkit(final Fragment pFragment, final View pContentView) {
        mActivity = pFragment.getActivity();
        mContentView = pContentView;
        mFragment = pFragment;
    }

    public static Display getDisplay() {
        final Context context = AppToolkit.getApplicationContext();
        return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    }

    public static void setApplication(final Application pApplication) {
        sApplication = pApplication;

        final Resources resources = pApplication.getResources();

        if (resources != null) {
            sDisplayMetrics = resources.getDisplayMetrics();
        }
    }

    private static View setContentView(final Activity pActivity, final int pLayoutResId) {
        pActivity.setContentView(pLayoutResId);
        return pActivity.findViewById(pLayoutResId);
    }

    public final View getContentView() {
        return mContentView;
    }

    public static Class<? extends Activity> getSettingsActivityClass() {
        return sSettingsActivityClass;
    }

    public static void setSettingsActivityClass(
            final Class<? extends Activity> pSettingsActivityClass) {
        sSettingsActivityClass = pSettingsActivityClass;
    }

    public static Dimension getDisplaySize() {
        final WindowManager manager = (WindowManager) sApplication
                .getSystemService(Context.WINDOW_SERVICE);
        final Display display = manager.getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        return new Dimension(size.x, size.y);
    }

    public static int getDensity() {
        if (sDensity == 0) {
            return sDisplayMetrics.densityDpi;
        }
        return sDensity;
    }

    public static float dip2pixels(final int pDip) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pDip, sDisplayMetrics);
    }

    public static float sp2pixels(final int pSp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, pSp, sDisplayMetrics);
    }
    
    public static int getNumberOfBottomActions() {
        return getNumberOfTopActions() + 3;
    }

    public static int getNumberOfTopActions() {
        final int density = getDensity();

        if (density >= 600) {
            return 5;
        } else if (density >= 500) {
            return 4;
        } else if (density >= 360) {
            return 3;
        } else {
            return 2;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> void collectViewsOfType(final Class<T> pType,
            final ViewGroup pViewGroup, final List<T> pCollectedViews) {
        final int count = pViewGroup.getChildCount();

        for (int i = 0; i < count; i++) {
            final View child = pViewGroup.getChildAt(i);

            if (pType.isAssignableFrom(child.getClass())) {
                pCollectedViews.add((T) child);

                if (child instanceof ViewGroup) {
                    collectViewsOfType(pType, (ViewGroup) child, pCollectedViews);
                }
            }
        }
    }

    public static void hideSoftInput(final View pView) {
        final InputMethodManager manager = (InputMethodManager) sApplication
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(pView.getApplicationWindowToken(), 0);
    }

    public static void showSoftInput(final View pView) {
        final InputMethodManager manager = (InputMethodManager) sApplication
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.showSoftInput(pView, 0);
    }

    public static void showToast(final Context pContext, final String pText, final int pDuration) {
        // context.runOnUiThread(new Runnable() {
        // public void run() {
        final Toast toast = Toast.makeText(pContext, pText, pDuration);
        toast.show();
        // }
        // });
    }

    public static void showToast(final Context pContext, final String pText, final int pDuration,
            final int pGravity, final int pXOffset, final int pYOffset) {
        final Toast toast = Toast.makeText(pContext, pText, pDuration);
        toast.setGravity(pGravity, pXOffset, pYOffset);
        toast.show();
    }

    public Activity getActivity(final View pView) {
        final Context context = pView.getContext();
        Activity activity = null;

        if (context instanceof Activity) {
            activity = (Activity) context;
        }
        return activity;
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

    public static void doGoSettings(final Context pContext) {
        final Intent intent = new Intent(pContext, sSettingsActivityClass);
        pContext.startActivity(intent);
    }

    public static void startActivity(final Class<? extends Activity> pActivityClass) {
        final Intent intent = new Intent(sApplication, pActivityClass);
        sApplication.startActivity(intent);
    }

    public final boolean getBoolean(final int pResId) {
        return mActivity.getResources().getBoolean(pResId);
    }

    public final int getColor(final int pResId) {
        return mActivity.getResources().getColor(pResId);
    }

    public final int getInteger(final int pResId) {
        return mActivity.getResources().getInteger(pResId);
    }

    @SuppressWarnings("unchecked")
    public final <T extends View> T get(final int pResId) {
        View view = mActivity.findViewById(pResId);

        if (view == null && mContentView != null) {
            view = mContentView.findViewById(pResId);
        }
        return (T) view;
    }

    public final Button getButton(final int pResId) {
        return get(pResId);
    }

    public final ImageButton getImageButton(final int pResId) {
        return get(pResId);
    }

    public final CheckBox getCheckBox(final int pResId) {
        return get(pResId);
    }

    public final EditText getEditText(final int pResId) {
        return get(pResId);
    }

    public final ImageView getImageView(final int pResId) {
        return get(pResId);
    }

    public final ProgressBar getProgressBar(final int pResId) {
        return get(pResId);
    }

    public final RadioButton getRadioButton(final int pResId) {
        return get(pResId);
    }

    public final Spinner getSpinner(final int pResId) {
        return get(pResId);
    }

    public final TextView getTextView(final int pResId) {
        return get(pResId);
    }

    public final ToggleButton getToggleButton(final int pResId) {
        return get(pResId);
    }

    public final View getView(final int pResId) {
        return get(pResId);
    }

    public final LocationManager getLocationManager() {
        return (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
    }

    public Animation getAnimation(final int pResId) {
        XmlResourceParser pParser = null;

        try {
            pParser = mActivity.getResources().getAnimation(pResId);
            return createAnimationFromXml(pParser);
        } catch (final XmlPullParserException e) {
            final NotFoundException rnf = new NotFoundException(
                    "Can't load animation resource ID #0x" + Integer.toHexString(pResId));
            rnf.initCause(e);
            throw rnf;
        } catch (final IOException e) {
            final NotFoundException nfe = new NotFoundException(
                    "Can't load animation resource ID #0x" + Integer.toHexString(pResId));
            nfe.initCause(e);
            throw nfe;
        } finally {
            if (pParser != null) {
                pParser.close();
            }
        }
    }

    private Animation createAnimationFromXml(final XmlPullParser pParser)
            throws XmlPullParserException, IOException {

        return createAnimationFromXml(pParser, null, Xml.asAttributeSet(pParser));
    }

    private Animation createAnimationFromXml(final XmlPullParser pParser,
            final AnimationSet parent, final AttributeSet attrs) throws XmlPullParserException,
            IOException {

        Animation anim = null;

        // Make sure we are on a start tag.
        int type;
        int depth = pParser.getDepth();

        while (((type = pParser.next()) != XmlPullParser.END_TAG || pParser.getDepth() > depth)
                && type != XmlPullParser.END_DOCUMENT) {

            if (type != XmlPullParser.START_TAG) {
                continue;
            }

            final String name = pParser.getName();

            if (name.equals("set")) {
                anim = new AnimationSet(mActivity, attrs);
                createAnimationFromXml(pParser, (AnimationSet) anim, attrs);
            } else if (name.equals("alpha")) {
                anim = new AlphaAnimation(mActivity, attrs);
            } else if (name.equals("scale")) {
                anim = new ScaleAnimation(mActivity, attrs);
            } else if (name.equals("rotate")) {
                anim = new RotateAnimation(mActivity, attrs);
            } else if (name.equals("translate")) {
                anim = new TranslateAnimation(mActivity, attrs);
            } else {
                throw new RuntimeException("Unknown animation name: " + pParser.getName());
            }

            if (parent != null) {
                parent.addAnimation(anim);
            }
        }

        return anim;

    }

    public LayoutAnimationController getLayoutAnimation(final int pResId) throws NotFoundException {

        XmlResourceParser pParser = null;

        try {
            pParser = mActivity.getResources().getAnimation(pResId);
            return createLayoutAnimationFromXml(pParser);
        } catch (final XmlPullParserException e) {
            final NotFoundException nfe = new NotFoundException(
                    "Can't load animation resource ID #0x" + Integer.toHexString(pResId));
            nfe.initCause(e);
            throw nfe;
        } catch (final IOException e) {
            final NotFoundException nfe = new NotFoundException(
                    "Can't load animation resource ID #0x" + Integer.toHexString(pResId));
            nfe.initCause(e);
            throw nfe;
        } finally {
            if (pParser != null) {
                pParser.close();
            }
        }
    }

    private LayoutAnimationController createLayoutAnimationFromXml(final XmlPullParser pParser)
            throws XmlPullParserException, IOException {
        return createLayoutAnimationFromXml(pParser, Xml.asAttributeSet(pParser));
    }

    private LayoutAnimationController createLayoutAnimationFromXml(final XmlPullParser pParser,
            final AttributeSet pAttrs) throws XmlPullParserException, IOException {

        LayoutAnimationController controller = null;
        final int depth = pParser.getDepth();

        int type;

        while (((type = pParser.next()) != XmlPullParser.END_TAG || pParser.getDepth() > depth)
                && type != XmlPullParser.END_DOCUMENT) {

            if (type != XmlPullParser.START_TAG) {
                continue;
            }

            final String name = pParser.getName();

            if ("layoutAnimation".equals(name)) {
                controller = new LayoutAnimationController(mActivity, pAttrs);
            } else if ("gridLayoutAnimation".equals(name)) {
                controller = new GridLayoutAnimationController(mActivity, pAttrs);
            } else {
                throw new RuntimeException("Unknown layout animation name: " + name);
            }
        }

        return controller;
    }

    public final DisplayMetrics getDisplayMetrics() {
        return mActivity.getResources().getDisplayMetrics();
    }

    public final Drawable getDrawable(final int pResId) {
        return mActivity.getResources().getDrawable(pResId);
    }

    public final String getString(final int pResId) {
        return mActivity.getString(pResId);
    }

    public String getString(final int pResId, final Object... pFormatArgs) {
        return mActivity.getString(pResId, pFormatArgs);
    }

    public String[] getStringArray(final int pResId) {
        return mActivity.getResources().getStringArray(pResId);
    }

    public final Bitmap getBitmap(final int pResId) {
        return getBitmap(pResId, true);
    }

    public Bitmap getBitmap(final int pResId, final boolean pUseCache) {
        Bitmap bitmap = null;

        if (pUseCache) {
            bitmap = getCachedBitmap(pResId);
        }

        if (bitmap == null) {
            final InputStream inputStream = mActivity.getResources().openRawResource(pResId);
            bitmap = BitmapFactory.decodeStream(inputStream);

            if (pUseCache) {
                BitmapManager.addBitmap(pResId, bitmap);
            }
        }

        return bitmap;
    }

    public static Bitmap loadBitmap(final int pResId, final boolean pUseCache) {
        Bitmap bitmap = null;

        if (pUseCache) {
            bitmap = getCachedBitmap(pResId);
        }

        if (sApplication != null) {
	        final Context context = sApplication.getApplicationContext();
	
	        if (bitmap == null) {
	            final InputStream inputStream = context.getResources().openRawResource(pResId);
	            bitmap = BitmapFactory.decodeStream(inputStream);
	
	            if (pUseCache) {
	                BitmapManager.addBitmap(pResId, bitmap);
	            }
	        }
        }

        return bitmap;
    }

    public static Bitmap getCachedBitmap(final int pResId) {
        return BitmapManager.getBitmap(pResId);
    }

    public static File getApplicationDirectory() {
        return AppToolkit.getApplicationDirectory();
    }

    public static String getApplicationDirectoryPath() {
        return AppToolkit.getApplicationDirectoryPath();
    }

    public static Vibrator getVibrator() {
        return AppToolkit.getVibrator();
    }

    public static AlertDialog.Builder createConfirmDialogBuilder(final Context pContext,
            final int pLayoutResId) {
        return createDialogBuilder(pContext, pLayoutResId, sConfirmDialogIconRes);
    }

    public static AlertDialog.Builder createDialogBuilder(final Context pContext,
            final int pLayoutResId, final int pIconResId) {
        final LayoutInflater inflater = LayoutInflater.from(pContext);
        final View layout = inflater.inflate(pLayoutResId, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(pContext);
        builder.setView(layout);
        builder.setIcon(pIconResId);
        builder.setCancelable(true);
        return builder;
    }

    public static int getDimensionInPixels(final int pDimenId) {
        return getResources().getDimensionPixelSize(pDimenId);
    }
    
    public static int getResourceId(final String pVariableName, final String pResourceName, final String pPackageName)
    {
        try {
            return getResources().getIdentifier(pVariableName, pResourceName, pPackageName);
        } catch (final Exception pException) {
            pException.printStackTrace(); // LOG
            return -1;
        } 
    }

    public static Resources getResources() {
        return sApplication.getResources();
    }

    public static boolean isPortrait() {
        final int rotation = getDisplay().getRotation();
        return (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180);
    }

    public static int getRotation() {
        return getDisplay().getRotation();
    }
}
