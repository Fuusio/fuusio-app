package org.fuusio.app;

import org.fuusio.api.app.ApplicationExceptionHandler;

public class FuusioSampleAppExceptionHandler extends ApplicationExceptionHandler<FuusioSampleAppError> {

	private FuusioSampleAppExceptionHandler(final Thread.UncaughtExceptionHandler pDefaultHandler) {
        super(pDefaultHandler);
	}

    @Override
    protected FuusioSampleAppError getAppSpecificUnknownError() {
        return FuusioSampleAppError.ERROR_UNKNOWN;
    }


}
