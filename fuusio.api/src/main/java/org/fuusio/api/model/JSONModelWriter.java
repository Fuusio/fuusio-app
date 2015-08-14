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
import java.util.HashMap;

import org.fuusio.api.util.DateToolkit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONModelWriter {

    private final ModelObjectContext mObjectContext;

    public JSONModelWriter(final ModelObjectContext pObjectContext) {
        mObjectContext = pObjectContext;
    }

    public JSONObject writeModel(final Model pModel) throws JSONException {
        final JSONObject serializedObject = writeModelObject(pModel);
        // DEBUG final String jsonString = serializedObject.toString(4); // DEBUG
        return serializedObject;
    }

    private JSONObject writeModelObject(final ModelObject pModelObject) throws JSONException {
        final Class<? extends ModelObject> modelObjectClass = pModelObject.getClass();
        final JSONObject serializedObject = new JSONObject();

        serializedObject.put(ModelObject.KEY_CLASS, pModelObject.getClass().getName());

        final JSONObject propertiesObject = new JSONObject();

        serializedObject.put(ModelObject.KEY_PROPERTIES, propertiesObject);

        for (final Property property : mObjectContext.getProperties(pModelObject.getClass())) {
            if (!property.isTransientFor(modelObjectClass)) {
                final Object value = property.get(pModelObject);
                writeValue(value, property, propertiesObject);
            }
        }

        return serializedObject;
    }

    protected void writeValue(final Object pValue, final Property pProperty,
            final JSONObject pPropertyObject) throws JSONException {
        final Class<?> valueType = pProperty.getType();
        final String key = pProperty.getName();

        writeValue(key, pValue, valueType, pPropertyObject);
    }

    protected void writeValue(final String pKey, final Object pValue,
            final Class<?> pValueType, final JSONObject pPropertyObject) throws JSONException {

        if (pValue == null) {
            pPropertyObject.put(pKey, null);
            return;
        }

        if (pValueType.equals(Boolean.class) || pValueType.equals(Boolean.TYPE)) {
            pPropertyObject.put(pKey, pValue);
        } else if (pValueType.equals(Byte.class) || pValueType.equals(Byte.TYPE)) {
            pPropertyObject.put(pKey, pValue);
        } else if (pValueType.equals(Date.class)) {
            try {
                final Date date = (Date) pValue;
                pPropertyObject.put(pKey, DateToolkit.formatRFC822(date));
            } catch (final Exception pException) {
                pException.printStackTrace();
            }
        } else if (pValueType.equals(Double.class) || pValueType.equals(Double.TYPE)) {
            pPropertyObject.put(pKey, pValue);
        } else if (pValueType.equals(Float.class) || pValueType.equals(Float.TYPE)) {
            pPropertyObject.put(pKey, pValue);
        } else if (pValueType.equals(Integer.class) || pValueType.equals(Integer.TYPE)) {
            pPropertyObject.put(pKey, pValue);
        } else if (pValueType.equals(Long.class) || pValueType.equals(Long.TYPE)) {
            pPropertyObject.put(pKey, pValue);
        } else if (pValueType.equals(Short.class) || pValueType.equals(Short.TYPE)) {
            pPropertyObject.put(pKey, pValue);
        } else if (pValueType.equals(String.class)) {
            pPropertyObject.put(pKey, pValue.toString());
        } else if (pValueType.isEnum()) {
            final String valueString = getEnumValueString(pValueType, pValue);
            pPropertyObject.put(pKey, valueString);
        } else if (HashMap.class.isAssignableFrom(pValueType)) {
            pPropertyObject.put(pKey, writeHashMap((HashMap<?, ?>) pValue));
        } else if (HashMap.class.isAssignableFrom(pValueType)) {
            pPropertyObject.put(pKey, writeHashMap((HashMap<?, ?>) pValue));
        } else if (pValueType.isArray()) {
            final Class<?> componentType = pValueType.getComponentType();
            final Object[] values = (Object[]) pValue;
            final JSONArray jsonArray = writeArray(values, componentType);
            pPropertyObject.put(pKey, jsonArray);
        } else if (ModelObject.class.isAssignableFrom(pValueType)) {
            final ModelObject modelObject = (ModelObject) pValue;
            pPropertyObject.put(pKey, writeModelObject(modelObject));
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private JSONArray writeArray(final Object[] pValues, final Class<?> pComponentType) {
        final JSONArray array = new JSONArray();

        if (pValues != null) {
            final int count = pValues.length;

            for (int i = 0; i < count; i++) {
                final Object valueObject = writeValue(pValues[i], pComponentType);
                array.put(valueObject);
            }
        }

        return array;
    }

    private Object writeValue(final Object pValue, final Class<?> pValueType) {

        if (pValueType.equals(Boolean.class) || pValueType.equals(Boolean.TYPE)) {
            return pValue;
        } else if (pValueType.equals(Byte.class) || pValueType.equals(Byte.TYPE)) {
            return pValue;
        } else if (pValueType.equals(Date.class)) {
            try {
                final Date date = (Date) pValue;
                return DateToolkit.formatRFC822(date);
            } catch (final Exception pException) {
                pException.printStackTrace();
            }
        } else if (pValueType.equals(Double.class) || pValueType.equals(Double.TYPE)) {
            return pValue;
        } else if (pValueType.equals(Float.class) || pValueType.equals(Float.TYPE)) {
            return pValue;
        } else if (pValueType.equals(Integer.class) || pValueType.equals(Integer.TYPE)) {
            return pValue;
        } else if (pValueType.equals(Long.class) || pValueType.equals(Long.TYPE)) {
            return pValue;
        } else if (pValueType.equals(Short.class) || pValueType.equals(Short.TYPE)) {
            return pValue;
        } else if (pValueType.equals(String.class)) {
            return pValue.toString();
        } else if (pValueType.isEnum()) {
            return getEnumValueString(pValueType, pValue);
        } else if (pValueType.isEnum()) {
            return getEnumValueString(pValueType, pValue);
        } else if (ModelObject.class.isAssignableFrom(pValueType)) {
            final ModelObject modelObject = (ModelObject) pValue;
            try {
                return writeModelObject(modelObject);
            } catch (final JSONException pException) {
                pException.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException();
        }
        return null;
    }

    private Object writeHashMap(final HashMap<?, ?> pHashMap) throws JSONException {

        final JSONObject serializedObject = new JSONObject();

        serializedObject.put(ModelObject.KEY_CLASS, pHashMap.getClass().getName());

        for (final Object key : pHashMap.keySet()) {
            final Object value = pHashMap.get(key);
            final Class<?> valueType = (value != null) ? value.getClass() : null;
            writeValue(key.toString(), value, valueType, serializedObject);
        }

        return serializedObject;
    }

    private String getEnumValueString(final Class<?> pEnumType, final Object pValue) {
        final Enum<?> enumValue = (Enum<?>) pValue;
        return enumValue.name();
    }

    public JSONObject writePropertiesObject(final ModelObject pObject) throws JSONException {

        final Class<? extends ModelObject> objectClass = pObject.getClass();
        final JSONObject propertiesObject = new JSONObject();

        for (final Property property : mObjectContext.getProperties(pObject.getClass())) {
            if (!property.isTransientFor(objectClass)) {
                final Object value = property.get(pObject);
                writeValue(value, property, propertiesObject);
            }
        }

        return propertiesObject;
    }
}
