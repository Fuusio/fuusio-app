/*
 * Copyright (C) 2014 - 2015 Marko Salmela.
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
package org.fuusio.api.rest.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link AbstractRequest} provides an abstract base class for implementing Volley {@link Request} and
 * provides additional framework support for using Volley to implement Rest communication.
 * @param <T_Response> The generic type of the response object.
 */
public abstract class AbstractRequest<T_Response> extends Request<T_Response> {

    protected static final String PROTOCOL_CHARSET = "utf-8";

    protected final Map<String, String> mHeaders;

    protected Response.Listener<T_Response> mResponseListener;

    protected AbstractRequest(final String url, final Listener<T_Response> responseListener, final ErrorListener errorListener) {
        this(Method.GET, url, responseListener, errorListener);
    }

    protected AbstractRequest(final int pMethod, String url, final Listener<T_Response> responseListener, final ErrorListener errorListener) {
        super(pMethod, url, errorListener);
        mResponseListener = responseListener;
        mHeaders = new HashMap<>();
    }

    @Override
    protected abstract Response<T_Response> parseNetworkResponse(NetworkResponse response);

    @Override
    protected void deliverResponse(final T_Response response) {
        if (mResponseListener != null) {
            mResponseListener.onResponse(response);
        }
    }

    public abstract void setBody(Object body);

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (mHeaders != null) {
            return mHeaders;
        }
        return null;
    }

    public void setHeaders(final Map<String, String> headers) {
        mHeaders.clear();
        if (headers != null) {
            mHeaders.putAll(headers);
        }
    }

}
