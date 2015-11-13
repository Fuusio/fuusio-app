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

import org.fuusio.api.model.Property.PropertyGetter;
import org.fuusio.api.model.Property.PropertySetter;
import org.fuusio.api.model.Property.PropertyValidator;
import org.fuusio.api.model.Property.TransientProperties;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.StringTokenizer;

public class ModelObjectMetaInfo {

    private final Class<? extends ModelObject> mObjectClass;
    private final ModelObjectContext mObjectContext;
    private final HashMap<String, Property> mProperties;

    protected ModelObjectMetaInfo(final Class<? extends ModelObject> objectClass, final ModelObjectContext objectContext) {
        mObjectClass = objectClass;
        mObjectContext = objectContext;
        mProperties = new HashMap<>();
    }

    public void setup() {
        collectProperties(mObjectClass);

        if (mObjectClass.isAnnotationPresent(TransientProperties.class)) {
            final TransientProperties transientProperties = mObjectClass
                    .getAnnotation(TransientProperties.class);
            final String properties = transientProperties.properties();
            final StringTokenizer tokens = new StringTokenizer(properties, ",");

            while (tokens.hasMoreTokens()) {
                final String propertyName = tokens.nextToken();
                final Property property = getProperty(propertyName);
                property.addTransitionExceptionFor(mObjectClass);
            }
        }
    }

    public final Class<? extends ModelObject> getObjectClass() {
        return mObjectClass;
    }

    public final Collection<Property> getProperties() {
        return mProperties.values();
    }

    public final Property getProperty(final String propertyName) {
        return mProperties.get(propertyName);
    }

    protected Property getProperty(final String propertyName, final boolean create) {
        Property property = mProperties.get(propertyName);

        if (property == null && create) {
            property = new Property(propertyName);
            mProperties.put(propertyName, property);
        }

        return property;
    }

    @SuppressWarnings("unchecked")
    protected void collectProperties(final Class<? extends ModelObject> objectClass) {

        for (final Method method : objectClass.getMethods()) {

            if (method.isAnnotationPresent(PropertyGetter.class)) {
                final PropertyGetter annotation = method.getAnnotation(PropertyGetter.class);
                final String propertyName = annotation.property();
                final boolean isDescriptor = annotation.isDescriptor();
                final boolean isSynthetic = annotation.isSynthetic();
                final boolean isTransient = annotation.isTransient();
                final boolean isKey = annotation.isKey();
                final int index = annotation.index();

                final Property property = getProperty(propertyName, true);
                property.setGetter(method);
                property.setDescriptor(isDescriptor);
                property.setSynthetic(isSynthetic);
                property.setTransient(isTransient);
                property.setKey(isKey);
                property.setColumnIndex(index);

            } else if (method.isAnnotationPresent(PropertySetter.class)) {
                final PropertySetter annotation = method.getAnnotation(PropertySetter.class);
                final String propertyName = annotation.property();
                final Property property = getProperty(propertyName, true);
                property.setSetter(method);
            } else if (method.isAnnotationPresent(Property.PropertyResetter.class)) {
                final Property.PropertyResetter annotation = method.getAnnotation(Property.PropertyResetter.class);
                final String propertyName = annotation.property();
                final Property property = getProperty(propertyName, true);
                property.setResetter(method);
            } else if (method.isAnnotationPresent(PropertyValidator.class)) {
                final PropertyValidator annotation = method.getAnnotation(PropertyValidator.class);
                final String propertyName = annotation.property();
                final Property property = getProperty(propertyName, true);
                property.setValidator(method);
            }
        }

        final Class<?> superClass = objectClass.getSuperclass();

        if (ModelObject.class.isAssignableFrom(superClass)) {

            final Class<? extends ModelObject> superObjectClass = (Class<? extends ModelObject>) superClass;
            ModelObjectMetaInfo metaInfo = mObjectContext.getMetaInfo(superObjectClass);

            if (metaInfo == null) {
                metaInfo = mObjectContext.registerObjectClass(superObjectClass);
            }

            for (final Property property : metaInfo.getProperties()) {
                mProperties.put(property.getName(), property);
            }
        }
    }

    public Collection<Property> getDescriptorProperties() {
        final ArrayList<Property> descriptorProperties = new ArrayList<Property>();

        for (final Property property : mProperties.values()) {
            if (property.isDescriptor()) {
                descriptorProperties.add(property);
            }
        }
        return descriptorProperties;
    }
}
