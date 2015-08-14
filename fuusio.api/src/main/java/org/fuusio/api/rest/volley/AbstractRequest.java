package org.fuusio.api.rest.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import org.fuusio.api.rest.HttpHeaders;
import org.fuusio.api.rest.HttpParams;

import java.util.Map;

/**
 * {@link AbstractRequest} provides an abstract base class for implementing Volley {@link Request} and
 * provides additional framework support for using Volley to implement Rest communication.
 * @param <T_Response> The generic type of the response object.
 */
public abstract class AbstractRequest<T_Response extends Object> extends Request<T_Response> {

    protected static final String PROTOCOL_CHARSET = "utf-8";

    protected final HttpHeaders mHeaders;

    protected HttpParams mParams;
    protected Response.Listener<T_Response> mResponseListener;

    protected AbstractRequest(final String pUrl, final Listener<T_Response> pResponseListener, final ErrorListener pErrorListener) {
        this(Method.GET, pUrl, pResponseListener, pErrorListener);
    }

    protected AbstractRequest(final int pMethod, String pUrl, final Listener<T_Response> pResponseListener, final ErrorListener pErrorListener) {
        super(pMethod, pUrl, pErrorListener);
        mResponseListener = pResponseListener;
        mHeaders = new HttpHeaders();
    }

    @Override
    protected abstract Response<T_Response> parseNetworkResponse(NetworkResponse pResponse);

    @Override
    protected void deliverResponse(final T_Response pResponse) {
        if (mResponseListener != null) {
            mResponseListener.onResponse(pResponse);
        }
    }

    public void addParam(final String pKey, final String pValue) {

        if (mParams != null) {
            mParams = new HttpParams(getParamsEncoding());
        }
        mParams.add(pKey, pValue);
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (mParams != null && mParams.getSize() > 0) {
            return mParams.encodeParameters(getParamsEncoding());
        }
        return null;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (mHeaders != null) {
            return mHeaders.getMap();
        }
        return null;
    }

    public void setParams(final HttpParams pParams) {
        mParams = pParams;
    }

    public void setHeaders(final HttpHeaders pHeaders) {
        mHeaders.clear();
        mHeaders.addAll(pHeaders);
    }
}
