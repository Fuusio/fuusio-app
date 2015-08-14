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

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.fuusio.api.util.DateToolkit;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONSerializer {

    private final ModelObjectContext mObjectContext;

    protected JSONObject mJsonObject;

    public JSONSerializer(final ModelObjectContext pObjectContext) {
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

    @SuppressWarnings({"unchecked", "rawtypes"})
    public HashMap<?, ?> readHashMap(final JSONObject pSerializedObject)
            throws JSONException {
        final HashMap hashMap = new HashMap();
        final Iterator i = pSerializedObject.keys();

        while (i.hasNext()) {
            final String key = i.next().toString();
            final Object value = pSerializedObject.get(key);

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

    public JSONObject writeModel(final Model pModel) throws JSONException {
        final JSONObject serializedObject = writeModelObject(pModel);
        // DEBUG final String jsonString = serializedObject.toString(4); // DEBUG
        return serializedObject;

        /*
         * final Class<? extends ModelObject> modelClass = pModel.getClass(); final JSONObject
         * serializedObject = new JSONObject(); final ModelObjectManager manager =
         * ModelObjectManager.getInstance();
         * 
         * serializedObject.put(KEY_CLASS, pModel.getClass().getName());
         * 
         * final JSONObject propertiesObject = new JSONObject();
         * 
         * serializedObject.put(NAME_Properties, propertiesObject);
         * 
         * for (final Property property : manager.getProperties(pModel)) { if
         * (!property.isTransientFor(modelClass)) { final Object value = property.getOrCreate(pModel);
         * writeValue(value, property, propertiesObject); } }
         * 
         * //final String jsonString = serializedObject.toString(4); // DEBUG
         * 
         * return serializedObject;
         */
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

    protected void readValue(final ModelObject pModelObject, final Property pProperty,
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
        } else if (valueType.equals(HashMap.class)) {
            final JSONObject hashMapObject = pPropertyObject.getJSONObject(valueName);
            value = readHashMap(hashMapObject);
        } else if (valueType.isEnum()) {
            value = readEnumValue(valueType, pPropertyObject.get(valueName).toString());
        } else if (ModelObject.class.isAssignableFrom(valueType)) {
            value = readModelObject(pPropertyObject.getJSONObject(valueName));
        } else {
            throw new UnsupportedOperationException();
        }
        pProperty.set(pModelObject, value);
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
        } else if (ModelObject.class.isAssignableFrom(pValueType)) {
            final ModelObject modelObject = (ModelObject) pValue;
            pPropertyObject.put(pKey, writeModelObject(modelObject));
        } else {
            throw new UnsupportedOperationException();
        }
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

    private String getEnumValueString(final Class<?> pEnumType, final Object pValue) {
        final Enum<?> enumValue = (Enum<?>) pValue;
        return enumValue.name();
    }

    public JSONObject writePropertiesObject(final ModelObject pModelObject)
            throws JSONException {

        final Class<? extends ModelObject> modelObjectClass = pModelObject.getClass();
        final JSONObject propertiesObject = new JSONObject();

        for (final Property property : mObjectContext.getProperties(pModelObject.getClass())) {
            if (!property.isTransientFor(modelObjectClass)) {
                final Object value = property.get(pModelObject);
                writeValue(value, property, propertiesObject);
            }
        }

        return propertiesObject;
    }
}
