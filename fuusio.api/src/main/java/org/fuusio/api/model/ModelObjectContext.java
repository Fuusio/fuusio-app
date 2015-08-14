package org.fuusio.api.model;

import android.content.ContentResolver;
import android.net.Uri;

import java.util.Collection;

public interface ModelObjectContext {

    Property getProperty(Class<? extends ModelObject> pObjectClass, String propertyName);

    Collection<Property> getProperties(Class<? extends ModelObject> pObjectClass);

    ContentResolver getContentResolver();

    Collection<Property> getDescriptorProperties(ModelObject pObject);

    ModelObjectMetaInfo getMetaInfo(Class<? extends ModelObject> pObjectClass);

    <T extends ModelObject> T getObject(Class<T> pObjectClass, long pId);

    <T extends ModelObject> T createInstance(String pClassName);

    <T extends ModelObject> T createInstance(Class<T> pClass);

    ModelObjectMetaInfo registerObjectClass(Class<? extends ModelObject> pObjectClass);

    boolean exists(Class<? extends ModelObject> pObjectClass, long pId);

    boolean existsInDatabase(Uri pUri, long pId);

    void notifyModelObjectChanged(ModelObject pObject);
}
