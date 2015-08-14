/*
 * Copyright (C) 2009 - 2015 Marko Salmela, http://fuusio.org
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
package org.fuusio.api.db;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.fuusio.api.dependency.D;
import org.fuusio.api.model.ModelObject;
import org.fuusio.api.model.ModelObjectManager;
import org.fuusio.api.model.ModelObjectMetaInfo;
import org.fuusio.api.model.Property;
import org.fuusio.api.util.DateToolkit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class DatabaseHelper extends SQLiteOpenHelper {

	private final HashMap<Class<? extends ModelObject>, ModelObjectTableDescriptor> mObjectTableDescriptors;
    private final ModelObjectManager mModelManager = D.get(ModelObjectManager.class);

	public DatabaseHelper(final Context pContext, final String pName, final CursorFactory pFactory,
			final int pVersion) {
		super(pContext, pName, pFactory, pVersion);
		mObjectTableDescriptors = new HashMap<Class<? extends ModelObject>, ModelObjectTableDescriptor>();
	}

	@Override
	public void onCreate(final SQLiteDatabase pDb) {
		createTables(pDb);
	}
	
	public ModelObjectTableDescriptor getTableDescriptorFor(final Class<? extends ModelObject> pObjectClass) {
		ModelObjectTableDescriptor descriptor = mObjectTableDescriptors.get(pObjectClass);
		
		if (descriptor == null) {
			final ModelObjectMetaInfo metaInfo = mModelManager.getMetaInfo(pObjectClass);
			
			descriptor = new ModelObjectTableDescriptor(metaInfo);
			mObjectTableDescriptors.put(pObjectClass, descriptor);
		}
		
		return descriptor;
	}

	protected abstract TablesDescriptor getTablesDescriptor();
	
	protected TableDescriptor[] getTableDescriptors() {
		final TablesDescriptor tablesDecriptor = getTablesDescriptor();
		
		if (tablesDecriptor != null) {
			return tablesDecriptor.getTableDescriptors();
		}
		return null;
	}
	
	protected void createTables(final SQLiteDatabase pDb) {

		final TableDescriptor[] tableDescriptors = getTableDescriptors();
		
		if (tableDescriptors != null) {
			for (final TableDescriptor descriptor : tableDescriptors) {
				final SqlStatement statement = SqlStatement.create(descriptor);
				pDb.execSQL(statement.execute());
			}
		} else {
	        final String[] createStatements = getTableCreateStatements();
	
	        for (int i = 0; i < createStatements.length; i++) {
	            pDb.execSQL(createStatements[i]);
	        }
		}
    }

    @Override
    public void onUpgrade(final SQLiteDatabase pDb, final int pOldVersion, final int pNewVersion) {

		final TableDescriptor[] tableDescriptors = getTableDescriptors();
		
		if (tableDescriptors != null) {
			for (final TableDescriptor descriptor : tableDescriptors) {
				 pDb.execSQL(SqlStatement.DROP_TABLE_IF_EXISTS + descriptor.getName());
			}
		} else {    	
	        final String[] tableNames = getTableNames();
	
	        for (int i = 0; i < tableNames.length; i++) {
	            pDb.execSQL(SqlStatement.DROP_TABLE_IF_EXISTS + tableNames[i]);
	        }
		}

        onCreate(pDb);
    }

    public abstract String[] getTableNames();

    public abstract String[] getTableCreateStatements();

    protected String getTableNameFor(final ModelObject pObject) {
        return getTableNameFor(pObject.getClass());
    }

    protected abstract String getTableNameFor(final Class<? extends ModelObject> pObjectClass);

    protected String getKeyIdFor(final Class<? extends ModelObject> pObjectClass) {
        return SqlStatement.KEY_ID; // TODO
    }

    public boolean isInDatabase(final ModelObject pObject) {
    	
    	final long objectId = pObject.getId();  
    	boolean isInDatabase = false;
    	
    	if (objectId > 0) {
	        final String tableName = getTableNameFor(pObject);
	        final SQLiteDatabase db = getReadableDatabase();
	        final String keyId = getKeyIdFor(pObject.getClass());
	    	final String selectQuery = SqlStatement.SELECT_FROM + tableName + SqlStatement.WHERE + keyId + " = " + objectId;
	    	final Cursor cursor = db.rawQuery(selectQuery, null);
	    	isInDatabase = (cursor != null);
	    	db.close();
    	}
    	
        return isInDatabase;
    }
    
    public long create(final ModelObject pObject) {

        final ContentValues values = new ContentValues();
        
        if (useColumnMapping()) {
        	getContentValues(pObject, values);
        } else {
        	pObject.getContentValues(values);
        }
        
        final String tableName = getTableNameFor(pObject);
        final SQLiteDatabase db = getWritableDatabase();
    	final long id = db.insert(tableName, null, values);
    	pObject.setId(id);
    	db.close();
        return id;
    }

    private void getContentValues(final ModelObject pObject, final ContentValues pValues) {
        
    	final Class<? extends ModelObject> objectClass = pObject.getClass();
        final ModelObjectTableDescriptor tableDescriptor = getTableDescriptorFor(objectClass);
        
        for (final Property property : mModelManager.getProperties(pObject)) {
            if (!property.isTransientFor(objectClass)) {
                final Object value = property.get(pObject);
                final int columnIndex = property.getColumnIndex();
                final ColumnDescriptor columnDescriptor = tableDescriptor.getColumnDescriptor(columnIndex);
                
                putContentValue(pValues, value, columnDescriptor);
            }
        }
    }

	private void putContentValue(final ContentValues pValues, final Object pValue, ColumnDescriptor pColumnDescriptor) {

		final ColumnDataType datatype = pColumnDescriptor.getType();
		final String key = pColumnDescriptor.getName();
	
		switch(datatype) {
			case BOOLEAN: {
				final Boolean value = Boolean.class.cast(pValue);
				pValues.put(key, value);
				break;
			}
			case DATE: {
				final Date value = Date.class.cast(pValue);
				pValues.put(key, DateToolkit.format(value));
				break;
			}
			case DOUBLE: {
				final Double value = Double.class.cast(pValue);
				pValues.put(key, value);
				break;
			}
			case FLOAT: {
				final Float value = Float.class.cast(pValue);
				pValues.put(key, value);
				break;
			}
			case INTEGER: {
				final Integer value = Integer.class.cast(pValue);
				pValues.put(key, value);
				break;
			}
			case LONG: {
				final Long value = Long.class.cast(pValue);
				pValues.put(key, value);
				break;
			}
			case REAL: {
				final Double value = Double.class.cast(pValue);
				pValues.put(key, value);
				break;
			}
			case TEXT: {
				pValues.put(key, (pValue != null) ? pValue.toString() : null);
				break;
			}
			case MODEL_OBJECT: {
				final ModelObject value = ModelObject.class.cast(pValue);
				final String tableName = getTableNameFor(value);
				pValues.put(key, (value != null) ? tableName + "[" + value.getId() + "]" : null);
				break;
			}
			case SERIALIZABLE:
			case BLOB: {
				final Serializable value = Serializable.class.cast(pValue);
				pValues.put(key, Database.toBlob(value));
				break;
			}
			default: {
				throw new IllegalStateException();
			}
		}
	}

	protected boolean useColumnMapping() {
		return true;
	}

	@SuppressWarnings("unchecked")
    public <T extends ModelObject> T getModelObject( final Class<? extends ModelObject> pObjectClass, final long pObjectId) {

        final String tableName = getTableNameFor(pObjectClass);
        final String keyId = getKeyIdFor(pObjectClass);
        final String selectQuery = SqlStatement.SELECT_FROM + tableName + SqlStatement.WHERE + keyId + " = " + pObjectId;
        final SQLiteDatabase db = getReadableDatabase();
        final Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        final String className = cursor.getString(cursor.getColumnIndex(ModelObject.KEY_CLASS));
        Class<? extends ModelObject> actualObjectClass = null;

        try {
            actualObjectClass = (Class<? extends ModelObject>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        final ModelObject object = mModelManager.createInstance(actualObjectClass);

        setProperties(object, cursor);

        db.close();
        
        return (T) object;
    }

    protected abstract void setProperties(final ModelObject pObject, final Cursor pCursor);

    @SuppressWarnings("unchecked")
    public <T extends ModelObject> List<T> getAllModelObjects(final Class<? extends ModelObject> pObjectClass) {

        final String tableName = getTableNameFor(pObjectClass);
        final String selectQuery = SqlStatement.SELECT_FROM + tableName;
        final SQLiteDatabase db = getReadableDatabase();
        final List<T> allObjects = new ArrayList<T>();
        final Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (cursor.moveToFirst()) {

            do {
                final ModelObject object = mModelManager.createInstance(pObjectClass);
                setProperties(object, cursor);
                allObjects.add((T) object);
            } while (cursor.moveToNext());
        }

        db.close();
        return allObjects;
    }

    public int updateModelObject(final ModelObject pObject) {

        final ContentValues values = new ContentValues();

        if (useColumnMapping()) {
        	getContentValues(pObject, values);
        } else {
        	pObject.getContentValues(values);
        }        

        final Class<? extends ModelObject> objectClass = pObject.getClass();
        final String tableName = getTableNameFor(objectClass);
        final String keyId = getKeyIdFor(objectClass);
        final long objectId = pObject.getId();
        final SQLiteDatabase db = getWritableDatabase();

        return db.update(tableName, values, keyId + " = ?", SqlStatement.whereArgs(objectId));
    }

    public void deleteModelObject(final ModelObject pObject) {
    	
        final Class<? extends ModelObject> objectClass = pObject.getClass();
        final String tableName = getTableNameFor(objectClass);
        final String keyId = getKeyIdFor(objectClass);
        final long objectId = pObject.getId();
        final SQLiteDatabase db = getWritableDatabase();
        db.delete(tableName, keyId + " = ?", SqlStatement.whereArgs(objectId));
        db.close();
    }
}
