/*
 * (C) Copyright 2009-2013 Marko Salmela (http://fuusio.org).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.fuusio.api.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

@SuppressLint("SimpleDateFormat")
public class LogFile {

	public final static String PREFIX_LOG_FILE = "Log_";
	
	protected final static String TYPE_DEBUG = "DEBUG - ";
	protected final static String TYPE_INFO = "INFO - ";
	protected final static String TYPE_WARNING = "WARNING - ";
	protected final static String TYPE_ERROR = "ERROR - ";
	protected final static String TYPE_WTF = "WTF - ";
	
	protected final static String DATE_FORMAT = "yyyyMMdd_HHmmss";
	protected final static String TIME_FORMAT = "HHmmss";
	
	protected static LogFile sInstance = null;
	
	protected SimpleDateFormat mTimeFormat;
	protected Date mCreatedDate;
	protected int mErrorCount;
	protected File mFile;
	protected FileWriter mWriter;
	protected boolean mUseInternalDirectory;

	private LogFile() {
		sInstance = this;
		mUseInternalDirectory = true;
	}
	
	private LogFile(final boolean pUseInternalDirectory) {
		this();
		mUseInternalDirectory = pUseInternalDirectory;
	}
	
	private LogFile(final Date pCreatedDate, final boolean pUseInternalDirectory) {
		this(pUseInternalDirectory);
		
		mCreatedDate = pCreatedDate;
		mErrorCount = 0;
		mTimeFormat =  new SimpleDateFormat(TIME_FORMAT);
	}
		
	public final Date getCreatedDate() {
		return mCreatedDate;
	}
	
	public static LogFile getInstance() {
		return sInstance;
	}

	public final File getFile() {
		return mFile;
	}	
	
	public final boolean isErrorDetected() {
		return (mErrorCount > 0);
	}
	
	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings("resource")
	public static LogFile start(final Context pContext, final String mAppName) {
		return start(pContext, mAppName, true);
	}
	
	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings("resource")
	public static LogFile start(final Context pContext, final String mAppName, final boolean pUseInternalDirectory) {

		final Date createdDate = new Date();
		final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		final StringBuilder fileName = new StringBuilder(PREFIX_LOG_FILE);
		fileName.append(mAppName);
		fileName.append("_");
		fileName.append(dateFormat.format(createdDate));
		fileName.append(".txt");
		
		
		final File directory = pUseInternalDirectory ? pContext.getFilesDir() : Environment.getExternalStorageDirectory();
		final File outputFile = new File(directory, fileName.toString());
		//FileWriter writer = null;
		
		if (!outputFile.exists()) {
		     try {
		    	 outputFile.createNewFile();
		    	 //writer = new FileWriter(outputFile);				
			} catch (final IOException pException) {
				System.out.println();
			}
		}
		
		final LogFile logFile = new LogFile(createdDate, pUseInternalDirectory);
		logFile.mFile = outputFile;
		logFile.mWriter = null;
		L.setLogFile(logFile);
		return logFile;
	}
	
	public static boolean stop() {
		if (sInstance != null) {
			
			L.setLogFile(null);
			
			if (sInstance.mWriter != null) {
				try {
					sInstance.mWriter.flush();
					sInstance.mWriter.close();
				} catch (IOException e) {
				}
			}
			
			sInstance.mFile = null;
			sInstance.mWriter = null;
			sInstance = null;
		}
		
		return false;
	}
	
    public void info(final String pTag, final String pMessage) {
    	write(TYPE_INFO, pTag, pMessage);
    }
    
    public void debug(final String pTag, final String pMessage) {
    	write(TYPE_DEBUG, pTag, pMessage);
    }

    public void error(final String pTag, final String pMessage) {
    	mErrorCount++;
    	write(TYPE_ERROR, pTag, pMessage);
    }
    
    public void warning(final String pTag, final String pMessage) {
    	write(TYPE_WARNING, pTag, pMessage);
    }

    public void wtf(final String pTag, final String pMessage) {
    	mErrorCount++;
    	write(TYPE_WTF, pTag, pMessage);
    }
    
    protected synchronized void write(final String mType, final String pTag, final String pMessage) {
    	if (mFile != null) {
	    	try {
	    		mWriter = new FileWriter(mFile, true);
	    		
	    		mWriter.append(mTimeFormat.format(new Date()));
	    		mWriter.append(" ");
	   			mWriter.append(mType);
				mWriter.append(pTag);
				mWriter.append(" : ");
				mWriter.append(pMessage);
				mWriter.append("\n");
				mWriter.flush();
				mWriter.close();
				mWriter = null;
			} catch (final IOException pException) {
				System.out.println(); //  REMOVE
			} 
    	}
    }

	public final int getErrorCount() {
		return mErrorCount;
	}    
}
