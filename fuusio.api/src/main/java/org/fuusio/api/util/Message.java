/**
 * Fuusio.org
 * 
 * Copyright (C) Marko Salmela 2013. All rights reserved.
 * 
 */

package org.fuusio.api.util;

/**
 * {@link Message} implements a generic object for passing a message with an identifier and optional
 * arguments.
 */
public class Message {

    private final int mId;
    private final Object[] mArgs;

    private Message(final int pId, final Object[] pArgs) {
        mId = pId;
        mArgs = pArgs;
    }

    public static Message create(final int pId, final Object... pArgs) {
        return new Message(pId, pArgs);
    }

    public final int getId() {
        return mId;
    }

    public final Object[] getArgs() {
        return mArgs;
    }
}
