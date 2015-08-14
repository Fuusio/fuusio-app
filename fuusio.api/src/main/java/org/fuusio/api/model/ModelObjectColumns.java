/*
 * Copyright (C) 2001 - 2015 Marko Salmela, http://fuusio.org
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
package org.fuusio.api.model;

import android.provider.BaseColumns;

public interface ModelObjectColumns extends BaseColumns {

    /**
     * The identifier for the data.
     */
    String ID = _ID;

    /**
     * The timestamp for when the data was created
     * <P>
     * Type: INTEGER (long from System.curentTimeMillis())
     * </P>
     */
    String CREATED_DATE = "Created";

    /**
     * The timestamp for when the data was last modified
     * <P>
     * Type: INTEGER (long from System.curentTimeMillis())
     * </P>
     */
    String MODIFIED_DATE = "Modified";

    /**
     * Gets the column index corresponding the given property name.
     * 
     * @param property The property name as a {@link String}.
     * @return The column index as an {@code int}. Returns -1 if no matching column is found.
     */
    // public int getColumnIndex(String property);
}
