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

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import org.fuusio.api.model.Property.PropertyGetter;
import org.fuusio.api.model.Property.PropertySetter;
import org.fuusio.api.util.L;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

public abstract class ModelObject extends Observable implements Observer {

    public final static String KEY_ID = "Id";
    public final static String KEY_CLASS = "Class";
    public final static String KEY_PROPERTIES = "Properties";

    public static long sLastGeneratedId = 0;

    protected long mId;

    protected transient boolean mChanged;
    protected transient ModelObjectContext mContext;
    protected transient boolean mInitialized;

    protected ModelObject() {
        super();

        mId = generateId();
        mChanged = false;
        mInitialized = false;
    }

    protected ModelObject(final ModelObject source) {
        this();
        mContext = source.mContext;
    }

    public final ModelObjectContext getContext() {
        return mContext;
    }

    public void setContext(final ModelObjectContext context) {
        mContext = context;
    }

    public <T extends ModelObject> T copy() {
        return null;
    }

    private long generateId() {
        long generatedId = 0;
        do {
            generatedId = System.currentTimeMillis();
        } while (generatedId == sLastGeneratedId);

        sLastGeneratedId = generatedId;
        return generatedId;
    }

    public boolean isChanged() {
        return mChanged;
    }

    public void setChanged(final boolean changed) {
        mChanged = changed;
    }

    public int getColumnIndex(final String propertyName) {

        if (KEY_ID.equals(propertyName)) {
            return 0;
        } else if (KEY_CLASS.equals(propertyName)) {
            return 1;
        } else if (KEY_PROPERTIES.equals(propertyName)) {
            return 2;
        }
        return -1;
    }

    public String[] getColumns() {
        return null;
    }

    public Uri getContentUri() {
        return null;
    }

    public Uri getObjectUri() {
        final String id = Long.toString(getId());
        return getContentUri().buildUpon().appendPath(id).build();
    }

    @PropertyGetter(property = "Id", isDescriptor = true)
    public final long getId() {
        return mId;
    }

    @PropertySetter(property = "Id")
    public final void setId(final long id) {
        mId = id;
    }

    public final boolean isInitialized() {
        return mInitialized;
    }

    public void setInitialized(final boolean pInitialized) {
        mInitialized = pInitialized;
    }

    public final <T> T get(final String propertyName) {
        final Property property = mContext.getProperty(getClass(), propertyName);
        return property.get(this);
    }

    public final <T> T get(final Property property) {
        return property.get(this);
    }

    public void set(final String propertyName, final Object propertyValue) {
        final Property property = mContext.getProperty(getClass(), propertyName);
        property.set(this, propertyValue);
    }

    public void set(final Property property, final Object propertyValue) {
        property.set(this, propertyValue);
    }

    public final Property getProperty(final String propertyName) {
        return mContext.getProperty(getClass(), propertyName);
    }

    public final Collection<Property> getProperties() {
        return mContext.getProperties(getClass());
    }

    public boolean existsInDatabase() {
        return mContext.existsInDatabase(getContentUri(), getId());
    }

    public boolean existsInDatabase(final long id) {
        return mContext.existsInDatabase(getContentUri(), id);
    }

    public boolean readFromDatabase(final long id) {
        final Uri uri = ContentUris.withAppendedId(getContentUri(), id);
        final ContentResolver resolver = mContext.getContentResolver();
        final ContentProviderClient providerClient = resolver.acquireContentProviderClient(uri);

        try {
            final Cursor cursor = providerClient.query(uri, getColumns(), null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    for (Property property : getProperties()) {
                        if (!property.isTransient() && !property.isSynthetic()) {
                            int columnIndex = getColumnIndex(property.getName());

                            if (!property.is("_id") && columnIndex >= 0) {
                                readProperty(property, cursor, columnIndex);
                            }
                        }
                    }
                }

                cursor.close();
            }
        } catch (final Exception pException) {
            // DEBUG L.wtf(this, "readFromDatabase", "Failed to ModelObject with id: " + id);
            return false;
        }

        providerClient.release();
        return true;
    }

    protected void readProperty(final Property property, final Cursor cursor, final int columnIndex) {
        final Object value = property.read(cursor, columnIndex);
        property.set(this, value);
    }

    public boolean saveToDatabase(final long id) {

        if (!isInitialized()) {
            L.wtf(this, "saveToDatabase", "ModelObject is not initialized");
            return false;
        }

        final ContentValues values = new ContentValues();

        for (final Property property : getProperties()) {

            if (!property.isTransient() && !property.isSynthetic()) {
                int columnIndex = getColumnIndex(property.getName());

                if (!property.is(KEY_ID) && columnIndex >= 0) {
                    saveProperty(property, values);
                }
            }
        }

        final NotifyingAsyncQueryHandler handler = new NotifyingAsyncQueryHandler(mContext.getContentResolver(), null);
        handler.startInsert(getContentUri(), values);
        return true;
    }

    public void copyPropertiesFrom(final ModelObject other) {
        for (final Property property : other.getProperties()) {
            final String propertyName = property.getName();
            this.set(propertyName, property.get(other));
        }
    }

    protected void saveProperty(final Property property, final ContentValues values) {
        // By default do nothing
    }

    public boolean removeFromDatabase() {
        final NotifyingAsyncQueryHandler handler = new NotifyingAsyncQueryHandler(
                mContext.getContentResolver(), null);
        handler.startDelete(getObjectUri());
        return true;
    }

    @Override
    public void update(final Observable observable, final Object data) {
        setChanged();
        notifyObservers(this);
    }

    public boolean serializeAsReferencedObject() {
        return true;
    }

    public void getContentValues(final ContentValues values) {
        values.put(KEY_ID, getId());
        values.put(KEY_CLASS, getClass().getName());

        JSONObject propertiesObject = null;

        try {
            final JSONSerializer serializer = new JSONSerializer(getContext());
            propertiesObject = serializer.writePropertiesObject(this);
        } catch (final JSONException pException) {
            pException.printStackTrace();
        }

        final String propertiesString = propertiesObject.toString();

        values.put(KEY_PROPERTIES, propertiesString);
    }

    public String getString(final int resId) {
        return ModelObjectManager.getString(resId);
    }
}
