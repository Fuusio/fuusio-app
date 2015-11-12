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

    public JSONModelReader(final ModelObjectContext objectContext) {
        mObjectContext = objectContext;
    }

    public <T extends Model> T readModel(final JSONObject serializedObject)
            throws JSONException {
        return readModel(serializedObject, false);
    }

    public <T extends Model> T readModelDescriptor(final JSONObject serializedObject)
            throws JSONException {
        return readModel(serializedObject, true);
    }

    @SuppressWarnings("unchecked")
    private <T extends Model> T readModel(final JSONObject serializedObject, final boolean readAsDescriptor)
            throws JSONException {

        final String className = serializedObject.getString(ModelObject.KEY_CLASS);
        Class<T> modelClass = null;

        try {
            modelClass = (Class<T>) Class.forName(className);
        } catch (final ClassNotFoundException pException) {
            pException.printStackTrace();
        }

        final T model = mObjectContext.createInstance(modelClass);
        final JSONObject propertiesObject = serializedObject
                .getJSONObject(ModelObject.KEY_PROPERTIES);
        final Collection<Property> properties = readAsDescriptor ? mObjectContext
                .getDescriptorProperties(model) : mObjectContext.getProperties(model.getClass());

        for (final Property property : properties) {
            readValue(model, property, propertiesObject);
        }
        return model;
    }

    @SuppressWarnings("unchecked")
    public ModelObject readModelObject(final JSONObject serializedObject)
            throws JSONException {
        final String className = serializedObject.getString(ModelObject.KEY_CLASS);
        Class<? extends ModelObject> objectClass = null;

        try {
            objectClass = (Class<? extends ModelObject>) Class.forName(className);
        } catch (final ClassNotFoundException pException) {
            pException.printStackTrace();
        }

        final ModelObject modelObject = mObjectContext.createInstance(objectClass);
        final JSONObject propertiesObject = serializedObject
                .getJSONObject(ModelObject.KEY_PROPERTIES);

        for (final Property property : mObjectContext.getProperties(modelObject.getClass())) {
            readValue(modelObject, property, propertiesObject);
        }
        return modelObject;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public HashMap<?, ?> readHashMap(final JSONObject jsonObject) throws JSONException {
        final HashMap hashMap = new HashMap();
        final Iterator i = jsonObject.keys();

        while (i.hasNext()) {
            final String key = i.next().toString();
            final Object value = jsonObject.get(key);

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
    public <T> T[] readArray(final JSONArray jsonArray, final Class<?> componentType)
            throws JSONException {
        final int size = jsonArray.length();
        final T[] array = (T[]) Array.newInstance(componentType, size);

        for (int i = 0; i < size; i++) {
            array[i] = (T) readValue(jsonArray.get(i), componentType);
        }
        return array;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ArrayList<?> readArrayList(final JSONArray jsonArray, final Class<?> listType,
            final Class<?> componentType) throws JSONException {
        final int size = jsonArray.length();

        ArrayList arrayList = null;
        try {
            arrayList = (ArrayList) listType.newInstance();
        } catch (final Exception pException) {
        }

        for (int i = 0; i < size; i++) {
            final Object value = readValue(jsonArray.get(i), componentType);
            arrayList.add(value);
        }
        return arrayList;
    }

    protected void readValue(final ModelObject object, final Property property,
            final JSONObject propertyObject) throws JSONException {
        final String valueName = property.getName();

        if (!propertyObject.has(valueName)) {
            return;
        }

        final Class<?> valueType = property.getType();
        String valueString = null;
        Object value = null;

        if (valueType.equals(Boolean.class) || valueType.equals(Boolean.TYPE)) {
            value = propertyObject.getBoolean(valueName);
        } else if (valueType.equals(Byte.class) || valueType.equals(Byte.TYPE)) {
            value = (byte) propertyObject.getInt(valueName);
        } else if (valueType.equals(Date.class)) {
            try {
                final Date date = DateToolkit.parseRFC822(valueString);
                value = date;
            } catch (final Exception pException) {
                Log.e("JSONSerializer", "Date parsing error");
            }
        } else if (valueType.equals(Double.class) || valueType.equals(Double.TYPE)) {
            value = propertyObject.getDouble(valueName);
        } else if (valueType.equals(Float.class) || valueType.equals(Float.TYPE)) {
            value = (float) propertyObject.getDouble(valueName);
        } else if (valueType.equals(Integer.class) || valueType.equals(Integer.TYPE)) {
            value = propertyObject.getInt(valueName);
        } else if (valueType.equals(Long.class) || valueType.equals(Long.TYPE)) {
            value = propertyObject.getLong(valueName);
        } else if (valueType.equals(Short.class) || valueType.equals(Short.TYPE)) {
            value = (short) propertyObject.getInt(valueName);
        } else if (valueType.equals(String.class)) {
            value = valueString;
        } else if (ArrayList.class.isAssignableFrom(valueType)) {
            final JSONArray valueArray = propertyObject.getJSONArray(valueName);
            final Class<?> componentType = property.getComponentType();
            value = readArrayList(valueArray, valueType, componentType);
        } else if (valueType.equals(HashMap.class)) {
            final JSONObject hashMapObject = propertyObject.getJSONObject(valueName);
            value = readHashMap(hashMapObject);
        } else if (valueType.isArray()) {
            final JSONArray valueArray = propertyObject.getJSONArray(valueName);
            value = readArray(valueArray, valueType.getComponentType());
        } else if (valueType.isEnum()) {
            value = readEnumValue(valueType, propertyObject.get(valueName).toString());
        } else if (ModelObject.class.isAssignableFrom(valueType)) {
            value = readModelObject(propertyObject.getJSONObject(valueName));
        } else {
            throw new UnsupportedOperationException();
        }
        property.set(object, value);
    }

    protected Object readValue(final Object object, final Class<?> type)
            throws JSONException {

        if (object == null) {
            return null;
        }

        if (type.equals(Boolean.class) || type.equals(Boolean.TYPE)) {

            if (object instanceof Boolean) {
                return object;
            } else {
                return Boolean.parseBoolean(object.toString());
            }
        } else if (type.equals(Byte.class) || type.equals(Byte.TYPE)) {

            if (object instanceof Byte) {
                return object;
            } else {
                return Byte.parseByte(object.toString());
            }
        } else if (type.equals(Date.class)) {
            try {
                return DateToolkit.parseRFC822(object.toString());
            } catch (final Exception pException) {
                Log.e("JSONSerializer", "Date parsing error");
            }
        } else if (type.equals(Double.class) || type.equals(Double.TYPE)) {

            if (object instanceof Double) {
                return object;
            } else {
                return Double.parseDouble(object.toString());
            }
        } else if (type.equals(Float.class) || type.equals(Float.TYPE)) {

            if (object instanceof Float) {
                return object;
            } else {
                return Float.parseFloat(object.toString());
            }
        } else if (type.equals(Integer.class) || type.equals(Integer.TYPE)) {

            if (object instanceof Integer) {
                return object;
            } else {
                return Integer.parseInt(object.toString());
            }
        } else if (type.equals(Long.class) || type.equals(Long.TYPE)) {

            if (object instanceof Long) {
                return object;
            } else {
                return Long.parseLong(object.toString());
            }
        } else if (type.equals(Short.class) || type.equals(Short.TYPE)) {

            if (object instanceof Short) {
                return object;
            } else {
                return Short.parseShort(object.toString());
            }
        } else if (type.equals(String.class)) {
            return object.toString();
        } else if (type.isEnum()) {
            return readEnumValue(type, object.toString());
        } else if (ModelObject.class.isAssignableFrom(type)) {
            if (object instanceof JSONObject) {
                return readModelObject((JSONObject) object);
            }
        } else {
            throw new UnsupportedOperationException();
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private Enum<?> readEnumValue(final Class<?> enumType, final String constantName) {
        final Class<Enum<?>> enumClass = (Class<Enum<?>>) enumType;
        Enum<?>[] constants = enumClass.getEnumConstants();

        for (int i = 0; i < constants.length; i++) {
            if (constants[i].name().equals(constantName)) {
                return constants[i];
            }
        }
        return null;
    }
}
