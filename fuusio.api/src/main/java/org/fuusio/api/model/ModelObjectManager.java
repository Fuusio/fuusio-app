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

    protected ModelObjectManager(final String pName) {
        super(pName);

        mMetaInfos = new HashMap<>();
        mModelObjects = new HashMap<>();
        mObjectFactory = this;
    }

    public void configure() {
        // TODO
    }

    public void setModelObjectFactory(final ModelObjectFactory pFactory) {
        mObjectFactory = pFactory;
    }

    public <T extends ModelObject> T getObject(final Class<T> pObjectClass, final long pId) {
        return getObject(pObjectClass, pId, true);
    }

    @SuppressWarnings("unchecked")
    public <T extends ModelObject> T getObject(final Class<T> pObjectClass, final long pId,
            final boolean pCreate) {
        HashMap<Long, ModelObject> instances = mModelObjects.get(pObjectClass);
        T pObject = null;

        if (instances == null) {
            instances = new HashMap<>();
            mModelObjects.put(pObjectClass, instances);
        } else {
            pObject = (T) instances.get(pId);
        }

        if (pObject == null && pCreate) {
            pObject = mObjectFactory.createInstance(pObjectClass);
            pObject.setContext(this);
            pObject.setId(pId);
            instances.put(pId, pObject);

            if (pObject.existsInDatabase(pId)) {
                pObject.readFromDatabase(pId);
                pObject.setInitialized(true);
            }
        }

        return pObject;
    }

    @SuppressWarnings("unchecked")
    public <T extends ModelObject> Collection<T> getObjects(final Class<T> pObjectClass) {
        HashMap<Long, ModelObject> instancesMap = mModelObjects.get(pObjectClass);

        if (instancesMap == null) {
            instancesMap = new HashMap<>();
            mModelObjects.put(pObjectClass, instancesMap);
        }

        return (Collection<T>) instancesMap.values();
    }

    @SuppressWarnings("unchecked")
    public <T extends ModelObject> void getObjects(final Class<T> pObjectClass,
            final Collection<T> pObjects) {
        HashMap<Long, ModelObject> instancesMap = mModelObjects.get(pObjectClass);

        if (instancesMap == null) {
            instancesMap = new HashMap<Long, ModelObject>();
            mModelObjects.put(pObjectClass, instancesMap);
        }

        pObjects.addAll((Collection<T>) instancesMap.values());
    }

    public final ModelObjectMetaInfo getMetaInfo(final Class<? extends ModelObject> pObjectClass) {
        return mMetaInfos.get(pObjectClass);
    }

    public final ModelObjectMetaInfo getMetaInfo(final ModelObject pObject) {
        return mMetaInfos.get(pObject.getClass());
    }

    public void addObject(final ModelObject pObject) {
        final Class<? extends ModelObject> objectClass = pObject.getClass();
        addObject(objectClass, pObject);
    }

    public void addObject(final Class<? extends ModelObject> pObjectClass, final ModelObject pObject) {
        HashMap<Long, ModelObject> instancesMap = mModelObjects.get(pObjectClass);

        if (instancesMap == null) {
            instancesMap = new HashMap<>();
            mModelObjects.put(pObjectClass, instancesMap);
        }

        final long pId = pObject.getId();
        assert (pId >= 0);
        instancesMap.put(pId, pObject);
        pObject.setContext(this);
    }

    public void removeObject(final ModelObject pObject) {
        final Class<? extends ModelObject> objectClass = pObject.getClass();
        final HashMap<Long, ModelObject> instancesMap = mModelObjects.get(objectClass);

        if (instancesMap != null) {
            long pId = pObject.getId();
            assert (pId >= 0);
            instancesMap.remove(pId);
        }
    }

    public ModelObjectMetaInfo registerObjectClass(final Class<? extends ModelObject> pObjectClass) {
        ModelObjectMetaInfo metaInfo = mMetaInfos.get(pObjectClass);

        if (metaInfo == null) {
            metaInfo = new ModelObjectMetaInfo(pObjectClass, this);
            mMetaInfos.put(pObjectClass, metaInfo);
            metaInfo.setup();
        }

        return metaInfo;
    }

    public final Collection<Property> getDescriptorProperties(final ModelObject pObject) {
        final ModelObjectMetaInfo metaInfo = getMetaInfo(pObject);
        return metaInfo.getDescriptorProperties();
    }

    public final Collection<Property> getProperties(final ModelObject pObject) {
        return getProperties(pObject.getClass());
    }

    public final Collection<Property> getProperties(final Class<? extends ModelObject> pObjectClass) {
        final ModelObjectMetaInfo metaInfo = getMetaInfo(pObjectClass);
        return metaInfo.getProperties();
    }

    public final Property getProperty(final ModelObject pObject, final String pPropertyName) {
        return getProperty(pObject.getClass(), pPropertyName);
    }

    public final Property getProperty(final Class<? extends ModelObject> pObjectClass, final String propertyName) {
        final ModelObjectMetaInfo metaInfo = getMetaInfo(pObjectClass);
        return metaInfo.getProperty(propertyName);
    }

    public void objectChanged(final Class<? extends ModelObject> pObjectClass, final long pId,
            final List<String> pProperties) {
        final ModelObject object = getObject(pObjectClass, pId);

        if (object != null) {
            object.readFromDatabase(pId); // TODO : Optimize to read only changed properties
        }
    }

    public boolean existsInDatabase(final Uri pContentUri, final long pId) { // TODO OPTIMIZE
                                                                                    // !
        if (pContentUri == null) {
            return false;
        }

        final Uri uri = ContentUris.withAppendedId(pContentUri, pId);
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
    public <T extends ModelObject> T createInstance(final String pClassName) {
        T instance = null;

        try {
            final Class<T> modelClass = (Class<T>)Class.forName(pClassName);
            instance = createInstance(modelClass);
        } catch (final Exception pException) {
            L.e(this, "createInstance", pException);
        }
        return instance;
    }

    @Override
    public <T extends ModelObject> T createInstance(final Class<T> pClass) {
        T instance = null;

        try {
            instance = pClass.newInstance();
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

    public boolean exists(final ModelObject pObject) {
        final Class<? extends ModelObject> objectClass = pObject.getClass();
        final HashMap<Long, ModelObject> instances = mModelObjects.get(objectClass);
        return instances.containsValue(pObject);
    }

    public boolean exists(final Class<? extends ModelObject> pObjectClass, final long pId) {
        final ModelObject modelObject = getObject(pObjectClass, pId, false);
        return (modelObject != null);
    }

    public static String getString(final int pResId) {
        return AppToolkit.getString(pResId);
    }


    public ContentResolver getContentResolver() {
        final Application application = D.get(Application.class);
        return application.getContentResolver();
    }

    public void notifyModelObjectChanged(final ModelObject pObject) {
        mModelObjectObserver.onModelObjectChanged(pObject);
    }
}
