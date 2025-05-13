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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    
    public int contarAdocoesPorUsuario(Usuario usuario) throws SQLException {
        String sql = "SELECT COUNT(*) FROM adocoes WHERE usuario_id = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, usuario.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdocaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    public Map<Integer, ArrayList> listarQuantidadePetsAdotadosPorMes() {
        String sql = "select count(id), extract(year from data) as ano, extract(month from data) as mes from adocoes group by ano, mes order by ano, mes";
        Map<Integer, ArrayList> retorno = new HashMap();
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {
                ArrayList linha = new ArrayList();
                if (!retorno.containsKey(resultado.getInt("ano")))
                {
                    linha.add(resultado.getInt("mes"));
                    linha.add(resultado.getInt("count"));
                    retorno.put(resultado.getInt("ano"), linha);
                }else{
                    ArrayList linhaNova = retorno.get(resultado.getInt("ano"));
                    linhaNova.add(resultado.getInt("mes"));
                    linhaNova.add(resultado.getInt("count"));
                }
            }
            return retorno;
        } catch (SQLException ex) {
            Logger.getLogger(AdocaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

}