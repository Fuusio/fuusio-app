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
package org.fuusio.api.util;

import java.io.IOException;
import java.io.Writer;

public class JavaWriter {

    public static final String ANNOTATION_OVERRIDE = "@Override";

    public enum Keyword {
        KEYWORD_ABSTRACT("abstract"),
        KEYWORD_CLASS("class"),
        KEYWORD_ELSE("else"),
        KEYWORD_EXTENDS("extends"),
        KEYWORD_FINAL("final"),
        KEYWORD_IF("if"),
        KEYWORD_IMPORT("import"),
        KEYWORD_IMPLEMENTS("implements"),
        KEYWORD_INTERFACE("interface"),
        KEYWORD_NULL("null"),
        KEYWORD_PACKAGE("package"),
        KEYWORD_PRIVATE("private"),
        KEYWORD_PROTECTED("protected"),
        KEYWORD_PUBLIC("public"),
        KEYWORD_RETURN("return"),
        KEYWORD_STATIC("static"),
        KEYWORD_SUPER("super"),
        KEYWORD_THIS("this");

        private final String mKeyword;

        private Keyword(final String pKeyword) {
            mKeyword = pKeyword;
        }

        public final String toString() {
            return mKeyword;
        }

        public void write(final JavaWriter pWriter) {
            pWriter.append(mKeyword);
            pWriter.append(" ");
        }
    }

    private static final String INDENTATION = "    ";

    private final Writer mWriter;

    private String mIndentation;
    private int mIndentationCount;

    public JavaWriter(final Writer pWriter) {
        mIndentation = INDENTATION;
        mWriter = pWriter;
        mIndentationCount = 0;
    }

    public final String getIndentation() {
        return mIndentation;
    }

    public void setIndentation(final String pIndentation) {
        mIndentation = pIndentation;
    }

    public final int getIndentationCount() {
        return mIndentationCount;
    }

    public void setIndentationCount(final int pCount) {
        mIndentationCount = pCount;
    }

    public JavaWriter a(final String pString) {
        try {
            mWriter.append(pString);
        } catch (final IOException pException) {
            L.e(this, "append", pException);
        }
        return this;
    }

    public JavaWriter append(final String pString) {
        try {
            mWriter.append(pString);
        } catch (final IOException pException) {
            L.e(this, "append", pException);
        }
        return this;
    }

    public JavaWriter append(final boolean pValue) {
        try {
            mWriter.append(Boolean.toString(pValue));
        } catch (final IOException pException) {
            L.e(this, "append", pException);
        }
        return this;
    }

    public JavaWriter append(final float pValue) {
        try {
            mWriter.append(Float.toString(pValue));
            mWriter.append('f');
        } catch (final IOException pException) {
            L.e(this, "append", pException);
        }
        return this;
    }

    public JavaWriter append(final long pValue) {
        try {
            mWriter.append(Long.toString(pValue));
            mWriter.append('L');
        } catch (final IOException pException) {
            L.e(this, "append", pException);
        }
        return this;
    }

    public JavaWriter append(final int pValue) {
        try {
            mWriter.append(Integer.toString(pValue));
        } catch (final IOException pException) {
            L.e(this, "append", pException);
        }
        return this;
    }

    public JavaWriter space() {
        try {
            mWriter.append(" ");
        } catch (final IOException pException) {
            L.e(this, "space", pException);
        }
        return this;
    }


    public JavaWriter intend() {
        try {
            for (int i = 0; i < mIndentationCount; i++) {
                mWriter.append(mIndentation);
            }
        } catch (final IOException pException) {
            L.e(this, "intend", pException);
        }
        return this;
    }

    public JavaWriter newLine() {
        return newLine(true);
    }

    public JavaWriter newLine(final boolean pIndent) {
        try {
            mWriter.append('\n');

            if (pIndent) {
                intend();
            }
        } catch (final IOException pException) {
            L.e(this, "newLine", pException);
        }
        return this;
    }

    public JavaWriter beginBlock() {
        try {
            mWriter.append('{');
            mWriter.append('\n');
            mIndentationCount++;
            intend();
        } catch (final IOException pException) {
            L.e(this, "beginBlock", pException);
        }
        return this;
    }

    public JavaWriter endBlock() {
        return endBlock(true);
    }

    public JavaWriter endBlock(final boolean pNewLine) {
        try {
            mIndentationCount--;
            intend();
            mWriter.append('}');

            if (pNewLine) {
                mWriter.append('\n');
            } else {
                mWriter.append(" ");
            }
        } catch (final IOException pException) {
            L.e(this, "endBlock", pException);
        }
        return this;
    }

    public JavaWriter openParenthesis() {
        try {
            mWriter.append('(');
        } catch (final IOException pException) {
            L.e(this, "openParenthesis", pException);
        }
        return this;
    }

    public JavaWriter closeParenthesis() {
        try {
            mWriter.append(')');
        } catch (final IOException pException) {
            L.e(this, "closeParenthesis", pException);
        }
        return this;
    }

    public JavaWriter endStatement() {
        return endStatement(true);
    }

    public JavaWriter endStatement(final boolean pIntend) {
        try {
            mWriter.append(';');

            mWriter.append('\n');
            if (pIntend) {
                intend();
            }
        } catch (final IOException pException) {
            L.e(this, "endBlock", pException);
        }
        return this;
    }

    public JavaWriter keyword(final Keyword pKeyword) {
        pKeyword.write(this);
        return this;
    }

    public JavaWriter writeImport(final String pImport) {
        Keyword.KEYWORD_IMPORT.write(this);
        append(pImport);
        endStatement();
        return this;
    }

    public JavaWriter writePackage(final String pPackageName) {
        Keyword.KEYWORD_PACKAGE.write(this);
        append(pPackageName);
        endStatement();
        return this;
    }

    public JavaWriter beginClass(final String pName) {
        return beginClass(pName, true);
    }

    public JavaWriter beginClass(final String pName, final boolean isPublic) {

        if (isPublic) {
            Keyword.KEYWORD_PUBLIC.write(this);
        } else {
            Keyword.KEYWORD_PRIVATE.write(this);
        }
        space();
        Keyword.KEYWORD_CLASS.write(this);
        space();
        append(pName);
        beginBlock();
        return this;
    }

    public JavaWriter endClass() {
        return endBlock();
    }
}
