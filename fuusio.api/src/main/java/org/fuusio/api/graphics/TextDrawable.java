// ============================================================================
// Floxp.com : Java Class Source File
// ============================================================================
//
// Class: TextDrawable
// Package: FloXP.com Android APIs (com.floxp.api) -
// Graphics API (com.floxp.api.graphics)
//
// Author: Marko Salmela
//
// Copyright (C) Marko Salmela, 2009-2011. All Rights Reserved.
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

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

/**
 * {@link TextDrawable} extends {@link Drawable} to implements TODO
 * 
 * @author Marko Salmela
 */
public class TextDrawable extends Drawable {

    /**
     * A {@code boolean} value specifying whether drawing is antialiazed.
     */
    protected boolean mAntialized;

    /**
     * A {@code boolean} flag specifying whether the background of this {@link TextDrawable} is
     * drawn.
     */
    protected boolean mBackgroundDrawn;

    /**
     * The {@link Paint} used for drawing the background of this {@link TextDrawable}.
     */
    protected Paint mBackgroundPaint;

    /**
     * The {@link Paint} used for filling the displayed font glyphs.
     */
    protected Paint mFillPaint;

    /**
     * The horizontal alignment of the display text.
     */
    protected HorizontalAlignment mHorizontalAlignment;

    /**
     * The opacity value of this {@link TextDrawable}.
     */
    protected int mOpacity;

    /**
     * A {@code boolean} flag indicating whether the stroke of the outlines of the font glyphs
     * displayed by this {@code TextNode} are drawn.
     */
    protected boolean mStroked;

    /**
     * The {@link Paint} used for drawing the stroke that outlines the displayed font glyphs.
     */
    protected Paint mStrokePaint;

    /**
     * The {@link String} to be displayed.
     */
    protected String mText;

    /**
     * The bounding box for the set text content.
     */
    protected Rect mTextBounds;

    /**
     * The vertical alignment of the display text.
     */
    protected VerticalAlignment mVerticalAlignment;

    /**
     * Constructs a new instance of {@link TextDrawable}.
     */
    public TextDrawable() {
        mAntialized = false;
        mBackgroundDrawn = false;
        mBackgroundPaint = createDefaultBackgroundPaint();
        mFillPaint = createDefaultFillPaint();
        mHorizontalAlignment = HorizontalAlignment.LEFT;
        mOpacity = 0xff; // TODO
        mStroked = false;
        mStrokePaint = createDefaultStrokePaint();
        mText = new String();
        mTextBounds = new Rect();
        mVerticalAlignment = VerticalAlignment.CENTER;
    }

    /**
     * Tests whether the drawing of this (@link TextDrawable) is set to be antialized.
     * 
     * @return {@code boolean} value.
     */
    public boolean isAntialized() {
        return mAntialized;
    }

    /**
     * Sets the drawing of this (@link TextDrawable) to be antialized depending on the given
     * {@code boolean} value.
     * 
     * @param pAntialized A {@code boolean} value.
     */
    public void setAntialized(final boolean pAntialized) {
        mAntialized = pAntialized;
    }

    /**
     * Gets the {@link Paint} used for drawing the background of this {@link TextDrawable}.
     * 
     * @return A {@link Paint}.
     */
    public Paint getBackgroundPaint() {
        return mBackgroundPaint;
    }

    /**
     * Sets the {@link Paint} used for drawing the background of this {@link TextDrawable}.
     * 
     * @param pPaint A {@link Paint}.
     */
    public void setBackgroundPaint(final Paint pPaint) {
        assert (pPaint != null); // TODO
        mBackgroundPaint = pPaint;
        mBackgroundPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * Sets the alpha value of the {@link Color} of the {@link Paint} used for drawing the
     * background of this {@link TextDrawable}.
     * 
     * @param pAlpha The alpha value as an {@code int}.
     */
    public void setBackgroundAlpha(final int pAlpha) {
        mBackgroundPaint.setAlpha(pAlpha);
    }

    /**
     * Sets the {@link Color} of {@link Paint} used for drawing the background of this
     * {@link TextDrawable}.
     * 
     * @param pColor A {@link Paint}.
     */
    public void setBackgroundColor(final int pColor) {
        mBackgroundPaint.setColor(pColor);
    }

    /**
     * Tests whether the background of this {@link TextDrawable} is drawn.
     * 
     * @return A {@code boolean} value.
     */
    public boolean isBackgroundDrawn() {
        return mBackgroundDrawn;
    }

    /**
     * Sets the background of this {@link TextDrawable} to be drawn depending on the given
     * {@code boolean} value.
     * 
     * @param pIsDrawn A {@code boolean} value.
     */
    public void setBackgroundDrawn(final boolean pIsDrawn) {
        mBackgroundDrawn = pIsDrawn;
    }

    /**
     * Gets the {@link Paint} used for filling the displayed font glyphs.
     * 
     * @return A {@link Paint}.
     */
    public Paint getFillPaint() {
        return mFillPaint;
    }

    /**
     * Sets the {@link Paint} used for filling the displayed font glyphs.
     * 
     * @param pPaint A {@link Paint}.
     */
    public void setFillPaint(final Paint pPaint) {
        assert (pPaint != null); // TODO
        mFillPaint = pPaint;
        mFillPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * Sets the alpha value of the {@link Color} of the {@link Paint} used filling the displayed
     * font glyphs.
     * 
     * @param pAlpha The alpha value as an {@code int}.
     */
    public void setFillAlpha(final int pAlpha) {
        mFillPaint.setAlpha(pAlpha);
    }

    /**
     * Sets the {@link Color} of {@link Paint} used for filling the displayed font glyphs.
     * 
     * @param pColor the color as an {@code int} value.
     */
    public void setFillColor(final int pColor) {
        mFillPaint.setColor(pColor);
    }

    /**
     * Gets the horizontal alignment.
     * 
     * @return The alignment value as a {@link HorizontalAlignment}.
     */
    public HorizontalAlignment getHorizontalAlignment() {
        return mHorizontalAlignment;
    }

    /**
     * Sets the horizontal alignment.
     * 
     * @param pAlignment The alignment value as a {@link HorizontalAlignment}.
     */
    public void setHorizontalAlignment(final HorizontalAlignment pAlignment) {
        mHorizontalAlignment = pAlignment;
    }

    /**
     * Gets the the intrinsic height of the underlying drawable object. Returns -1 if it has no
     * intrinsic height, such as with a solid color.
     * 
     * @return The intrinsic height as an {@code int} value.
     */
    @Override
    public int getIntrinsicHeight() {
        if (mText != null && mFillPaint != null) {
            mFillPaint.getTextBounds(mText, 0, mText.length(), mTextBounds);
            return mTextBounds.height();
        }

        return super.getIntrinsicHeight();
    }

    /**
     * Gets the the intrinsic width of the underlying drawable object. Returns -1 if it has no
     * intrinsic width, such as with a solid color.
     * 
     * @return The intrinsic width as an {@code int} value.
     */
    @Override
    public int getIntrinsicWidth() {
        if (mText != null && mFillPaint != null) {
            mFillPaint.getTextBounds(mText, 0, mText.length(), mTextBounds);
            return mTextBounds.width();
        }

        return super.getIntrinsicWidth();
    }

    @Override
    public void setAlpha(final int pAlpha) // TODo
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void setColorFilter(final ColorFilter pColorFilter) // TODO
    {
        // TODO Auto-generated method stub

    }

    /**
     * Gets the opacity value.
     * 
     * @return The opacity value as an {@code int}.
     */
    @Override
    public int getOpacity() {
        return mOpacity;
    }

    /**
     * Sets the opacity value.
     * 
     * @param pOpacity The opacity value as an {@code int}.
     */
    public void setOpacity(final int pOpacity) {
        mOpacity = pOpacity;
    }

    /**
     * Gets the {@link Paint} used for drawing the stroke that outlines the displayed font glyphs.
     * 
     * @return A {@link Paint}.
     */
    public Paint getStrokePaint() {
        return mStrokePaint;
    }

    /**
     * Sets the {@link Paint} used for drawing the stroke that outlines the displayed font glyphs.
     * 
     * @param pPaint A {@link Paint}.
     */
    public void setStrokePaint(final Paint pPaint) {
        assert (pPaint != null); // TODO
        mStrokePaint = pPaint;
        mStrokePaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * Sets the cap parameter of {@link Paint} used for drawing the stroke that outlines the
     * displayed font glyphs.
     * 
     * @param pCap A {@link Paint.Cap} value.
     */
    public void setStrokeCap(final Paint.Cap pCap) {
        mStrokePaint.setStrokeCap(pCap);
    }

    /**
     * Sets the alpha value of the {@link Color} of the {@link Paint} used for drawing the stroke
     * the displayed font glyphs.
     * 
     * @param pAlpha The alpha value as an {@code int}.
     */
    public void setStrokeAlpha(final int pAlpha) {
        mStrokePaint.setAlpha(pAlpha);
    }

    /**
     * Sets the {@link Color} of {@link Paint} used for drawing the stroke that outlines the
     * displayed font glyphs.
     * 
     * @param pColor the color as a {@link int} value.
     */
    public void setStrokeColor(final int pColor) {
        mStrokePaint.setColor(pColor);
    }

    public void setStroked(final boolean pStroked) {
        mStroked = pStroked;
    }

    /**
     * Sets the join parameter of {@link Paint} used for drawing the stroke that outlines the
     * displayed font glyphs.
     * 
     * @param pJoin A {@link Paint.Join} value.
     */
    public void setStrokeJoin(final Paint.Join pJoin) {
        mStrokePaint.setStrokeJoin(pJoin);
    }

    /**
     * Sets the miter parameter of {@link Paint} used for drawing the stroke that outlines the
     * displayed font glyphs.
     * 
     * @param pMiter A {@code float} value.
     */
    public void setStrokeMiter(final float pMiter) {
        mStrokePaint.setStrokeMiter(pMiter);
    }

    /**
     * Sets the width parameter of {@link Paint} used for drawing the stroke that outlines the
     * displayed font glyphs.
     * 
     * @param pWidth A {@code float} value.
     */
    public void setStrokeWidth(final float pWidth) {
        mStrokePaint.setStrokeWidth(pWidth);
    }

    /**
     * Gets the {@link String} to be displayed.
     * 
     * @return A {@link String}.
     */
    public String getText() {
        return mText;
    }

    /**
     * Sets the {@link String} to be displayed.
     * 
     * @param pText A {@link String}.
     */
    public void setText(final String pText) {
        mText = pText;
    }

    /**
     * Gets the text size.
     * 
     * @return The size as a {@link float}.
     */
    public float getTextSize() {
        return mFillPaint.getTextSize();
    }

    /**
     * Sets the text size.
     * 
     * @param pSize The size as a {@link float}.
     */
    public void setTextSize(final float pSize) {
        mFillPaint.setTextSize(pSize);
        mStrokePaint.setTextSize(pSize);
    }

    /**
     * Gets the used {@link Typeface}.
     * 
     * @return A {@link Typeface}. May be {@link null} if not set.
     */
    public Typeface getTypeface() {
        return mFillPaint.getTypeface();
    }

    /**
     * Sets the used {@link Typeface}.
     * 
     * @param pTypeface A {@link Typeface}. May be {@link null}.
     */
    public void setTypeface(final Typeface pTypeface) {
        mFillPaint.setTypeface(pTypeface);
        mStrokePaint.setTypeface(pTypeface);
    }

    /**
     * Gets the vertical alignment.
     * 
     * @return The alignment value as a {@link VerticalAlignment}.
     */
    public VerticalAlignment getVerticalAlignment() {
        return mVerticalAlignment;
    }

    /**
     * Sets the vertical alignment.
     * 
     * @param pAlignment The alignment value as a {@link VerticalAlignment}.
     */
    public void setVerticalAlignment(final VerticalAlignment pAlignment) {
        mVerticalAlignment = pAlignment;
    }

    /**
     * Creates an appropriate instance of {@link Paint} to be used as a default background paint to
     * draw the background of a {@link TextDrawable}.
     * 
     * @return The created {@link Paint} instance.
     */
    protected Paint createDefaultBackgroundPaint() {
        final Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        return paint;
    }

    /**
     * Creates an appropriate instance of {@link Paint} to be used as a default fillPaint to fill
     * the interior of a {@link TextDrawable}.
     * 
     * @return The created {@link Paint} instance.
     */
    protected Paint createDefaultFillPaint() {
        final Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        return paint;
    }

    /**
     * Creates an appropriate instance of {@link Paint} to be used as a default fillPaint of a
     * stroke drawn around the outline of a {@link TextDrawable}.
     * 
     * @return The created {@link Paint} instance.
     */
    protected Paint createDefaultStrokePaint() {
        final Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        return paint;
    }

    /**
     * Clears the contents of the drawn {@code String}.
     */
    public void clearText() {
        mText = "";
    }

    /**
     * Applies the rendering parameters defined for this {@link TextDrawable}.
     */
    protected void applyRenderingParameters() {
        if (mOpacity < 1.0f) {
            float alphaValue = mBackgroundPaint.getAlpha();
            int alpha = (int) (alphaValue * mOpacity);
            mBackgroundPaint.setAlpha(alpha);

            alphaValue = mFillPaint.getAlpha();
            alpha = (int) (alphaValue * mOpacity);
            mFillPaint.setAlpha(alpha);

            alphaValue = mStrokePaint.getAlpha();
            alpha = (int) (alphaValue * mOpacity);
            mStrokePaint.setAlpha(alpha);
        }
    }

    /**
     * Draws this {@link TextDrawable} on the given {@link Canvas}.
     * 
     * @param pCanvas A {@link Canvas}.
     */
    @Override
    public void draw(final Canvas pCanvas) {
        if (mText != null) {
            final Rect bounds = getBounds();
            int x = bounds.left;
            int y = bounds.top;

            mFillPaint.getTextBounds(mText, 0, mText.length(), mTextBounds);

            switch (mHorizontalAlignment) {
                case LEFT: {
                    break;
                }
                case CENTER: {
                    x += (bounds.width() - mTextBounds.width()) / 2;
                    break;
                }
                case RIGHT: {
                    x = bounds.right - mTextBounds.width();
                    break;
                }
            }

            switch (mVerticalAlignment) {
                case TOP: {
                    y += mTextBounds.height();
                    break;
                }
                case CENTER: {
                    y += (bounds.height() + mTextBounds.height()) / 2;
                    break;
                }
                case BOTTOM: {
                    y = bounds.bottom;
                    break;
                }
            }

            mFillPaint.setAntiAlias(mAntialized);
            mFillPaint.setTextAlign(mHorizontalAlignment.getPaintAlign());
            pCanvas.drawText(mText, x, y, mFillPaint);

            if (mStroked) {
                mStrokePaint.setAntiAlias(mAntialized);
                mStrokePaint.setTextAlign(mHorizontalAlignment.getPaintAlign());
                pCanvas.drawText(mText, x, y, mStrokePaint);
            }
        }
    }
}
