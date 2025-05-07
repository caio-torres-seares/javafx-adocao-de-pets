// ALUNA: GABRIELA BENEVIDES PEREIRA MARQUES

package adocaopets.model.dao;

import adocaopets.model.domain.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuarioDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void inserir(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, cpf) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf());
            stmt.executeUpdate();
        }
    }
    
    public void atualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nome = ?, cpf = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf());
            stmt.setInt(3, usuario.getId());
            stmt.executeUpdate();
        }
    }
        
    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    public Usuario buscar(Usuario usuario) {
        String sql = "SELECT * FROM usuarios WHERE id=?";
        Usuario retorno = new Usuario();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, usuario.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                usuario.setNome(resultado.getString("nome"));
                usuario.setCpf(resultado.getString("cpf"));
                usuario.setVoluntario(resultado.getBoolean("voluntario"));
                retorno = usuario;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    
    public List<Usuario> listar() {
        String sql = "SELECT * FROM usuarios";
        List<Usuario> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(resultado.getInt("id"));
                usuario.setNome(resultado.getString("nome"));
                usuario.setCpf(resultado.getString("cpf"));
                usuario.setVoluntario(resultado.getBoolean("voluntario"));
                retorno.add(usuario);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

//    private Usuario criarUsuarioDoResultSet(ResultSet rs) throws SQLException {;
//        int id = rs.getInt("id");
//        String nome = rs.getString("nome");
//        String cpf = rs.getString("cpf");
//        return new Usuario(id, nome, cpf);
//    }
}
