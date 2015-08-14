/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * Revised 5/19/2010 by GORGES
 * Now supports two-dimensional view scrolling
 * http://GORGES.us
 */
/*
 * Revised 25/12/2012 by Julien Vermet
 * Add Scroll Listener
 * ju.vermet@gmail.com
 */

package org.fuusio.api.widget;

import java.util.List;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * Layout container for a view hierarchy that can be scrolled by the user,
 * allowing it to be larger than the physical display.  A ScrollView2D
 * is a {@link FrameLayout}, meaning you should place one child in it
 * containing the entire contents to scroll; this child may itself be a layout
 * manager with a complex hierarchy of objects.  A child that is often used
 * is a {@link LinearLayout} in a vertical orientation, presenting a vertical
 * array of top-level items that the user can scroll through.
 * <p/>
 * <p>The {@link TextView} class also
 * takes care of its own scrolling, so does not require a ScrollView2D, but
 * using the two together is possible to achieve the effect of a text view
 * within a larger container.
 */
public class ScrollView2D extends FrameLayout {

    static final int ANIMATED_SCROLL_GAP = 250;
    static final float MAX_SCROLL_FACTOR = 0.5f;

    /**
     * A {@link Rect} for temporarily storing a rect.
     */
    private final Rect mTempRect;

    /**
     * A {@link Scroller} used by this {@link ScrollView2D}.
     */
    private Scroller mScroller;

    /**
     * {@link ScrollListener} defines a listener interface for the scrolling state change events
     * produced by this {@link ScrollView2D}.
     */
    private ScrollListener mListener;

    /**
     * A time stamp for last performed scroll.
     */
    private long mLastScrollTime;

    /**
     * Flag to indicate that we are moving focus ourselves. This is so the
     * code that watches for focus changes initiated outside this ScrollView2D
     * knows that it does not have to do anything.
     */
    private boolean mScrollView2DMovedFocus;

    /**
     * Position of the last motion event.
     */
    private float mLastMotionY;
    private float mLastMotionX;

    /**
     * True when the layout has changed but the traversal has not come through yet.
     * Ideally the view hierarchy would keep track of this for us.
     */
    private boolean mIsLayoutDirty;

    /**
     * The child to give focus to in the event that a child has requested focus while the
     * layout is dirty. This prevents the scroll from being wrong if the child has not been
     * laid out before requesting focus.
     */
    private View mChildToScrollTo;

    /**
     * True if the user is currently dragging this ScrollView2D around. This is
     * not the same as 'is being flinged', which can be checked by
     * mScroller.isFinished() (flinging begins when the user lifts his finger).
     */
    private boolean mIsBeingDragged;

    /**
     * Determines speed during touch scrolling
     */
    private VelocityTracker mVelocityTracker;

    /**
     * Whether arrow scrolling is animated.
     */
    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;

    public ScrollView2D(final Context pContext) {
        super(pContext);
        mTempRect = new Rect();
        init();
    }

    public ScrollView2D(final Context pContext, final AttributeSet pAttrs) {
        super(pContext, pAttrs);
        mTempRect = new Rect();
        init();
    }

    public ScrollView2D(final Context pContext, final AttributeSet pAttrs, final int pDefStyle) {
        super(pContext, pAttrs, pDefStyle);
        mTempRect = new Rect();
        init();
    }

    private void init() {
        mIsLayoutDirty = true;
        mIsBeingDragged = false;
        mScroller = new Scroller(getContext());
        setFocusable(true);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        setWillNotDraw(false);
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    public void setOnScrollListener(final ScrollListener pListener) {
        mListener = pListener;
    }

    @Override
    protected float getTopFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }
        final int length = getVerticalFadingEdgeLength();
        if (getScrollY() < length) {
            return getScrollY() / (float) length;
        }
        return 1.0f;
    }

    @Override
    protected float getBottomFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }
        final int length = getVerticalFadingEdgeLength();
        final int bottomEdge = getHeight() - getPaddingBottom();
        final int span = getChildAt(0).getBottom() - getScrollY() - bottomEdge;
        if (span < length) {
            return span / (float) length;
        }
        return 1.0f;
    }

    @Override
    protected float getLeftFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }
        final int length = getHorizontalFadingEdgeLength();
        if (getScrollX() < length) {
            return getScrollX() / (float) length;
        }
        return 1.0f;
    }

    @Override
    protected float getRightFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }
        final int length = getHorizontalFadingEdgeLength();
        final int rightEdge = getWidth() - getPaddingRight();
        final int span = getChildAt(0).getRight() - getScrollX() - rightEdge;
        if (span < length) {
            return span / (float) length;
        }
        return 1.0f;
    }

    /**
     * @return The maximum amount this scroll view will scroll vertically in response to
     * an arrow event.
     */
    public int getMaxScrollAmountVertical() {
        return (int) (MAX_SCROLL_FACTOR * getHeight());
    }

    /**
     * @return The maximum amount this scroll view will scroll horizontally in response to
     * an arrow event.
     */
    public int getMaxScrollAmountHorizontal() {
        return (int) (MAX_SCROLL_FACTOR * getWidth());
    }

    @Override
    public void addView(final View pChild) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ScrollView2D can host only one direct child");
        }
        super.addView(pChild);
    }

    @Override
    public void addView(final View pChild, final int pIndex) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ScrollView2D can host only one direct child");
        }
        super.addView(pChild, pIndex);
    }

    @Override
    public void addView(final View pChild, final ViewGroup.LayoutParams pParams) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ScrollView2D can host only one direct child");
        }
        super.addView(pChild, pParams);
    }

    @Override
    public void addView(final View pChild, final int pIndex, final ViewGroup.LayoutParams pParams) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ScrollView2D can host only one direct child");
        }
        super.addView(pChild, pIndex, pParams);
    }

    /**
     * @return Returns true this ScrollView2D can be scrolled
     */
    private boolean canScroll() {
        View child = getChildAt(0);
        if (child != null) {
            int childHeight = child.getHeight();
            int childWidth = child.getWidth();
            return (getHeight() < childHeight + getPaddingTop() + getPaddingBottom()) ||
                    (getWidth() < childWidth + getPaddingLeft() + getPaddingRight());
        }
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(final KeyEvent pEvent) {
        // Let the focused view and/or our descendants getOrCreate the key first
        boolean handled = super.dispatchKeyEvent(pEvent);
        if (handled) {
            return true;
        }
        return executeKeyEvent(pEvent);
    }

    /**
     * You can call this function yourself to have the scroll view perform
     * scrolling from a key event, just as if the event had been dispatched to
     * it by the view hierarchy.
     *
     * @param pEvent The key event to execute.
     * @return Return true if the event was handled, else false.
     */
    public boolean executeKeyEvent(final KeyEvent pEvent) {
        mTempRect.setEmpty();

        if (!canScroll()) {
            if (isFocused()) {
                View currentFocused = findFocus();
                if (currentFocused == this) currentFocused = null;
                View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, View.FOCUS_DOWN);
                return nextFocused != null && nextFocused != this && nextFocused.requestFocus(View.FOCUS_DOWN);
            }
            return false;
        }
        boolean handled = false;
        if (pEvent.getAction() == KeyEvent.ACTION_DOWN) {
            switch (pEvent.getKeyCode()) {
                case KeyEvent.KEYCODE_DPAD_UP:
                    if (!pEvent.isAltPressed()) {
                        handled = arrowScroll(View.FOCUS_UP, false);
                    } else {
                        handled = fullScroll(View.FOCUS_UP, false);
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    if (!pEvent.isAltPressed()) {
                        handled = arrowScroll(View.FOCUS_DOWN, false);
                    } else {
                        handled = fullScroll(View.FOCUS_DOWN, false);
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    if (!pEvent.isAltPressed()) {
                        handled = arrowScroll(View.FOCUS_LEFT, true);
                    } else {
                        handled = fullScroll(View.FOCUS_LEFT, true);
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (!pEvent.isAltPressed()) {
                        handled = arrowScroll(View.FOCUS_RIGHT, true);
                    } else {
                        handled = fullScroll(View.FOCUS_RIGHT, true);
                    }
                    break;
            }
        }
        return handled;
    }

    @Override
    public boolean onInterceptTouchEvent(final MotionEvent pEvent) {
        /*
		 * This method JUST determines whether we want to intercept the motion.
		 * If we return true, onMotionEvent will be called and we do the actual
		 * scrolling there.
		 *
		 * Shortcut the most recurring case: the user is in the dragging
		 * state and he is moving his finger.  We want to intercept this
		 * motion.
		 */
        final int action = pEvent.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
            return true;
        }
        if (!canScroll()) {
            mIsBeingDragged = false;
            return false;
        }
        final float y = pEvent.getY();
        final float x = pEvent.getX();
        switch (action) {
            case MotionEvent.ACTION_MOVE:
			/*
			 * mIsBeingDragged == false, otherwise the shortcut would have caught it. Check
			 * whether the user has moved far enough from his original down touch.
			 */
			/*
			 * Locally do absolute value. mLastMotionY is set to the y value
			 * of the down event.
			 */
                final int yDiff = (int) Math.abs(y - mLastMotionY);
                final int xDiff = (int) Math.abs(x - mLastMotionX);
                if (yDiff > mTouchSlop || xDiff > mTouchSlop) {
                    mIsBeingDragged = true;
                }
                break;

            case MotionEvent.ACTION_DOWN:
			/* Remember location of down touch */
                mLastMotionY = y;
                mLastMotionX = x;

			/*
			 * If being flinged and user touches the screen, initiate drag;
			 * otherwise don't.  mScroller.isFinished should be false when
			 * being flinged.
			 */
                mIsBeingDragged = !mScroller.isFinished();
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
			/* Release the drag */
                mIsBeingDragged = false;
                break;
        }

		/*
		 * The only time we want to intercept motion events is if we are in the
		 * drag mode.
		 */
        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(final MotionEvent pEvent) {

        if (pEvent.getAction() == MotionEvent.ACTION_DOWN && pEvent.getEdgeFlags() != 0) {
            // Don't handle edge touches immediately -- they may actually belong to one of our
            // descendants.
            return false;
        }

        if (!canScroll()) {
            return false;
        }

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(pEvent);

        final int action = pEvent.getAction();
        final float y = pEvent.getY();
        final float x = pEvent.getX();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
			/*
			 * If being flinged and user touches, stop the fling. isFinished
			 * will be false if being flinged.
			 */
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                // Remember where the motion event started
                mLastMotionY = y;
                mLastMotionX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                // Scroll to follow the motion event
                int deltaX = (int) (mLastMotionX - x);
                int deltaY = (int) (mLastMotionY - y);
                mLastMotionX = x;
                mLastMotionY = y;

                if (deltaX < 0) {
                    if (getScrollX() < 0) {
                        deltaX = 0;
                    }
                } else if (deltaX > 0) {
                    final int rightEdge = getWidth() - getPaddingRight();
                    final int availableToScroll = getChildAt(0).getRight() - getScrollX() - rightEdge;
                    if (availableToScroll > 0) {
                        deltaX = Math.min(availableToScroll, deltaX);
                    } else {
                        deltaX = 0;
                    }
                }
                if (deltaY < 0) {
                    if (getScrollY() < 0) {
                        deltaY = 0;
                    }
                } else if (deltaY > 0) {
                    final int bottomEdge = getHeight() - getPaddingBottom();
                    final int availableToScroll = getChildAt(0).getBottom() - getScrollY() - bottomEdge;
                    if (availableToScroll > 0) {
                        deltaY = Math.min(availableToScroll, deltaY);
                    } else {
                        deltaY = 0;
                    }
                }
                if (deltaY != 0 || deltaX != 0)
                    scrollBy(deltaX, deltaY);
                break;
            case MotionEvent.ACTION_UP:
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int initialXVelocity = (int) velocityTracker.getXVelocity();
                int initialYVelocity = (int) velocityTracker.getYVelocity();
                if ((Math.abs(initialXVelocity) + Math.abs(initialYVelocity) > mMinimumVelocity) && getChildCount() > 0) {
                    fling(-initialXVelocity, -initialYVelocity);
                }
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
        }
        return true;
    }

    /**
     * Finds the next focusable component that fits in this View's bounds
     * (excluding fading edges) pretending that this View's top is located at
     * the parameter top.
     *
     * @param pTopFocus           look for a candidate is the one at the top of the bounds
     *                           if topFocus is true, or at the bottom of the bounds if topFocus is
     *                           false
     * @param pTop                the top offset of the bounds in which a focusable must be
     *                           found (the fading edge is assumed to start at this position)
     * @param pPreferredFocusable the View that has highest priority and will be
     *                           returned if it is within my bounds (null is valid)
     * @return the next focusable component in the bounds or null if none can be
     * found
     */
    private View findFocusableViewInMyBounds(final boolean pTopFocus, final int pTop, final boolean pLeftFocus, final int pLeft, final View pPreferredFocusable) {
		/*
		 * The fading edge's transparent side should be considered for focus
		 * since it's mostly visible, so we divide the actual fading edge length
		 * by 2.
		 */
        final int verticalFadingEdgeLength = getVerticalFadingEdgeLength() / 2;
        final int topWithoutFadingEdge = pTop + verticalFadingEdgeLength;
        final int bottomWithoutFadingEdge = pTop + getHeight() - verticalFadingEdgeLength;
        final int horizontalFadingEdgeLength = getHorizontalFadingEdgeLength() / 2;
        final int leftWithoutFadingEdge = pLeft + horizontalFadingEdgeLength;
        final int rightWithoutFadingEdge = pLeft + getWidth() - horizontalFadingEdgeLength;

        if ((pPreferredFocusable != null)
                && (pPreferredFocusable.getTop() < bottomWithoutFadingEdge)
                && (pPreferredFocusable.getBottom() > topWithoutFadingEdge)
                && (pPreferredFocusable.getLeft() < rightWithoutFadingEdge)
                && (pPreferredFocusable.getRight() > leftWithoutFadingEdge)) {
            return pPreferredFocusable;
        }
        return findFocusableViewInBounds(pTopFocus, topWithoutFadingEdge, bottomWithoutFadingEdge, pLeftFocus, leftWithoutFadingEdge, rightWithoutFadingEdge);
    }

    /**
     * Finds the next focusable component that fits in the specified bounds.
     * </p>
     *
     * @param pTopFocus look for a candidate is the one at the top of the bounds
     *                 if topFocus is true, or at the bottom of the bounds if topFocus is
     *                 false
     * @param pTop      the top offset of the bounds in which a focusable must be
     *                 found
     * @param pBottom   the bottom offset of the bounds in which a focusable must
     *                 be found
     * @return the next focusable component in the bounds or null if none can
     * be found
     */
    private View findFocusableViewInBounds(final boolean pTopFocus, final int pTop, final int pBottom, final boolean pLeftFocus, final int left, final int pRight) {
        List<View> focusables = getFocusables(View.FOCUS_FORWARD);
        View focusCandidate = null;

		/*
		 * A fully contained focusable is one where its top is below the bound's
		 * top, and its bottom is above the bound's bottom. A partially
		 * contained focusable is one where some part of it is within the
		 * bounds, but it also has some part that is not within bounds.  A fully contained
		 * focusable is preferred to a partially contained focusable.
		 */
        boolean foundFullyContainedFocusable = false;

        int count = focusables.size();
        for (int i = 0; i < count; i++) {
            View view = focusables.get(i);
            int viewTop = view.getTop();
            int viewBottom = view.getBottom();
            int viewLeft = view.getLeft();
            int viewRight = view.getRight();

            if (pTop < viewBottom && viewTop < pBottom && left < viewRight && viewLeft < pRight) {
				/*
				 * the focusable is in the target area, it is a candidate for
				 * focusing
				 */
                final boolean viewIsFullyContained = (pTop < viewTop) && (viewBottom < pBottom) && (left < viewLeft) && (viewRight < pRight);
                if (focusCandidate == null) {
					/* No candidate, take this one */
                    focusCandidate = view;
                    foundFullyContainedFocusable = viewIsFullyContained;
                } else {
                    final boolean viewIsCloserToVerticalBoundary =
                            (pTopFocus && viewTop < focusCandidate.getTop()) ||
                                    (!pTopFocus && viewBottom > focusCandidate.getBottom());
                    final boolean viewIsCloserToHorizontalBoundary =
                            (pLeftFocus && viewLeft < focusCandidate.getLeft()) ||
                                    (!pLeftFocus && viewRight > focusCandidate.getRight());
                    if (foundFullyContainedFocusable) {
                        if (viewIsFullyContained && viewIsCloserToVerticalBoundary && viewIsCloserToHorizontalBoundary) {
							/*
							 * We're dealing with only fully contained views, so
							 * it has to be closer to the boundary to beat our
							 * candidate
							 */
                            focusCandidate = view;
                        }
                    } else {
                        if (viewIsFullyContained) {
							/* Any fully contained view beats a partially contained view */
                            focusCandidate = view;
                            foundFullyContainedFocusable = true;
                        } else if (viewIsCloserToVerticalBoundary && viewIsCloserToHorizontalBoundary) {
							/*
							 * Partially contained view beats another partially
							 * contained view if it's closer
							 */
                            focusCandidate = view;
                        }
                    }
                }
            }
        }
        return focusCandidate;
    }

    /**
     * <p>Handles scrolling in response to a "home/end" shortcut press. This
     * method will scroll the view to the top or bottom and give the focus
     * to the topmost/bottommost component in the new visible area. If no
     * component is a good candidate for focus, this scrollview reclaims the
     * focus.</p>
     *
     * @param pDirection the scroll direction: {@link android.view.View#FOCUS_UP}
     *                  to go the top of the view or
     *                  {@link android.view.View#FOCUS_DOWN} to go the bottom
     * @return true if the key event is consumed by this method, false otherwise
     */
    public boolean fullScroll(final int pDirection, final boolean pHorizontal) {
        if (!pHorizontal) {
            boolean down = pDirection == View.FOCUS_DOWN;
            int height = getHeight();
            mTempRect.top = 0;
            mTempRect.bottom = height;
            if (down) {
                int count = getChildCount();
                if (count > 0) {
                    View view = getChildAt(count - 1);
                    mTempRect.bottom = view.getBottom();
                    mTempRect.top = mTempRect.bottom - height;
                }
            }
            return scrollAndFocus(pDirection, mTempRect.top, mTempRect.bottom, 0, 0, 0);
        } else {
            boolean right = pDirection == View.FOCUS_DOWN;
            int width = getWidth();
            mTempRect.left = 0;
            mTempRect.right = width;
            if (right) {
                int count = getChildCount();
                if (count > 0) {
                    View view = getChildAt(count - 1);
                    mTempRect.right = view.getBottom();
                    mTempRect.left = mTempRect.right - width;
                }
            }
            return scrollAndFocus(0, 0, 0, pDirection, mTempRect.top, mTempRect.bottom);
        }
    }

    /**
     * <p>Scrolls the view to make the area defined by <code>top</code> and
     * <code>bottom</code> visible. This method attempts to give the focus
     * to a component visible in this area. If no component can be focused in
     * the new visible area, the focus is reclaimed by this scrollview.</p>
     *
     * @param pDirectionX the scroll direction: {@link android.view.View#FOCUS_UP}
     *                   to go upward
     *                   {@link android.view.View#FOCUS_DOWN} to downward
     * @param pDirectionY the scroll direction: {@link android.view.View#FOCUS_UP}
     *                   to go upward
     *                   {@link android.view.View#FOCUS_DOWN} to downward
     * @param pTop        the top offset of the new area to be made visible
     * @param pBottom     the bottom offset of the new area to be made visible
     * @return true if the key event is consumed by this method, false otherwise
     */
    private boolean scrollAndFocus(final int pDirectionY, final int pTop, final int pBottom, final int pDirectionX, final int pLeft, final int pRight) {
        boolean handled = true;
        int height = getHeight();
        int containerTop = getScrollY();
        int containerBottom = containerTop + height;
        boolean up = pDirectionY == View.FOCUS_UP;
        int width = getWidth();
        int containerLeft = getScrollX();
        int containerRight = containerLeft + width;
        boolean leftwards = pDirectionX == View.FOCUS_UP;
        View newFocused = findFocusableViewInBounds(up, pTop, pBottom, leftwards, pLeft, pRight);
        if (newFocused == null) {
            newFocused = this;
        }
        if ((pTop >= containerTop && pBottom <= containerBottom) || (pLeft >= containerLeft && pRight <= containerRight)) {
            handled = false;
        } else {
            int deltaY = up ? (pTop - containerTop) : (pBottom - containerBottom);
            int deltaX = leftwards ? (pLeft - containerLeft) : (pRight - containerRight);
            doScroll(deltaX, deltaY);
        }
        if (newFocused != findFocus() && newFocused.requestFocus(pDirectionY)) {
            mScrollView2DMovedFocus = true;
            mScrollView2DMovedFocus = false;
        }
        return handled;
    }

    /**
     * Handle scrolling in response to an up or down arrow click.
     *
     * @param pDirection The direction corresponding to the arrow key that was
     *                  pressed
     * @return True if we consumed the event, false otherwise
     */
    public boolean arrowScroll(final int pDirection, final boolean pHorizontal) {
        View currentFocused = findFocus();
        if (currentFocused == this) currentFocused = null;
        View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, pDirection);
        final int maxJump = pHorizontal ? getMaxScrollAmountHorizontal() : getMaxScrollAmountVertical();

        if (!pHorizontal) {
            if (nextFocused != null) {
                nextFocused.getDrawingRect(mTempRect);
                offsetDescendantRectToMyCoords(nextFocused, mTempRect);
                int scrollDelta = computeScrollDeltaToGetChildRectOnScreen(mTempRect);
                doScroll(0, scrollDelta);
                nextFocused.requestFocus(pDirection);
            } else {
                // no new focus
                int scrollDelta = maxJump;
                if (pDirection == View.FOCUS_UP && getScrollY() < scrollDelta) {
                    scrollDelta = getScrollY();
                } else if (pDirection == View.FOCUS_DOWN) {
                    if (getChildCount() > 0) {
                        int daBottom = getChildAt(0).getBottom();
                        int screenBottom = getScrollY() + getHeight();
                        if (daBottom - screenBottom < maxJump) {
                            scrollDelta = daBottom - screenBottom;
                        }
                    }
                }
                if (scrollDelta == 0) {
                    return false;
                }
                doScroll(0, pDirection == View.FOCUS_DOWN ? scrollDelta : -scrollDelta);
            }
        } else {
            if (nextFocused != null) {
                nextFocused.getDrawingRect(mTempRect);
                offsetDescendantRectToMyCoords(nextFocused, mTempRect);
                int scrollDelta = computeScrollDeltaToGetChildRectOnScreen(mTempRect);
                doScroll(scrollDelta, 0);
                nextFocused.requestFocus(pDirection);
            } else {
                // no new focus
                int scrollDelta = maxJump;
                if (pDirection == View.FOCUS_UP && getScrollY() < scrollDelta) {
                    scrollDelta = getScrollY();
                } else if (pDirection == View.FOCUS_DOWN) {
                    if (getChildCount() > 0) {
                        int daBottom = getChildAt(0).getBottom();
                        int screenBottom = getScrollY() + getHeight();
                        if (daBottom - screenBottom < maxJump) {
                            scrollDelta = daBottom - screenBottom;
                        }
                    }
                }
                if (scrollDelta == 0) {
                    return false;
                }
                doScroll(pDirection == View.FOCUS_DOWN ? scrollDelta : -scrollDelta, 0);
            }
        }
        return true;
    }

    /**
     * Smooth scroll by a Y and X delta.
     *
     * @param pDeltaX the number of pixels to scroll by on the X axis.
     * @param pDeltaY the number of pixels to scroll by on the Y axis.
     */
    private void doScroll(final int pDeltaX, final int pDeltaY) {
        if (pDeltaX != 0 || pDeltaY != 0) {
            smoothScrollBy(pDeltaX, pDeltaY);
        }
    }

    /**
     * Like {@link View#scrollBy}, but scroll smoothly instead of immediately.
     *
     * @param pDeltaX the number of pixels to scroll by on the X axis.
     * @param pDeltaY the number of pixels to scroll by on the Y axis.
     */
    public final void smoothScrollBy(final int pDeltaX, final int pDeltaY) {
        long duration = AnimationUtils.currentAnimationTimeMillis() - mLastScrollTime;

        if (duration > ANIMATED_SCROLL_GAP) {
            mScroller.startScroll(getScrollX(), getScrollY(), pDeltaX, pDeltaY);
            awakenScrollBars(mScroller.getDuration());
            invalidate();
        } else {
            if (!mScroller.isFinished()) {
                mScroller.abortAnimation();
            }
            scrollBy(pDeltaX, pDeltaY);
        }
        mLastScrollTime = AnimationUtils.currentAnimationTimeMillis();
    }

    /**
     * Like {@link #scrollTo}, but scroll smoothly instead of immediately.
     *
     * @param pX the position where to scroll on the X axis
     * @param pY the position where to scroll on the Y axis
     */
    public final void smoothScrollTo(final int pX, final int pY) {
        smoothScrollBy(pX - getScrollX(), pY - getScrollY());
    }

    /**
     * <p>The scroll range of a scroll view is the overall height of all of its
     * children.</p>
     */
    @Override
    protected int computeVerticalScrollRange() {
        final int count = getChildCount();
        return count == 0 ? getHeight() : (getChildAt(0)).getBottom();
    }

    @Override
    protected int computeHorizontalScrollRange() {
        final int count = getChildCount();
        return count == 0 ? getWidth() : (getChildAt(0)).getRight();
    }

    @Override
    protected void measureChild(final View pChild, final int pParentWidthMeasureSpec, final int pParentHeightMeasureSpec) {

        final ViewGroup.LayoutParams lp = pChild.getLayoutParams();
        final int childWidthMeasureSpec = getChildMeasureSpec(pParentWidthMeasureSpec, getPaddingLeft() + getPaddingRight(), lp.width);
        final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        pChild.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void measureChildWithMargins(final View pChild, final int pParentWidthMeasureSpec, final int pWidthUsed, final int pParentHeightMeasureSpec, int pHeightUsed) {
        final MarginLayoutParams lp = (MarginLayoutParams) pChild.getLayoutParams();
        final int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(lp.leftMargin + lp.rightMargin, MeasureSpec.UNSPECIFIED);
        final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(lp.topMargin + lp.bottomMargin, MeasureSpec.UNSPECIFIED);

        pChild.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            // This is called at drawing time by ViewGroup.  We don't want to
            // re-show the scrollbars at this point, which scrollTo will do,
            // so we replicate most of scrollTo here.
            //
            //         It's a little odd to call onScrollChanged from inside the drawing.
            //
            //         It is, except when you remember that computeScroll() is used to
            //         animate scrolling. So unless we want to defer the onScrollChanged()
            //         until the end of the animated scrolling, we don't really have a
            //         choice here.
            //
            //         I agree.  The alternative, which I think would be worse, is to post
            //         something and tell the subclasses later.  This is bad because there
            //         will be a window where mScrollX/Y is different from what the app
            //         thinks it is.
            //
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            if (getChildCount() > 0) {
                View child = getChildAt(0);
                scrollTo(clamp(x, getWidth() - getPaddingRight() - getPaddingLeft(), child.getWidth()),
                        clamp(y, getHeight() - getPaddingBottom() - getPaddingTop(), child.getHeight()));
            } else {
                scrollTo(x, y);
            }
            if (oldX != getScrollX() || oldY != getScrollY()) {
                onScrollChanged(getScrollX(), getScrollY(), oldX, oldY);
            }

            // Keep on drawing until the animation has finished.
            postInvalidate();
        }
    }

    /**
     * Scrolls the view to the given child.
     *
     * @param pChild the View to scroll to
     */
    private void scrollToChild(final View pChild) {
        pChild.getDrawingRect(mTempRect);
		/* Offset from child's local coordinates to ScrollView2D coordinates */
        offsetDescendantRectToMyCoords(pChild, mTempRect);
        int scrollDelta = computeScrollDeltaToGetChildRectOnScreen(mTempRect);
        if (scrollDelta != 0) {
            scrollBy(0, scrollDelta);
        }
    }

    /**
     * If rect is off screen, scroll just enough to getOrCreate it (or at least the
     * first screen size chunk of it) on screen.
     *
     * @param pRect      The rectangle.
     * @param pImmediate True to scroll immediately without animation
     * @return true if scrolling was performed
     */
    private boolean scrollToChildRect(final Rect pRect, final boolean pImmediate) {
        final int delta = computeScrollDeltaToGetChildRectOnScreen(pRect);
        final boolean scroll = delta != 0;
        if (scroll) {
            if (pImmediate) {
                scrollBy(0, delta);
            } else {
                smoothScrollBy(0, delta);
            }
        }
        return scroll;
    }

    /**
     * Compute the amount to scroll in the Y direction in order to getOrCreate
     * a rectangle completely on the screen (or, if taller than the screen,
     * at least the first screen size chunk of it).
     *
     * @param pRect The rect.
     * @return The scroll delta.
     */
    protected int computeScrollDeltaToGetChildRectOnScreen(final Rect pRect) {

        if (getChildCount() == 0) {
            return 0;
        }

        final int height = getHeight();
        final int fadingEdge = getVerticalFadingEdgeLength();

        int screenTop = getScrollY();
        int screenBottom = screenTop + height;


        // leave room for top fading edge as long as rect isn't at very top
        if (pRect.top > 0) {
            screenTop += fadingEdge;
        }

        // leave room for bottom fading edge as long as rect isn't at very bottom
        if (pRect.bottom < getChildAt(0).getHeight()) {
            screenBottom -= fadingEdge;
        }
        int scrollYDelta = 0;
        if (pRect.bottom > screenBottom && pRect.top > screenTop) {
            // need to move down to getOrCreate it in view: move down just enough so
            // that the entire rectangle is in view (or at least the first
            // screen size chunk).
            if (pRect.height() > height) {
                // just enough to getOrCreate screen size chunk on
                scrollYDelta += (pRect.top - screenTop);
            } else {
                // getOrCreate entire rect at bottom of screen
                scrollYDelta += (pRect.bottom - screenBottom);
            }

            // make sure we aren't scrolling beyond the end of our content
            int bottom = getChildAt(0).getBottom();
            int distanceToBottom = bottom - screenBottom;
            scrollYDelta = Math.min(scrollYDelta, distanceToBottom);

        } else if (pRect.top < screenTop && pRect.bottom < screenBottom) {
            // need to move up to getOrCreate it in view: move up just enough so that
            // entire rectangle is in view (or at least the first screen
            // size chunk of it).

            if (pRect.height() > height) {
                // screen size chunk
                scrollYDelta -= (screenBottom - pRect.bottom);
            } else {
                // entire rect at top
                scrollYDelta -= (screenTop - pRect.top);
            }

            // make sure we aren't scrolling any further than the top our content
            scrollYDelta = Math.max(scrollYDelta, -getScrollY());
        }
        return scrollYDelta;
    }

    @Override
    public void requestChildFocus(final View pChild, final View pFocused) {
        if (!mScrollView2DMovedFocus) {
            if (!mIsLayoutDirty) {
                scrollToChild(pFocused);
            } else {
                // The child may not be laid out yet, we can't compute the scroll yet
                mChildToScrollTo = pFocused;
            }
        }
        super.requestChildFocus(pChild, pFocused);
    }

    /**
     * When looking for focus in children of a scroll view, need to be a little
     * more careful not to give focus to something that is scrolled off screen.
     * <p/>
     * This is more expensive than the default {@link android.view.ViewGroup}
     * implementation, otherwise this behavior might have been made the default.
     */
    @Override
    protected boolean onRequestFocusInDescendants(final int pDirection, final Rect pPreviouslyFocusedRect) {
        // convert from forward / backward notation to up / down / left / right
        // (ugh).

        int direction = pDirection;

        if (direction == View.FOCUS_FORWARD) {
            direction = View.FOCUS_DOWN;
        } else if (direction == View.FOCUS_BACKWARD) {
            direction = View.FOCUS_UP;
        }

        final View nextFocus = pPreviouslyFocusedRect == null ?
                FocusFinder.getInstance().findNextFocus(this, null, direction) :
                FocusFinder.getInstance().findNextFocusFromRect(this,
                        pPreviouslyFocusedRect, direction);

        if (nextFocus == null) {
            return false;
        }

        return nextFocus.requestFocus(direction, pPreviouslyFocusedRect);
    }

    @Override
    public boolean requestChildRectangleOnScreen(final View pChild, final Rect pRectangle, final boolean pImmidiate) {
        // offset into coordinate space of this scroll view
        pRectangle.offset(pChild.getLeft() - pChild.getScrollX(), pChild.getTop() - pChild.getScrollY());
        return scrollToChildRect(pRectangle, pImmidiate);
    }

    @Override
    public void requestLayout() {
        mIsLayoutDirty = true;
        super.requestLayout();
    }

    @Override
    protected void onLayout(final boolean pChanged, final int pLeft, final int pTop, final int pRight, final int pBottom) {
        super.onLayout(pChanged, pLeft, pTop, pRight, pBottom);

        mIsLayoutDirty = false;
        // Give a child focus if it needs it
        if (mChildToScrollTo != null && isViewDescendantOf(mChildToScrollTo, this)) {
            scrollToChild(mChildToScrollTo);
        }
        mChildToScrollTo = null;

        // Calling this with the present values causes it to re-clam them
        scrollTo(getScrollX(), getScrollY());
    }

    @Override
    protected void onSizeChanged(final int pWidth, final int pHeight, final int pOldWidth, final int pOldHeight) {
        super.onSizeChanged(pWidth, pHeight, pOldWidth, pOldHeight);

        final View currentFocused = findFocus();

        if (null == currentFocused || this == currentFocused) {
            return;
        }

        // If the currently-focused view was visible on the screen when the
        // screen was at the old height, then scroll the screen to make that
        // view visible with the new screen height.
        currentFocused.getDrawingRect(mTempRect);
        offsetDescendantRectToMyCoords(currentFocused, mTempRect);

        final int scrollDeltaX = computeScrollDeltaToGetChildRectOnScreen(mTempRect);
        final int scrollDeltaY = computeScrollDeltaToGetChildRectOnScreen(mTempRect);
        doScroll(scrollDeltaX, scrollDeltaY);
    }

    @Override
    protected void onScrollChanged(final int pLeft, final int pTop, final int pOldLeft, final int pOldTop) {
        super.onScrollChanged(pLeft, pTop, pOldLeft, pOldTop);

        if (mListener != null) {
            mListener.onScrollChanged(pLeft, pTop, pOldLeft, pOldTop);
        }
    }

    /**
     * Tests if the given child is an descendant of parent, (or equal to the parent).

     * @param pChild A child {@link View}.
     * @param pParent A parent {@link View}.
     * @return A {@code boolean} value.
     */
    private boolean isViewDescendantOf(final View pChild, final View pParent) {
        if (pChild == pParent) {
            return true;
        }

        final ViewParent parent = pChild.getParent();
        return (parent instanceof ViewGroup) && isViewDescendantOf((View) parent, pParent);
    }

    /**
     * Fling the scroll view
     *
     * @param pVelocityX The initial velocity in the X direction. Positive
     *                  numbers mean that the finger/cursor is moving down the screen,
     *                  which means we want to scroll towards the right.
     * @param pVelocityY The initial velocity in the Y direction. Positive
     *                  numbers mean that the finger/cursor is moving down the screen,
     *                  which means we want to scroll towards the top.
     */
    public void fling(final int pVelocityX, final int pVelocityY) {
        if (getChildCount() > 0) {
            int height = getHeight() - getPaddingBottom() - getPaddingTop();
            int bottom = getChildAt(0).getHeight();
            int width = getWidth() - getPaddingRight() - getPaddingLeft();
            int right = getChildAt(0).getWidth();

            mScroller.fling(getScrollX(), getScrollY(), pVelocityX, pVelocityY, 0, right - width, 0, bottom - height);

            final boolean movingDown = pVelocityY > 0;
            final boolean movingRight = pVelocityX > 0;

            View newFocused = findFocusableViewInMyBounds(movingRight, mScroller.getFinalX(), movingDown, mScroller.getFinalY(), findFocus());
            if (newFocused == null) {
                newFocused = this;
            }

            if (newFocused != findFocus() && newFocused.requestFocus(movingDown ? View.FOCUS_DOWN : View.FOCUS_UP)) {
                mScrollView2DMovedFocus = true;
                mScrollView2DMovedFocus = false;
            }

            awakenScrollBars(mScroller.getDuration());
            invalidate();
        }
    }

    /**
     * {@inheritDoc}
     * <p/>
     * <p>This version also clamps the scrolling to the bounds of our child.
     */
    public void scrollTo(final int pX, final int pY) {
        // we rely on the fact the View.scrollBy calls scrollTo.

        if (getChildCount() > 0) {
            final View child = getChildAt(0);
            final int x = clamp(pX, getWidth() - getPaddingRight() - getPaddingLeft(), child.getWidth());
            final int y = clamp(pY, getHeight() - getPaddingBottom() - getPaddingTop(), child.getHeight());

            if (x != getScrollX() || y != getScrollY()) {
                super.scrollTo(x, y);
            }
        }
    }

    private int clamp(final int pScrollOffset, final int pMyWidth, final int pChildWidth) {
        if (pMyWidth >= pChildWidth || pScrollOffset < 0) {
			/* my >= child is this case:
			 *                    |--------------- me ---------------|
			 *     |------ child ------|
			 * or
			 *     |--------------- me ---------------|
			 *            |------ child ------|
			 * or
			 *     |--------------- me ---------------|
			 *                                  |------ child ------|
			 *
			 * n < 0 is this case:
			 *     |------ me ------|
			 *                    |-------- child --------|
			 *     |-- mScrollX --|
			 */
            return 0;
        }
        if ((pMyWidth + pScrollOffset) > pChildWidth) {
			/* this case:
			 *                    |------ me ------|
			 *     |------ child ------|
			 *     |-- mScrollX --|
			 */
            return pChildWidth - pMyWidth;
        }
        return pScrollOffset;
    }


    /**
     * {@link ScrollListener} defines a listener interface for listening scroll state change events
     * produced by an instance of {@link ScrollView2D}.
     */
    public interface ScrollListener {
        void onScrollChanged(int pLeft, int pTop, int pOldLeft, int pOldTop);
    }
}