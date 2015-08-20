package org.fuusio.app;

import android.content.res.Resources;

import org.fuusio.api.app.ApplicationError;

public enum FuusioSampleAppError implements ApplicationError {

	ERROR_NONE(0, 0),
	
	// Native app errors
	
	ERROR_DB_FAILED_TO_OPEN_DB(100, R.string.error_db_failed_to_open_db),
	ERROR_DB_FAILED_TO_INSERT_USER(101, R.string.error_db_failed_to_insert_user),
	ERROR_DB_FAILED_TO_UPDATE_USER(102, R.string.error_db_failed_to_update_user),
    ERROR_DB_FAILED_TO_QUERY_USERS(103, R.string.error_db_failed_to_query_users),

	ERROR_UNKNOWN(Integer.MAX_VALUE, R.string.error_unknown_error);
	
	private final int mCode;
	private final int mMessageResId;

	String mMessage;

	private FuusioSampleAppError(final int pCode, final int pMessageResId) {
		mCode = pCode;
		mMessageResId = pMessageResId;
	}

	@Override
	public int getCode() {
		return mCode;
	}

	@Override
	public String getMessage(final Object... pArgs) {

		if (mMessage == null) {
			final Resources resources = FuusioSampleApp.getApplicationResources();
			mMessage = resources.getString(mMessageResId, pArgs);
		}
		return mMessage;
	}
}
