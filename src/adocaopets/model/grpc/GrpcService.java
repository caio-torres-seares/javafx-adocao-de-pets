package adocaopets.model.grpc;

import java.util.List;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pet.Pet.Empty;
import pet.Pet.MedicineRequest;
import pet.Pet.MedicineResponse;
import pet.Pet.TeamResponse;
import pet.PetServiceGrpc;

public class GrpcService {

    private final PetServiceGrpc.PetServiceBlockingStub stub;
    private final ManagedChannel channel;

    public GrpcService() {
        // Abre conexão com servidor gRPC
        channel = ManagedChannelBuilder
                .forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        stub = PetServiceGrpc.newBlockingStub(channel);
    }

    public List<String> consultarMedicamentos(String condicao) {
        MedicineRequest request = MedicineRequest.newBuilder()
                .setCondition(condicao)
                .build();

        MedicineResponse response = stub.getMedicines(request);
        return response.getMedicinesList();
    }

    public List<String> getTeamMembers() {
        Empty request = Empty.newBuilder().build();
        TeamResponse response = stub.getTeamMembers(request);
        return response.getMembersList();
    }


    public void close() {
        channel.shutdown();
    }
}
