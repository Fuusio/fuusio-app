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
package org.fuusio.api.binding;

import android.view.View;

import org.fuusio.api.mvp.Presenter;
import org.fuusio.api.util.MessageContext;

/**
 * {@link ViewBinding} provides an abstract base class for objects that establish bindings from
 * UI widgets ({@link View} instances) to other objects, such as {@link Presenter}s.
 */
public abstract class ViewBinding<T_View extends View> implements View.OnClickListener {

    protected  MessageContext mErrorMessage;
    protected T_View mView;

    protected ViewBinding() {
    }

    protected ViewBinding(final T_View pView) {
        if (pView != null) {
            setView(pView);
        } else {
            mView = null;
        }
    }

    /**
     * Tests if the attached {@link View} is enabled.
     * @return A {@link boolean}.
     */
    public final boolean isViewEnabled() {
        return mView.isEnabled();
    }

    public final void setViewEnabled(final boolean pEnabled) {
        mView.setEnabled(pEnabled);
    }

    @SuppressWarnings("unchecked")
    public final <T> T getViewTag() {
        return (T)mView.getTag();
    }

    @SuppressWarnings("unchecked")
    public final <T> T getViewTag(final int pKey) {
        return (T)mView.getTag(pKey);
    }

    public void setViewTag(final Object pTag) {
        mView.setTag(pTag);
    }

    public void setViewTag(final int pKey, final Object pTag) {
        mView.setTag(pKey, pTag);
    }

    /**
     * Gets the {@link View} bound to this {@link ViewBinding}.
     * @return A {@link View}.
     */
    public final T_View getView() {
        return mView;
    }

    /**
     * Sets the {@link View} bound to this {@link ViewBinding}.
     * @param pView A {@link View}.
     */
    public void setView(final T_View pView) {

        if (pView != null) {
            mView = pView;
            mErrorMessage = new MessageContext(pView.getContext());
            attachListeners(mView);
        } else if (mView != null){
            detachListeners(mView);
            mView = null;
        }
    }

    public final int getViewVisibility() {
        return mView.getVisibility();
    }

    public final void setViewVisibility(final int pVisibility) {
        mView.setVisibility(pVisibility);
    }

    /**
     * Tests if the given {@link View} can be bound to this {@link ViewBinding}.
     * @param pView A  {@link View}.
     * @return A {@code boolean} value.
     */
    public abstract boolean canBind(final View pView);

    /**
     * Invoked to attach the listeners to the given {@link View}. Methods overriding this method
     * has to call {@code super.attachListener(pView)}.
     * @param pView A {@link View}.
     */
    protected void attachListeners(final T_View pView) {
        pView.setOnClickListener(this);
    }

    /**
     * Invoked to detach the listeners from the given {@link View}. Methods overriding this method
     * has to call {@code super.detachListeners(pView)}.
     * @param pView A {@link View}.
     */
    protected void detachListeners(final T_View pView) {
        pView.setOnClickListener(null);
    }

    @Override
    public void onClick(final View pView) {
        clicked();
    }

    /**
     * This method should be overridden for dispatching {@link View.OnClickListener} events.
     */
    protected void clicked() {
    }

    public void setErrorMessage(final String pMessage, final Object... pArgs) {
        mErrorMessage.setMessage(pMessage);
        mErrorMessage.setMessageArgs(pArgs);
    }

    public void setErrorMessage(final int pMessageResId, final Object... pArgs) {
        mErrorMessage.setMessage(pMessageResId);
        mErrorMessage.setMessageArgs(pArgs);
    }

    public void clearErrorMessage() {
        mErrorMessage.clear();
    }
}
