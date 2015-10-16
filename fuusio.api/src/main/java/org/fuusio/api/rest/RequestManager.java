package org.fuusio.api.rest;

import org.fuusio.api.component.Component;

/**
 * {@link RequestManager} define interface for a {@link Component} that is used to execute
 * {@link RestRequest}s.
 */
public interface RequestManager<T_Request extends RestRequest> extends Component {

    <T extends T_Request> T execute(final T_Request pRequest);

    void cancelPendingRequests(final Object pTag);

    void cancelAllRequests();

    void clearRequestCache();
}
