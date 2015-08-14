/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.fuusio.api.rest;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * This socket factory will create ssl socket that accepts self signed certificate
 * 
 * @author olamy
 * @version $Id: EasySSLSocketFactory.java 765355 2009-04-15 20:59:07Z evenisse $
 * @since 1.2.3
 */
public class EasySSLSocketFactory implements LayeredSocketFactory {

    private SSLContext sslcontext = null;

    private static SSLContext createEasySSLContext() throws IOException {
        try {
            final SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new TrustManager[] { new EasyX509TrustManager(null) }, null);
            return context;
        } catch (final Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    private SSLContext getSSLContext() throws IOException {
        if (sslcontext == null) {
            sslcontext = createEasySSLContext();
        }
        return sslcontext;
    }

    /**
     * @see org.apache.http.conn.scheme.SocketFactory#connectSocket(java.net.Socket,
     *      java.lang.String, int, java.net.InetAddress, int, org.apache.http.params.HttpParams)
     */
    @Override
    public Socket connectSocket(final Socket pSock, final String pHost, final int pPort,
            final InetAddress pLocalAddress, final int pLocalPort, final HttpParams pParams)
            throws IOException, UnknownHostException, ConnectTimeoutException {
        final int connTimeout = HttpConnectionParams.getConnectionTimeout(pParams);
        final int soTimeout = HttpConnectionParams.getSoTimeout(pParams);
        final InetSocketAddress remoteAddress = new InetSocketAddress(pHost, pPort);
        final SSLSocket sslsock = (SSLSocket) ((pSock != null) ? pSock : createSocket());
        int port = pLocalPort;

        if ((pLocalAddress != null) || (pLocalPort > 0)) {
            // we need to bind explicitly
            if (port < 0) {
                port = 0; // indicates "any"
            }
            final InetSocketAddress localSocketAddress = new InetSocketAddress(pLocalAddress, port);
            sslsock.bind(localSocketAddress);
        }

        sslsock.connect(remoteAddress, connTimeout);
        sslsock.setSoTimeout(soTimeout);
        return sslsock;

    }

    /**
     * @see org.apache.http.conn.scheme.SocketFactory#createSocket()
     */
    @Override
    public Socket createSocket() throws IOException {
        return getSSLContext().getSocketFactory().createSocket();
    }

    /**
     * @see org.apache.http.conn.scheme.SocketFactory#isSecure(java.net.Socket)
     */
    @Override
    public boolean isSecure(final Socket socket) throws IllegalArgumentException {
        return true;
    }

    /**
     * @see org.apache.http.conn.scheme.LayeredSocketFactory#createSocket(java.net.Socket,
     *      java.lang.String, int, boolean)
     */
    @Override
    public Socket createSocket(final Socket socket, final String host, final int port,
            final boolean autoClose) throws IOException, UnknownHostException {
        // return getSSLContext().getSocketFactory().createSocket();
        return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    // -------------------------------------------------------------------
    // javadoc in org.apache.http.conn.scheme.SocketFactory says :
    // Both Object.equals() and Object.hashCode() must be overridden
    // for the correct operation of some connection managers
    // -------------------------------------------------------------------

    @Override
    public boolean equals(final Object obj) {
        return ((obj != null) && obj.getClass().equals(EasySSLSocketFactory.class));
    }

    @Override
    public int hashCode() {
        return EasySSLSocketFactory.class.hashCode();
    }

}