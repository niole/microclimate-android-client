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
    comments = "Source: user.proto")
public final class UserServiceGrpc {

  private UserServiceGrpc() {}

  public static final String SERVICE_NAME = "api.UserService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<api.UserOuterClass.GetUserByEmailRequest,
      api.UserOuterClass.User> getGetUserByEmailMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetUserByEmail",
      requestType = api.UserOuterClass.GetUserByEmailRequest.class,
      responseType = api.UserOuterClass.User.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<api.UserOuterClass.GetUserByEmailRequest,
      api.UserOuterClass.User> getGetUserByEmailMethod() {
    io.grpc.MethodDescriptor<api.UserOuterClass.GetUserByEmailRequest, api.UserOuterClass.User> getGetUserByEmailMethod;
    if ((getGetUserByEmailMethod = UserServiceGrpc.getGetUserByEmailMethod) == null) {
      synchronized (UserServiceGrpc.class) {
        if ((getGetUserByEmailMethod = UserServiceGrpc.getGetUserByEmailMethod) == null) {
          UserServiceGrpc.getGetUserByEmailMethod = getGetUserByEmailMethod =
              io.grpc.MethodDescriptor.<api.UserOuterClass.GetUserByEmailRequest, api.UserOuterClass.User>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetUserByEmail"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  api.UserOuterClass.GetUserByEmailRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  api.UserOuterClass.User.getDefaultInstance()))
              .build();
        }
      }
    }
    return getGetUserByEmailMethod;
  }

  private static volatile io.grpc.MethodDescriptor<api.UserOuterClass.NewUser,
      api.UserOuterClass.User> getCreateUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateUser",
      requestType = api.UserOuterClass.NewUser.class,
      responseType = api.UserOuterClass.User.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<api.UserOuterClass.NewUser,
      api.UserOuterClass.User> getCreateUserMethod() {
    io.grpc.MethodDescriptor<api.UserOuterClass.NewUser, api.UserOuterClass.User> getCreateUserMethod;
    if ((getCreateUserMethod = UserServiceGrpc.getCreateUserMethod) == null) {
      synchronized (UserServiceGrpc.class) {
        if ((getCreateUserMethod = UserServiceGrpc.getCreateUserMethod) == null) {
          UserServiceGrpc.getCreateUserMethod = getCreateUserMethod =
              io.grpc.MethodDescriptor.<api.UserOuterClass.NewUser, api.UserOuterClass.User>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  api.UserOuterClass.NewUser.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  api.UserOuterClass.User.getDefaultInstance()))
              .build();
        }
      }
    }
    return getCreateUserMethod;
  }

  private static volatile io.grpc.MethodDescriptor<api.UserOuterClass.UpdateUserEmailRequest,
      api.UserOuterClass.User> getUpdateUserEmailMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateUserEmail",
      requestType = api.UserOuterClass.UpdateUserEmailRequest.class,
      responseType = api.UserOuterClass.User.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<api.UserOuterClass.UpdateUserEmailRequest,
      api.UserOuterClass.User> getUpdateUserEmailMethod() {
    io.grpc.MethodDescriptor<api.UserOuterClass.UpdateUserEmailRequest, api.UserOuterClass.User> getUpdateUserEmailMethod;
    if ((getUpdateUserEmailMethod = UserServiceGrpc.getUpdateUserEmailMethod) == null) {
      synchronized (UserServiceGrpc.class) {
        if ((getUpdateUserEmailMethod = UserServiceGrpc.getUpdateUserEmailMethod) == null) {
          UserServiceGrpc.getUpdateUserEmailMethod = getUpdateUserEmailMethod =
              io.grpc.MethodDescriptor.<api.UserOuterClass.UpdateUserEmailRequest, api.UserOuterClass.User>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateUserEmail"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  api.UserOuterClass.UpdateUserEmailRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  api.UserOuterClass.User.getDefaultInstance()))
              .build();
        }
      }
    }
    return getUpdateUserEmailMethod;
  }

  private static volatile io.grpc.MethodDescriptor<api.UserOuterClass.RemoveUserRequest,
      api.EmptyOuterClass.Empty> getRemoveUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RemoveUser",
      requestType = api.UserOuterClass.RemoveUserRequest.class,
      responseType = api.EmptyOuterClass.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<api.UserOuterClass.RemoveUserRequest,
      api.EmptyOuterClass.Empty> getRemoveUserMethod() {
    io.grpc.MethodDescriptor<api.UserOuterClass.RemoveUserRequest, api.EmptyOuterClass.Empty> getRemoveUserMethod;
    if ((getRemoveUserMethod = UserServiceGrpc.getRemoveUserMethod) == null) {
      synchronized (UserServiceGrpc.class) {
        if ((getRemoveUserMethod = UserServiceGrpc.getRemoveUserMethod) == null) {
          UserServiceGrpc.getRemoveUserMethod = getRemoveUserMethod =
              io.grpc.MethodDescriptor.<api.UserOuterClass.RemoveUserRequest, api.EmptyOuterClass.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RemoveUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  api.UserOuterClass.RemoveUserRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  api.EmptyOuterClass.Empty.getDefaultInstance()))
              .build();
        }
      }
    }
    return getRemoveUserMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static UserServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserServiceStub>() {
        @java.lang.Override
        public UserServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserServiceStub(channel, callOptions);
        }
      };
    return UserServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static UserServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserServiceBlockingStub>() {
        @java.lang.Override
        public UserServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserServiceBlockingStub(channel, callOptions);
        }
      };
    return UserServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static UserServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserServiceFutureStub>() {
        @java.lang.Override
        public UserServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserServiceFutureStub(channel, callOptions);
        }
      };
    return UserServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class UserServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void getUserByEmail(api.UserOuterClass.GetUserByEmailRequest request,
        io.grpc.stub.StreamObserver<api.UserOuterClass.User> responseObserver) {
      asyncUnimplementedUnaryCall(getGetUserByEmailMethod(), responseObserver);
    }

    /**
     */
    public void createUser(api.UserOuterClass.NewUser request,
        io.grpc.stub.StreamObserver<api.UserOuterClass.User> responseObserver) {
      asyncUnimplementedUnaryCall(getCreateUserMethod(), responseObserver);
    }

    /**
     */
    public void updateUserEmail(api.UserOuterClass.UpdateUserEmailRequest request,
        io.grpc.stub.StreamObserver<api.UserOuterClass.User> responseObserver) {
      asyncUnimplementedUnaryCall(getUpdateUserEmailMethod(), responseObserver);
    }

    /**
     * <pre>
     * TODO i think there is a packaged golang empty data type
     * </pre>
     */
    public void removeUser(api.UserOuterClass.RemoveUserRequest request,
        io.grpc.stub.StreamObserver<api.EmptyOuterClass.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getRemoveUserMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetUserByEmailMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                api.UserOuterClass.GetUserByEmailRequest,
                api.UserOuterClass.User>(
                  this, METHODID_GET_USER_BY_EMAIL)))
          .addMethod(
            getCreateUserMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                api.UserOuterClass.NewUser,
                api.UserOuterClass.User>(
                  this, METHODID_CREATE_USER)))
          .addMethod(
            getUpdateUserEmailMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                api.UserOuterClass.UpdateUserEmailRequest,
                api.UserOuterClass.User>(
                  this, METHODID_UPDATE_USER_EMAIL)))
          .addMethod(
            getRemoveUserMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                api.UserOuterClass.RemoveUserRequest,
                api.EmptyOuterClass.Empty>(
                  this, METHODID_REMOVE_USER)))
          .build();
    }
  }

  /**
   */
  public static final class UserServiceStub extends io.grpc.stub.AbstractAsyncStub<UserServiceStub> {
    private UserServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserServiceStub(channel, callOptions);
    }

    /**
     */
    public void getUserByEmail(api.UserOuterClass.GetUserByEmailRequest request,
        io.grpc.stub.StreamObserver<api.UserOuterClass.User> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetUserByEmailMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void createUser(api.UserOuterClass.NewUser request,
        io.grpc.stub.StreamObserver<api.UserOuterClass.User> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCreateUserMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateUserEmail(api.UserOuterClass.UpdateUserEmailRequest request,
        io.grpc.stub.StreamObserver<api.UserOuterClass.User> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getUpdateUserEmailMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * TODO i think there is a packaged golang empty data type
     * </pre>
     */
    public void removeUser(api.UserOuterClass.RemoveUserRequest request,
        io.grpc.stub.StreamObserver<api.EmptyOuterClass.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRemoveUserMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class UserServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<UserServiceBlockingStub> {
    private UserServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public api.UserOuterClass.User getUserByEmail(api.UserOuterClass.GetUserByEmailRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetUserByEmailMethod(), getCallOptions(), request);
    }

    /**
     */
    public api.UserOuterClass.User createUser(api.UserOuterClass.NewUser request) {
      return blockingUnaryCall(
          getChannel(), getCreateUserMethod(), getCallOptions(), request);
    }

    /**
     */
    public api.UserOuterClass.User updateUserEmail(api.UserOuterClass.UpdateUserEmailRequest request) {
      return blockingUnaryCall(
          getChannel(), getUpdateUserEmailMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * TODO i think there is a packaged golang empty data type
     * </pre>
     */
    public api.EmptyOuterClass.Empty removeUser(api.UserOuterClass.RemoveUserRequest request) {
      return blockingUnaryCall(
          getChannel(), getRemoveUserMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class UserServiceFutureStub extends io.grpc.stub.AbstractFutureStub<UserServiceFutureStub> {
    private UserServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<api.UserOuterClass.User> getUserByEmail(
        api.UserOuterClass.GetUserByEmailRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetUserByEmailMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<api.UserOuterClass.User> createUser(
        api.UserOuterClass.NewUser request) {
      return futureUnaryCall(
          getChannel().newCall(getCreateUserMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<api.UserOuterClass.User> updateUserEmail(
        api.UserOuterClass.UpdateUserEmailRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getUpdateUserEmailMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * TODO i think there is a packaged golang empty data type
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<api.EmptyOuterClass.Empty> removeUser(
        api.UserOuterClass.RemoveUserRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRemoveUserMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_USER_BY_EMAIL = 0;
  private static final int METHODID_CREATE_USER = 1;
  private static final int METHODID_UPDATE_USER_EMAIL = 2;
  private static final int METHODID_REMOVE_USER = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final UserServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(UserServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_USER_BY_EMAIL:
          serviceImpl.getUserByEmail((api.UserOuterClass.GetUserByEmailRequest) request,
              (io.grpc.stub.StreamObserver<api.UserOuterClass.User>) responseObserver);
          break;
        case METHODID_CREATE_USER:
          serviceImpl.createUser((api.UserOuterClass.NewUser) request,
              (io.grpc.stub.StreamObserver<api.UserOuterClass.User>) responseObserver);
          break;
        case METHODID_UPDATE_USER_EMAIL:
          serviceImpl.updateUserEmail((api.UserOuterClass.UpdateUserEmailRequest) request,
              (io.grpc.stub.StreamObserver<api.UserOuterClass.User>) responseObserver);
          break;
        case METHODID_REMOVE_USER:
          serviceImpl.removeUser((api.UserOuterClass.RemoveUserRequest) request,
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
      synchronized (UserServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .addMethod(getGetUserByEmailMethod())
              .addMethod(getCreateUserMethod())
              .addMethod(getUpdateUserEmailMethod())
              .addMethod(getRemoveUserMethod())
              .build();
        }
      }
    }
    return result;
  }
}
