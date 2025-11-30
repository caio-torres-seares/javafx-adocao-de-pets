package pet;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.63.0)",
    comments = "Source: pet.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class PetServiceGrpc {

  private PetServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "PetService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<Pet.MedicineRequest,
      Pet.MedicineResponse> getGetMedicinesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetMedicines",
      requestType = Pet.MedicineRequest.class,
      responseType = Pet.MedicineResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<Pet.MedicineRequest,
      Pet.MedicineResponse> getGetMedicinesMethod() {
    io.grpc.MethodDescriptor<Pet.MedicineRequest, Pet.MedicineResponse> getGetMedicinesMethod;
    if ((getGetMedicinesMethod = PetServiceGrpc.getGetMedicinesMethod) == null) {
      synchronized (PetServiceGrpc.class) {
        if ((getGetMedicinesMethod = PetServiceGrpc.getGetMedicinesMethod) == null) {
          PetServiceGrpc.getGetMedicinesMethod = getGetMedicinesMethod =
              io.grpc.MethodDescriptor.<Pet.MedicineRequest, Pet.MedicineResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetMedicines"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  Pet.MedicineRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  Pet.MedicineResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PetServiceMethodDescriptorSupplier("GetMedicines"))
              .build();
        }
      }
    }
    return getGetMedicinesMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static PetServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PetServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PetServiceStub>() {
        @java.lang.Override
        public PetServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PetServiceStub(channel, callOptions);
        }
      };
    return PetServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static PetServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PetServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PetServiceBlockingStub>() {
        @java.lang.Override
        public PetServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PetServiceBlockingStub(channel, callOptions);
        }
      };
    return PetServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static PetServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PetServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PetServiceFutureStub>() {
        @java.lang.Override
        public PetServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PetServiceFutureStub(channel, callOptions);
        }
      };
    return PetServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void getMedicines(Pet.MedicineRequest request,
        io.grpc.stub.StreamObserver<Pet.MedicineResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetMedicinesMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service PetService.
   */
  public static abstract class PetServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return PetServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service PetService.
   */
  public static final class PetServiceStub
      extends io.grpc.stub.AbstractAsyncStub<PetServiceStub> {
    private PetServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PetServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PetServiceStub(channel, callOptions);
    }

    /**
     */
    public void getMedicines(Pet.MedicineRequest request,
        io.grpc.stub.StreamObserver<Pet.MedicineResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetMedicinesMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service PetService.
   */
  public static final class PetServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<PetServiceBlockingStub> {
    private PetServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PetServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PetServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public Pet.MedicineResponse getMedicines(Pet.MedicineRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetMedicinesMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service PetService.
   */
  public static final class PetServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<PetServiceFutureStub> {
    private PetServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PetServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PetServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<Pet.MedicineResponse> getMedicines(
        Pet.MedicineRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetMedicinesMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_MEDICINES = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_MEDICINES:
          serviceImpl.getMedicines((Pet.MedicineRequest) request,
              (io.grpc.stub.StreamObserver<Pet.MedicineResponse>) responseObserver);
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

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getGetMedicinesMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              Pet.MedicineRequest,
              Pet.MedicineResponse>(
                service, METHODID_GET_MEDICINES)))
        .build();
  }

  private static abstract class PetServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    PetServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return Pet.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("PetService");
    }
  }

  private static final class PetServiceFileDescriptorSupplier
      extends PetServiceBaseDescriptorSupplier {
    PetServiceFileDescriptorSupplier() {}
  }

  private static final class PetServiceMethodDescriptorSupplier
      extends PetServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    PetServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (PetServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new PetServiceFileDescriptorSupplier())
              .addMethod(getGetMedicinesMethod())
              .build();
        }
      }
    }
    return result;
  }
}
