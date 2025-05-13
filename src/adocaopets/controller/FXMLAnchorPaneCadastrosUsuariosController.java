// ALUNA: GABRIELA BENEVIDES PEREIRA MARQUES
package adocaopets.controller;

import adocaopets.model.dao.UsuarioDAO;
import adocaopets.model.database.Database;
import adocaopets.model.database.DatabaseFactory;
import adocaopets.model.domain.Usuario;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class FXMLAnchorPaneCadastrosUsuariosController implements Initializable {

    // tabela
    @FXML
    private TableView<Usuario> tableViewUsuarios;
    @FXML
    private TableColumn<Usuario, String> tableColumnUsuarioNome;
    @FXML
    private TableColumn<Usuario, String> tableColumnUsuarioCPF;

    // Botões
    @FXML
    private Button buttonInserir;
    @FXML
    private Button buttonRemover;
    @FXML
    private Button buttonSalvar;
    @FXML
    private Button buttonCancelar;

    // Campos do formulário
    @FXML
    private TextField textFieldUsuarioId;
    @FXML
    private TextField textFieldUsuarioNome;
    @FXML
    private TextField textFieldUsuarioCPF;
    @FXML
    private CheckBox checkBoxUsuarioVoluntario;

    private List<Usuario> listUsuarios;
    private ObservableList<Usuario> listaUsuarios;
    private Usuario usuarioSelecionado;

    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuarioDAO.setConnection(connection);

        tableColumnUsuarioNome.setCellValueFactory(
                new PropertyValueFactory<>("nome")
        );
        tableColumnUsuarioCPF.setCellValueFactory(
                new PropertyValueFactory<>("cpf")
        );

        carregarTableViewUsuarios();
        configurarListeners();
    }

    private void configurarListeners() {
        // Listener para seleção na tabela
        tableViewUsuarios.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        usuarioSelecionado = newValue;
                        preencherCampos(newValue);
                        buttonInserir.setDisable(true);
                        buttonRemover.setDisable(false);
                    } else {
                        limparCampos();
                        buttonInserir.setDisable(false);
                        buttonRemover.setDisable(true);
                    }
                }
        );

        // Listener para botão de inserir
        buttonInserir.setOnAction(event -> {
            limparCampos();
            usuarioSelecionado = null;
            buttonInserir.setDisable(true);
            buttonRemover.setDisable(true);
        });

        // Listener para botão de remover
        buttonRemover.setOnAction(event -> {
            if (usuarioSelecionado != null) {
                try {
                    if (usuarioDAO.remover(usuarioSelecionado)) {
                        listaUsuarios.remove(usuarioSelecionado);
                        limparCampos();
                        mostrarAlerta("Sucesso", "Usuário removido com sucesso", null);
                    } else {
                        mostrarAlerta("Erro", "Erro ao remover usuário", null);
                    }
                } catch (SQLException ex) {
                    mostrarAlerta("Erro", "Erro ao remover usuário", ex.getMessage());
                }
            }
        });

        // Listener para botão de salvar
        buttonSalvar.setOnAction(event -> {
            if (!validarEntradaDeDados()) {
                return;
            }
            try {
                Usuario usuario = criarUsuarioDosCampos();
                if (usuarioSelecionado == null) {
                    // Cadastrar novo usuário
                    if (usuarioDAO.inserir(usuario)) {
                        listaUsuarios.add(usuario);
                        limparCampos();
                        mostrarAlerta("Sucesso", "Usuário cadastrado com sucesso", null);
                    } else {
                        mostrarAlerta("Erro", "Erro ao cadastrar usuário", null);
                    }
                } else {
                    // Atualizar usuário existente
                    usuario.setId(usuarioSelecionado.getId());
                    if (usuarioDAO.alterar(usuario)) {
                        int index = listaUsuarios.indexOf(usuarioSelecionado);
                        listaUsuarios.set(index, usuario);
                        limparCampos();
                        mostrarAlerta("Sucesso", "Usuário atualizado com sucesso", null);
                    } else {
                        mostrarAlerta("Erro", "Erro ao atualizar usuário", null);
                    }
                }
            } catch (SQLException ex) {
                mostrarAlerta("Erro", "Erro ao salvar usuário", ex.getMessage());
            }
        });

        // Listener para botão de cancelar
        buttonCancelar.setOnAction(event -> {
            limparCampos();
            usuarioSelecionado = null;
            buttonInserir.setDisable(false);
            buttonRemover.setDisable(true);
        });
    }

    private void preencherCampos(Usuario usuario) {
        textFieldUsuarioId.setText(String.valueOf(usuario.getId()));
        textFieldUsuarioNome.setText(usuario.getNome());
        textFieldUsuarioCPF.setText(usuario.getCpf());
        checkBoxUsuarioVoluntario.setSelected(usuario.isVoluntario());
    }

    private void limparCampos() {
        textFieldUsuarioId.clear();
        textFieldUsuarioNome.clear();
        textFieldUsuarioCPF.clear();
        checkBoxUsuarioVoluntario.setSelected(false);
    }

    private Usuario criarUsuarioDosCampos() {
        Usuario usuario = new Usuario();
        usuario.setNome(textFieldUsuarioNome.getText());
        usuario.setCpf(textFieldUsuarioCPF.getText());
        usuario.setVoluntario(false);
        return usuario;
    }

    private void mostrarAlerta(String titulo, String cabecalho, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        if (textFieldUsuarioNome.getText() == null || textFieldUsuarioNome.getText().length() == 0) {
            errorMessage += "Nome inválido!\n";
        }
        if (textFieldUsuarioCPF.getText() == null || textFieldUsuarioCPF.getText().length() == 0) {
            errorMessage += "CPF inválido!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Mostrando a mensagem de erro
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campos inválidos, por favor, corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }

    private void carregarTableViewUsuarios() {
        List<Usuario> lista = usuarioDAO.listar();
        listaUsuarios = FXCollections.observableArrayList(lista);
        tableViewUsuarios.setItems(listaUsuarios);
    }

}
