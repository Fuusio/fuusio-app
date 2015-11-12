package org.fuusio.api.rest;

import org.fuusio.api.component.Component;

/**
 * {@link RequestManager} define interface for a {@link Component} that is used to execute
 * {@link RestRequest}s.
 */
public interface RequestManager<T_Request extends RestRequest> extends Component {

    <T extends T_Request> T execute(final T_Request request);

    void cancelPendingRequests(final Object tag);

    void cancelAllRequests();

    void clearRequestCache();
}
