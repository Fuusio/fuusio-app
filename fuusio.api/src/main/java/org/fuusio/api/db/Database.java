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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import org.fuusio.api.util.DateToolkit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;

public abstract class Database<T_DatabaseHelper extends DatabaseHelper> {

	protected final Context mContext;
	protected SQLiteDatabase mDb;
	protected final T_DatabaseHelper mHelper;
	protected boolean mOpen;
	
	protected Database(final Context context) {
		mContext = context;
		mHelper = createHelper(context);
		mOpen = false;
	}
	
	public final SQLiteDatabase getDb() {
		return mDb;
	}
	
	public final T_DatabaseHelper getHelper() {
		return mHelper;
	}
	
	protected abstract T_DatabaseHelper createHelper(final Context context);
	
	public SQLiteDatabase open() {
	    mDb = mHelper.getWritableDatabase();
		mOpen = (mDb != null);
		return mDb;
	}
	
	public void close() {
		mHelper.close();
		mOpen = false;
	}
	
	public static double toSqlValue(final float value) {
		return value;
	}
	
	public static int toSqlValue(final boolean value) {
		return value ? 1 : 0;
	}
	
	public static String toSqlValue(final Date date) {
		return DateToolkit.format(date);
	}

	public static boolean toBoolean(final int value) {
		return (value > 0);
	}
	
	public static Date toDate(final String value) {
		
		if (value != null) {
			try {
				return DateToolkit.parse(value);
			} catch (ParseException e) {
			}
		}
		
		return null;
	}
	
	public static byte[] toSqlValue(final Serializable serializable) {
		return toBlob(serializable);
	}
	
	public static <T> T toSerializable(final byte[] blop) {
		
		if (blop != null) {
			try {
				return fromBlob(blop);
			} catch (final Exception pException) {
				throw new IllegalStateException();
			}
		}
		
		return null;
	}
	
    @SuppressWarnings("unchecked")
	public static <T> T fromString(final String string) throws IOException, ClassNotFoundException {
        final byte [] data = Base64.decode(string, Base64.DEFAULT);
        final ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(data));
        final T object  = (T)inputStream.readObject();
        inputStream.close();
        return object;
    }

    public static String toString(final Serializable object) throws IOException {
        final ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        final ObjectOutputStream outputStream = new ObjectOutputStream(byteOutputStream);
        outputStream.writeObject(object);
        outputStream.close();
        return new String(Base64.encodeToString(byteOutputStream.toByteArray(), Base64.DEFAULT));
    }
    
    @SuppressWarnings("unchecked")
	public static <T> T fromBlob(final byte[] blop) {
       
        T object = null;
        
		try {
			final ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(blop));
			object = (T)inputStream.readObject();
			inputStream.close();
		} catch (final Exception pException) {
			throw new IllegalArgumentException();
		}
        
        return object;
    }

    public static byte[] toBlob(final Serializable object) {
        final ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
       
		try {
			final ObjectOutputStream outputStream = new ObjectOutputStream(byteOutputStream);
		    outputStream.writeObject(object);
		    outputStream.close();			 
		} catch (final IOException pException) {
		}

        return byteOutputStream.toByteArray();
    }	
}
