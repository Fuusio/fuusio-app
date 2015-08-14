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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.fuusio.api.util.DateToolkit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONModelReader {

    private final ModelObjectContext mObjectContext;

    public JSONModelReader(final ModelObjectContext pObjectContext) {
        mObjectContext = pObjectContext;
    }

    public <T extends Model> T readModel(final JSONObject pSerializedObject)
            throws JSONException {
        return readModel(pSerializedObject, false);
    }

    public <T extends Model> T readModelDescriptor(final JSONObject pSerializedObject)
            throws JSONException {
        return readModel(pSerializedObject, true);
    }

    @SuppressWarnings("unchecked")
    private <T extends Model> T readModel(final JSONObject pSerializedObject,
            final boolean pReadAsDescriptor) throws JSONException {

        final String className = pSerializedObject.getString(ModelObject.KEY_CLASS);
        Class<T> modelClass = null;

        try {
            modelClass = (Class<T>) Class.forName(className);
        } catch (final ClassNotFoundException pException) {
            pException.printStackTrace();
        }

        final T model = mObjectContext.createInstance(modelClass);
        final JSONObject propertiesObject = pSerializedObject
                .getJSONObject(ModelObject.KEY_PROPERTIES);
        final Collection<Property> properties = pReadAsDescriptor ? mObjectContext
                .getDescriptorProperties(model) : mObjectContext.getProperties(model.getClass());

        for (final Property property : properties) {
            readValue(model, property, propertiesObject);
        }
        return model;
    }

    @SuppressWarnings("unchecked")
    public ModelObject readModelObject(final JSONObject pSerializedObject)
            throws JSONException {
        final String className = pSerializedObject.getString(ModelObject.KEY_CLASS);
        Class<? extends ModelObject> objectClass = null;

        try {
            objectClass = (Class<? extends ModelObject>) Class.forName(className);
        } catch (final ClassNotFoundException pException) {
            pException.printStackTrace();
        }

        final ModelObject modelObject = mObjectContext.createInstance(objectClass);
        final JSONObject propertiesObject = pSerializedObject
                .getJSONObject(ModelObject.KEY_PROPERTIES);

        for (final Property property : mObjectContext.getProperties(modelObject.getClass())) {
            readValue(modelObject, property, propertiesObject);
        }
        return modelObject;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public HashMap<?, ?> readHashMap(final JSONObject pObject) throws JSONException {
        final HashMap hashMap = new HashMap();
        final Iterator i = pObject.keys();

        while (i.hasNext()) {
            final String key = i.next().toString();
            final Object value = pObject.get(key);

            if (value instanceof JSONObject) {
                final JSONObject valueObject = JSONObject.class.cast(value);

                if (valueObject.has(ModelObject.KEY_CLASS)) {
                    final ModelObject modelObject = readModelObject(valueObject);
                    hashMap.put(key, modelObject);
                } else {
                    // TODO
                }
            }
        }
        return hashMap;
    }

    @SuppressWarnings("unchecked")
    public <T> T[] readArray(final JSONArray pArray, final Class<?> pComponentType)
            throws JSONException {
        final int size = pArray.length();
        final T[] array = (T[]) Array.newInstance(pComponentType, size);

        for (int i = 0; i < size; i++) {
            array[i] = (T) readValue(pArray.get(i), pComponentType);
        }
        return array;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ArrayList<?> readArrayList(final JSONArray pArray, final Class<?> pListType,
            final Class<?> pComponentType) throws JSONException {
        final int size = pArray.length();

        ArrayList arrayList = null;
        try {
            arrayList = (ArrayList) pListType.newInstance();
        } catch (final Exception pException) {
        }

        for (int i = 0; i < size; i++) {
            final Object value = readValue(pArray.get(i), pComponentType);
            arrayList.add(value);
        }
        return arrayList;
    }

    protected void readValue(final ModelObject pObject, final Property pProperty,
            final JSONObject pPropertyObject) throws JSONException {
        final String valueName = pProperty.getName();

        if (!pPropertyObject.has(valueName)) {
            return;
        }

        final Class<?> valueType = pProperty.getType();
        String valueString = null;
        Object value = null;

        if (valueType.equals(Boolean.class) || valueType.equals(Boolean.TYPE)) {
            value = pPropertyObject.getBoolean(valueName);
        } else if (valueType.equals(Byte.class) || valueType.equals(Byte.TYPE)) {
            value = (byte) pPropertyObject.getInt(valueName);
        } else if (valueType.equals(Date.class)) {
            try {
                final Date date = DateToolkit.parseRFC822(valueString);
                value = date;
            } catch (final Exception pException) {
                Log.e("JSONSerializer", "Date parsing error");
            }
        } else if (valueType.equals(Double.class) || valueType.equals(Double.TYPE)) {
            value = pPropertyObject.getDouble(valueName);
        } else if (valueType.equals(Float.class) || valueType.equals(Float.TYPE)) {
            value = (float) pPropertyObject.getDouble(valueName);
        } else if (valueType.equals(Integer.class) || valueType.equals(Integer.TYPE)) {
            value = pPropertyObject.getInt(valueName);
        } else if (valueType.equals(Long.class) || valueType.equals(Long.TYPE)) {
            value = pPropertyObject.getLong(valueName);
        } else if (valueType.equals(Short.class) || valueType.equals(Short.TYPE)) {
            value = (short) pPropertyObject.getInt(valueName);
        } else if (valueType.equals(String.class)) {
            value = valueString;
        } else if (ArrayList.class.isAssignableFrom(valueType)) {
            final JSONArray valueArray = pPropertyObject.getJSONArray(valueName);
            final Class<?> componentType = pProperty.getComponentType();
            value = readArrayList(valueArray, valueType, componentType);
        } else if (valueType.equals(HashMap.class)) {
            final JSONObject hashMapObject = pPropertyObject.getJSONObject(valueName);
            value = readHashMap(hashMapObject);
        } else if (valueType.isArray()) {
            final JSONArray valueArray = pPropertyObject.getJSONArray(valueName);
            value = readArray(valueArray, valueType.getComponentType());
        } else if (valueType.isEnum()) {
            value = readEnumValue(valueType, pPropertyObject.get(valueName).toString());
        } else if (ModelObject.class.isAssignableFrom(valueType)) {
            value = readModelObject(pPropertyObject.getJSONObject(valueName));
        } else {
            throw new UnsupportedOperationException();
        }
        pProperty.set(pObject, value);
    }

    protected Object readValue(final Object pObject, final Class<?> pType)
            throws JSONException {

        if (pObject == null) {
            return null;
        }

        if (pType.equals(Boolean.class) || pType.equals(Boolean.TYPE)) {

            if (pObject instanceof Boolean) {
                return pObject;
            } else {
                return Boolean.parseBoolean(pObject.toString());
            }
        } else if (pType.equals(Byte.class) || pType.equals(Byte.TYPE)) {

            if (pObject instanceof Byte) {
                return pObject;
            } else {
                return Byte.parseByte(pObject.toString());
            }
        } else if (pType.equals(Date.class)) {
            try {
                return DateToolkit.parseRFC822(pObject.toString());
            } catch (final Exception pException) {
                Log.e("JSONSerializer", "Date parsing error");
            }
        } else if (pType.equals(Double.class) || pType.equals(Double.TYPE)) {

            if (pObject instanceof Double) {
                return pObject;
            } else {
                return Double.parseDouble(pObject.toString());
            }
        } else if (pType.equals(Float.class) || pType.equals(Float.TYPE)) {

            if (pObject instanceof Float) {
                return pObject;
            } else {
                return Float.parseFloat(pObject.toString());
            }
        } else if (pType.equals(Integer.class) || pType.equals(Integer.TYPE)) {

            if (pObject instanceof Integer) {
                return pObject;
            } else {
                return Integer.parseInt(pObject.toString());
            }
        } else if (pType.equals(Long.class) || pType.equals(Long.TYPE)) {

            if (pObject instanceof Long) {
                return pObject;
            } else {
                return Long.parseLong(pObject.toString());
            }
        } else if (pType.equals(Short.class) || pType.equals(Short.TYPE)) {

            if (pObject instanceof Short) {
                return pObject;
            } else {
                return Short.parseShort(pObject.toString());
            }
        } else if (pType.equals(String.class)) {
            return pObject.toString();
        } else if (pType.isEnum()) {
            return readEnumValue(pType, pObject.toString());
        } else if (ModelObject.class.isAssignableFrom(pType)) {
            if (pObject instanceof JSONObject) {
                return readModelObject((JSONObject) pObject);
            }
        } else {
            throw new UnsupportedOperationException();
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private Enum<?> readEnumValue(final Class<?> pEnumType, final String pConstantName) {
        final Class<Enum<?>> enumClass = (Class<Enum<?>>) pEnumType;
        Enum<?>[] constants = enumClass.getEnumConstants();

        for (int i = 0; i < constants.length; i++) {
            if (constants[i].name().equals(pConstantName)) {
                return constants[i];
            }
        }
        return null;
    }
}
