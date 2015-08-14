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

    public Property(final String pName, final PropertyModifier... modifiers) {

        mModifiers = new HashMap<>();
        mModifiers.put(PropertyModifier.DESCRIPTOR, false);
        mModifiers.put(PropertyModifier.MODIFIABLE, true);
        mModifiers.put(PropertyModifier.SYNTHETIC, false);
        mModifiers.put(PropertyModifier.TRANSIENT, false);
        mName = pName;

        for (int i = 0; i < modifiers.length; i++) {
            mModifiers.put(modifiers[i], true);
        }

        mTransientExceptions = new ArrayList<Class<? extends ModelObject>>();
        mColumnIndex = -1;
        mKey = false;        
    }

    public final boolean isKey() {
        return mKey;
    }

    public void setKey(final boolean pKey) {
        mKey = pKey;
    }
    
    public final Method getGetter() {
        return mGetter;
    }

    public final int getColumnIndex() {
		return mColumnIndex;
	}

	public void setColumnIndex(final int pColumnIndex) {
		mColumnIndex = pColumnIndex;
	}

	public void setGetter(final Method pGetter) {
        mGetter = pGetter;
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
        } catch (final Exception pException) {
            // TODO
        }
    }

    public final Method getResetter() {
        return mResetter;
    }

    public void setResetter(final Method pResetter) {
        mResetter = pResetter;
    }

    public final Method getSetter() {
        return mSetter;
    }

    public void setSetter(final Method pSetter) {
        mSetter = pSetter;
    }

    public final Method getValidator() {
        return mValidator;
    }

    public void setValidator(final Method pValidator) {
        mValidator = pValidator;
    }

    public final Method getParser() {
        return mParser;
    }

    public void setParser(final Method pParser) {
        mParser = pParser;
    }

    /**
     * Sets this {@code Property} to represent a descriptor property (i.e. the property whose value
     * is read when a {@link ModelObject} is read as a descriptor and not fully).
     * 
     * @param pDescriptor A {@code boolean} value.
     */
    public void setDescriptor(final boolean pDescriptor) {
        mModifiers.put(PropertyModifier.DESCRIPTOR, pDescriptor);
    }

    /**
     * Sets this {@code Property} to represent a synthetic property (i.e. the property whose value
     * is dynamically computed from other property values and therefore is not persistent) depending
     * on the given {@code boolean} value.
     * 
     * @param pSynthetic A {@code boolean} value.
     */
    public void setSynthetic(final boolean pSynthetic) {
        mModifiers.put(PropertyModifier.SYNTHETIC, pSynthetic);
    }

    /**
     * Sets this {@code Property} to represent a transient property (i.e. the property whose value
     * is not persistent and is not stored into database).
     * 
     * @param pTransient A {@code boolean} value.
     */
    public void setTransient(final boolean pTransient) {
        mModifiers.put(PropertyModifier.TRANSIENT, pTransient);
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
     * @param pObject A {@code ModelObject}.
     * @return The value as an {@code Object}.
     */
    @SuppressWarnings("unchecked")
    public <T> T get(final ModelObject pObject) {
        if (mGetter != null) {
            try {
                return (T) mGetter.invoke(pObject);
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
     * @param pObject A {@code ModelObject}.
     * @param pValue The value as a {@code Object}.
     * @return A {@code boolean} value indicating whether set property value was actually changed.
     */
    public boolean set(final ModelObject pObject, final Object pValue) {
        if (mSetter != null) {
            try {
                boolean changed = false;

                if (mGetter != null) {
                    final Object oldValue = get(pObject);
                    changed = isChanged(oldValue, pValue);
                }

                mSetter.invoke(pObject, pValue);
                pObject.setChanged(changed);
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

    public boolean setValueFromString(final ModelObject pObject, final String pValueString) {

        if (pValueString == null) {
            L.w(this, "set", "Null value detected"); // TODO
            return false;
        }

        return set(pObject, convertStringToValue(pObject, pValueString, mType));
    }

    public void set(final ModelObject pObject, final String pValueName, final JSONObject pJsonObject)
            throws JSONException {

        String valueString = null;

        try {
            valueString = pJsonObject.getString(pValueName);
        } catch (JSONException e) {
        }

        if (valueString == null || valueString.equalsIgnoreCase("null")) {
            return;
        }

        if (mType.equals(Bitmap.class)) {
            // TODO
        } else if (mType.equals(Boolean.class) || mType.equals(Boolean.TYPE)) {
            set(pObject, pJsonObject.getBoolean(pValueName));
        } else if (mType.equals(Byte.class) || mType.equals(Byte.TYPE)) {
            set(pObject, pJsonObject.getInt(pValueName));
        } else if (mType.equals(Date.class)) {
            try {
                final Date date = DateToolkit.parse(valueString);
                set(pObject, date);
            } catch (final Exception e) {
                L.w(this, "set", e.getMessage());
            }
        } else if (mType.equals(Double.class) || mType.equals(Double.TYPE)) {
            set(pObject, pJsonObject.getDouble(pValueName));
        } else if (mType.equals(Float.class) || mType.equals(Float.TYPE)) {
            set(pObject, (float) pJsonObject.getDouble(pValueName));
        } else if (mType.equals(Integer.class) || mType.equals(Integer.TYPE)) {
            set(pObject, pJsonObject.getInt(pValueName));
        } else if (mType.equals(Long.class) || mType.equals(Long.TYPE)) {
            set(pObject, pJsonObject.getLong(pValueName));
        } else if (mType.equals(Short.class) || mType.equals(Short.TYPE)) {
            set(pObject, pJsonObject.getInt(pValueName));
        } else if (mType.equals(String.class)) {
            set(pObject, valueString);
        } else if (mType.equals(byte[].class)) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Tests if this {@code Property} is the one specified by the given name.
     * 
     * @param pName The name of the property,
     * @return A {@code boolean}.
     */
    public final boolean is(final String pName) {
        return mName.equals(pName);
    }

    private boolean isChanged(final Object pOldValue, final Object pNewValue) {
        if (pOldValue == null && pNewValue == null) {
            return false;
        }

        if (pOldValue == null || pNewValue == null) {
            return true;
        }

        return !pOldValue.equals(pNewValue);
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
     * @param pType A {@link Class} specifying the type of the {@link ModelObject}.
     * @return A {@code boolean} value.
     */
    public boolean isTransientFor(final Class<? extends ModelObject> pType) {
        return mTransientExceptions.contains(pType);
    }

/**
     * Tests whether the given {@code Object} represents a valid value of 
     * this {@code Property} for the given {@code ModelObject}.
     *      
     * @param pObject A {@code ModelObject].     
     * @param pValue The value as an {@code Object}. May be {@code null}.
     * @return A {@code boolean} value.
     */
    public boolean validate(final ModelObject pObject, final Object pValue) {
        final Class<?> propertyType = getType();

        if (pValue == null) {
            if (propertyType.isPrimitive()) {
                return false;
            }
        }

        if (mValidator != null) {
            try {
                return (Boolean) mValidator.invoke(pObject, pValue);
            } catch (final IllegalAccessException e) {
                L.wtf(this, "validate", e.getMessage());
            } catch (final InvocationTargetException e) {
                L.wtf(this, "validate", e.getMessage());
            } catch (final NullPointerException e) {
                L.wtf(this, "validate", e.getMessage());
            }
        }

        return propertyType.isAssignableFrom(pValue.getClass());
    }

    public boolean validate(final ModelObject pObject, final Object pValue, final MessageContext pMessageContext) {
        final Class<?> propertyType = getType();

        if (pValue == null) {
            if (propertyType.isPrimitive()) {
                return false;
            }
        }

        if (mValidator != null) {
            try {
                return (Boolean) mValidator.invoke(pObject, pValue, pMessageContext);
            } catch (final IllegalAccessException e) {
                L.wtf(this, "validate", e.getMessage());
            } catch (final InvocationTargetException e) {
                L.wtf(this, "validate", e.getMessage());
            } catch (final NullPointerException e) {
                L.wtf(this, "validate", e.getMessage());
            }
        }

        return propertyType.isAssignableFrom(pValue.getClass());
    }

    public Object parse(final ModelObject pObject, final String pValueString) {

        if (mValidator != null) {
            try {
                return mParser.invoke(pObject, pValueString);
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

    public Object convertStringToValue(final ModelObject pObject, final String pValueString, final Class<?> pType) {

        if (mParser != null) {
            return parse(pObject, pValueString);
        }

        if (pValueString == null) { // TODO Check value is allowed
            return null;
        }

        if (pType.equals(Bitmap.class)) {
            // TODO
        } else if (pType.equals(Boolean.class) || pType.equals(Boolean.TYPE)) {
            return pValueString.equalsIgnoreCase("true");
        } else if (pType.equals(Byte.class) || pType.equals(Byte.TYPE)) {
            return Byte.parseByte(pValueString);
        } else if (pType.equals(Date.class)) {
            try {
                final Date date = DateToolkit.parse(pValueString);
                return date;
            } catch (final Exception e) {
                L.w(Property.class, "convertStringToValue", e.getMessage());
            }
        } else if (pType.equals(Double.class) || pType.equals(Double.TYPE)) {
            return Double.parseDouble(pValueString);
        } else if (pType.equals(Float.class) || pType.equals(Float.TYPE)) {
            return Float.parseFloat(pValueString);
        } else if (pType.equals(Integer.class) || pType.equals(Integer.TYPE)) {
            return Integer.parseInt(pValueString);
        } else if (pType.equals(Long.class) || pType.equals(Long.TYPE)) {
            return Long.parseLong(pValueString);
        } else if (pType.equals(Short.class) || pType.equals(Short.TYPE)) {
            return Short.parseShort(pValueString);
        } else if (pType.equals(String.class)) {
            return pValueString;
        }

        throw new UnsupportedOperationException();
    }
    /**
     * Changes the first character of the given {@code String} to be a uppercase character.
     * 
     * @param pString The given {@code String}. It must contain at least one character.
     * @return The modified {@code String}.
     */
    public static String upperCaseFirstCharacter(final String pString) {
        final char firstChar = pString.charAt(0);

        if (Character.isUpperCase(firstChar)) {
            return pString;
        } else {
            final StringBuffer buffer = new StringBuffer(pString);
            buffer.setCharAt(0, Character.toUpperCase(firstChar));
            return buffer.toString();
        }
    }

    public Object read(final Cursor pCursor, final int pColumnIndex) {

        if (mType.equals(Bitmap.class)) {
            final byte[] imageByteArray = pCursor.getBlob(pColumnIndex);
            Bitmap bitmap = null;

            if (imageByteArray != null) {
                final ByteArrayInputStream imageStream = new ByteArrayInputStream(imageByteArray);
                bitmap = BitmapFactory.decodeStream(imageStream);
            }

            return bitmap;
        } else if (mType.equals(Boolean.class) || mType.equals(Boolean.TYPE)) {
            return (pCursor.getInt(pColumnIndex) != 0) ? true : false;
        } else if (mType.equals(Byte.class) || mType.equals(Byte.TYPE)) {
            return (byte) pCursor.getInt(pColumnIndex);
        } else if (mType.equals(Date.class)) {
            String value = pCursor.getString(pColumnIndex);

            try {
                return DateToolkit.parse(value);
            } catch (final Exception e) {
                return new Date(); // TODO
            }
        } else if (mType.equals(Double.class) || mType.equals(Double.TYPE)) {
            return pCursor.getDouble(pColumnIndex);
        } else if (mType.equals(Float.class) || mType.equals(Float.TYPE)) {
            return pCursor.getFloat(pColumnIndex);
        } else if (mType.equals(Integer.class) || mType.equals(Integer.TYPE)) {
            return pCursor.getInt(pColumnIndex);
        } else if (mType.equals(Long.class) || mType.equals(Long.TYPE)) {
            return pCursor.getLong(pColumnIndex);
        } else if (mType.equals(Short.class) || mType.equals(Short.TYPE)) {
            return pCursor.getShort(pColumnIndex);
        } else if (mType.equals(String.class)) {
            return pCursor.getString(pColumnIndex);
        } else if (mType.equals(byte[].class)) {
            return pCursor.getBlob(pColumnIndex);
        }

        return null;
    }

    public void save(final ModelObject pObject, final ContentValues pValues) {

        final Object value = get(pObject);

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

            pValues.put(mName, blob);
        } else if (value instanceof Boolean) {
            pValues.put(mName, (Boolean) value);
        } else if (value instanceof Byte) {
            pValues.put(mName, (Byte) value);
        } else if (value instanceof Date) {
            final Date date = (Date) value;
            pValues.put(mName, DateToolkit.format(date));
        } else if (value instanceof Double) {
            pValues.put(mName, (Double) value);
        } else if (value instanceof Float) {
            pValues.put(mName, (Float) value);
        } else if (value instanceof Integer) {
            pValues.put(mName, (Integer) value);
        } else if (value instanceof Long) {
            pValues.put(mName, (Long) value);
        } else if (value instanceof Short) {
            pValues.put(mName, (Short) value);
        } else if (value instanceof String) {
            pValues.put(mName, (String) value);
        } else if (value instanceof byte[]) {
            pValues.put(mName, (byte[]) value);
        }
    }

    public void addTransitionExceptionFor(final Class<? extends ModelObject> pObjectClass) {
        mTransientExceptions.add(pObjectClass);
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
