package org.fuusio.api.rest.volley;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.fuusio.api.rest.HttpHeaders;
import org.fuusio.api.rest.HttpMethod;
import org.fuusio.api.rest.HttpParams;
import org.fuusio.api.rest.RequestListener;
import org.fuusio.api.rest.RestRequest;
import org.fuusio.api.util.KeyValue;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public abstract class VolleyRestRequest<T_Response> extends RestRequest<T_Response, AbstractRequest<T_Response>> {

    protected final Response.ErrorListener mErrorListener;
    protected final Response.Listener<T_Response> mResponseListener;

    protected VolleyRestRequest(final String relativeUrl, final RequestListener<T_Response> requestListener) {
        this(HttpMethod.GET, relativeUrl, requestListener);
    }

    protected VolleyRestRequest(final HttpMethod method, final String relativeUrl, final RequestListener<T_Response> requestListener) {
        super(method, relativeUrl, requestListener);


        mErrorListener = createErrorListener(requestListener);
        mResponseListener = createResponseListener(requestListener);
    }

    protected void initializeRequest(AbstractRequest<T_Response> request) {
        request.setTag(getClass().getSimpleName());

        // If GET method we set the params that are used to define query params. If in rare case
        // where POST method has query params (in addition to body) the query params are handled in
        // RestRequest#composeUrl(HttpParams)
        /*
        if (isGet()) {
            mVolleyRequest.setParams(mParams);
        }*/

        // If POST method then add the body
        if (isPost()) {
            request.setBody(mBody);
        }

        // Set the headers
        request.setHeaders(getHeaders().getMap());
    }

    protected final AbstractRequest<T_Response> createRequest() {
        return createRequest(mResponseListener, mErrorListener);
    }
    protected abstract AbstractRequest<T_Response> createRequest(Response.Listener<T_Response> responseListener, Response.ErrorListener errorListener);

    protected Response.Listener<T_Response> createResponseListener(final RequestListener<T_Response> requestListener) {
        return new Response.Listener<T_Response>() {
            @Override
            public void onResponse(final T_Response response) {
                Log.d("VolleyRestRequest", "onResponse");
                requestListener.onResponse(response);
            }
        };
    }

    protected Response.ErrorListener createErrorListener(final RequestListener<T_Response> requestListener) {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(final VolleyError error) {
                Log.d("VolleyRestRequest", "onError");
                requestListener.onError(error);
            }
        };
    }
}