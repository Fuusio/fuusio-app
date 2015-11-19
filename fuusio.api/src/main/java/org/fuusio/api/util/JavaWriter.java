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

        Keyword(final String keyword) {
            mKeyword = keyword;
        }

        public final String toString() {
            return mKeyword;
        }

        public void write(final JavaWriter writer) {
            writer.append(mKeyword);
            writer.append(" ");
        }
    }

    private static final String INDENTATION = "    ";

    private final Writer mWriter;

    private String mIndentation;
    private int mIndentationCount;

    public JavaWriter(final Writer writer) {
        mIndentation = INDENTATION;
        mWriter = writer;
        mIndentationCount = 0;
    }

    public final String getIndentation() {
        return mIndentation;
    }

    public void setIndentation(final String indentation) {
        mIndentation = indentation;
    }

    public final int getIndentationCount() {
        return mIndentationCount;
    }

    public void setIndentationCount(final int count) {
        mIndentationCount = count;
    }

    public JavaWriter a(final String string) {
        try {
            mWriter.append(string);
        } catch (IOException e) {
            L.e(this, "a", e);
        }
        return this;
    }

    public JavaWriter append(final String string) {
        try {
            mWriter.append(string);
        } catch (IOException e) {
            L.e(this, "append", e);
        }
        return this;
    }

    public JavaWriter append(final boolean value) {
        try {
            mWriter.append(Boolean.toString(value));
        } catch (IOException e) {
            L.e(this, "append", e);
        }
        return this;
    }

    public JavaWriter append(final float value) {
        try {
            mWriter.append(Float.toString(value));
            mWriter.append('f');
        } catch (IOException e) {
            L.e(this, "append", e);
        }
        return this;
    }

    public JavaWriter append(final long value) {
        try {
            mWriter.append(Long.toString(value));
            mWriter.append('L');
        } catch (IOException e) {
            L.e(this, "append", e);
        }
        return this;
    }

    public JavaWriter append(final int value) {
        try {
            mWriter.append(Integer.toString(value));
        } catch (IOException e) {
            L.e(this, "append", e);
        }
        return this;
    }

    public JavaWriter space() {
        try {
            mWriter.append(' ');
        } catch (IOException e) {
            L.e(this, "space", e);
        }
        return this;
    }


    public JavaWriter intend() {
        try {
            for (int i = 0; i < mIndentationCount; i++) {
                mWriter.append(mIndentation);
            }
        } catch (IOException e) {
            L.e(this, "intend", e);
        }
        return this;
    }

    public JavaWriter newLine() {
        return newLine(true);
    }

    public JavaWriter newLine(final boolean indented) {
        try {
            mWriter.append('\n');

            if (indented) {
                intend();
            }
        } catch (IOException e) {
            L.e(this, "newLine", e);
        }
        return this;
    }

    public JavaWriter beginBlock() {
        try {
            mWriter.append('{');
            mWriter.append('\n');
            mIndentationCount++;
            intend();
        } catch (IOException e) {
            L.e(this, "beginBlock", e);
        }
        return this;
    }

    public JavaWriter endBlock() {
        return endBlock(true);
    }

    public JavaWriter endBlock(final boolean newLine) {
        try {
            mIndentationCount--;
            intend();
            mWriter.append('}');

            if (newLine) {
                mWriter.append('\n');
            } else {
                mWriter.append(" ");
            }
        } catch (IOException e) {
            L.e(this, "endBlock", e);
        }
        return this;
    }

    public JavaWriter openParenthesis() {
        try {
            mWriter.append('(');
        } catch (IOException e) {
            L.e(this, "openParenthesis", e);
        }
        return this;
    }

    public JavaWriter closeParenthesis() {
        try {
            mWriter.append(')');
        } catch (IOException e) {
            L.e(this, "closeParenthesis", e);
        }
        return this;
    }

    public JavaWriter endStatement() {
        return endStatement(true);
    }

    public JavaWriter endStatement(final boolean intented) {
        try {
            mWriter.append(';');

            mWriter.append('\n');
            if (intented) {
                intend();
            }
        } catch (IOException e) {
            L.e(this, "endBlock", e);
        }
        return this;
    }

    public JavaWriter keyword(final Keyword keyword) {
        keyword.write(this);
        return this;
    }

    public JavaWriter writeImport(final String packageName) {
        Keyword.KEYWORD_IMPORT.write(this);
        append(packageName);
        endStatement();
        return this;
    }

    public JavaWriter writePackage(final String packageName) {
        Keyword.KEYWORD_PACKAGE.write(this);
        append(packageName);
        endStatement();
        return this;
    }

    public JavaWriter beginClass(final String name) {
        return beginClass(name, true);
    }

    public JavaWriter beginClass(final String name, final boolean isPublic) {
        return beginClass(name, null, isPublic);
    }

    public JavaWriter beginClass(final String name, final String superClass, final boolean isPublic) {

        if (isPublic) {
            Keyword.KEYWORD_PUBLIC.write(this);
        } else {
            Keyword.KEYWORD_PRIVATE.write(this);
        }
        Keyword.KEYWORD_CLASS.write(this);
        append(name);

        if (superClass != null) {
            Keyword.KEYWORD_EXTENDS.write(this);
            append(superClass);
        }
        space();
        beginBlock();
        return this;
    }

    public JavaWriter endClass() {
        return endBlock();
    }
}
