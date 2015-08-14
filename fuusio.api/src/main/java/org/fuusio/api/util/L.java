package org.fuusio.api.util;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

public class L extends Object {

    private static LogFile sLogFile = null;

    public static int i(final Object pObject, final String pMethod,
                        final String pMessage) {
        final String tag = createTag(pObject, pMethod);

        if (sLogFile != null) {
            sLogFile.info(tag, pMessage);
        }
        return Log.i(tag, pMessage);
    }

    public static int d(final Object pObject, final String pMethod,
                        final String pMessage) {
        final String tag = createTag(pObject, pMethod);

        if (sLogFile != null) {
            sLogFile.debug(tag, pMessage);
        }
        return Log.d(tag, pMessage);
    }

    public static int e(final Object pObject, final String pMethod,
                        final String pMessage) {
        final String tag = createTag(pObject, pMethod);

        if (sLogFile != null) {
            sLogFile.error(tag, pMessage);
        }
        return Log.e(tag, pMessage);
    }

    public static void e(final Object pObject, final String pMethod, final Exception pException) {
        L.e(pObject, pMethod, pException.getMessage());

        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new  PrintWriter(stringWriter);
        pException.printStackTrace(printWriter);
        printWriter.flush();
        L.e(pObject, pMethod, printWriter.toString());
        printWriter.close();
    }

    public static int w(final Object pObject, final String pMethod,
                        final String pMessage) {
        final String tag = createTag(pObject, pMethod);

        if (sLogFile != null) {
            sLogFile.warning(tag, pMessage);
        }
        return Log.w(createTag(pObject, pMethod), pMessage);
    }

    public static int wtf(final Object pObject, final String pMethod,
                          final String pMessage) {
        final String tag = createTag(pObject, pMethod);

        if (sLogFile != null) {
            sLogFile.wtf(tag, pMessage);
        }
        return Log.wtf(createTag(pObject, pMethod), pMessage);
    }

    public static String createTag(final Object pObject, final String pMethod) {
        final StringBuilder tag = new StringBuilder(pObject.getClass()
                .getSimpleName());
        tag.append('.');
        tag.append(pMethod);
        return tag.toString();
    }

    public static void wtf(final Object pObject, final String pMethod,
                           final Exception pException) {
        L.wtf(pObject, pMethod, pException.getMessage());
        pException.printStackTrace();
    }

    public static void setLogFile(final LogFile pLogFile) {
        sLogFile = pLogFile;
    }

    public static void writeMessage(final String pMessage) {
        if (sLogFile != null) {
            sLogFile.write("", "", pMessage);
        }
    }
}
