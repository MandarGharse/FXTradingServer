package com.fx.client.grpc;

import io.grpc.*;
import io.grpc.Metadata.Key;

public class SsoAuthClientInterceptor implements ClientInterceptor {

    private final String user;
    private final String credential;

    public SsoAuthClientInterceptor(String user, String credential) {
        this.user = user;
        this.credential = credential;
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor,
                                                               CallOptions callOptions, Channel channel) {
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(
                channel.newCall(methodDescriptor, callOptions)
        ) {
            @Override
            public void start(ClientCall.Listener<RespT> responseListener, Metadata headers) {
                headers.put(Key.of("user", Metadata.ASCII_STRING_MARSHALLER), user);
                headers.put(Key.of("credential", Metadata.ASCII_STRING_MARSHALLER), credential);
                super.start(responseListener, headers);
            }
        };
    }
}
