// AUTOR : CAIO TORRES SEARES
package adocaopets.model.dao;

import adocaopets.model.domain.Adocao;
import adocaopets.model.domain.Pet;
import adocaopets.model.domain.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdocaoDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Adocao adocao) throws SQLException {
        String sql = "INSERT INTO adocoes(usuario_id, pet_id, data) VALUES (?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, adocao.getUsuario().getId());
            stmt.setInt(2, adocao.getPet().getId());
            stmt.setDate(3, java.sql.Date.valueOf(adocao.getData()));
            stmt.execute();
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(AdocaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Adocao adocao) throws SQLException {
        String sql = "UPDATE adocoes SET usuario_id=?, pet_id=?, data=? WHERE id=?";
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, adocao.getUsuario().getId());
            stmt.setInt(2, adocao.getPet().getId());
            stmt.setDate(3, java.sql.Date.valueOf(adocao.getData()));
            stmt.setInt(4, adocao.getId());
            stmt.execute();
            
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(AdocaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Adocao adocao) throws SQLException {
        String sql = "DELETE FROM adocoes WHERE id=?";
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, adocao.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(AdocaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
        
    public Adocao buscar(Adocao adocao) {
        String sql = "SELECT * FROM adocoes WHERE id=?";
        Adocao retorno = new Adocao();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, adocao.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                // Criar objetos Usuario e Pet com apenas o ID inicialmente
                Usuario usuario = new Usuario();
                usuario.setId(resultado.getInt("usuario_id"));
                
                Pet pet = new Pet();
                pet.setId(resultado.getInt("pet_id"));
                
                adocao.setUsuario(usuario);
                adocao.setPet(pet);
                adocao.setData(resultado.getDate("data").toLocalDate());
                
                retorno = adocao;
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdocaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public List<Adocao> listar() {
        String sql = "SELECT a.*, u.nome as usuario_nome, p.nome as pet_nome " +
                     "FROM adocoes a " +
                     "JOIN usuarios u ON a.usuario_id = u.id " +
                     "JOIN pets p ON a.pet_id = p.id";
        List<Adocao> retorno = new ArrayList<>();
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            
            while (resultado.next()) {
                Adocao adocao = new Adocao();
                adocao.setId(resultado.getInt("id"));
                
                // Criar e preencher Usuario
                Usuario usuario = new Usuario();
                usuario.setId(resultado.getInt("usuario_id"));
                usuario.setNome(resultado.getString("usuario_nome"));
                
                // Criar e preencher Pet
                Pet pet = new Pet();
                pet.setId(resultado.getInt("pet_id"));
                pet.setNome(resultado.getString("pet_nome"));
                
                adocao.setUsuario(usuario);
                adocao.setPet(pet);
                adocao.setData(resultado.getDate("data").toLocalDate());
                
                retorno.add(adocao);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdocaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
}