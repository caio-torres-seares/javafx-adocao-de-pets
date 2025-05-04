// AUTOR: CAIO TORRES SEARES

package adocaopets.model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import adocaopets.model.domain.Pet;
import adocaopets.model.domain.SexoPetEnum;
import adocaopets.model.domain.StatusPetEnum;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PetDAO {

    private Connection connection;

    public PetDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Pet pet) {
        String sql = "INSERT INTO pets(nome, especie, raca, idade, sexo, status) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, pet.getNome());
            stmt.setString(2, pet.getEspecie());
            stmt.setString(3, pet.getRaca());
            stmt.setInt(4, pet.getIdade());
            stmt.setString(5, pet.getSexo().getValorBanco());
            stmt.setString(6, pet.getStatus().name()); 
            stmt.execute();
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PetDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
      
    }


    public boolean alterar(Pet pet) throws SQLException {
        String sql = "UPDATE pets SET nome=?, especie=?, raca=?, idade=?, sexo=?, status=? WHERE id=?";
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, pet.getNome());
            stmt.setString(2, pet.getEspecie());
            stmt.setString(3, pet.getRaca());
            stmt.setInt(4, pet.getIdade());
            stmt.setString(5, pet.getSexo().getValorBanco());
            stmt.setString(6, pet.getStatus().name());
            stmt.setInt(7, pet.getId());
            stmt.execute();
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PetDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Pet pet) throws SQLException {
        String sql = "DELETE FROM pets WHERE id=?";
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, pet.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PetDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
        
    public Pet buscar(Pet pet) {
        String sql = "SELECT * FROM pets WHERE id=?";
        Pet retorno = new Pet();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, pet.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                pet.setNome(resultado.getString("nome"));
                pet.setEspecie(resultado.getString("especie"));
                pet.setRaca(resultado.getString("raca"));
                pet.setIdade(resultado.getInt("idade"));
                pet.setSexo(SexoPetEnum.fromValorBanco(resultado.getString("sexo")));
                pet.setStatus(StatusPetEnum.valueOf(resultado.getString("status")));

                retorno = pet;
            }
        } catch (SQLException ex) {
            Logger.getLogger(PetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public List<Pet> listarTodos() {
        String sql = "SELECT * FROM pets";
        List<Pet> retorno = new ArrayList<>();
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            
            while (resultado.next()) {
                Pet pet = new Pet();
                pet.setId(resultado.getInt("id"));
                pet.setNome(resultado.getString("nome"));
                pet.setEspecie(resultado.getString("especie"));
                pet.setRaca(resultado.getString("raca"));
                pet.setIdade(resultado.getInt("idade"));
                pet.setSexo(SexoPetEnum.fromValorBanco(resultado.getString("sexo")));
                pet.setStatus(StatusPetEnum.valueOf(resultado.getString("status")));

                retorno.add(pet);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
}

