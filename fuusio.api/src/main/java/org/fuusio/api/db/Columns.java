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

public enum Columns implements ColumnDescriptor {

	COLUMN_ID("Id", "id", 0, ColumnDataType.INTEGER, true);
	
	private final String mLabel;
	private final String mName;
	private final int mIndex;
	private final ColumnDataType mType;
	private final boolean mKey;
	
	private Columns(final String label, final String name, final int index, final ColumnDataType type) {
		this(label, name, index, type, false);
	}
	
	private Columns(final String label, final String name, final int index, final ColumnDataType type, final boolean key) {
		mLabel = label;
		mName = name;
		mIndex = index;
		mType = type;
		mKey = key;
	}

	public static ColumnDescriptor[] getAllColumns() {
		return values();
	}
	
	public final String getLabel() {
		return mLabel;
	}

	public final String getName() {
		return mName;
	}

	public final int getIndex() {
		return mIndex;
	}
	
	public final ColumnDataType getType() {
		return mType;
	}

	public final boolean isKey() {
		return mKey;
	}

	public final String getSqlType() {
		return mType.getSqlType();
	}	
}
