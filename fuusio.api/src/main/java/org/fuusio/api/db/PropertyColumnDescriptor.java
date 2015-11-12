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

import org.fuusio.api.model.Property;

public class PropertyColumnDescriptor implements ColumnDescriptor {

	private final Property mProperty;
	private final ColumnDataType mColumnType;
	
	public PropertyColumnDescriptor(final Property property) {
		mProperty = property;
		mColumnType = ColumnDataType.getForProperty(property);
	}
	
	@Override
	public String getLabel() {
		return mProperty.getName();
	}

	@Override
	public String getName() {
		return mProperty.getName();
	}

	@Override
	public int getIndex() {
		return mProperty.getColumnIndex();
	}

	@Override
	public String getSqlType() {
		return mColumnType.getSqlType();
	}

	@Override
	public ColumnDataType getType() {
		return mColumnType;
	}

	@Override
	public boolean isKey() {
		return mProperty.isKey();
	}

}
