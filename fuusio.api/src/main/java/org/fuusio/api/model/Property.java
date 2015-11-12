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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.fuusio.api.util.DateToolkit;
import org.fuusio.api.util.L;
import org.fuusio.api.util.MessageContext;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Property {

    /**
     * An {@code int} code defining the column index.
     */
	private int mColumnIndex;

    /**
     * A {@code boolean} value defining if this {@link Property} represents key column.
     */
	private boolean mKey;
    
    /**
     * A {@code String} containing a name of the represented property.
     */
    private String mName;

    /**
     * The modifiers of this {@code PropertyObject} that define whether this {@code PropertyObject}
     * is, for instance, transient or modifiable.
     */
    private final HashMap<PropertyModifier, Boolean> mModifiers;

    /**
     * The mGetter {@link Method} for the property in the owner object.
     */
    protected Method mGetter;

    /**
     * The mResetter {@link Method} for the property in the owner object.
     */
    private Method mResetter;

    /**
     * The setter {@link Method} for the property in the owner object.
     */
    private Method mSetter;

    /**
     * A {@code boolean} property specifying whether the property is transient or not.
     */
    private final ArrayList<Class<? extends ModelObject>> mTransientExceptions;

    /**
     * The type of the property as a {@link Class}.
     */
    private Class<?> mType;

    /**
     * The type of the property as a {@link Class}.
     */
    private Class<?> mComponentType;

    /**
     * The validator {@link Method} for parsing a value from {@link String}.
     */
    private Method mParser;

    /**
     * The validator {@link Method} for the property in the owner object.
     */
    private Method mValidator;

    public Property(final String name, final PropertyModifier... modifiers) {

        mModifiers = new HashMap<>();
        mModifiers.put(PropertyModifier.DESCRIPTOR, false);
        mModifiers.put(PropertyModifier.MODIFIABLE, true);
        mModifiers.put(PropertyModifier.SYNTHETIC, false);
        mModifiers.put(PropertyModifier.TRANSIENT, false);
        mName = name;

        for (int i = 0; i < modifiers.length; i++) {
            mModifiers.put(modifiers[i], true);
        }

        mTransientExceptions = new ArrayList<>();
        mColumnIndex = -1;
        mKey = false;        
    }

    public final boolean isKey() {
        return mKey;
    }

    public void setKey(final boolean key) {
        mKey = key;
    }
    
    public final Method getGetter() {
        return mGetter;
    }

    public final int getColumnIndex() {
		return mColumnIndex;
	}

	public void setColumnIndex(final int columnIndex) {
		mColumnIndex = columnIndex;
	}

	public void setGetter(final Method getter) {
        mGetter = getter;
        mType = mGetter.getReturnType();

        try { // TODO
            final ParameterizedType type = (ParameterizedType) mType.getGenericSuperclass();

            if (type != null) {
                final Type[] actualTypes = type.getActualTypeArguments();

                if (actualTypes != null && actualTypes.length > 0) {
                    final Type actualType = actualTypes[0];

                    if (actualType != null && !actualType.equals(Object.class)) {
                        mComponentType = (Class<?>) actualType;
                    }
                }
            }
        } catch (final Exception e) {
            // TODO
        }
    }

    public final Method getResetter() {
        return mResetter;
    }

    public void setResetter(final Method resetter) {
        mResetter = resetter;
    }

    public final Method getSetter() {
        return mSetter;
    }

    public void setSetter(final Method setter) {
        mSetter = setter;
    }

    public final Method getValidator() {
        return mValidator;
    }

    public void setValidator(final Method validator) {
        mValidator = validator;
    }

    public final Method getParser() {
        return mParser;
    }

    public void setParser(final Method parser) {
        mParser = parser;
    }

    /**
     * Sets this {@code Property} to represent a descriptor property (i.e. the property whose value
     * is read when a {@link ModelObject} is read as a descriptor and not fully).
     * 
     * @param isDescriptor A {@code boolean} value.
     */
    public void setDescriptor(final boolean isDescriptor) {
        mModifiers.put(PropertyModifier.DESCRIPTOR, isDescriptor);
    }

    /**
     * Sets this {@code Property} to represent a synthetic property (i.e. the property whose value
     * is dynamically computed from other property values and therefore is not persistent) depending
     * on the given {@code boolean} value.
     * 
     * @param isSynthetic A {@code boolean} value.
     */
    public void setSynthetic(final boolean isSynthetic) {
        mModifiers.put(PropertyModifier.SYNTHETIC, isSynthetic);
    }

    /**
     * Sets this {@code Property} to represent a transient property (i.e. the property whose value
     * is not persistent and is not stored into database).
     * 
     * @param isTransient A {@code boolean} value.
     */
    public void setTransient(final boolean isTransient) {
        mModifiers.put(PropertyModifier.TRANSIENT, isTransient);
    }

    /**
     * Gets the name of this {@code Property}.
     * 
     * @return The name as a {@code String}.
     */
    public final String getName() {
        return mName;
    }

    /**
     * Gets the {@link Method} that represents the property getter.
     * 
     * @param getters An {@link ArrayList} containing getter method candidates.
     * @return A {@link Method}. May return {@code null}.
     * @throws java.lang.NoSuchMethodException If the getter method for the property is not
     *         available. REMOVE
     * @SuppressWarnings("unused") private Method getGetter(ArrayList<Method> getters) throws
     *                             NoSuchMethodException {
     * 
     *                             for (Method method : getters) {
     * 
     *                             Class<?>[] parameterTypes = method.getParameterTypes(); Class<?>
     *                             returnType = method.getReturnType();
     * 
     *                             if (parameterTypes.length == 0 && !returnType.equals(Void.TYPE))
     *                             { mType = returnType; return method; } }
     * 
     *                             return null; }
     */

    /**
     * Gets the {@link Method} that represents the property resetter.
     * 
     * @param resetters An {@link ArrayList} containing resetter method candidates.
     * @return A {@link Method}. May return {@code null}.
     * @throws java.lang.NoSuchMethodException If the mGetter method for the property is not
     *         available. REMOVE private Method getResetter(ArrayList<Method> resetters) throws
     *         NoSuchMethodException {
     * 
     *         for ( Method method : resetters ) {
     * 
     *         Class<?>[] parameterTypes = method.getParameterTypes();
     * 
     *         if (parameterTypes.length == 0) { return method; } }
     * 
     *         return null; }
     */

    /**
     * Gets the {@link Method}s that represents the property setters.
     * 
     * @param setters An {@link ArrayList} containing setter method candidates.
     * @return A {@code List} of {@link Method}s. May return an empty {@code List}.
     * @throws java.lang.NoSuchMethodException If the setter method for the property is not
     *         available. REMOVE private Method getSetter(ArrayList<Method> setters) throws
     *         NoSuchMethodException {
     * 
     *         for ( Method method : setters ) {
     * 
     *         Class<?>[] parameterTypes = method.getParameterTypes();
     * 
     *         if (parameterTypes.length == 1 && parameterTypes[0].equals(mType)) { return method; }
     *         }
     * 
     *         return null; }
     */

    /**
     * Gets the {@link Method} that represents a property validator.
     * 
     * @param validators An {@link ArrayList} containing validator method candidates.
     * @return A {@link Method}. May return {@code null}.
     * @throws java.lang.NoSuchMethodException If the validator method for the property is not
     *         available. REMOVE private Method getValidator(ArrayList<Method> validators) throws
     *         NoSuchMethodException {
     * 
     *         for ( Method method : validators ) {
     * 
     *         Class<?>[] parameterTypes = method.getParameterTypes();
     * 
     *         if (parameterTypes.length == 1 && parameterTypes[0].equals(mType)) { return method; }
     *         }
     * 
     *         return null; }
     */

    /**
     * Gets the type of this {@code Property}.
     * 
     * @return The type as a {@code Class}.
     */
    public final Class<?> getType() {
        return mType;
    }

    /**
     * Gets the generic parameter type of this {@code Property}.
     * 
     * @return The type as a {@code Class}.
     */
    public final Class<?> getComponentType() {
        return mComponentType;
    }

    /**
     * Gets the specified property value from the given {@code ModelObject}.
     * 
     * @param object A {@code ModelObject}.
     * @return The value as an {@code Object}.
     */
    @SuppressWarnings("unchecked")
    public <T> T get(final ModelObject object) {
        if (mGetter != null) {
            try {
                return (T) mGetter.invoke(object);
            } catch (final IllegalAccessException e) {
                L.wtf(this, "get", e.getMessage());
            } catch (final InvocationTargetException e) {
                L.wtf(this, "get", e.getMessage());
            } catch (final NullPointerException e) {
                L.wtf(this, "get", e.getMessage());
            }
        } else {
            L.wtf(this, "get", "No getter method defined");
        }

        throw new RuntimeException("Failed to get value for property; " + mName);
    }

    /**
     * Resets the specified property value from the given {@code ModelObject}.
     * 
     * @param object A {@code ModelObject}.
     * @return The value as an {@code Object}.
     */
    @SuppressWarnings("unchecked")
    public <T> T reset(final ModelObject object) {
        if (mResetter != null) {
            try {
                return (T) mResetter.invoke(object);
            } catch (final IllegalAccessException e) {
                L.wtf(this, "reset", e.getMessage());
            } catch (final InvocationTargetException e) {
                L.wtf(this, "reset", e.getMessage());
            } catch (final NullPointerException e) {
                L.wtf(this, "reset", e.getMessage());
            }
        } else {
            L.wtf(this, "reset", "No mResetter method defined");
        }

        throw new RuntimeException("Failed to get value for property; " + mName);
    }

    /**
     * Applies the given property value to the given {@code ModelObject}.
     * 
     * @param object A {@code ModelObject}.
     * @param value The value as a {@code Object}.
     * @return A {@code boolean} value indicating whether set property value was actually changed.
     */
    public boolean set(final ModelObject object, final Object value) {
        if (mSetter != null) {
            try {
                boolean changed = false;

                if (mGetter != null) {
                    final Object oldValue = get(object);
                    changed = isChanged(oldValue, value);
                }

                mSetter.invoke(object, value);
                object.setChanged(changed);
                return true;
            } catch (final IllegalAccessException e) {
                L.wtf(this, "set", e.getMessage());
            } catch (final InvocationTargetException e) {
                L.wtf(this, "set", e.getMessage());
            } catch (final NullPointerException e) {
                L.wtf(this, "set", e.getMessage());
            }
        } else {
            L.wtf(this, "setValue", "No setter method defined");
        }

        return false;
    }

    public boolean setValueFromString(final ModelObject object, final String valueString) {

        if (valueString == null) {
            L.w(this, "set", "Null value detected"); // TODO
            return false;
        }

        return set(object, convertStringToValue(object, valueString, mType));
    }

    public void set(final ModelObject object, final String valueName, final JSONObject jsonObject)
            throws JSONException {

        String valueString = null;

        try {
            valueString = jsonObject.getString(valueName);
        } catch (JSONException e) {
        }

        if (valueString == null || valueString.equalsIgnoreCase("null")) {
            return;
        }

        if (mType.equals(Bitmap.class)) {
            // TODO
        } else if (mType.equals(Boolean.class) || mType.equals(Boolean.TYPE)) {
            set(object, jsonObject.getBoolean(valueName));
        } else if (mType.equals(Byte.class) || mType.equals(Byte.TYPE)) {
            set(object, jsonObject.getInt(valueName));
        } else if (mType.equals(Date.class)) {
            try {
                final Date date = DateToolkit.parse(valueString);
                set(object, date);
            } catch (final Exception e) {
                L.w(this, "set", e.getMessage());
            }
        } else if (mType.equals(Double.class) || mType.equals(Double.TYPE)) {
            set(object, jsonObject.getDouble(valueName));
        } else if (mType.equals(Float.class) || mType.equals(Float.TYPE)) {
            set(object, (float) jsonObject.getDouble(valueName));
        } else if (mType.equals(Integer.class) || mType.equals(Integer.TYPE)) {
            set(object, jsonObject.getInt(valueName));
        } else if (mType.equals(Long.class) || mType.equals(Long.TYPE)) {
            set(object, jsonObject.getLong(valueName));
        } else if (mType.equals(Short.class) || mType.equals(Short.TYPE)) {
            set(object, jsonObject.getInt(valueName));
        } else if (mType.equals(String.class)) {
            set(object, valueString);
        } else if (mType.equals(byte[].class)) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Tests if this {@code Property} is the one specified by the given name.
     * 
     * @param name The name of the property,
     * @return A {@code boolean}.
     */
    public final boolean is(final String name) {
        return mName.equals(name);
    }

    private boolean isChanged(final Object oldValue, final Object newValue) {
        if (oldValue == null && newValue == null) {
            return false;
        }

        if (oldValue == null || newValue == null) {
            return true;
        }

        return !oldValue.equals(newValue);
    }

    /**
     * Tests if this {@code Property} is descriptor property.
     * 
     * @return A {@code boolean}.
     */
    public boolean isDescriptor() {
        return mModifiers.get(PropertyModifier.DESCRIPTOR);
    }

    /**
     * Tests if this {@code Property} is modifiable.
     * 
     * @return A {@code boolean}.
     */
    public boolean isModifiable() {
        return mModifiers.get(PropertyModifier.MODIFIABLE);
    }

    /**
     * Tests whether this {@code Property} represents a synthetic property (i.e. the property whose
     * value is dynamically computed from other property values and therefore is not persistent).
     * 
     * @return A {@code boolean} value.
     */
    public boolean isSynthetic() {
        return mModifiers.get(PropertyModifier.SYNTHETIC);
    }

    /**
     * Tests whether this {@code Property} represents a transient property (i.e. the property whose
     * value is not persistent and is not stored into database).
     * 
     * @return A {@code boolean} value.
     */
    public boolean isTransient() {
        return mModifiers.get(PropertyModifier.TRANSIENT);
    }

    /**
     * Tests whether this {@code Property} represents a transient property (i.e. the property whose
     * value is not persistent and is not stored into database) for the specified
     * {@link ModelObject} type.
     * 
     * @param type A {@link Class} specifying the type of the {@link ModelObject}.
     * @return A {@code boolean} value.
     */
    public boolean isTransientFor(final Class<? extends ModelObject> type) {
        return mTransientExceptions.contains(type);
    }

/**
     * Tests whether the given {@code Object} represents a valid value of 
     * this {@code Property} for the given {@code ModelObject}.
     *      
     * @param object A {@code ModelObject].     
     * @param value The value as an {@code Object}. May be {@code null}.
     * @return A {@code boolean} value.
     */
    public boolean validate(final ModelObject object, final Object value) {
        final Class<?> propertyType = getType();

        if (value == null) {
            if (propertyType.isPrimitive()) {
                return false;
            }
        }

        if (mValidator != null) {
            try {
                return (Boolean) mValidator.invoke(object, value);
            } catch (final IllegalAccessException e) {
                L.wtf(this, "validate", e.getMessage());
            } catch (final InvocationTargetException e) {
                L.wtf(this, "validate", e.getMessage());
            } catch (final NullPointerException e) {
                L.wtf(this, "validate", e.getMessage());
            }
        }

        return propertyType.isAssignableFrom(value.getClass());
    }

    public boolean validate(final ModelObject object, final Object value, final MessageContext messageContext) {
        final Class<?> propertyType = getType();

        if (value == null) {
            if (propertyType.isPrimitive()) {
                return false;
            }
        }

        if (mValidator != null) {
            try {
                return (Boolean) mValidator.invoke(object, value, messageContext);
            } catch (final IllegalAccessException e) {
                L.wtf(this, "validate", e.getMessage());
            } catch (final InvocationTargetException e) {
                L.wtf(this, "validate", e.getMessage());
            } catch (final NullPointerException e) {
                L.wtf(this, "validate", e.getMessage());
            }
        }

        return propertyType.isAssignableFrom(value.getClass());
    }

    public Object parse(final ModelObject object, final String valueString) {

        if (mValidator != null) {
            try {
                return mParser.invoke(object, valueString);
            } catch (final IllegalAccessException e) {
                L.wtf(this, "parse", e.getMessage());
            } catch (final InvocationTargetException e) {
                L.wtf(this, "parse", e.getMessage());
            } catch (final NullPointerException e) {
                L.wtf(this, "parse", e.getMessage());
            }
        }

        throw new UnsupportedOperationException();
    }

    public Object convertStringToValue(final ModelObject object, final String valueString, final Class<?> type) {

        if (mParser != null) {
            return parse(object, valueString);
        }

        if (valueString == null) { // TODO Check value is allowed
            return null;
        }

        if (type.equals(Bitmap.class)) {
            // TODO
        } else if (type.equals(Boolean.class) || type.equals(Boolean.TYPE)) {
            return valueString.equalsIgnoreCase("true");
        } else if (type.equals(Byte.class) || type.equals(Byte.TYPE)) {
            return Byte.parseByte(valueString);
        } else if (type.equals(Date.class)) {
            try {
                final Date date = DateToolkit.parse(valueString);
                return date;
            } catch (final Exception e) {
                L.w(Property.class, "convertStringToValue", e.getMessage());
            }
        } else if (type.equals(Double.class) || type.equals(Double.TYPE)) {
            return Double.parseDouble(valueString);
        } else if (type.equals(Float.class) || type.equals(Float.TYPE)) {
            return Float.parseFloat(valueString);
        } else if (type.equals(Integer.class) || type.equals(Integer.TYPE)) {
            return Integer.parseInt(valueString);
        } else if (type.equals(Long.class) || type.equals(Long.TYPE)) {
            return Long.parseLong(valueString);
        } else if (type.equals(Short.class) || type.equals(Short.TYPE)) {
            return Short.parseShort(valueString);
        } else if (type.equals(String.class)) {
            return valueString;
        }

        throw new UnsupportedOperationException();
    }
    /**
     * Changes the first character of the given {@code String} to be a uppercase character.
     * 
     * @param string The given {@code String}. It must contain at least one character.
     * @return The modified {@code String}.
     */
    public static String upperCaseFirstCharacter(final String string) {
        final char firstChar = string.charAt(0);

        if (Character.isUpperCase(firstChar)) {
            return string;
        } else {
            final StringBuffer buffer = new StringBuffer(string);
            buffer.setCharAt(0, Character.toUpperCase(firstChar));
            return buffer.toString();
        }
    }

    public Object read(final Cursor pCursor, final int columnIndex) {

        if (mType.equals(Bitmap.class)) {
            final byte[] imageByteArray = pCursor.getBlob(columnIndex);
            Bitmap bitmap = null;

            if (imageByteArray != null) {
                final ByteArrayInputStream imageStream = new ByteArrayInputStream(imageByteArray);
                bitmap = BitmapFactory.decodeStream(imageStream);
            }

            return bitmap;
        } else if (mType.equals(Boolean.class) || mType.equals(Boolean.TYPE)) {
            return (pCursor.getInt(columnIndex) != 0) ? true : false;
        } else if (mType.equals(Byte.class) || mType.equals(Byte.TYPE)) {
            return (byte) pCursor.getInt(columnIndex);
        } else if (mType.equals(Date.class)) {
            String value = pCursor.getString(columnIndex);

            try {
                return DateToolkit.parse(value);
            } catch (final Exception e) {
                return new Date(); // TODO
            }
        } else if (mType.equals(Double.class) || mType.equals(Double.TYPE)) {
            return pCursor.getDouble(columnIndex);
        } else if (mType.equals(Float.class) || mType.equals(Float.TYPE)) {
            return pCursor.getFloat(columnIndex);
        } else if (mType.equals(Integer.class) || mType.equals(Integer.TYPE)) {
            return pCursor.getInt(columnIndex);
        } else if (mType.equals(Long.class) || mType.equals(Long.TYPE)) {
            return pCursor.getLong(columnIndex);
        } else if (mType.equals(Short.class) || mType.equals(Short.TYPE)) {
            return pCursor.getShort(columnIndex);
        } else if (mType.equals(String.class)) {
            return pCursor.getString(columnIndex);
        } else if (mType.equals(byte[].class)) {
            return pCursor.getBlob(columnIndex);
        }

        return null;
    }

    public void save(final ModelObject object, final ContentValues values) {

        final Object value = get(object);

        if (value == null) {
            // TODO
        } else if (value instanceof Bitmap) {
            final Bitmap bitmap = (Bitmap) value;
            final int size = bitmap.getWidth() * bitmap.getHeight();
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(size);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            final byte[] blob = outputStream.toByteArray();

            try {
                outputStream.flush();
                outputStream.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }

            values.put(mName, blob);
        } else if (value instanceof Boolean) {
            values.put(mName, (Boolean) value);
        } else if (value instanceof Byte) {
            values.put(mName, (Byte) value);
        } else if (value instanceof Date) {
            final Date date = (Date) value;
            values.put(mName, DateToolkit.format(date));
        } else if (value instanceof Double) {
            values.put(mName, (Double) value);
        } else if (value instanceof Float) {
            values.put(mName, (Float) value);
        } else if (value instanceof Integer) {
            values.put(mName, (Integer) value);
        } else if (value instanceof Long) {
            values.put(mName, (Long) value);
        } else if (value instanceof Short) {
            values.put(mName, (Short) value);
        } else if (value instanceof String) {
            values.put(mName, (String) value);
        } else if (value instanceof byte[]) {
            values.put(mName, (byte[]) value);
        }
    }

    public void addTransitionExceptionFor(final Class<? extends ModelObject> objectClass) {
        mTransientExceptions.add(objectClass);
    }



    /**
     * Annotation type {@code PropertyGetter} is used to explicitly define a property getter,
     * setter, resetter, or validator method.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface PropertyGetter {
        String property();

        boolean isDescriptor() default false;

        boolean isSynthetic() default false;

        boolean isTransient() default false;
        
        boolean isKey() default false;
        
        int index() default -1;
    }

    /**
     * Annotation type {@code PropertyResetter} is used to explicitly define a property resetter
     * method.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface PropertyResetter {
        String property();
    }

    /**
     * Annotation type {@code PropertySetter} is used to explicitly define a property setter method.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface PropertySetter {
        String property();
    }

    /**
     * Annotation type {@code PropertyValidator} is used to explicitly define a property validator
     * method.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface PropertyValidator {
        String property();
    }

    /**
     * Annotation type {@code PropertyValidator} is used to explicitly define a property validator
     * method.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface PropertyParser {
        String property();
    }

    /**
     * Annotation type {@code PropertyValidator} is used to explicitly define a property validator
     * method.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface TransientProperties {
        String properties();
    }


}
