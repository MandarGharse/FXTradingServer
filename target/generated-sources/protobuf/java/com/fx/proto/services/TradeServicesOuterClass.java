// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: com/fx/proto/services/TradeServices.proto

package com.fx.proto.services;

public final class TradeServicesOuterClass {
  private TradeServicesOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n)com/fx/proto/services/TradeServices.pr" +
      "oto\022\025com.fx.proto.services\032*com/fx/proto" +
      "/messaging/TradeMessages.proto2\234\001\n\rTrade" +
      "Services\022\212\001\n\017SubscribeTrades\0228.com.fx.pr" +
      "oto.messaging.TradesSubscriptionRequestM" +
      "essage\0329.com.fx.proto.messaging.TradesSu" +
      "bscriptionResponseMessage(\0010\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.fx.proto.messaging.TradeMessages.getDescriptor(),
        });
    com.fx.proto.messaging.TradeMessages.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
