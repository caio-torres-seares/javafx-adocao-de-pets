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
import java.util.HashMap;
import java.util.Map;

public class VoluntarioDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Voluntario voluntario) {
        try {
            // Desativa o autoCommit para controle manual da transação
            connection.setAutoCommit(false);

            // 1. Insere o voluntário na tabela 'voluntarios'
            String sqlVoluntario = "INSERT INTO voluntarios(usuario_id, data_cadastro, ativo) VALUES(?,?,?)";
            try (PreparedStatement stmtVoluntario = connection.prepareStatement(sqlVoluntario, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmtVoluntario.setInt(1, voluntario.getUsuario().getId());
                stmtVoluntario.setDate(2, Date.valueOf(voluntario.getDataCadastro()));
                stmtVoluntario.setBoolean(3, voluntario.isAtivo());
                stmtVoluntario.executeUpdate();

                // Recupera o ID gerado
                try (ResultSet rs = stmtVoluntario.getGeneratedKeys()) {
                    if (rs.next()) {
                        voluntario.setId(rs.getInt(1));
                    }
                }
            }

            // 2. Atualiza o status do usuário para "voluntário"
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            usuarioDAO.setConnection(connection);
            usuarioDAO.atualizarStatusVoluntario(voluntario.getUsuario().getId(), true);

            // 3. Insere as funções na tabela 'voluntarios_funcoes'
            if (!voluntario.getFuncoes().isEmpty()) {
                String sqlFuncoes = "INSERT INTO voluntarios_funcoes(voluntario_id, funcao_id) VALUES(?,?)";
                try (PreparedStatement stmtFuncoes = connection.prepareStatement(sqlFuncoes)) {
                    for (FuncaoVoluntario funcao : voluntario.getFuncoes()) {
                        stmtFuncoes.setInt(1, voluntario.getId());
                        stmtFuncoes.setInt(2, funcao.getId());
                        stmtFuncoes.addBatch(); // Otimização para inserções em lote
                    }
                    stmtFuncoes.executeBatch(); // Executa todas as inserções de uma vez
                }
            }

            // Confirma todas as operações
            connection.commit();
            return true;

        } catch (SQLException ex) {
            try {
                connection.rollback(); // Reverte em caso de erro
            } catch (SQLException e) {
                Logger.getLogger(VoluntarioDAO.class.getName()).log(Level.SEVERE, "Falha ao reverter transação", e);
            }
            Logger.getLogger(VoluntarioDAO.class.getName()).log(Level.SEVERE, "Erro ao inserir voluntário", ex);
            return false;
        } finally {
            try {
                connection.setAutoCommit(true); // Restaura o autoCommit padrão
            } catch (SQLException ex) {
                Logger.getLogger(VoluntarioDAO.class.getName()).log(Level.SEVERE, "Falha ao restaurar autoCommit", ex);
            }
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
        try {
            // Desativa o autoCommit para controlar a transação manualmente
            connection.setAutoCommit(false);

            // 1. Remove as funções do voluntário
            String sqlDeleteFuncoes = "DELETE FROM voluntarios_funcoes WHERE voluntario_id = ?";
            try (PreparedStatement stmtFuncoes = connection.prepareStatement(sqlDeleteFuncoes)) {
                stmtFuncoes.setInt(1, voluntario.getId());
                stmtFuncoes.executeUpdate();
            }

            // 2. Remove o voluntário
            String sqlDeleteVoluntario = "DELETE FROM voluntarios WHERE id = ?";
            try (PreparedStatement stmtVoluntario = connection.prepareStatement(sqlDeleteVoluntario)) {
                stmtVoluntario.setInt(1, voluntario.getId());
                stmtVoluntario.executeUpdate();
            }

            // 3. Atualiza o status do usuário para "não voluntário"
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            usuarioDAO.setConnection(connection); // Usa a mesma conexão
            usuarioDAO.atualizarStatusVoluntario(voluntario.getUsuario().getId(), false);

            // Confirma a transação
            connection.commit();
            return true;

        } catch (SQLException ex) {
            try {
                // Reverte a transação em caso de erro
                connection.rollback();
            } catch (SQLException e) {
                Logger.getLogger(VoluntarioDAO.class.getName()).log(Level.SEVERE, "Falha ao reverter transação", e);
            }
            Logger.getLogger(VoluntarioDAO.class.getName()).log(Level.SEVERE, "Erro ao remover voluntário", ex);
            return false;
        } finally {
            try {
                // Restaura o autoCommit para o estado padrão
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                Logger.getLogger(VoluntarioDAO.class.getName()).log(Level.SEVERE, "Falha ao restaurar autoCommit", ex);
            }
        }
    }

    public List<Voluntario> listar() {
        String sql = "SELECT v.*, u.nome as usuario_nome FROM voluntarios v JOIN usuarios u ON v.usuario_id = u.id";
        List<Voluntario> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Voluntario voluntario = new Voluntario();
                Usuario usuario = new Usuario();
                usuario.setId(resultado.getInt("usuario_id"));
                usuario.setNome(resultado.getString("usuario_nome"));

                voluntario.setId(resultado.getInt("id"));
                voluntario.setUsuario(usuario);
                voluntario.setDataCadastro(resultado.getDate("data_cadastro").toLocalDate());
                voluntario.setAtivo(resultado.getBoolean("ativo"));

                // Buscar funções do voluntário
                String sqlFuncoes = "SELECT f.* FROM funcoes_voluntario f "
                        + "INNER JOIN voluntarios_funcoes vf ON f.id = vf.funcao_id "
                        + "WHERE vf.voluntario_id = ?";
                PreparedStatement stmtFuncoes = connection.prepareStatement(sqlFuncoes);
                stmtFuncoes.setInt(1, voluntario.getId());
                ResultSet resultadoFuncoes = stmtFuncoes.executeQuery();
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

    public boolean usuarioEVoluntario(int usuarioId) {
        String sql = "SELECT COUNT(*) FROM voluntarios WHERE usuario_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(VoluntarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
 
        public Map<Integer, ArrayList> listarQuantidadeVoluntariosPorMes() {
        String sql = "select count(id), extract(year from data_cadastro) as ano, extract(month from data_cadastro) as mes from voluntarios group by ano, mes order by ano, mes";
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
            Logger.getLogger(VoluntarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
}   
