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

public enum Tables implements TableDescriptor, TablesDescriptor {

    TABLE_FOO("Foo", null);

    private final String mName;
    private final ColumnDescriptor[] mColumnDescriptors;

    private Tables(final String name, final ColumnDescriptor[] columnDescriptors) {
        mName = name;
        mColumnDescriptors = columnDescriptors;
    }

    public final ColumnDescriptor[] getColumnDescriptors() {
        return mColumnDescriptors;
    }

    public final String getName() {
        return mName;
    }

    @Override
    public TableDescriptor[] getTableDescriptors() {
        return values();
    }
}
