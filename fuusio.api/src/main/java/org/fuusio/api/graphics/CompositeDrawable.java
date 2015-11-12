// ============================================================================
// Fuusio.org : Java Class Source File
// ============================================================================
//
// Class: CompositeDrawable
// Package: Fuusio Graphics API (org.fuusio.api.graphics) -
// Painter Nodes (org.fuusio.api.graphics.painter)
//
// Author: Marko Salmela
//
// Copyright (C) Marko Salmela, 2000-2008. All Rights Reserved.
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

package org.fuusio.api.graphics;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;

/**
 * {@code CompositeDrawable} extends {@code Drawable} to implements a composite drawables that can
 * contain other {@code Drawable}s as child components.
 * 
 * @author Marko Salmela
 */

public class CompositeDrawable extends Drawable {

    protected int mBackgroudColor;

    protected boolean mBackgroundPainted;

    protected final ArrayList<Drawable> mComponentDrawables;

    protected boolean mDrawBorder;

    /**
     * Constructs a new instance of {@code CompositeDrawable}.
     */
    public CompositeDrawable() {
        mBackgroudColor = Color.WHITE;
        mBackgroundPainted = true;
        mComponentDrawables = new ArrayList<Drawable>();
        mDrawBorder = false;
    }

    /**
     * Gets the component {@code PainterNodes}.
     * 
     * @return A {@code List} containing the component {@code PainterNodes}.
     */

    public List<Drawable> getDrawables() {
        return mComponentDrawables;
    }

    /**
     * Sets the border of this {@code CompositeDrawable} to be drawn depending on the given
     * {@code boolean} value.
     * 
     * @param drawBorder The given {@code boolean} value.
     */
    public void setDrawBorder(final boolean drawBorder) {
        mDrawBorder = drawBorder;
    }

    /**
     * Adds the given {@code Drawable} to {@code CompositeDrawable}.
     * 
     * @param drawable The {@code Drawable} to be added.
     * @return The added {@code Drawable}. May return {@code null} if adding fails.
     */
    public Drawable addDrawable(final Drawable drawable) {
        if (!mComponentDrawables.contains(drawable)) {
            mComponentDrawables.add(drawable);
            return drawable;
        }

        return null;
    }

    /**
     * Adds the given {@code Drawable}s to this {@code CompositeDrawable}.
     * 
     * @param drawables A {@link List} of {@code Drawable}s to be added.
     */
    public void addDrawables(final List<Drawable> drawables) {
        for (final Drawable drawable : drawables) {
            addDrawable(drawable);
        }
    }

    /**
     * Inserts the given {@code Drawable} as a component painter to this {@code CompositeDrawable}.
     * 
     * @param drawable The {@code Drawable} to be inserted.
     * @param index The specified index.
     * @return The inserted {@code Drawable}. May return {@code null} if inserting fails.
     */
    public Drawable insertDrawable(final Drawable drawable, final int index) {
        if (!mComponentDrawables.contains(drawable)) {
            mComponentDrawables.add(index, drawable);
            return drawable;
        }

        return null;
    }

    /**
     * Removes the given component {@code Drawable} from this {@code CompositeDrawable}.
     * 
     * @param drawable The {@code Drawable} to be removed.
     * @return The removed {@code Drawable}. May return {@code null} if removing fails.
     */
    public Drawable removeDrawable(final Drawable drawable) {
        mComponentDrawables.remove(drawable);
        return drawable;
    }

    /**
     * Removes all the component {@link Drawable}s from this {@code CompositeDrawable}.
     */
    public void removeAllDrawables() {
        mComponentDrawables.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.graphics.drawable.Drawable#draw(android.graphics.Canvas)
     */
    @Override
    public void draw(final Canvas canvas) {

        if (mBackgroundPainted) {
            // TODO
        }

        if (mDrawBorder) {
            // TODO

        }
        final int count = mComponentDrawables.size();

        for (int i = count - 1; i >= 0; i--) {
            final Drawable drawable = mComponentDrawables.get(i);
            drawable.draw(canvas);
        }
    }

    /**
     * Tests whether a border is drawn for this {@code CompositeDrawable}.
     * 
     * @return A {@code boolean}.
     */
    public boolean isDrawBorder() {
        return mDrawBorder;
    }

    /**
     * @return
     */
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.graphics.drawable.Drawable#getOpacity()
     */
    @Override
    public int getOpacity() {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.graphics.drawable.Drawable#setAlpha(int)
     */
    @Override
    public void setAlpha(final int alpha) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see android.graphics.drawable.Drawable#setColorFilter(android.graphics.ColorFilter)
     */
    @Override
    public void setColorFilter(final ColorFilter filter) {
        // TODO Auto-generated method stub

    }

    public boolean isEmpty() {
        return mComponentDrawables.isEmpty();
    }
}
