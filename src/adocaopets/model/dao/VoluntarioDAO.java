//ALUNA: GABRIELA BENEVIDES PEREIRA MARQUES
package adocaopets.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import adocaopets.model.domain.FuncaoVoluntario;
import adocaopets.model.domain.Usuario;
import adocaopets.model.domain.Voluntario;

public class VoluntarioDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Voluntario voluntario) {
        String sql = "INSERT INTO voluntarios(usuario_id, data_cadastro, ativo) VALUES(?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, voluntario.getUsuario().getId());
            stmt.setDate(2, Date.valueOf(voluntario.getDataCadastro()));
            stmt.setBoolean(3, voluntario.isAtivo());
            stmt.execute();
            
            // Inserir funções do voluntário
            if (!voluntario.getFuncoes().isEmpty()) {
                sql = "INSERT INTO voluntarios_funcoes(voluntario_id, funcao_id) VALUES(?,?)";
                stmt = connection.prepareStatement(sql);
                for (FuncaoVoluntario funcao : voluntario.getFuncoes()) {
                    stmt.setInt(1, voluntario.getId());
                    stmt.setInt(2, funcao.getId());
                    stmt.execute();
                }
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VoluntarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Voluntario voluntario) {
        String sql = "UPDATE voluntarios SET usuario_id=?, data_cadastro=?, ativo=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, voluntario.getUsuario().getId());
            stmt.setDate(2, Date.valueOf(voluntario.getDataCadastro()));
            stmt.setBoolean(3, voluntario.isAtivo());
            stmt.setInt(4, voluntario.getId());
            stmt.execute();
            
            // Atualizar funções do voluntário
            sql = "DELETE FROM voluntarios_funcoes WHERE voluntario_id=?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, voluntario.getId());
            stmt.execute();
            
            if (!voluntario.getFuncoes().isEmpty()) {
                sql = "INSERT INTO voluntarios_funcoes(voluntario_id, funcao_id) VALUES(?,?)";
                stmt = connection.prepareStatement(sql);
                for (FuncaoVoluntario funcao : voluntario.getFuncoes()) {
                    stmt.setInt(1, voluntario.getId());
                    stmt.setInt(2, funcao.getId());
                    stmt.execute();
                }
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VoluntarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Voluntario voluntario) {
        String sql = "DELETE FROM voluntarios_funcoes WHERE voluntario_id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, voluntario.getId());
            stmt.execute();
            
            sql = "DELETE FROM voluntarios WHERE id=?";
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, voluntario.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(VoluntarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Voluntario> listar() {
        String sql = "SELECT * FROM voluntarios";
        List<Voluntario> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Voluntario voluntario = new Voluntario();
                Usuario usuario = new Usuario();
                usuario.setId(resultado.getInt("usuario_id"));
                
                voluntario.setId(resultado.getInt("id"));
                voluntario.setUsuario(usuario);
                voluntario.setDataCadastro(resultado.getDate("data_cadastro").toLocalDate());
                voluntario.setAtivo(resultado.getBoolean("ativo"));
                
                // Buscar funções do voluntário
                sql = "SELECT f.* FROM funcoes_voluntario f "
                    + "INNER JOIN voluntarios_funcoes vf ON f.id = vf.funcao_id "
                    + "WHERE vf.voluntario_id = ?";
                stmt = connection.prepareStatement(sql);
                stmt.setInt(1, voluntario.getId());
                ResultSet resultadoFuncoes = stmt.executeQuery();
                List<FuncaoVoluntario> funcoes = new ArrayList<>();
                while (resultadoFuncoes.next()) {
                    FuncaoVoluntario funcao = new FuncaoVoluntario();
                    funcao.setId(resultadoFuncoes.getInt("id"));
                    funcao.setNome(resultadoFuncoes.getString("nome"));
                    funcao.setDescricao(resultadoFuncoes.getString("descricao"));
                    funcao.setLimiteVoluntarios(resultadoFuncoes.getInt("limite_voluntarios"));
                    funcoes.add(funcao);
                }
                voluntario.setFuncoes(funcoes);
                
                retorno.add(voluntario);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VoluntarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Voluntario buscar(Voluntario voluntario) {
        String sql = "SELECT * FROM voluntarios WHERE id=?";
        Voluntario retorno = new Voluntario();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, voluntario.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(resultado.getInt("usuario_id"));
                
                voluntario.setUsuario(usuario);
                voluntario.setDataCadastro(resultado.getDate("data_cadastro").toLocalDate());
                voluntario.setAtivo(resultado.getBoolean("ativo"));
                
                // Buscar funções do voluntário
                sql = "SELECT f.* FROM funcoes_voluntario f "
                    + "INNER JOIN voluntarios_funcoes vf ON f.id = vf.funcao_id "
                    + "WHERE vf.voluntario_id = ?";
                stmt = connection.prepareStatement(sql);
                stmt.setInt(1, voluntario.getId());
                ResultSet resultadoFuncoes = stmt.executeQuery();
                List<FuncaoVoluntario> funcoes = new ArrayList<>();
                while (resultadoFuncoes.next()) {
                    FuncaoVoluntario funcao = new FuncaoVoluntario();
                    funcao.setId(resultadoFuncoes.getInt("id"));
                    funcao.setNome(resultadoFuncoes.getString("nome"));
                    funcao.setDescricao(resultadoFuncoes.getString("descricao"));
                    funcao.setLimiteVoluntarios(resultadoFuncoes.getInt("limite_voluntarios"));
                    funcoes.add(funcao);
                }
                voluntario.setFuncoes(funcoes);
                
                retorno = voluntario;
            }
        } catch (SQLException ex) {
            Logger.getLogger(VoluntarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
} 