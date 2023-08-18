package com.fx.proto.services;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.34.0)",
    comments = "Source: com/fx/proto/services/TradeServices.proto")
public final class TradeServicesGrpc {

  private TradeServicesGrpc() {}

  public static final String SERVICE_NAME = "com.fx.proto.services.TradeServices";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.fx.proto.messaging.TradeMessages.TradesSubscriptionRequestMessage,
      com.fx.proto.messaging.TradeMessages.TradesSubscriptionResponseMessage> getSubscribeTradesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SubscribeTrades",
      requestType = com.fx.proto.messaging.TradeMessages.TradesSubscriptionRequestMessage.class,
      responseType = com.fx.proto.messaging.TradeMessages.TradesSubscriptionResponseMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<com.fx.proto.messaging.TradeMessages.TradesSubscriptionRequestMessage,
      com.fx.proto.messaging.TradeMessages.TradesSubscriptionResponseMessage> getSubscribeTradesMethod() {
    io.grpc.MethodDescriptor<com.fx.proto.messaging.TradeMessages.TradesSubscriptionRequestMessage, com.fx.proto.messaging.TradeMessages.TradesSubscriptionResponseMessage> getSubscribeTradesMethod;
    if ((getSubscribeTradesMethod = TradeServicesGrpc.getSubscribeTradesMethod) == null) {
      synchronized (TradeServicesGrpc.class) {
        if ((getSubscribeTradesMethod = TradeServicesGrpc.getSubscribeTradesMethod) == null) {
          TradeServicesGrpc.getSubscribeTradesMethod = getSubscribeTradesMethod =
              io.grpc.MethodDescriptor.<com.fx.proto.messaging.TradeMessages.TradesSubscriptionRequestMessage, com.fx.proto.messaging.TradeMessages.TradesSubscriptionResponseMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SubscribeTrades"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fx.proto.messaging.TradeMessages.TradesSubscriptionRequestMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.fx.proto.messaging.TradeMessages.TradesSubscriptionResponseMessage.getDefaultInstance()))
              .setSchemaDescriptor(new TradeServicesMethodDescriptorSupplier("SubscribeTrades"))
              .build();
        }
      }
    }
    return getSubscribeTradesMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TradeServicesStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TradeServicesStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TradeServicesStub>() {
        @java.lang.Override
        public TradeServicesStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TradeServicesStub(channel, callOptions);
        }
      };
    return TradeServicesStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TradeServicesBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TradeServicesBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TradeServicesBlockingStub>() {
        @java.lang.Override
        public TradeServicesBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TradeServicesBlockingStub(channel, callOptions);
        }
      };
    return TradeServicesBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TradeServicesFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<TradeServicesFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<TradeServicesFutureStub>() {
        @java.lang.Override
        public TradeServicesFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new TradeServicesFutureStub(channel, callOptions);
        }
      };
    return TradeServicesFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class TradeServicesImplBase implements io.grpc.BindableService {

    /**
     */
    public io.grpc.stub.StreamObserver<com.fx.proto.messaging.TradeMessages.TradesSubscriptionRequestMessage> subscribeTrades(
        io.grpc.stub.StreamObserver<com.fx.proto.messaging.TradeMessages.TradesSubscriptionResponseMessage> responseObserver) {
      return asyncUnimplementedStreamingCall(getSubscribeTradesMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSubscribeTradesMethod(),
            asyncBidiStreamingCall(
              new MethodHandlers<
                com.fx.proto.messaging.TradeMessages.TradesSubscriptionRequestMessage,
                com.fx.proto.messaging.TradeMessages.TradesSubscriptionResponseMessage>(
                  this, METHODID_SUBSCRIBE_TRADES)))
          .build();
    }
  }

  /**
   */
  public static final class TradeServicesStub extends io.grpc.stub.AbstractAsyncStub<TradeServicesStub> {
    private TradeServicesStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TradeServicesStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TradeServicesStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.fx.proto.messaging.TradeMessages.TradesSubscriptionRequestMessage> subscribeTrades(
        io.grpc.stub.StreamObserver<com.fx.proto.messaging.TradeMessages.TradesSubscriptionResponseMessage> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(getSubscribeTradesMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   */
  public static final class TradeServicesBlockingStub extends io.grpc.stub.AbstractBlockingStub<TradeServicesBlockingStub> {
    private TradeServicesBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TradeServicesBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TradeServicesBlockingStub(channel, callOptions);
    }
  }

  /**
   */
  public static final class TradeServicesFutureStub extends io.grpc.stub.AbstractFutureStub<TradeServicesFutureStub> {
    private TradeServicesFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TradeServicesFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new TradeServicesFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_SUBSCRIBE_TRADES = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final TradeServicesImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(TradeServicesImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SUBSCRIBE_TRADES:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.subscribeTrades(
              (io.grpc.stub.StreamObserver<com.fx.proto.messaging.TradeMessages.TradesSubscriptionResponseMessage>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class TradeServicesBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TradeServicesBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.fx.proto.services.TradeServicesOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("TradeServices");
    }
  }

  private static final class TradeServicesFileDescriptorSupplier
      extends TradeServicesBaseDescriptorSupplier {
    TradeServicesFileDescriptorSupplier() {}
  }

  private static final class TradeServicesMethodDescriptorSupplier
      extends TradeServicesBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    TradeServicesMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (TradeServicesGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TradeServicesFileDescriptorSupplier())
              .addMethod(getSubscribeTradesMethod())
              .build();
        }
      }
    }
    return result;
  }
}
