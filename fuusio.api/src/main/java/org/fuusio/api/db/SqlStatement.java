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

public class SqlStatement {

    protected final static String CREATE_TABLE = "CREATE TABLE";
    protected static final String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS";
    protected static final String KEY_ID = "_id";
    protected static final String PRIMARY_KEY = "PRIMARY KEY";
    protected static final String SELECT_FROM = "SELECT * FROM";
    protected static final String SPACE = " ";
    protected static final String WHERE = "WHERE";
    
    private final StringBuilder mBuilder;
    private boolean mClosingParenthesisNeeded;
    private boolean mFirstColumn;    
    private String mStatementString;

    private SqlStatement() {
        mBuilder = new StringBuilder();
        mFirstColumn = true;
        mClosingParenthesisNeeded = false;
    }

    public SqlStatement space() {
        mBuilder.append(' ');
        return this;
    }
    
    public SqlStatement append(final String stringValue) {
        mBuilder.append(stringValue);
        mBuilder.append(' ');
        return this;
    }

    public SqlStatement add(final int intValue) {
        mBuilder.append(Integer.toString(intValue));
        mBuilder.append(' ');
        return this;
    }

    public SqlStatement add(final float floatValue) {
        mBuilder.append(Float.toString(floatValue));
        mBuilder.append(' ');
        return this;
    }

    public SqlStatement openParenthesis() {
        mBuilder.append('(');
        return this;
    }

    public SqlStatement closeParenthesis() {
        mBuilder.append(')');
        return this;
    }

    public SqlStatement addComma() {
        mBuilder.append(',');
        return this;
    }

    public static SqlStatement newCreateTable(final String tableName) {
        final SqlStatement statement = new SqlStatement();
        statement.mClosingParenthesisNeeded = true;
        statement.append(CREATE_TABLE);
        statement.append(tableName);
        statement.openParenthesis();
        return statement;
    }

    public static SqlStatement newCreateTable(final String tableName, final TableDescriptor tableDescriptor) {
        final SqlStatement statement = new SqlStatement();
        statement.mClosingParenthesisNeeded = true;
        statement.append(CREATE_TABLE);
        statement.append(tableName);
        statement.openParenthesis();
        
        for (final ColumnDescriptor columnDescriptor : tableDescriptor.getColumnDescriptors()) {
        	statement.addColumn(columnDescriptor);
        }
        
        return statement;
    }
    
    public SqlStatement addIntegerColumn(final String columnName) {
        return addIntegerColumn(columnName, false);
    }

    public SqlStatement addColumn(final ColumnDescriptor descriptor) {

        if (mFirstColumn) {
            mFirstColumn = false;
            append(",");
        }

        append(descriptor.getName());
        append(descriptor.getSqlType());

        if (descriptor.isKey()) {
            append(PRIMARY_KEY);
        }

        return this;
    }
    
        
    public SqlStatement addIntegerColumn(final String columnName, final boolean primaryKey) {

        if (mFirstColumn) {
            mFirstColumn = false;
            append(",");
        }

        append(columnName);
        append(ColumnDataType.INTEGER.getSqlType());

        if (primaryKey) {
            append(PRIMARY_KEY);
        }

        return this;
    }

    public SqlStatement addRealColumn(final String columnName) {

        if (mFirstColumn) {
            mFirstColumn = false;
            append(",");
        }

        append(columnName);
        append(ColumnDataType.REAL.getSqlType());
        return this;
    }

    public SqlStatement addTextColumn(final String columnName) {

        if (mFirstColumn) {
            mFirstColumn = false;
            mBuilder.append(",");
        }

        append(columnName);
        append(ColumnDataType.TEXT.getSqlType());
        return this;
    }

    @Override
    public String toString() {

        if (mStatementString == null) {

            if (mClosingParenthesisNeeded) {
                mClosingParenthesisNeeded = false;
                closeParenthesis();
            }

            mStatementString = mBuilder.toString();
            mBuilder.setLength(0);
        }

        return mStatementString;
    }

    public SqlStatement addPrimaryKeyColumn(final String columnName) {
        return addIntegerColumn(columnName, true);
    }

	public final String execute() {
		return toString();
	}

	public static SqlStatement create(final TableDescriptor tableDescriptor) {
		return newCreateTable(tableDescriptor.getName(), tableDescriptor);
	}
	
	public static String[] whereArgs(final Object... values) {
		final int count = values.length;
		final String[] wheraArgs = new String[count];
		
		for (int  i = count - 1; i >= 0; i--) {
			wheraArgs[i] = (values[i] != null) ? values[i].toString() : null;
		}
    	return wheraArgs;
    }	
}
