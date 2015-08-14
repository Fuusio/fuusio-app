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

import java.util.Collection;

import org.fuusio.api.model.ModelObjectMetaInfo;
import org.fuusio.api.model.Property;

public class ModelObjectTableDescriptor implements TableDescriptor {

	private final ColumnDescriptor[] mColumnDescriptors;
	private final ModelObjectMetaInfo mObjectMetaInfo;
	
	
	public ModelObjectTableDescriptor(final ModelObjectMetaInfo pObjectMetaInfo) {
		mColumnDescriptors = createColumnDescriptors(pObjectMetaInfo);
		mObjectMetaInfo = pObjectMetaInfo;
	}
	
	@Override
	public ColumnDescriptor[] getColumnDescriptors() {
		return mColumnDescriptors;
	}

	@Override
	public String getName() {
		final Class<?> objectClass = mObjectMetaInfo.getObjectClass();
		final String className = objectClass.getSimpleName();
		final int index = className.lastIndexOf('.');
		return (index > 0) ? className.substring(index + 1) : className;
	}

	public static ColumnDescriptor[] createColumnDescriptors(final ModelObjectMetaInfo pObjectMetaInfo) {
		final Collection<Property> properties = pObjectMetaInfo.getProperties();
		final int count = properties.size();
		final ColumnDescriptor[] descriptors = new ColumnDescriptor[count];
		
		int index = 0;
		
		for (final Property property : properties) {
			descriptors[index++] = new PropertyColumnDescriptor(property);
		}
		
		return descriptors;
	}

	public ColumnDescriptor getColumnDescriptor(int columnIndex) {
		
		for (final ColumnDescriptor columnDescriptor : mColumnDescriptors) {
			if (columnDescriptor.getIndex() == columnIndex) {
				return columnDescriptor;
			}
		}
		
		return null;
	}

}
