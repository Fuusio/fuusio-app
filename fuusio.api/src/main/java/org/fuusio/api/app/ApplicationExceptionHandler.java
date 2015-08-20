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
