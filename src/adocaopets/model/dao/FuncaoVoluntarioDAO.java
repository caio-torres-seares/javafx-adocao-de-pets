//ALUNOS: CAIO TORRES SEARES e GABRIELA BENEVIDES PEREIRA MARQUES
package adocaopets.model.dao;

import adocaopets.model.domain.FuncaoVoluntario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FuncaoVoluntarioDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(FuncaoVoluntario funcao) {
        String sql = "INSERT INTO funcoes_voluntario(nome, descricao, limite_voluntarios) VALUES(?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, funcao.getNome());
            stmt.setString(2, funcao.getDescricao());
            stmt.setInt(3, funcao.getLimiteVoluntarios());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(FuncaoVoluntarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(FuncaoVoluntario funcao) {
        String sql = "UPDATE funcoes_voluntario SET nome=?, descricao=?, limite_voluntarios=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, funcao.getNome());
            stmt.setString(2, funcao.getDescricao());
            stmt.setInt(3, funcao.getLimiteVoluntarios());
            stmt.setInt(4, funcao.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(FuncaoVoluntarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(FuncaoVoluntario funcao) {
        String sql = "DELETE FROM funcoes_voluntario WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, funcao.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(FuncaoVoluntarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<FuncaoVoluntario> listar() {
        String sql = "SELECT * FROM funcoes_voluntario";
        List<FuncaoVoluntario> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                FuncaoVoluntario funcao = new FuncaoVoluntario();
                funcao.setId(resultado.getInt("id"));
                funcao.setNome(resultado.getString("nome"));
                funcao.setDescricao(resultado.getString("descricao"));
                funcao.setLimiteVoluntarios(resultado.getInt("limite_voluntarios"));
                retorno.add(funcao);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FuncaoVoluntarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public FuncaoVoluntario buscar(FuncaoVoluntario funcao) {
        String sql = "SELECT * FROM funcoes_voluntario WHERE id=?";
        FuncaoVoluntario retorno = new FuncaoVoluntario();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, funcao.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                funcao.setNome(resultado.getString("nome"));
                funcao.setDescricao(resultado.getString("descricao"));
                funcao.setLimiteVoluntarios(resultado.getInt("limite_voluntarios"));
                retorno = funcao;
            }
        } catch (SQLException ex) {
            Logger.getLogger(FuncaoVoluntarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
} 