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

import android.content.ContentValues;

import org.fuusio.api.model.Property.PropertyGetter;
import org.fuusio.api.model.Property.PropertySetter;
import org.fuusio.api.util.DateToolkit;

import java.util.Date;

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

    protected Model(final String name) {
        this();
        mName = name;
    }

    @Override
    public int getColumnIndex(final String propertyName) {

        if (KEY_CREATED_DATE.equals(propertyName)) {
            return 3;
        } else if (KEY_NAME.equals(propertyName)) {
            return 4;
        } else if (KEY_PROPERTIES.equals(propertyName)) {
            return 5;
        } else {
            return super.getColumnIndex(propertyName);
        }
    }

    @Override
    public void getContentValues(final ContentValues values) {
        super.getContentValues(values);

        values.put(KEY_NAME, getName());
        values.put(KEY_CREATED_DATE, DateToolkit.format(getCreatedDate()));
    }

    @PropertyGetter(property = "CreatedDate", isDescriptor = true)
    public Date getCreatedDate() {
        return mCreatedDate;
    }

    @PropertySetter(property = "CreatedDate")
    public void setCreatedDate(final Date date) {
        mCreatedDate = date;
    }

    public boolean isDescriptor() {
        return mDescriptor;
    }

    public void setDescriptor(final boolean descriptor) {
        mDescriptor = descriptor;
    }

    // OPTION @PropertyGetter(property = "Modifiable", isDescriptor = true)
    public boolean isModifiable() {
        return mModifiable;
    }

    // OPTION @PropertySetter(property = "Modifiable")
    public void setModifiable(final boolean modifiable) {
        mModifiable = modifiable;
    }

    public String getModelTypeName() {
        return getClass().getSimpleName();
    }

    @PropertyGetter(property = "Name", isDescriptor = true)
    public String getName() {
        return mName;
    }

    @PropertySetter(property = "Name")
    public void setName(final String name) {
        mName = name;
    }

    @PropertyGetter(property = "Namespace", isDescriptor = true)
    public String getNamespace() {
        return mNamespace;
    }

    @PropertySetter(property = "Namespace")
    public void setNamespace(final String namespace) {
        mNamespace = namespace;
    }    
    /*
     * OPTION
     * 
     * @PropertyGetter(property = "Namespace", isDescriptor = true) public String getNamespace()
     * { return mNamespace; }
     * 
     * @PropertySetter(property = "Namespace") public void setNamespace(final String namespace)
     * { mNamespace = namespace; }
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

    public void setRemovable(final boolean removable) {
        mRemovable = removable;
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
