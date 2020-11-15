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
    comments = "Source: events.proto")
public final class PeripheralMeasurementEventServiceGrpc {

  private PeripheralMeasurementEventServiceGrpc() {}

  public static final String SERVICE_NAME = "api.PeripheralMeasurementEventService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<api.Events.NewMeasurementEvent,
      api.EmptyOuterClass.Empty> getSendEventMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendEvent",
      requestType = api.Events.NewMeasurementEvent.class,
      responseType = api.EmptyOuterClass.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<api.Events.NewMeasurementEvent,
      api.EmptyOuterClass.Empty> getSendEventMethod() {
    io.grpc.MethodDescriptor<api.Events.NewMeasurementEvent, api.EmptyOuterClass.Empty> getSendEventMethod;
    if ((getSendEventMethod = PeripheralMeasurementEventServiceGrpc.getSendEventMethod) == null) {
      synchronized (PeripheralMeasurementEventServiceGrpc.class) {
        if ((getSendEventMethod = PeripheralMeasurementEventServiceGrpc.getSendEventMethod) == null) {
          PeripheralMeasurementEventServiceGrpc.getSendEventMethod = getSendEventMethod =
              io.grpc.MethodDescriptor.<api.Events.NewMeasurementEvent, api.EmptyOuterClass.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SendEvent"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  api.Events.NewMeasurementEvent.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  api.EmptyOuterClass.Empty.getDefaultInstance()))
              .build();
        }
      }
    }
    return getSendEventMethod;
  }

  private static volatile io.grpc.MethodDescriptor<api.Events.MeasurementEventFilterRequest,
      api.Events.MeasurementEvent> getFilterEventsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "FilterEvents",
      requestType = api.Events.MeasurementEventFilterRequest.class,
      responseType = api.Events.MeasurementEvent.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<api.Events.MeasurementEventFilterRequest,
      api.Events.MeasurementEvent> getFilterEventsMethod() {
    io.grpc.MethodDescriptor<api.Events.MeasurementEventFilterRequest, api.Events.MeasurementEvent> getFilterEventsMethod;
    if ((getFilterEventsMethod = PeripheralMeasurementEventServiceGrpc.getFilterEventsMethod) == null) {
      synchronized (PeripheralMeasurementEventServiceGrpc.class) {
        if ((getFilterEventsMethod = PeripheralMeasurementEventServiceGrpc.getFilterEventsMethod) == null) {
          PeripheralMeasurementEventServiceGrpc.getFilterEventsMethod = getFilterEventsMethod =
              io.grpc.MethodDescriptor.<api.Events.MeasurementEventFilterRequest, api.Events.MeasurementEvent>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "FilterEvents"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  api.Events.MeasurementEventFilterRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  api.Events.MeasurementEvent.getDefaultInstance()))
              .build();
        }
      }
    }
    return getFilterEventsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static PeripheralMeasurementEventServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PeripheralMeasurementEventServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PeripheralMeasurementEventServiceStub>() {
        @java.lang.Override
        public PeripheralMeasurementEventServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PeripheralMeasurementEventServiceStub(channel, callOptions);
        }
      };
    return PeripheralMeasurementEventServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static PeripheralMeasurementEventServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PeripheralMeasurementEventServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PeripheralMeasurementEventServiceBlockingStub>() {
        @java.lang.Override
        public PeripheralMeasurementEventServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PeripheralMeasurementEventServiceBlockingStub(channel, callOptions);
        }
      };
    return PeripheralMeasurementEventServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static PeripheralMeasurementEventServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PeripheralMeasurementEventServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PeripheralMeasurementEventServiceFutureStub>() {
        @java.lang.Override
        public PeripheralMeasurementEventServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PeripheralMeasurementEventServiceFutureStub(channel, callOptions);
        }
      };
    return PeripheralMeasurementEventServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class PeripheralMeasurementEventServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void sendEvent(api.Events.NewMeasurementEvent request,
        io.grpc.stub.StreamObserver<api.EmptyOuterClass.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getSendEventMethod(), responseObserver);
    }

    /**
     */
    public void filterEvents(api.Events.MeasurementEventFilterRequest request,
        io.grpc.stub.StreamObserver<api.Events.MeasurementEvent> responseObserver) {
      asyncUnimplementedUnaryCall(getFilterEventsMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSendEventMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                api.Events.NewMeasurementEvent,
                api.EmptyOuterClass.Empty>(
                  this, METHODID_SEND_EVENT)))
          .addMethod(
            getFilterEventsMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                api.Events.MeasurementEventFilterRequest,
                api.Events.MeasurementEvent>(
                  this, METHODID_FILTER_EVENTS)))
          .build();
    }
  }

  /**
   */
  public static final class PeripheralMeasurementEventServiceStub extends io.grpc.stub.AbstractAsyncStub<PeripheralMeasurementEventServiceStub> {
    private PeripheralMeasurementEventServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PeripheralMeasurementEventServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PeripheralMeasurementEventServiceStub(channel, callOptions);
    }

    /**
     */
    public void sendEvent(api.Events.NewMeasurementEvent request,
        io.grpc.stub.StreamObserver<api.EmptyOuterClass.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendEventMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void filterEvents(api.Events.MeasurementEventFilterRequest request,
        io.grpc.stub.StreamObserver<api.Events.MeasurementEvent> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getFilterEventsMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class PeripheralMeasurementEventServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<PeripheralMeasurementEventServiceBlockingStub> {
    private PeripheralMeasurementEventServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PeripheralMeasurementEventServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PeripheralMeasurementEventServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public api.EmptyOuterClass.Empty sendEvent(api.Events.NewMeasurementEvent request) {
      return blockingUnaryCall(
          getChannel(), getSendEventMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<api.Events.MeasurementEvent> filterEvents(
        api.Events.MeasurementEventFilterRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getFilterEventsMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class PeripheralMeasurementEventServiceFutureStub extends io.grpc.stub.AbstractFutureStub<PeripheralMeasurementEventServiceFutureStub> {
    private PeripheralMeasurementEventServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PeripheralMeasurementEventServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PeripheralMeasurementEventServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<api.EmptyOuterClass.Empty> sendEvent(
        api.Events.NewMeasurementEvent request) {
      return futureUnaryCall(
          getChannel().newCall(getSendEventMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SEND_EVENT = 0;
  private static final int METHODID_FILTER_EVENTS = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final PeripheralMeasurementEventServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(PeripheralMeasurementEventServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SEND_EVENT:
          serviceImpl.sendEvent((api.Events.NewMeasurementEvent) request,
              (io.grpc.stub.StreamObserver<api.EmptyOuterClass.Empty>) responseObserver);
          break;
        case METHODID_FILTER_EVENTS:
          serviceImpl.filterEvents((api.Events.MeasurementEventFilterRequest) request,
              (io.grpc.stub.StreamObserver<api.Events.MeasurementEvent>) responseObserver);
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
      synchronized (PeripheralMeasurementEventServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .addMethod(getSendEventMethod())
              .addMethod(getFilterEventsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
