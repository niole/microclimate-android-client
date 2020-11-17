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
    comments = "Source: deployment.proto")
public final class DeploymentManagementServiceGrpc {

  private DeploymentManagementServiceGrpc() {}

  public static final String SERVICE_NAME = "api.DeploymentManagementService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<api.DeploymentOuterClass.GetDeploymentsForUserRequest,
      api.DeploymentOuterClass.Deployment> getGetDeploymentsForUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetDeploymentsForUser",
      requestType = api.DeploymentOuterClass.GetDeploymentsForUserRequest.class,
      responseType = api.DeploymentOuterClass.Deployment.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<api.DeploymentOuterClass.GetDeploymentsForUserRequest,
      api.DeploymentOuterClass.Deployment> getGetDeploymentsForUserMethod() {
    io.grpc.MethodDescriptor<api.DeploymentOuterClass.GetDeploymentsForUserRequest, api.DeploymentOuterClass.Deployment> getGetDeploymentsForUserMethod;
    if ((getGetDeploymentsForUserMethod = DeploymentManagementServiceGrpc.getGetDeploymentsForUserMethod) == null) {
      synchronized (DeploymentManagementServiceGrpc.class) {
        if ((getGetDeploymentsForUserMethod = DeploymentManagementServiceGrpc.getGetDeploymentsForUserMethod) == null) {
          DeploymentManagementServiceGrpc.getGetDeploymentsForUserMethod = getGetDeploymentsForUserMethod =
              io.grpc.MethodDescriptor.<api.DeploymentOuterClass.GetDeploymentsForUserRequest, api.DeploymentOuterClass.Deployment>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetDeploymentsForUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  api.DeploymentOuterClass.GetDeploymentsForUserRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  api.DeploymentOuterClass.Deployment.getDefaultInstance()))
              .build();
        }
      }
    }
    return getGetDeploymentsForUserMethod;
  }

  private static volatile io.grpc.MethodDescriptor<api.DeploymentOuterClass.NewDeployment,
      api.DeploymentOuterClass.Deployment> getCreateDeploymentMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateDeployment",
      requestType = api.DeploymentOuterClass.NewDeployment.class,
      responseType = api.DeploymentOuterClass.Deployment.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<api.DeploymentOuterClass.NewDeployment,
      api.DeploymentOuterClass.Deployment> getCreateDeploymentMethod() {
    io.grpc.MethodDescriptor<api.DeploymentOuterClass.NewDeployment, api.DeploymentOuterClass.Deployment> getCreateDeploymentMethod;
    if ((getCreateDeploymentMethod = DeploymentManagementServiceGrpc.getCreateDeploymentMethod) == null) {
      synchronized (DeploymentManagementServiceGrpc.class) {
        if ((getCreateDeploymentMethod = DeploymentManagementServiceGrpc.getCreateDeploymentMethod) == null) {
          DeploymentManagementServiceGrpc.getCreateDeploymentMethod = getCreateDeploymentMethod =
              io.grpc.MethodDescriptor.<api.DeploymentOuterClass.NewDeployment, api.DeploymentOuterClass.Deployment>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateDeployment"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  api.DeploymentOuterClass.NewDeployment.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  api.DeploymentOuterClass.Deployment.getDefaultInstance()))
              .build();
        }
      }
    }
    return getCreateDeploymentMethod;
  }

  private static volatile io.grpc.MethodDescriptor<api.DeploymentOuterClass.GetDeploymentRequest,
      api.DeploymentOuterClass.Deployment> getGetDeploymentMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetDeployment",
      requestType = api.DeploymentOuterClass.GetDeploymentRequest.class,
      responseType = api.DeploymentOuterClass.Deployment.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<api.DeploymentOuterClass.GetDeploymentRequest,
      api.DeploymentOuterClass.Deployment> getGetDeploymentMethod() {
    io.grpc.MethodDescriptor<api.DeploymentOuterClass.GetDeploymentRequest, api.DeploymentOuterClass.Deployment> getGetDeploymentMethod;
    if ((getGetDeploymentMethod = DeploymentManagementServiceGrpc.getGetDeploymentMethod) == null) {
      synchronized (DeploymentManagementServiceGrpc.class) {
        if ((getGetDeploymentMethod = DeploymentManagementServiceGrpc.getGetDeploymentMethod) == null) {
          DeploymentManagementServiceGrpc.getGetDeploymentMethod = getGetDeploymentMethod =
              io.grpc.MethodDescriptor.<api.DeploymentOuterClass.GetDeploymentRequest, api.DeploymentOuterClass.Deployment>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetDeployment"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  api.DeploymentOuterClass.GetDeploymentRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  api.DeploymentOuterClass.Deployment.getDefaultInstance()))
              .build();
        }
      }
    }
    return getGetDeploymentMethod;
  }

  private static volatile io.grpc.MethodDescriptor<api.DeploymentOuterClass.RemoveDeploymentRequest,
      api.EmptyOuterClass.Empty> getRemoveDeploymentMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RemoveDeployment",
      requestType = api.DeploymentOuterClass.RemoveDeploymentRequest.class,
      responseType = api.EmptyOuterClass.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<api.DeploymentOuterClass.RemoveDeploymentRequest,
      api.EmptyOuterClass.Empty> getRemoveDeploymentMethod() {
    io.grpc.MethodDescriptor<api.DeploymentOuterClass.RemoveDeploymentRequest, api.EmptyOuterClass.Empty> getRemoveDeploymentMethod;
    if ((getRemoveDeploymentMethod = DeploymentManagementServiceGrpc.getRemoveDeploymentMethod) == null) {
      synchronized (DeploymentManagementServiceGrpc.class) {
        if ((getRemoveDeploymentMethod = DeploymentManagementServiceGrpc.getRemoveDeploymentMethod) == null) {
          DeploymentManagementServiceGrpc.getRemoveDeploymentMethod = getRemoveDeploymentMethod =
              io.grpc.MethodDescriptor.<api.DeploymentOuterClass.RemoveDeploymentRequest, api.EmptyOuterClass.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RemoveDeployment"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  api.DeploymentOuterClass.RemoveDeploymentRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  api.EmptyOuterClass.Empty.getDefaultInstance()))
              .build();
        }
      }
    }
    return getRemoveDeploymentMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DeploymentManagementServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DeploymentManagementServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DeploymentManagementServiceStub>() {
        @java.lang.Override
        public DeploymentManagementServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DeploymentManagementServiceStub(channel, callOptions);
        }
      };
    return DeploymentManagementServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DeploymentManagementServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DeploymentManagementServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DeploymentManagementServiceBlockingStub>() {
        @java.lang.Override
        public DeploymentManagementServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DeploymentManagementServiceBlockingStub(channel, callOptions);
        }
      };
    return DeploymentManagementServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DeploymentManagementServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DeploymentManagementServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DeploymentManagementServiceFutureStub>() {
        @java.lang.Override
        public DeploymentManagementServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DeploymentManagementServiceFutureStub(channel, callOptions);
        }
      };
    return DeploymentManagementServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class DeploymentManagementServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void getDeploymentsForUser(api.DeploymentOuterClass.GetDeploymentsForUserRequest request,
        io.grpc.stub.StreamObserver<api.DeploymentOuterClass.Deployment> responseObserver) {
      asyncUnimplementedUnaryCall(getGetDeploymentsForUserMethod(), responseObserver);
    }

    /**
     */
    public void createDeployment(api.DeploymentOuterClass.NewDeployment request,
        io.grpc.stub.StreamObserver<api.DeploymentOuterClass.Deployment> responseObserver) {
      asyncUnimplementedUnaryCall(getCreateDeploymentMethod(), responseObserver);
    }

    /**
     */
    public void getDeployment(api.DeploymentOuterClass.GetDeploymentRequest request,
        io.grpc.stub.StreamObserver<api.DeploymentOuterClass.Deployment> responseObserver) {
      asyncUnimplementedUnaryCall(getGetDeploymentMethod(), responseObserver);
    }

    /**
     */
    public void removeDeployment(api.DeploymentOuterClass.RemoveDeploymentRequest request,
        io.grpc.stub.StreamObserver<api.EmptyOuterClass.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getRemoveDeploymentMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetDeploymentsForUserMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                api.DeploymentOuterClass.GetDeploymentsForUserRequest,
                api.DeploymentOuterClass.Deployment>(
                  this, METHODID_GET_DEPLOYMENTS_FOR_USER)))
          .addMethod(
            getCreateDeploymentMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                api.DeploymentOuterClass.NewDeployment,
                api.DeploymentOuterClass.Deployment>(
                  this, METHODID_CREATE_DEPLOYMENT)))
          .addMethod(
            getGetDeploymentMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                api.DeploymentOuterClass.GetDeploymentRequest,
                api.DeploymentOuterClass.Deployment>(
                  this, METHODID_GET_DEPLOYMENT)))
          .addMethod(
            getRemoveDeploymentMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                api.DeploymentOuterClass.RemoveDeploymentRequest,
                api.EmptyOuterClass.Empty>(
                  this, METHODID_REMOVE_DEPLOYMENT)))
          .build();
    }
  }

  /**
   */
  public static final class DeploymentManagementServiceStub extends io.grpc.stub.AbstractAsyncStub<DeploymentManagementServiceStub> {
    private DeploymentManagementServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DeploymentManagementServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DeploymentManagementServiceStub(channel, callOptions);
    }

    /**
     */
    public void getDeploymentsForUser(api.DeploymentOuterClass.GetDeploymentsForUserRequest request,
        io.grpc.stub.StreamObserver<api.DeploymentOuterClass.Deployment> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getGetDeploymentsForUserMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void createDeployment(api.DeploymentOuterClass.NewDeployment request,
        io.grpc.stub.StreamObserver<api.DeploymentOuterClass.Deployment> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCreateDeploymentMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getDeployment(api.DeploymentOuterClass.GetDeploymentRequest request,
        io.grpc.stub.StreamObserver<api.DeploymentOuterClass.Deployment> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetDeploymentMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void removeDeployment(api.DeploymentOuterClass.RemoveDeploymentRequest request,
        io.grpc.stub.StreamObserver<api.EmptyOuterClass.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRemoveDeploymentMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class DeploymentManagementServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<DeploymentManagementServiceBlockingStub> {
    private DeploymentManagementServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DeploymentManagementServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DeploymentManagementServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<api.DeploymentOuterClass.Deployment> getDeploymentsForUser(
        api.DeploymentOuterClass.GetDeploymentsForUserRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getGetDeploymentsForUserMethod(), getCallOptions(), request);
    }

    /**
     */
    public api.DeploymentOuterClass.Deployment createDeployment(api.DeploymentOuterClass.NewDeployment request) {
      return blockingUnaryCall(
          getChannel(), getCreateDeploymentMethod(), getCallOptions(), request);
    }

    /**
     */
    public api.DeploymentOuterClass.Deployment getDeployment(api.DeploymentOuterClass.GetDeploymentRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetDeploymentMethod(), getCallOptions(), request);
    }

    /**
     */
    public api.EmptyOuterClass.Empty removeDeployment(api.DeploymentOuterClass.RemoveDeploymentRequest request) {
      return blockingUnaryCall(
          getChannel(), getRemoveDeploymentMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class DeploymentManagementServiceFutureStub extends io.grpc.stub.AbstractFutureStub<DeploymentManagementServiceFutureStub> {
    private DeploymentManagementServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DeploymentManagementServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DeploymentManagementServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<api.DeploymentOuterClass.Deployment> createDeployment(
        api.DeploymentOuterClass.NewDeployment request) {
      return futureUnaryCall(
          getChannel().newCall(getCreateDeploymentMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<api.DeploymentOuterClass.Deployment> getDeployment(
        api.DeploymentOuterClass.GetDeploymentRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetDeploymentMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<api.EmptyOuterClass.Empty> removeDeployment(
        api.DeploymentOuterClass.RemoveDeploymentRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRemoveDeploymentMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_DEPLOYMENTS_FOR_USER = 0;
  private static final int METHODID_CREATE_DEPLOYMENT = 1;
  private static final int METHODID_GET_DEPLOYMENT = 2;
  private static final int METHODID_REMOVE_DEPLOYMENT = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final DeploymentManagementServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(DeploymentManagementServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_DEPLOYMENTS_FOR_USER:
          serviceImpl.getDeploymentsForUser((api.DeploymentOuterClass.GetDeploymentsForUserRequest) request,
              (io.grpc.stub.StreamObserver<api.DeploymentOuterClass.Deployment>) responseObserver);
          break;
        case METHODID_CREATE_DEPLOYMENT:
          serviceImpl.createDeployment((api.DeploymentOuterClass.NewDeployment) request,
              (io.grpc.stub.StreamObserver<api.DeploymentOuterClass.Deployment>) responseObserver);
          break;
        case METHODID_GET_DEPLOYMENT:
          serviceImpl.getDeployment((api.DeploymentOuterClass.GetDeploymentRequest) request,
              (io.grpc.stub.StreamObserver<api.DeploymentOuterClass.Deployment>) responseObserver);
          break;
        case METHODID_REMOVE_DEPLOYMENT:
          serviceImpl.removeDeployment((api.DeploymentOuterClass.RemoveDeploymentRequest) request,
              (io.grpc.stub.StreamObserver<api.EmptyOuterClass.Empty>) responseObserver);
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
      synchronized (DeploymentManagementServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .addMethod(getGetDeploymentsForUserMethod())
              .addMethod(getCreateDeploymentMethod())
              .addMethod(getGetDeploymentMethod())
              .addMethod(getRemoveDeploymentMethod())
              .build();
        }
      }
    }
    return result;
  }
}
