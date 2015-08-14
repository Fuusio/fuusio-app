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
    
    public SqlStatement append(final String pString) {
        mBuilder.append(pString);
        mBuilder.append(' ');
        return this;
    }

    public SqlStatement add(final int pInt) {
        mBuilder.append(Integer.toString(pInt));
        mBuilder.append(' ');
        return this;
    }

    public SqlStatement add(final float pFloat) {
        mBuilder.append(Float.toString(pFloat));
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

    public static SqlStatement newCreateTable(final String pTableName) {
        final SqlStatement statement = new SqlStatement();
        statement.mClosingParenthesisNeeded = true;
        statement.append(CREATE_TABLE);
        statement.append(pTableName);
        statement.openParenthesis();
        return statement;
    }

    public static SqlStatement newCreateTable(final String pTableName, final TableDescriptor pTableDescriptor) {
        final SqlStatement statement = new SqlStatement();
        statement.mClosingParenthesisNeeded = true;
        statement.append(CREATE_TABLE);
        statement.append(pTableName);
        statement.openParenthesis();
        
        for (final ColumnDescriptor columnDescriptor : pTableDescriptor.getColumnDescriptors()) {
        	statement.addColumn(columnDescriptor);
        }
        
        return statement;
    }
    
    public SqlStatement addIntegerColumn(final String pColumnName) {
        return addIntegerColumn(pColumnName, false);
    }

    public SqlStatement addColumn(final ColumnDescriptor pDescriptor) {

        if (mFirstColumn) {
            mFirstColumn = false;
            append(",");
        }

        append(pDescriptor.getName());
        append(pDescriptor.getSqlType());

        if (pDescriptor.isKey()) {
            append(PRIMARY_KEY);
        }

        return this;
    }
    
        
    public SqlStatement addIntegerColumn(final String pColumnName, final boolean pPrimaryKey) {

        if (mFirstColumn) {
            mFirstColumn = false;
            append(",");
        }

        append(pColumnName);
        append(ColumnDataType.INTEGER.getSqlType());

        if (pPrimaryKey) {
            append(PRIMARY_KEY);
        }

        return this;
    }

    public SqlStatement addRealColumn(final String pColumnName) {

        if (mFirstColumn) {
            mFirstColumn = false;
            append(",");
        }

        append(pColumnName);
        append(ColumnDataType.REAL.getSqlType());
        return this;
    }

    public SqlStatement addTextColumn(final String pColumnName) {

        if (mFirstColumn) {
            mFirstColumn = false;
            mBuilder.append(",");
        }

        append(pColumnName);
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

    public SqlStatement addPrimaryKeyColumn(final String pColumnName) {
        return addIntegerColumn(pColumnName, true);
    }

	public final String execute() {
		return toString();
	}

	public static SqlStatement create(final TableDescriptor pTableDescriptor) {
		return newCreateTable(pTableDescriptor.getName(), pTableDescriptor);
	}
	
	public static String[] whereArgs(final Object... pValues) {
		final int count = pValues.length;
		final String[] wheraArgs = new String[count];
		
		for (int  i = count - 1; i >= 0; i--) {
			wheraArgs[i] = (pValues[i] != null) ? pValues[i].toString() : null;
		}
    	return wheraArgs;
    }	
}
