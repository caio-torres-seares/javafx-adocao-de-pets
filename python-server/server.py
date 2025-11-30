import grpc
from concurrent import futures
import time

import pet_pb2
import pet_pb2_grpc

class PetService(pet_pb2_grpc.PetServiceServicer):
    def GetMedicines(self, request, context):
        condition = request.condition.lower()

        fake_db = {
            "carrapato": ["Bravecto", "NexGard", "Simparic"],
            "verminose": ["Vermivet", "Endogard"],
            "dor": ["Dipirona Pet", "Tramal Vet"],
            "pulga": ["Advantage", "Capstar", "Revolution"]
        }

        medicines = fake_db.get(condition, ["Nenhum medicamento encontrado"])

        return pet_pb2.MedicineResponse(medicines=medicines)

def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    pet_pb2_grpc.add_PetServiceServicer_to_server(PetService(), server)

    server.add_insecure_port('[::]:50051')
    server.start()
    print("Servidor gRPC rodando na porta 50051...")
    server.wait_for_termination()

if __name__ == '__main__':
    serve()
