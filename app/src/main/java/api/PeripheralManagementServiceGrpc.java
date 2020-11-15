package api;

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
    value = "by gRPC proto compiler (version 1.34.0-SNAPSHOT)",
    comments = "Source: peripheral.proto")
public final class PeripheralManagementServiceGrpc {

  private PeripheralManagementServiceGrpc() {}

  public static final String SERVICE_NAME = "api.PeripheralManagementService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<api.PeripheralOuterClass.GetPeripheralRequest,
      api.PeripheralOuterClass.Peripheral> getGetPeripheralMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetPeripheral",
      requestType = api.PeripheralOuterClass.GetPeripheralRequest.class,
      responseType = api.PeripheralOuterClass.Peripheral.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<api.PeripheralOuterClass.GetPeripheralRequest,
      api.PeripheralOuterClass.Peripheral> getGetPeripheralMethod() {
    io.grpc.MethodDescriptor<api.PeripheralOuterClass.GetPeripheralRequest, api.PeripheralOuterClass.Peripheral> getGetPeripheralMethod;
    if ((getGetPeripheralMethod = PeripheralManagementServiceGrpc.getGetPeripheralMethod) == null) {
      synchronized (PeripheralManagementServiceGrpc.class) {
        if ((getGetPeripheralMethod = PeripheralManagementServiceGrpc.getGetPeripheralMethod) == null) {
          PeripheralManagementServiceGrpc.getGetPeripheralMethod = getGetPeripheralMethod =
              io.grpc.MethodDescriptor.<api.PeripheralOuterClass.GetPeripheralRequest, api.PeripheralOuterClass.Peripheral>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetPeripheral"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  api.PeripheralOuterClass.GetPeripheralRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  api.PeripheralOuterClass.Peripheral.getDefaultInstance()))
              .build();
        }
      }
    }
    return getGetPeripheralMethod;
  }

  private static volatile io.grpc.MethodDescriptor<api.PeripheralOuterClass.NewPeripheral,
      api.PeripheralOuterClass.Peripheral> getCreatePeripheralMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreatePeripheral",
      requestType = api.PeripheralOuterClass.NewPeripheral.class,
      responseType = api.PeripheralOuterClass.Peripheral.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<api.PeripheralOuterClass.NewPeripheral,
      api.PeripheralOuterClass.Peripheral> getCreatePeripheralMethod() {
    io.grpc.MethodDescriptor<api.PeripheralOuterClass.NewPeripheral, api.PeripheralOuterClass.Peripheral> getCreatePeripheralMethod;
    if ((getCreatePeripheralMethod = PeripheralManagementServiceGrpc.getCreatePeripheralMethod) == null) {
      synchronized (PeripheralManagementServiceGrpc.class) {
        if ((getCreatePeripheralMethod = PeripheralManagementServiceGrpc.getCreatePeripheralMethod) == null) {
          PeripheralManagementServiceGrpc.getCreatePeripheralMethod = getCreatePeripheralMethod =
              io.grpc.MethodDescriptor.<api.PeripheralOuterClass.NewPeripheral, api.PeripheralOuterClass.Peripheral>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreatePeripheral"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  api.PeripheralOuterClass.NewPeripheral.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  api.PeripheralOuterClass.Peripheral.getDefaultInstance()))
              .build();
        }
      }
    }
    return getCreatePeripheralMethod;
  }

  private static volatile io.grpc.MethodDescriptor<api.PeripheralOuterClass.Peripheral,
      api.EmptyOuterClass.Empty> getRemovePeripheralMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RemovePeripheral",
      requestType = api.PeripheralOuterClass.Peripheral.class,
      responseType = api.EmptyOuterClass.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<api.PeripheralOuterClass.Peripheral,
      api.EmptyOuterClass.Empty> getRemovePeripheralMethod() {
    io.grpc.MethodDescriptor<api.PeripheralOuterClass.Peripheral, api.EmptyOuterClass.Empty> getRemovePeripheralMethod;
    if ((getRemovePeripheralMethod = PeripheralManagementServiceGrpc.getRemovePeripheralMethod) == null) {
      synchronized (PeripheralManagementServiceGrpc.class) {
        if ((getRemovePeripheralMethod = PeripheralManagementServiceGrpc.getRemovePeripheralMethod) == null) {
          PeripheralManagementServiceGrpc.getRemovePeripheralMethod = getRemovePeripheralMethod =
              io.grpc.MethodDescriptor.<api.PeripheralOuterClass.Peripheral, api.EmptyOuterClass.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RemovePeripheral"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  api.PeripheralOuterClass.Peripheral.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  api.EmptyOuterClass.Empty.getDefaultInstance()))
              .build();
        }
      }
    }
    return getRemovePeripheralMethod;
  }

  private static volatile io.grpc.MethodDescriptor<api.PeripheralOuterClass.GetDeploymentPeripheralsRequest,
      api.PeripheralOuterClass.Peripheral> getGetDeploymentPeripheralsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetDeploymentPeripherals",
      requestType = api.PeripheralOuterClass.GetDeploymentPeripheralsRequest.class,
      responseType = api.PeripheralOuterClass.Peripheral.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<api.PeripheralOuterClass.GetDeploymentPeripheralsRequest,
      api.PeripheralOuterClass.Peripheral> getGetDeploymentPeripheralsMethod() {
    io.grpc.MethodDescriptor<api.PeripheralOuterClass.GetDeploymentPeripheralsRequest, api.PeripheralOuterClass.Peripheral> getGetDeploymentPeripheralsMethod;
    if ((getGetDeploymentPeripheralsMethod = PeripheralManagementServiceGrpc.getGetDeploymentPeripheralsMethod) == null) {
      synchronized (PeripheralManagementServiceGrpc.class) {
        if ((getGetDeploymentPeripheralsMethod = PeripheralManagementServiceGrpc.getGetDeploymentPeripheralsMethod) == null) {
          PeripheralManagementServiceGrpc.getGetDeploymentPeripheralsMethod = getGetDeploymentPeripheralsMethod =
              io.grpc.MethodDescriptor.<api.PeripheralOuterClass.GetDeploymentPeripheralsRequest, api.PeripheralOuterClass.Peripheral>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetDeploymentPeripherals"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  api.PeripheralOuterClass.GetDeploymentPeripheralsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  api.PeripheralOuterClass.Peripheral.getDefaultInstance()))
              .build();
        }
      }
    }
    return getGetDeploymentPeripheralsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static PeripheralManagementServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PeripheralManagementServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PeripheralManagementServiceStub>() {
        @java.lang.Override
        public PeripheralManagementServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PeripheralManagementServiceStub(channel, callOptions);
        }
      };
    return PeripheralManagementServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static PeripheralManagementServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PeripheralManagementServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PeripheralManagementServiceBlockingStub>() {
        @java.lang.Override
        public PeripheralManagementServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PeripheralManagementServiceBlockingStub(channel, callOptions);
        }
      };
    return PeripheralManagementServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static PeripheralManagementServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PeripheralManagementServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PeripheralManagementServiceFutureStub>() {
        @java.lang.Override
        public PeripheralManagementServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PeripheralManagementServiceFutureStub(channel, callOptions);
        }
      };
    return PeripheralManagementServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class PeripheralManagementServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void getPeripheral(api.PeripheralOuterClass.GetPeripheralRequest request,
        io.grpc.stub.StreamObserver<api.PeripheralOuterClass.Peripheral> responseObserver) {
      asyncUnimplementedUnaryCall(getGetPeripheralMethod(), responseObserver);
    }

    /**
     */
    public void createPeripheral(api.PeripheralOuterClass.NewPeripheral request,
        io.grpc.stub.StreamObserver<api.PeripheralOuterClass.Peripheral> responseObserver) {
      asyncUnimplementedUnaryCall(getCreatePeripheralMethod(), responseObserver);
    }

    /**
     */
    public void removePeripheral(api.PeripheralOuterClass.Peripheral request,
        io.grpc.stub.StreamObserver<api.EmptyOuterClass.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getRemovePeripheralMethod(), responseObserver);
    }

    /**
     */
    public void getDeploymentPeripherals(api.PeripheralOuterClass.GetDeploymentPeripheralsRequest request,
        io.grpc.stub.StreamObserver<api.PeripheralOuterClass.Peripheral> responseObserver) {
      asyncUnimplementedUnaryCall(getGetDeploymentPeripheralsMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetPeripheralMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                api.PeripheralOuterClass.GetPeripheralRequest,
                api.PeripheralOuterClass.Peripheral>(
                  this, METHODID_GET_PERIPHERAL)))
          .addMethod(
            getCreatePeripheralMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                api.PeripheralOuterClass.NewPeripheral,
                api.PeripheralOuterClass.Peripheral>(
                  this, METHODID_CREATE_PERIPHERAL)))
          .addMethod(
            getRemovePeripheralMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                api.PeripheralOuterClass.Peripheral,
                api.EmptyOuterClass.Empty>(
                  this, METHODID_REMOVE_PERIPHERAL)))
          .addMethod(
            getGetDeploymentPeripheralsMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                api.PeripheralOuterClass.GetDeploymentPeripheralsRequest,
                api.PeripheralOuterClass.Peripheral>(
                  this, METHODID_GET_DEPLOYMENT_PERIPHERALS)))
          .build();
    }
  }

  /**
   */
  public static final class PeripheralManagementServiceStub extends io.grpc.stub.AbstractAsyncStub<PeripheralManagementServiceStub> {
    private PeripheralManagementServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PeripheralManagementServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PeripheralManagementServiceStub(channel, callOptions);
    }

    /**
     */
    public void getPeripheral(api.PeripheralOuterClass.GetPeripheralRequest request,
        io.grpc.stub.StreamObserver<api.PeripheralOuterClass.Peripheral> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetPeripheralMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void createPeripheral(api.PeripheralOuterClass.NewPeripheral request,
        io.grpc.stub.StreamObserver<api.PeripheralOuterClass.Peripheral> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCreatePeripheralMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void removePeripheral(api.PeripheralOuterClass.Peripheral request,
        io.grpc.stub.StreamObserver<api.EmptyOuterClass.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRemovePeripheralMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getDeploymentPeripherals(api.PeripheralOuterClass.GetDeploymentPeripheralsRequest request,
        io.grpc.stub.StreamObserver<api.PeripheralOuterClass.Peripheral> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getGetDeploymentPeripheralsMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class PeripheralManagementServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<PeripheralManagementServiceBlockingStub> {
    private PeripheralManagementServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PeripheralManagementServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PeripheralManagementServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public api.PeripheralOuterClass.Peripheral getPeripheral(api.PeripheralOuterClass.GetPeripheralRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetPeripheralMethod(), getCallOptions(), request);
    }

    /**
     */
    public api.PeripheralOuterClass.Peripheral createPeripheral(api.PeripheralOuterClass.NewPeripheral request) {
      return blockingUnaryCall(
          getChannel(), getCreatePeripheralMethod(), getCallOptions(), request);
    }

    /**
     */
    public api.EmptyOuterClass.Empty removePeripheral(api.PeripheralOuterClass.Peripheral request) {
      return blockingUnaryCall(
          getChannel(), getRemovePeripheralMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<api.PeripheralOuterClass.Peripheral> getDeploymentPeripherals(
        api.PeripheralOuterClass.GetDeploymentPeripheralsRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getGetDeploymentPeripheralsMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class PeripheralManagementServiceFutureStub extends io.grpc.stub.AbstractFutureStub<PeripheralManagementServiceFutureStub> {
    private PeripheralManagementServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PeripheralManagementServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PeripheralManagementServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<api.PeripheralOuterClass.Peripheral> getPeripheral(
        api.PeripheralOuterClass.GetPeripheralRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetPeripheralMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<api.PeripheralOuterClass.Peripheral> createPeripheral(
        api.PeripheralOuterClass.NewPeripheral request) {
      return futureUnaryCall(
          getChannel().newCall(getCreatePeripheralMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<api.EmptyOuterClass.Empty> removePeripheral(
        api.PeripheralOuterClass.Peripheral request) {
      return futureUnaryCall(
          getChannel().newCall(getRemovePeripheralMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_PERIPHERAL = 0;
  private static final int METHODID_CREATE_PERIPHERAL = 1;
  private static final int METHODID_REMOVE_PERIPHERAL = 2;
  private static final int METHODID_GET_DEPLOYMENT_PERIPHERALS = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final PeripheralManagementServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(PeripheralManagementServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_PERIPHERAL:
          serviceImpl.getPeripheral((api.PeripheralOuterClass.GetPeripheralRequest) request,
              (io.grpc.stub.StreamObserver<api.PeripheralOuterClass.Peripheral>) responseObserver);
          break;
        case METHODID_CREATE_PERIPHERAL:
          serviceImpl.createPeripheral((api.PeripheralOuterClass.NewPeripheral) request,
              (io.grpc.stub.StreamObserver<api.PeripheralOuterClass.Peripheral>) responseObserver);
          break;
        case METHODID_REMOVE_PERIPHERAL:
          serviceImpl.removePeripheral((api.PeripheralOuterClass.Peripheral) request,
              (io.grpc.stub.StreamObserver<api.EmptyOuterClass.Empty>) responseObserver);
          break;
        case METHODID_GET_DEPLOYMENT_PERIPHERALS:
          serviceImpl.getDeploymentPeripherals((api.PeripheralOuterClass.GetDeploymentPeripheralsRequest) request,
              (io.grpc.stub.StreamObserver<api.PeripheralOuterClass.Peripheral>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (PeripheralManagementServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .addMethod(getGetPeripheralMethod())
              .addMethod(getCreatePeripheralMethod())
              .addMethod(getRemovePeripheralMethod())
              .addMethod(getGetDeploymentPeripheralsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
