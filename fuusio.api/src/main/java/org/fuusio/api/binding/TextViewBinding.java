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

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

public class TextViewBinding extends ViewBinding<TextView> implements TextWatcher {

    private boolean mValidValue;

    public TextViewBinding(final TextView pView) {
        super(pView);
        mValidValue = true;
    }

    protected TextViewBinding() {
        this(null);
    }

    /**
     * Set text of the bound {@link TextView}.
     * @param pText A {@link String} containing the text.
     */
    public void setText(final String pText) {
        mView.setText(pText);
    }

    @Override
    public final boolean canBind(final View pView) {
        return (pView instanceof TextView);
    }

    /**
     * TODO
     * @param pView
     */
    @Override
    protected final void attachListeners(final TextView pView) {
        super.attachListeners(pView);
        pView.addTextChangedListener(this);
    }

    /**
     * TODO
     * @param pView
     */
    @Override
    protected final void detachListeners(final TextView pView) {
        super.detachListeners(pView);
        pView.removeTextChangedListener(this);
    }

    @Override
    public final void beforeTextChanged(final CharSequence pSequence, final int pStart, final int pCount, final int pAfter) {
        textChanging(pSequence.toString(), pStart, pCount, pAfter);
    }

    @Override
    public final void onTextChanged(final CharSequence pSequence, final int pStart, final int pBefore, final int pCount) {
        mErrorMessage.clear();

        mValidValue = isValidValue(pSequence.toString());

        if (mValidValue) {
            textChanged(pSequence.toString(), pStart, pBefore, pCount);
        } else {
            // TODO
        }
    }

    @Override
    public final void afterTextChanged(final Editable pEditable) {

        final String text = pEditable.toString();
        mValidValue = isValidValue(text);

        if (mValidValue) {

            setValue(text);

            editableCommitted(pEditable);
            textCommitted(pEditable.toString());
        } else {
            // TODO
        }
    }
    /**
     * This method should be overridden for delegating {@link TextWatcher#afterTextChanged(Editable)}
     * events with {@link Editable} parameter.
     */
    protected void editableCommitted(final Editable pEditable) {
    }

    /**
     * This method should be overridden for delegating {@link TextWatcher#afterTextChanged(Editable)}
     * events with {@link String} parameter.
     */
    protected void textCommitted(final String pText) {
    }

    /**
     * This method should be overridden for delegating {@link TextWatcher#beforeTextChanged(CharSequence, int, int, int)} events.
     */
    protected void textChanging(final String pText, final int pStart, final int pCount, final int pAfter) {
    }

    /**
     * This method should be overridden for delegating {@link TextWatcher#onTextChanged(CharSequence, int, int, int)} events.
     */
    protected void textChanged(final CharSequence pSequence, final int pStart, final int pBefore, final int pCount) {
    }

    /**
     * Sets the given value via this {@link TextViewBinding} to target.
     * @param pText A {@link String} representing the value.
     * @return The value as an {@link Object}. By default this value is {@code null}.
     */
    protected Object setValue(final String pText) {
        // Do nothing by default
        return null;
    }

    /**
     * Tests if the given text represents a valid input value for the assigned {@link View}.
     * @param pText The input value given as a {@link String}.
     * @return A {@code boolean} value.
     */
    protected boolean isValidValue(final String pText) {
        return true;
    }
}
