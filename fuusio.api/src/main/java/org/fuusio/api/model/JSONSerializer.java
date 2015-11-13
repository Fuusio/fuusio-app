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

import android.util.Log;

import org.fuusio.api.util.DateToolkit;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class JSONSerializer {

    private final ModelObjectContext mObjectContext;

    protected JSONObject mJsonObject;

    public JSONSerializer(final ModelObjectContext objectContext) {
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
    private <T extends Model> T readModel(final JSONObject serializedObject,
                                          final boolean readAsDescriptor) throws JSONException {
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

    @SuppressWarnings({"unchecked", "rawtypes"})
    public HashMap<?, ?> readHashMap(final JSONObject serializedObject)
            throws JSONException {
        final HashMap hashMap = new HashMap();
        final Iterator i = serializedObject.keys();

        while (i.hasNext()) {
            final String key = i.next().toString();
            final Object value = serializedObject.get(key);

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

    private JSONObject writeModelObject(final ModelObject object) throws JSONException {
        final Class<? extends ModelObject> modelObjectClass = object.getClass();
        final JSONObject serializedObject = new JSONObject();

        serializedObject.put(ModelObject.KEY_CLASS, object.getClass().getName());

        final JSONObject propertiesObject = new JSONObject();

        serializedObject.put(ModelObject.KEY_PROPERTIES, propertiesObject);

        for (final Property property : mObjectContext.getProperties(object.getClass())) {
            if (!property.isTransientFor(modelObjectClass)) {
                final Object value = property.get(object);
                writeValue(value, property, propertiesObject);
            }
        }

        return serializedObject;
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
        } else if (valueType.equals(HashMap.class)) {
            final JSONObject hashMapObject = propertyObject.getJSONObject(valueName);
            value = readHashMap(hashMapObject);
        } else if (valueType.isEnum()) {
            value = readEnumValue(valueType, propertyObject.get(valueName).toString());
        } else if (ModelObject.class.isAssignableFrom(valueType)) {
            value = readModelObject(propertyObject.getJSONObject(valueName));
        } else {
            throw new UnsupportedOperationException();
        }
        property.set(object, value);
    }

    protected void writeValue(final Object value, final Property property,
                              final JSONObject propertyObject) throws JSONException {
        final Class<?> valueType = property.getType();
        final String key = property.getName();

        writeValue(key, value, valueType, propertyObject);
    }

    protected void writeValue(final String key, final Object value,
                              final Class<?> valueType, final JSONObject propertyObject) throws JSONException {

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
        } else if (ModelObject.class.isAssignableFrom(valueType)) {
            final ModelObject modelObject = (ModelObject) value;
            propertyObject.put(key, writeModelObject(modelObject));
        } else {
            throw new UnsupportedOperationException();
        }
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

    @SuppressWarnings("unchecked")
    private Enum<?> readEnumValue(final Class<?> enumType, final String pConstantName) {
        final Class<Enum<?>> enumClass = (Class<Enum<?>>) enumType;
        final Enum<?>[] constants = enumClass.getEnumConstants();

        for (int i = 0; i < constants.length; i++) {
            if (constants[i].name().equals(pConstantName)) {
                return constants[i];
            }
        }
        return null;
    }

    private String getEnumValueString(final Class<?> enumType, final Object value) {
        final Enum<?> enumValue = (Enum<?>) value;
        return enumValue.name();
    }

    public JSONObject writePropertiesObject(final ModelObject object)
            throws JSONException {

        final Class<? extends ModelObject> modelObjectClass = object.getClass();
        final JSONObject propertiesObject = new JSONObject();

        for (final Property property : mObjectContext.getProperties(object.getClass())) {
            if (!property.isTransientFor(modelObjectClass)) {
                final Object value = property.get(object);
                writeValue(value, property, propertiesObject);
            }
        }

        return propertiesObject;
    }
}
