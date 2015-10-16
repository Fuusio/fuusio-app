package org.fuusio.api.rest.volley;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import org.fuusio.api.dependency.D;
import org.fuusio.api.graphics.BitmapManager;
import org.fuusio.api.rest.RequestManager;
import org.fuusio.api.rest.RestRequest;

/**
 * {@link VolleyRequestManager} implements {@link RequestManager} using the Volley Networking
 * framework.
 */
public class VolleyRequestManager implements RequestManager<VolleyRestRequest<AbstractRequest>> {

    private BitmapManager mBitmapManager;
    private ImageLoader.ImageCache mImageCache;
    private ImageLoader mImageLoader;
    private RequestQueue mRequestQueue;
    private UrlStack mUrlStack;
    public VolleyRequestManager() {
        mImageLoader = createImageLoader();
        mUrlStack = new UrlStack();
        mRequestQueue = Volley.newRequestQueue(D.get(Context.class), mUrlStack);
    }

    protected ImageLoader createImageLoader() {
        mBitmapManager = D.get(BitmapManager.class);

        mImageCache = new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(final String pKey) {
                return mBitmapManager.getBitmap(pKey);
            }

            @Override
            public void putBitmap(final String pKey, final Bitmap pBitmap) {
                mBitmapManager.addBitmap(pKey, pBitmap);
            }
        };

        return new ImageLoader(mRequestQueue, mImageCache);
    }

    protected ImageLoader getImageLoader() {
        return mImageLoader;
    }

    protected RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    protected int getRequestTimeout() {
        return 10000;
    }

    protected RetryPolicy createRetryPolicy() {
        return new DefaultRetryPolicy(getRequestTimeout(), DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }

    /**
     * Executes the given {@link RestRequest} by constructing it and by adding it to the request queue.
     *
     * @param pRequest A {@link RestRequest}.
     * @return The {@link RestRequest}.
     */
    @Override
    public <T extends VolleyRestRequest<AbstractRequest>> T execute(final VolleyRestRequest<AbstractRequest> pRequest) {
        pRequest.constructRequest();

        final AbstractRequest peerRequest = pRequest.getPeerRequest();
        peerRequest.setRetryPolicy(createRetryPolicy());
        getRequestQueue().add(peerRequest);
        return (T)pRequest;
    }

    @Override
    public void cancelPendingRequests(final Object pTag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(pTag);
        }
    }

    @Override
    public void cancelAllRequests() {
        mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(final Request<?> pRequest) {
                return true;
            }
        });
    }

    @Override
    public void clearRequestCache() {
        mRequestQueue.getCache().clear();
    }
}
