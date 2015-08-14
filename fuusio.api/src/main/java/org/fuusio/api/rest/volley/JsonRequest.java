package org.fuusio.api.rest.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.UnsupportedEncodingException;

public class JsonRequest extends AbstractRequest<JsonElement> {

    public JsonRequest(final String pUrl, final Listener<JsonElement> pResponseListener, final ErrorListener pErrorListener) {
        super(pUrl, pResponseListener, pErrorListener);
    }

    public JsonRequest(final int pMethod, String pUrl, final Listener<JsonElement> pResponseListener, final ErrorListener pErrorListener) {
        super(pMethod, pUrl, pResponseListener, pErrorListener);
    }

    @Override
    protected Response<JsonElement> parseNetworkResponse(final NetworkResponse pResponse) {
        try {
            final String jsonString = new String(pResponse.data, HttpHeaderParser.parseCharset(pResponse.headers));
            final JsonElement jsonElement = new JsonParser().parse(jsonString);
            return Response.success(jsonElement, HttpHeaderParser.parseCacheHeaders(pResponse));
        } catch (final UnsupportedEncodingException pException) {
            return Response.error(new ParseError(pException));
        }
    }
}
