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
package org.fuusio.api.model;

import java.util.Date;

import org.fuusio.api.model.Property.PropertyGetter;
import org.fuusio.api.model.Property.PropertySetter;
import org.fuusio.api.util.DateToolkit;

import android.content.ContentValues;

public abstract class Model extends ModelObject {

    public final static String KEY_CREATED_DATE = "CreatedDate";
    public final static String KEY_NAME = "Name";

    protected Date mCreatedDate;
    protected String mName;
    protected String mNamespace;

    protected transient boolean mDescriptor;
    protected transient boolean mModifiable;
    protected transient boolean mRemovable;

    protected Model() {
        super();
        mChanged = true;
        mModifiable = true;
    }

    protected Model(final String pName) {
        this();
        mName = pName;
    }

    @Override
    public int getColumnIndex(final String pPropertyName) {

        if (KEY_CREATED_DATE.equals(pPropertyName)) {
            return 3;
        } else if (KEY_NAME.equals(pPropertyName)) {
            return 4;
        } else if (KEY_PROPERTIES.equals(pPropertyName)) {
            return 5;
        } else {
            return super.getColumnIndex(pPropertyName);
        }
    }

    @Override
    public void getContentValues(final ContentValues pValues) {
        super.getContentValues(pValues);

    	pValues.put(KEY_NAME, getName());
    	pValues.put(KEY_CREATED_DATE, DateToolkit.format(getCreatedDate()));
    }

    @PropertyGetter(property = "CreatedDate", isDescriptor = true)
    public Date getCreatedDate() {
        return mCreatedDate;
    }

    @PropertySetter(property = "CreatedDate")
    public void setCreatedDate(final Date pDate) {
        mCreatedDate = pDate;
    }

    public boolean isDescriptor() {
        return mDescriptor;
    }

    public void setDescriptor(final boolean pDescriptor) {
        mDescriptor = pDescriptor;
    }

    // OPTION @PropertyGetter(property = "Modifiable", isDescriptor = true)
    public boolean isModifiable() {
        return mModifiable;
    }

    // OPTION @PropertySetter(property = "Modifiable")
    public void setModifiable(final boolean pModifiable) {
        mModifiable = pModifiable;
    }

    public String getModelTypeName() {
        return getClass().getSimpleName();
    }

    @PropertyGetter(property = "Name", isDescriptor = true)
    public String getName() {
        return mName;
    }

    @PropertySetter(property = "Name")
    public void setName(final String pName) {
        mName = pName;
    }

    @PropertyGetter(property = "Namespace", isDescriptor = true)
    public String getNamespace() {
        return mNamespace;
    }

    @PropertySetter(property = "Namespace")
    public void setNamespace(final String pNamespace) {
    	mNamespace = pNamespace;
    }    
    /*
     * OPTION
     * 
     * @PropertyGetter(property = "Namespace", isDescriptor = true) public String getNamespace()
     * { return mNamespace; }
     * 
     * @PropertySetter(property = "Namespace") public void setNamespace(final String pNamespace)
     * { mNamespace = pNamespace; }
     */
    /*
    @SuppressWarnings("unchecked")
    public <T_Parent extends Model> T_Parent getParent() {
        return (T_Parent) mParent;
    }

    public void setParent(final Model pParent) {
        mParent = pParent;
    }*/

    public boolean isRemovable() {
        return mRemovable;
    }

    public void setRemovable(final boolean pRemovable) {
        mRemovable = pRemovable;
    }

    public String getFullyQualifiedName() {
        final StringBuilder name = new StringBuilder();

        /* OPTION
         * if (mNamespace != null) { name.append(mNamespace); name.append('.'); }
         */

        if (mName != null) {
            name.append(mName);
        }

        return name.toString();
    }

}
