package adocaopets.model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import adocaopets.model.domain.Pet;
import adocaopets.model.domain.StatusPetEnum;

public class PetDAO {

    private Connection connection;

    public PetDAO(Connection connection) {
        this.connection = connection;
    }

    public void inserir(Pet pet) throws SQLException {
        String sql = "INSERT INTO pets (nome, especie, raca, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, pet.getNome());
            stmt.setString(2, pet.getEspecie());
            stmt.setString(3, pet.getRaca());
            stmt.setString(4, pet.getStatus().name());
            stmt.executeUpdate();
        }
    }

    public void atualizar(Pet pet) throws SQLException {
        String sql = "UPDATE pets SET nome = ?, especie = ?, raca = ?, status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, pet.getNome());
            stmt.setString(2, pet.getEspecie());
            stmt.setString(3, pet.getRaca());
            stmt.setString(4, pet.getStatus().name());
            stmt.setInt(5, pet.getId());
            stmt.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM pets WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Pet buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM pets WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarPetDoResultSet(rs);
                }
            }
        }
        return null;
    }

    public List<Pet> listarTodos() throws SQLException {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM pets";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                pets.add(criarPetDoResultSet(rs));
            }
        }
        return pets;
    }

    private Pet criarPetDoResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nome = rs.getString("nome");
        String especie = rs.getString("especie");
        String raca = rs.getString("raca");
        StatusPetEnum status = StatusPetEnum.valueOf(rs.getString("status"));
        return new Pet(id, nome, especie, raca, status);
    }
}

