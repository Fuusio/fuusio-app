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
import java.util.Date;

import org.fuusio.api.model.ModelObject;
import org.fuusio.api.model.Property;

public enum ColumnDataType {
	
	NULL("NULL"),
	BOOLEAN("INTEGER"),
	DATE("TEXT"),
	DOUBLE("REAL"),
	FLOAT("REAL"),
	INTEGER("INTEGER"),
	LONG("INTEGER"),
	REAL("REAL"),
	TEXT("TEXT"),
	MODEL_OBJECT("TEXT"),
	SERIALIZABLE("BLOB"),
	BLOB("BLOB");
	
	private final String mSqlType;
	
	private ColumnDataType(final String sqlType) {
		mSqlType =  sqlType;
	}

	public final String getSqlType() {
		return mSqlType;
	}

	public static ColumnDataType getForProperty(final Property property) {
		final Class<?> type = property.getType();
		
		if (type.equals(Boolean.class) || type.equals(Boolean.TYPE)) {
			return BOOLEAN;
		} else if (type.equals(Date.class)) {
			return DATE;
		} else if (type.equals(Double.class) || type.equals(Double.TYPE)) {
			return DOUBLE;
		} else if (type.equals(Float.class) || type.equals(Float.TYPE)) {
			return FLOAT;
		} else if (type.equals(Integer.class) || type.equals(Integer.TYPE)) {
			return INTEGER;
		} else if (type.equals(Long.class) || type.equals(Long.TYPE)) {
			return LONG;
		}else if (type.equals(String.class)) {
			return TEXT;
		} else if (ModelObject.class.isAssignableFrom(type)) {
			return MODEL_OBJECT;
		} else if (Serializable.class.isAssignableFrom(type)) {
			return SERIALIZABLE;
		}
		else {
			throw new IllegalArgumentException();
		}
	}	
	    
}	

