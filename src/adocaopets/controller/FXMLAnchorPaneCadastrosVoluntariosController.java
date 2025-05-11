/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adocaopets.controller;

import adocaopets.model.dao.FuncaoVoluntarioDAO;
import adocaopets.model.dao.UsuarioDAO;
import adocaopets.model.dao.VoluntarioDAO;
import adocaopets.model.database.Database;
import adocaopets.model.database.DatabaseFactory;
import adocaopets.model.domain.FuncaoVoluntario;
import adocaopets.model.domain.Usuario;
import adocaopets.model.domain.Voluntario;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import java.util.stream.Collectors;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;

/**
 * FXML Controller class
 *
 * @author Gabriela
 */
public class FXMLAnchorPaneCadastrosVoluntariosController implements Initializable {

    // Tabela
    @FXML
    private TableView<Voluntario> tableViewVoluntarios;
    @FXML
    private TableColumn<Voluntario, String> tableColumnVoluntarioNome;
    @FXML
    private TableColumn<Voluntario, String> tableColumnVoluntarioFuncao;

    //Botões
    @FXML
    private Button buttonAdicionarVoluntario;
    @FXML
    private Button buttonRemoverVoluntario;

    //Campos do Formulário
    @FXML
    private ComboBox comboBoxVoluntarioUsuarios;
    @FXML
    private ComboBox comboBoxVoluntarioFuncoes;
    @FXML
    private CheckBox checkBoxVoluntarioAtivo;

    @FXML
    private TableView tableViewVoluntarioFuncoes;
    @FXML
    private TableColumn<Voluntario, String> tableColumnVoluntarioFuncaoNome;
    @FXML
    private Button buttonAdicionarFuncao;
    @FXML
    private Button buttonRemoverFuncao;

    private List<Usuario> listUsuarios;
    private ObservableList<Usuario> observableListUsuarios;

    private List<FuncaoVoluntario> listFuncoes;
    private ObservableList<FuncaoVoluntario> observableListFuncoes;

    private List<Voluntario> listVoluntarios;
    private ObservableList<Voluntario> observableListVoluntarios;

    private ObservableList<FuncaoVoluntario> observableListFuncoesVoluntario;

    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final VoluntarioDAO voluntarioDAO = new VoluntarioDAO();
    private final FuncaoVoluntarioDAO funcaoVoluntarioDAO = new FuncaoVoluntarioDAO();

    private Voluntario voluntario;

    private boolean editando = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuarioDAO.setConnection(connection);
        voluntarioDAO.setConnection(connection);
        funcaoVoluntarioDAO.setConnection(connection);

        // Da TableView dos voluntários
        tableColumnVoluntarioNome.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getUsuario().getNome())
        );

        tableColumnVoluntarioFuncao.setCellValueFactory(cellData -> {
            List<FuncaoVoluntario> funcoes = cellData.getValue().getFuncoes();
            String nomesFuncoes = funcoes.stream()
                    .map(FuncaoVoluntario::getNome)
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(nomesFuncoes);
        });

        tableViewVoluntarios.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> preencherCamposFormulario(newValue)
        );

        comboBoxVoluntarioUsuarios.setCellFactory(param -> new ListCell<Usuario>() {
            @Override
            protected void updateItem(Usuario usuario, boolean empty) {
                super.updateItem(usuario, empty);
                if (usuario != null) {
                    setText(usuario.getNome());
                } else {
                    setText(null);
                }
            }
        });

        comboBoxVoluntarioUsuarios.setButtonCell(new ListCell<Usuario>() {
            @Override
            protected void updateItem(Usuario usuario, boolean empty) {
                super.updateItem(usuario, empty);
                if (usuario != null) {
                    setText(usuario.getNome());
                } else {
                    setText(null);
                }
            }
        });

        // Da tableView das funções
        tableColumnVoluntarioFuncaoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        observableListFuncoesVoluntario = FXCollections.observableArrayList();
        tableViewVoluntarioFuncoes.setItems(observableListFuncoesVoluntario);

        carregarTableViewVoluntarios();
        carregarComboBoxUsuarios();
        carregarComboBoxFuncoes();
    }

    public void carregarComboBoxUsuarios() {
        listUsuarios = usuarioDAO.listar();
        observableListUsuarios = FXCollections.observableArrayList(listUsuarios);
        comboBoxVoluntarioUsuarios.setItems(observableListUsuarios);
    }

    public void carregarComboBoxFuncoes() {
        listFuncoes = funcaoVoluntarioDAO.listar();
        observableListFuncoes = FXCollections.observableArrayList(listFuncoes);
        comboBoxVoluntarioFuncoes.setItems(observableListFuncoes);
    }

    private void carregarTableViewVoluntarios() {
        listVoluntarios = voluntarioDAO.listar();
        observableListVoluntarios = FXCollections.observableArrayList(listVoluntarios);
        tableViewVoluntarios.setItems(observableListVoluntarios);
    }

    private void preencherCamposFormulario(Voluntario voluntario) {
        if (voluntario != null) {
            this.voluntario = voluntario;
            editando = true;

            // Define o usuário no ComboBox
            comboBoxVoluntarioUsuarios.getSelectionModel().select(voluntario.getUsuario());

            // Define o estado do CheckBox (ativo)
            checkBoxVoluntarioAtivo.setSelected(voluntario.isAtivo());

            // Atualiza a lista de funções
            observableListFuncoesVoluntario.setAll(voluntario.getFuncoes());
        };
    }

    @FXML
    private void handleButtonAdicionarFuncao() {
        FuncaoVoluntario funcaoSelecionada
                = (FuncaoVoluntario) comboBoxVoluntarioFuncoes.getSelectionModel().getSelectedItem();

        if (funcaoSelecionada != null) {
            if (observableListFuncoesVoluntario.contains(funcaoSelecionada)) {
                mostrarAlerta("Função Duplicada", "Esta função já foi adicionada!", Alert.AlertType.WARNING);
            } // Verifica o limite máximo de funções (exemplo: 2)
            else if (observableListFuncoesVoluntario.size() >= 2) {
                mostrarAlerta("Limite Atingido", "Máximo de 2 funções permitidas!", Alert.AlertType.WARNING);
            } else {
                observableListFuncoesVoluntario.add(funcaoSelecionada);
            }
        }
    }

    @FXML
    private void handleButtonRemoverFuncao() {
        FuncaoVoluntario funcaoSelecionada = (FuncaoVoluntario) tableViewVoluntarioFuncoes.getSelectionModel().getSelectedItem();
        if (funcaoSelecionada != null) {
            observableListFuncoesVoluntario.remove(funcaoSelecionada);
        }
    }

    @FXML
    private void handleButtonAdicionarVoluntario() {
        editando = false;
        limparCamposFormulario();
        buttonRemoverVoluntario.disabledProperty();
    }

    @FXML
    private void handleButtonRemoverVoluntario() {
        Voluntario voluntarioSelecionado = tableViewVoluntarios.getSelectionModel().getSelectedItem();
        if (voluntarioSelecionado != null) {
            if (voluntarioDAO.remover(voluntarioSelecionado)) {
                carregarTableViewVoluntarios();
                limparCamposFormulario();
                mostrarAlerta("Sucesso!", "Voluntário removido com sucesso!", Alert.AlertType.INFORMATION);
            }
        } else {
            mostrarAlerta("Erro!", "Nenhum voluntário selecionado!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleButtonSalvar() {
        if (validarEntradaDeDados()) {
            Usuario usuario = (Usuario) comboBoxVoluntarioUsuarios.getValue();

            Voluntario voluntario = new Voluntario();
            voluntario.setUsuario(usuario);
            voluntario.setDataCadastro(LocalDate.now());
            voluntario.setAtivo(checkBoxVoluntarioAtivo.isSelected());
            voluntario.setFuncoes(new ArrayList<>(observableListFuncoesVoluntario));

            try {
                if (editando) {
                    voluntario.setId(tableViewVoluntarios.getSelectionModel().getSelectedItem().getId());
                    if (voluntarioDAO.alterar(voluntario)) {
                        mostrarAlerta("Sucesso!", "Voluntário atualizado!", Alert.AlertType.INFORMATION);
                    }
                } else {
                    if (voluntarioDAO.inserir(voluntario)) {
                        mostrarAlerta("Sucesso!", "Voluntário cadastrado!", Alert.AlertType.INFORMATION);
                    }
                }

                carregarTableViewVoluntarios();
                limparCamposFormulario();

            } catch (Exception e) {
                mostrarAlerta("Erro!", "Erro ao salvar: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleButtonCancelar() {
        limparCamposFormulario();
    }

    private void limparCamposFormulario() {
        comboBoxVoluntarioFuncoes.setValue(null);
        comboBoxVoluntarioUsuarios.setValue(null);
        checkBoxVoluntarioAtivo.setSelected(false);
        observableListFuncoesVoluntario.clear();
        editando = false;
    }

    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        // Verifica se um usuário foi selecionado
        Usuario usuarioSelecionado = (Usuario) comboBoxVoluntarioUsuarios.getValue();
        if (usuarioSelecionado == null) {
            errorMessage += "Selecione um usuário!\n";
        }

        // Verifica se há funções selecionadas
        if (observableListFuncoesVoluntario.isEmpty()) {
            errorMessage += "Adicione pelo menos uma função!\n";
        }

        // Validação para novo cadastro
        if (!editando && usuarioSelecionado != null) {
            if (voluntarioDAO.usuarioEVoluntario(usuarioSelecionado.getId())) {
                errorMessage += "Este usuário já é um voluntário!\n";
            }
        }

        if (errorMessage.isEmpty()) {
            for (FuncaoVoluntario funcao : observableListFuncoesVoluntario) {
                int voluntariosAtuais = funcaoVoluntarioDAO.contarVoluntariosPorFuncao(funcao.getId());
                int limite = funcao.getLimiteVoluntarios();

                // Se for edição, subtrai 1 se a função já estava associada ao voluntário
                if (editando && voluntario.getFuncoes().contains(funcao)) {
                    voluntariosAtuais--;
                }

                if (voluntariosAtuais + 1 > limite) {
                    errorMessage += String.format(
                            "A função '%s' atingiu o limite de %d voluntários!\n",
                            funcao.getNome(),
                            limite
                    );
                }
            }
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            mostrarAlerta("Erro de Validação", errorMessage, Alert.AlertType.ERROR);
            return false;
        }
}

private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

}
