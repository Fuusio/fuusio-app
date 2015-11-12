package org.fuusio.api.model;

import android.content.ContentResolver;
import android.net.Uri;

import java.util.Collection;

public interface ModelObjectContext {

    Property getProperty(Class<? extends ModelObject> objectClass, String propertyName);

    Collection<Property> getProperties(Class<? extends ModelObject> objectClass);

    ContentResolver getContentResolver();

    Collection<Property> getDescriptorProperties(ModelObject object);

    ModelObjectMetaInfo getMetaInfo(Class<? extends ModelObject> objectClass);

    <T extends ModelObject> T getObject(Class<T> objectClass, long id);

    <T extends ModelObject> T createInstance(String className);

    <T extends ModelObject> T createInstance(Class<T> objectClass);

    ModelObjectMetaInfo registerObjectClass(Class<? extends ModelObject> objectClass);

    boolean exists(Class<? extends ModelObject> objectClass, long id);

    boolean existsInDatabase(Uri uri, long id);

    void notifyModelObjectChanged(ModelObject object);
}
