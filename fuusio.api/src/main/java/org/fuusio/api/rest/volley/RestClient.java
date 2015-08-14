package org.fuusio.api.rest.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public abstract class RestClient {

    private final RequestQueue mRequestQueue;
    private final Context mContext;

    protected RestClient(final Context pContext) {
        mContext = pContext;
        mRequestQueue = Volley.newRequestQueue(pContext);
    }

    public void executeRequest(final Request<?> pRequest) {
        mRequestQueue.add(pRequest);
    }

}
