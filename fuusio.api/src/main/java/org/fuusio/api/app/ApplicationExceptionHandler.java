package org.fuusio.api.app;

import java.lang.Thread.UncaughtExceptionHandler;

import org.fuusio.api.util.L;

import android.util.Log;

public abstract class ApplicationExceptionHandler<T_ApplicationError extends ApplicationError> implements UncaughtExceptionHandler {

    private static ApplicationExceptionHandler sInstance = null;

	protected ApplicationExceptionHandler() {
        sInstance = this;
	}
	
	@Override
	public void uncaughtException(final Thread pThread, final Throwable pException) {
		createLogEntryFor(pThread, getUnknownError(), pException);
	}
	
	public static void createLogEntryFor(final Object pObject, final Throwable pException) {
		createLogEntryFor(pObject, getUnknownError(), pException);
	}

	public static void createLogEntryFor(final Object pObject, final String pMessage) {
		createLogEntryFor(pObject, getUnknownError(), pMessage);
	}
	
	public static void createLogEntryFor(final Object pObject, final ApplicationError pError, final String pMessage) {
		if (pMessage != null) {
			L.wtf(pObject, "ERROR - Error:", pMessage);			
		}
	}

	public static void createLogEntryFor(final Object pObject, final ApplicationError pError, final Throwable pException) {
		if (pException != null) {
			final String stackTrace = Log.getStackTraceString(pException); 
			
			L.wtf(pObject, "ERROR - UncaughtException", pException.getMessage());
			L.wtf(pObject, "ERROR - Stack Trace", stackTrace);				
		}
	}

    protected static ApplicationError getUnknownError() {
       return sInstance.getAppSpecificUnknownError();
    }

    protected abstract T_ApplicationError getAppSpecificUnknownError();

}
