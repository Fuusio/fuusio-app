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

import org.fuusio.api.util.DateToolkit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

public class JSONModelWriter {

    private final ModelObjectContext mObjectContext;

    public JSONModelWriter(final ModelObjectContext objectContext) {
        mObjectContext = objectContext;
    }

    public JSONObject writeModel(final Model model) throws JSONException {
        final JSONObject serializedObject = writeModelObject(model);
        // DEBUG final String jsonString = serializedObject.toString(4); // DEBUG
        return serializedObject;
    }

    private JSONObject writeModelObject(final ModelObject object) throws JSONException {
        final Class<? extends ModelObject> objectClass = object.getClass();
        final JSONObject serializedObject = new JSONObject();

        serializedObject.put(ModelObject.KEY_CLASS, object.getClass().getName());

        final JSONObject propertiesObject = new JSONObject();

        serializedObject.put(ModelObject.KEY_PROPERTIES, propertiesObject);

        for (final Property property : mObjectContext.getProperties(object.getClass())) {
            if (!property.isTransientFor(objectClass)) {
                final Object value = property.get(object);
                writeValue(value, property, propertiesObject);
            }
        }

        return serializedObject;
    }

    protected void writeValue(final Object value, final Property property, final JSONObject propertyObject)
            throws JSONException {
        final Class<?> valueType = property.getType();
        final String key = property.getName();

        writeValue(key, value, valueType, propertyObject);
    }

    protected void writeValue(final String key, final Object value, final Class<?> valueType, final JSONObject propertyObject)
            throws JSONException {

        if (value == null) {
            propertyObject.put(key, null);
            return;
        }

        if (valueType.equals(Boolean.class) || valueType.equals(Boolean.TYPE)) {
            propertyObject.put(key, value);
        } else if (valueType.equals(Byte.class) || valueType.equals(Byte.TYPE)) {
            propertyObject.put(key, value);
        } else if (valueType.equals(Date.class)) {
            try {
                final Date date = (Date) value;
                propertyObject.put(key, DateToolkit.formatRFC822(date));
            } catch (final Exception pException) {
                pException.printStackTrace();
            }
        } else if (valueType.equals(Double.class) || valueType.equals(Double.TYPE)) {
            propertyObject.put(key, value);
        } else if (valueType.equals(Float.class) || valueType.equals(Float.TYPE)) {
            propertyObject.put(key, value);
        } else if (valueType.equals(Integer.class) || valueType.equals(Integer.TYPE)) {
            propertyObject.put(key, value);
        } else if (valueType.equals(Long.class) || valueType.equals(Long.TYPE)) {
            propertyObject.put(key, value);
        } else if (valueType.equals(Short.class) || valueType.equals(Short.TYPE)) {
            propertyObject.put(key, value);
        } else if (valueType.equals(String.class)) {
            propertyObject.put(key, value.toString());
        } else if (valueType.isEnum()) {
            final String valueString = getEnumValueString(valueType, value);
            propertyObject.put(key, valueString);
        } else if (HashMap.class.isAssignableFrom(valueType)) {
            propertyObject.put(key, writeHashMap((HashMap<?, ?>) value));
        } else if (HashMap.class.isAssignableFrom(valueType)) {
            propertyObject.put(key, writeHashMap((HashMap<?, ?>) value));
        } else if (valueType.isArray()) {
            final Class<?> componentType = valueType.getComponentType();
            final Object[] values = (Object[]) value;
            final JSONArray jsonArray = writeArray(values, componentType);
            propertyObject.put(key, jsonArray);
        } else if (ModelObject.class.isAssignableFrom(valueType)) {
            final ModelObject object = (ModelObject) value;
            propertyObject.put(key, writeModelObject(object));
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private JSONArray writeArray(final Object[] values, final Class<?> componentType) {
        final JSONArray array = new JSONArray();

        if (values != null) {
            final int count = values.length;

            for (int i = 0; i < count; i++) {
                final Object valueObject = writeValue(values[i], componentType);
                array.put(valueObject);
            }
        }

        return array;
    }

    private Object writeValue(final Object value, final Class<?> valueType) {

        if (valueType.equals(Boolean.class) || valueType.equals(Boolean.TYPE)) {
            return value;
        } else if (valueType.equals(Byte.class) || valueType.equals(Byte.TYPE)) {
            return value;
        } else if (valueType.equals(Date.class)) {
            try {
                final Date date = (Date) value;
                return DateToolkit.formatRFC822(date);
            } catch (final Exception pException) {
                pException.printStackTrace();
            }
        } else if (valueType.equals(Double.class) || valueType.equals(Double.TYPE)) {
            return value;
        } else if (valueType.equals(Float.class) || valueType.equals(Float.TYPE)) {
            return value;
        } else if (valueType.equals(Integer.class) || valueType.equals(Integer.TYPE)) {
            return value;
        } else if (valueType.equals(Long.class) || valueType.equals(Long.TYPE)) {
            return value;
        } else if (valueType.equals(Short.class) || valueType.equals(Short.TYPE)) {
            return value;
        } else if (valueType.equals(String.class)) {
            return value.toString();
        } else if (valueType.isEnum()) {
            return getEnumValueString(valueType, value);
        } else if (valueType.isEnum()) {
            return getEnumValueString(valueType, value);
        } else if (ModelObject.class.isAssignableFrom(valueType)) {
            final ModelObject object = (ModelObject) value;
            try {
                return writeModelObject(object);
            } catch (final JSONException pException) {
                pException.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException();
        }
        return null;
    }

    private Object writeHashMap(final HashMap<?, ?> hashMap) throws JSONException {

        final JSONObject serializedObject = new JSONObject();

        serializedObject.put(ModelObject.KEY_CLASS, hashMap.getClass().getName());

        for (final Object key : hashMap.keySet()) {
            final Object value = hashMap.get(key);
            final Class<?> valueType = (value != null) ? value.getClass() : null;
            writeValue(key.toString(), value, valueType, serializedObject);
        }

        return serializedObject;
    }

    private String getEnumValueString(final Class<?> enumType, final Object value) {
        final Enum<?> enumValue = (Enum<?>) value;
        return enumValue.name();
    }

    public JSONObject writePropertiesObject(final ModelObject object) throws JSONException {

        final Class<? extends ModelObject> objectClass = object.getClass();
        final JSONObject propertiesObject = new JSONObject();

        for (final Property property : mObjectContext.getProperties(object.getClass())) {
            if (!property.isTransientFor(objectClass)) {
                final Object value = property.get(object);
                writeValue(value, property, propertiesObject);
            }
        }

        return propertiesObject;
    }
}
