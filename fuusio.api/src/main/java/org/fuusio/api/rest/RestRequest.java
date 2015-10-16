package org.fuusio.api.rest;

import org.fuusio.api.util.KeyValue;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public abstract class RestRequest<T_Response, T_PeerRequest> {

    protected static final String DEFAULT_PARAMS_ENCODING = "UTF-8";
    protected static final String PATH_SEPARATOR = "/";

    protected final HttpHeaders mHeaders;
    protected final HttpParams mParams;
    protected final HttpParams mPathParams;

    protected Object mBody;
    protected HttpMethod mMethod;
    protected T_PeerRequest mPeerRequest;
    protected String mRelativeUrl;
    protected RequestListener<T_Response> mRequestListener;

    protected RestRequest(final String pRelativeUrl, final RequestListener<T_Response> pRequestListener) {
        this(HttpMethod.GET, pRelativeUrl, pRequestListener);
    }

    protected RestRequest(final HttpMethod pMethod, final String pRelativeUrl, final RequestListener<T_Response> pRequestListener) {
        mMethod = pMethod;
        mRelativeUrl = pRelativeUrl;
        mRequestListener = pRequestListener;
        mHeaders = new HttpHeaders();
        mPathParams = new HttpParams(getParamsEncoding());
        mParams = new HttpParams(getParamsEncoding());
    }

    protected abstract String getBaseUrl();

    protected String getParamsEncoding() {
        return DEFAULT_PARAMS_ENCODING;
    }

    public String getRelativeUrl() {
        return mRelativeUrl;
    }

    public T_PeerRequest getPeerRequest() {
        return mPeerRequest;
    }

    public int getMethodCode() {
        return mMethod.getMethodCode();
    }

    /**
     * Construct the request url.
     *
     * @return The constructed url as a {@link String}.
     */
    protected String constructUrl() {

        final String baseUrl = getBaseUrl();
        final StringBuilder builder = new StringBuilder(baseUrl);
        final String relativeUrl = getRelativeUrl();

        if (!baseUrl.endsWith(PATH_SEPARATOR)) {
            if (!relativeUrl.startsWith(PATH_SEPARATOR)) {
                builder.append(PATH_SEPARATOR);
            }
            builder.append(relativeUrl);
        } else {
            if (relativeUrl.startsWith(PATH_SEPARATOR)) {
                builder.append(relativeUrl.substring(1));
            } else {
                builder.append(relativeUrl);
            }
        }

        if (hasQueryParams()) {
            builder.append('?');
            getParams().encodeParameters(builder);
        }

        // Process path parameters
        String url = builder.toString();

        if (mPathParams != null) {
            try {
                final List<KeyValue<String, String>> keyValues = mPathParams.getKeyValues();

                for (final KeyValue<String, String> keyValue : keyValues) {
                    final String key = "{" + keyValue.getKey() + "}";
                    final String value = URLEncoder.encode(keyValue.getValue(), HttpParams.DEFAULT_ENCODING);
                    url = url.replace(key, value);
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Failed to encode path parameter.", e);
            }
        }

        return url;
    }

    public final void constructRequest() {
        mPeerRequest = createRequest();
        initializeRequest(mPeerRequest);
    }

    protected abstract T_PeerRequest createRequest();

    protected abstract void initializeRequest(T_PeerRequest pRequest);

    public boolean hasQueryParams() {
        return (mParams != null && mParams.getSize() > 0);
    }

    /**
     * Set the body of the request. The given object is assumed to be a POJO that can be converted
     * by GSON to JSON.
     *
     * @param pBody A POJO as an {@link Object}.
     */
    public void setBody(final Object pBody) {
        mBody = pBody;
    }

    /**
     * Add the specified query parameter with the given value.
     *
     * @param pKey The name of the parameter to be added as a {@link String}.
     * @param pValue The value of the parameter.
     */
    public RestRequest addParam(final String pKey, final String pValue) {
        mParams.add(pKey, pValue);
        return this;
    }

    public RestRequest addParam(final String pKey, final boolean pValue) {
        mParams.add(pKey, Boolean.toString(pValue));
        return this;
    }

    public RestRequest addParam(final String pKey, final float pValue) {
        mParams.add(pKey, Float.toString(pValue));
        return this;
    }

    public RestRequest addParam(final String pKey, final int pValue) {
        mParams.add(pKey, Integer.toString(pValue));
        return this;
    }

    public RestRequest addParam(final String pKey, final long pValue) {
        mParams.add(pKey, Long.toString(pValue));
        return this;
    }

    /**
     * Add the specified path parameter with the given value.
     *
     * @param pKey The name of the parameter to be added as a {@link String}.
     * @param pValue The value of the parameter.
     */
    public RestRequest addPathParam(final String pKey, final String pValue) {
        mPathParams.add(pKey, pValue);
        return this;
    }

    public RestRequest addPathParam(final String pKey, final boolean pValue) {
        mPathParams.add(pKey, Boolean.toString(pValue));
        return this;
    }

    public RestRequest addPathParam(final String pKey, final float pValue) {
        mPathParams.add(pKey, Float.toString(pValue));
        return this;
    }

    public RestRequest addPathParam(final String pKey, final int pValue) {
        mPathParams.add(pKey, Integer.toString(pValue));
        return this;
    }

    public RestRequest addPathParam(final String pKey, final long pValue) {
        mPathParams.add(pKey, Long.toString(pValue));
        return this;
    }


    public RestRequest addHeader(final String pField, final String pValue) {
        mHeaders.add(pField, pValue);
        return this;
    }

    public final HttpHeaders getHeaders() {
        return mHeaders;
    }

    public final HttpParams getParams() {
        return mParams;
    }

    public void setParams(final HttpParams pParams) {
        mParams.clear();
        mParams.addAll(pParams);
    }

    public final HttpParams getPathParams() {
        return mPathParams;
    }

    public void setPathParams(final HttpParams pParams) {
        mPathParams.clear();
        mPathParams.addAll(pParams);
    }

    public final RequestListener<T_Response> getRequestListener() {
        return mRequestListener;
    }

    public boolean isDelete() {
        return (mMethod == HttpMethod.DELETE);
    }

    public boolean isGet() {
        return (mMethod == HttpMethod.GET);
    }

    public boolean isPost() {
        return (mMethod == HttpMethod.POST);
    }

    public boolean isPut() {
        return (mMethod == HttpMethod.PUT);
    }
}