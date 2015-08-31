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
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import org.fuusio.api.util.L;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class GsonRequest<T> extends AbstractRequest<T> {

    private final Gson mGson;
    private final String mRequestBody;
    private final Class<T> mResponseClass;

    public GsonRequest(final String pUrl, final Class pResponseClass,
                       final Response.Listener pListener, final Response.ErrorListener pErrorListener){
        this(Method.GET, pUrl, null, pResponseClass, pListener, pErrorListener);
    }

    public GsonRequest(final int pMethod, final String pUrl, final Class pResponseClass,
                       final Response.Listener pListener, final Response.ErrorListener pErrorListener){
        this(pMethod, pUrl, null, pResponseClass, pListener, pErrorListener);
    }

    public GsonRequest(final int pMethod, final String pUrl, final Object pBody, final Class pResponseClass,
                             final Response.Listener pListener, final Response.ErrorListener pErrorListener){
        super(pMethod, pUrl, pListener, pErrorListener);

        mGson = new Gson();
        mResponseClass = pResponseClass;

        if (pBody!=null){
            mRequestBody = mGson.toJson(pBody);
        } else {
            mRequestBody = null;
        }
    }

    @Override
    public byte[] getBody(){
        try {
            return mRequestBody == null? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
        } catch (final UnsupportedEncodingException pException){
            L.wtf(this, "getBody", pException);
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, PROTOCOL_CHARSET);
            return null;
        }
    }

    @Override
    protected Response parseNetworkResponse(final NetworkResponse pResponse){
        try{
            final String jsonString = new String(pResponse.data, HttpHeaderParser.parseCharset(pResponse.headers));
            return Response.success(mGson.fromJson(jsonString, mResponseClass), HttpHeaderParser.parseCacheHeaders(pResponse));
        } catch (final UnsupportedEncodingException pException){
            return Response.error(new ParseError(pException));
        }
    }
}
