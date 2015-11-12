// ============================================================================
// Floxp.com : Java Class Source File
// ============================================================================
//
// Class: ModelObjectManager
// Package: FloXP Model API (org.fuusio.api.model)
//
// Author: Marko Salmela
//
// Copyright (C) Marko Salmela, 2000-2011. All Rights Reserved.
//
// This software is the proprietary information of Marko Salmela.
// Use is subject to license terms. This software is protected by
// copyright and distributed under licenses restricting its use,
// copying, distribution, and decompilation. No part of this software
// or associated documentation may be reproduced in any form by any
// means without prior written authorization of Marko Salmela.
//
// Disclaimer:
// -----------
//
// This software is provided by the author 'as is' and any express or implied
// warranties, including, but not limited to, the implied warranties of
// merchantability and fitness for a particular purpose are disclaimed.
// In no event shall the author be liable for any direct, indirect,
// incidental, special, exemplary, or consequential damages (including, but
// not limited to, procurement of substitute goods or services, loss of use,
// data, or profits; or business interruption) however caused and on any
// theory of liability, whether in contract, strict liability, or tort
// (including negligence or otherwise) arising in any way out of the use of
// this software, even if advised of the possibility of such damage.
// ============================================================================

package org.fuusio.api.model;

import android.app.Application;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;

import org.fuusio.api.dependency.D;
import org.fuusio.api.plugin.PluginComponent;
import org.fuusio.api.util.AppToolkit;
import org.fuusio.api.util.L;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public abstract class ModelObjectManager extends PluginComponent implements ModelObjectContext, ModelObjectFactory {

    private final HashMap<Class<? extends ModelObject>, ModelObjectMetaInfo> mMetaInfos;
    private final HashMap<Class<? extends ModelObject>, HashMap<Long, ModelObject>> mModelObjects;

    private ModelObjectFactory mObjectFactory;

    @Plug
    ModelObjectObserver mModelObjectObserver;

    protected ModelObjectManager(final String name) {
        super(name);

        mMetaInfos = new HashMap<>();
        mModelObjects = new HashMap<>();
        mObjectFactory = this;
    }

    public void configure() {
        // TODO
    }

    public void setModelObjectFactory(final ModelObjectFactory factory) {
        mObjectFactory = factory;
    }

    public <T extends ModelObject> T getObject(final Class<T> objectClass, final long id) {
        return getObject(objectClass, id, true);
    }

    @SuppressWarnings("unchecked")
    public <T extends ModelObject> T getObject(final Class<T> objectClass, final long id,
            final boolean create) {
        HashMap<Long, ModelObject> instances = mModelObjects.get(objectClass);
        T object = null;

        if (instances == null) {
            instances = new HashMap<>();
            mModelObjects.put(objectClass, instances);
        } else {
            object = (T) instances.get(id);
        }

        if (object == null && create) {
            object = mObjectFactory.createInstance(objectClass);
            object.setContext(this);
            object.setId(id);
            instances.put(id, object);

            if (object.existsInDatabase(id)) {
                object.readFromDatabase(id);
                object.setInitialized(true);
            }
        }

        return object;
    }

    @SuppressWarnings("unchecked")
    public <T extends ModelObject> Collection<T> getObjects(final Class<T> objectClass) {
        HashMap<Long, ModelObject> instancesMap = mModelObjects.get(objectClass);

        if (instancesMap == null) {
            instancesMap = new HashMap<>();
            mModelObjects.put(objectClass, instancesMap);
        }

        return (Collection<T>) instancesMap.values();
    }

    @SuppressWarnings("unchecked")
    public <T extends ModelObject> void getObjects(final Class<T> objectClass,
            final Collection<T> objects) {
        HashMap<Long, ModelObject> instancesMap = mModelObjects.get(objectClass);

        if (instancesMap == null) {
            instancesMap = new HashMap<Long, ModelObject>();
            mModelObjects.put(objectClass, instancesMap);
        }

        objects.addAll((Collection<T>) instancesMap.values());
    }

    public final ModelObjectMetaInfo getMetaInfo(final Class<? extends ModelObject> objectClass) {
        return mMetaInfos.get(objectClass);
    }

    public final ModelObjectMetaInfo getMetaInfo(final ModelObject object) {
        return mMetaInfos.get(object.getClass());
    }

    public void addObject(final ModelObject object) {
        final Class<? extends ModelObject> objectClass = object.getClass();
        addObject(objectClass, object);
    }

    public void addObject(final Class<? extends ModelObject> objectClass, final ModelObject object) {
        HashMap<Long, ModelObject> instancesMap = mModelObjects.get(objectClass);

        if (instancesMap == null) {
            instancesMap = new HashMap<>();
            mModelObjects.put(objectClass, instancesMap);
        }

        final long id = object.getId();
        assert (id >= 0);
        instancesMap.put(id, object);
        object.setContext(this);
    }

    public void removeObject(final ModelObject object) {
        final Class<? extends ModelObject> objectClass = object.getClass();
        final HashMap<Long, ModelObject> instancesMap = mModelObjects.get(objectClass);

        if (instancesMap != null) {
            long id = object.getId();
            assert (id >= 0);
            instancesMap.remove(id);
        }
    }

    public ModelObjectMetaInfo registerObjectClass(final Class<? extends ModelObject> objectClass) {
        ModelObjectMetaInfo metaInfo = mMetaInfos.get(objectClass);

        if (metaInfo == null) {
            metaInfo = new ModelObjectMetaInfo(objectClass, this);
            mMetaInfos.put(objectClass, metaInfo);
            metaInfo.setup();
        }

        return metaInfo;
    }

    public final Collection<Property> getDescriptorProperties(final ModelObject object) {
        final ModelObjectMetaInfo metaInfo = getMetaInfo(object);
        return metaInfo.getDescriptorProperties();
    }

    public final Collection<Property> getProperties(final ModelObject object) {
        return getProperties(object.getClass());
    }

    public final Collection<Property> getProperties(final Class<? extends ModelObject> objectClass) {
        final ModelObjectMetaInfo metaInfo = getMetaInfo(objectClass);
        return metaInfo.getProperties();
    }

    public final Property getProperty(final ModelObject object, final String propertyName) {
        return getProperty(object.getClass(), propertyName);
    }

    public final Property getProperty(final Class<? extends ModelObject> objectClass, final String propertyName) {
        final ModelObjectMetaInfo metaInfo = getMetaInfo(objectClass);
        return metaInfo.getProperty(propertyName);
    }

    public void objectChanged(final Class<? extends ModelObject> objectClass, final long id,
            final List<String> pProperties) {
        final ModelObject object = getObject(objectClass, id);

        if (object != null) {
            object.readFromDatabase(id); // TODO : Optimize to read only changed properties
        }
    }

    public boolean existsInDatabase(final Uri contentUri, final long id) { // TODO OPTIMIZE
                                                                                    // !
        if (contentUri == null) {
            return false;
        }

        final Uri uri = ContentUris.withAppendedId(contentUri, id);
        final ContentResolver resolver = getContentResolver();
        final ContentProviderClient providerClient = resolver.acquireContentProviderClient(uri);
        boolean exists = false;

        try {
            Cursor cursor = providerClient.query(uri, new String[0], null, null, null); // TODO

            if (cursor != null) {
                int count = cursor.getCount();
                cursor.close();
                exists = (count > 0);
            }
        } catch (final Exception pException) {
            L.e(this, "existsInDatabase", pException);
        }

        providerClient.release();
        return exists;
    }

    @Override
    public <T extends ModelObject> T createInstance(final String className) {
        T instance = null;

        try {
            final Class<T> modelClass = (Class<T>)Class.forName(className);
            instance = createInstance(modelClass);
        } catch (final Exception pException) {
            L.e(this, "createInstance", pException);
        }
        return instance;
    }

    @Override
    public <T extends ModelObject> T createInstance(final Class<T> objectClass) {
        T instance = null;

        try {
            instance = objectClass.newInstance();
            instance.setContext(this);
        } catch (final InstantiationException pException) {
            L.e(this, "createInstance", pException);
        } catch (final IllegalAccessException pException) {
            L.e(this, "createInstance", pException);
        }
        return instance;
    }

    public void onPause() {
        saveChangedModels();
    }

    public void onResume() {
    }

    private void saveChangedModels() {
        for (final HashMap<Long, ModelObject> objects : mModelObjects.values()) {
            for (final ModelObject object : objects.values()) {
                if (object instanceof Model) {
                    final Model model = (Model) object;

                    if (model.isChanged()) {
                        // TODO
                    }
                }
            }
        }
    }

    public boolean exists(final ModelObject object) {
        final Class<? extends ModelObject> objectClass = object.getClass();
        final HashMap<Long, ModelObject> instances = mModelObjects.get(objectClass);
        return instances.containsValue(object);
    }

    public boolean exists(final Class<? extends ModelObject> objectClass, final long id) {
        final ModelObject modelObject = getObject(objectClass, id, false);
        return (modelObject != null);
    }

    public static String getString(final int resId) {
        return AppToolkit.getString(resId);
    }


    public ContentResolver getContentResolver() {
        final Application application = D.get(Application.class);
        return application.getContentResolver();
    }

    public void notifyModelObjectChanged(final ModelObject object) {
        mModelObjectObserver.onModelObjectChanged(object);
    }
}
