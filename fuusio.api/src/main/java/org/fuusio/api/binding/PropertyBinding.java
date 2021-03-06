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
import android.widget.TextView;

import org.fuusio.api.model.ModelObject;
import org.fuusio.api.model.Property;

public class PropertyBinding extends TextViewBinding {

    private ModelObject mModelObject;
    private Property mProperty;

    public PropertyBinding(final TextView view) {
        super(view);
    }

    /**
     * Gets the assigned {@link ModelObject} that owns the target {@link Property}.
     *
     * @return A {@link ModelObject}
     */
    public final ModelObject getModelObject() {
        return mModelObject;
    }

    /**
     * Sets the assigned {@link ModelObject} that owns the target {@link Property}.
     *
     * @param object A {@link ModelObject}
     */
    public void setModelObject(final ModelObject object) {
        mModelObject = object;
    }

    /**
     * Gets the target {@link Property}.
     *
     * @return A {@link Property}
     */
    public final Property getProperty() {
        return mProperty;
    }

    /**
     * Sets the target {@link Property}.
     *
     * @param property A {@link Property}
     */
    public void setProperty(final Property property) {
        mProperty = property;
    }

    /**
     * Sets the given value via this {@link TextViewBinding} to target.
     *
     * @param text A {@link String} representing the value.
     */
    protected final Object setValue(final String text) {
        final Object value = mProperty.convertStringToValue(mModelObject, text, mProperty.getType());
        mProperty.set(mModelObject, value);
        return value;
    }

    /**
     * Tests if the given text represents a valid input value for the assigned {@link View}.
     *
     * @param text The input value given as a {@link String}.
     * @return A {@code boolean} value.
     */
    protected boolean isValidValue(final String text) {
        final Object value = mProperty.convertStringToValue(mModelObject, text, mProperty.getType());
        return mProperty.validate(mModelObject, value, mErrorMessage);
    }
}
