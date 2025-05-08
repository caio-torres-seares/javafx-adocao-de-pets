//ALUNO: CAIO TORRES SEARES
package adocaopets.controller;

import adocaopets.model.dao.AdocaoDAO;
import adocaopets.model.dao.PetDAO;
import adocaopets.model.dao.UsuarioDAO;
import adocaopets.model.database.Database;
import adocaopets.model.database.DatabaseFactory;
import adocaopets.model.domain.Adocao;
import adocaopets.model.domain.Pet;
import adocaopets.model.domain.SexoPetEnum;
import adocaopets.model.domain.Usuario;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author 20231si012
 */
public class FXMLAnchorPaneCadastrosAdocoesController implements Initializable {

    @FXML
    private TableView<Adocao> tableViewAdocoes;
    @FXML
    private TableColumn<Adocao, Integer> tableColumnAdocaoId;
    @FXML
    private TableColumn<Adocao, Usuario> tableColumnAdocaoUsuario;
    @FXML
    private TableColumn<Adocao, Pet> tableColumnAdocaoPet;
    @FXML
    private TableColumn<Adocao, LocalDate> tableColumnAdocaoData;
    @FXML
    private Button buttonNovaAdocao;
    @FXML
    private Button buttonRemoverAdocao;
    @FXML
    private ComboBox<Usuario> comboBoxUsuarios;
    @FXML
    private TableView<Pet> tableViewPets;
    @FXML
    private TableColumn<Pet, String> columnNome;
    @FXML
    private TableColumn<Pet, String> columnEspecie;
    @FXML
    private TableColumn<Pet, String> columnRaca;
    @FXML
    private TextArea textAreaObservacoes;
    @FXML
    private Button buttonCancelar;
    @FXML
    private Button buttonSalvar;
    
    private Adocao adocaoSelecionada;
    
    private List<Usuario> listUsuarios;
    private List<Pet> listPets;
    private ObservableList<Usuario> observableListUsuarios;
    private ObservableList<Pet> observableListPets;
    
    private ObservableList<Adocao> observableListAdocoes;
    
    //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final PetDAO petDAO = new PetDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final AdocaoDAO adocaoDAO = new AdocaoDAO();
            

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuarioDAO.setConnection(connection);
        petDAO.setConnection(connection);
        carregarComboBoxUsuarios();
        carregarTableViewPets();
        
        observableListAdocoes = FXCollections.observableArrayList();
        tableViewAdocoes.setItems(observableListAdocoes);
        
        configurarListeners();
        /*tableColumnAdocaoId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnAdocaoUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        tableColumnAdocaoPet.setCellValueFactory(new PropertyValueFactory<>("pet"));
        tableColumnAdocaoData.setCellValueFactory(new PropertyValueFactory<>("data"));
        */
    }    
    
    
    private void configurarListeners() {
        // Listener para seleção na tabela
        tableViewAdocoes.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    adocaoSelecionada = newValue;
                    preencherCampos(newValue);
                    buttonNovaAdocao.setDisable(true);
                    buttonRemoverAdocao.setDisable(false);
                } else {
                    limparCampos();
                    buttonNovaAdocao.setDisable(false);
                    buttonRemoverAdocao.setDisable(true);
                }
            }
        );
        
        // Listener para botão de inserir
        buttonNovaAdocao.setOnAction(event -> {
            limparCampos();
            adocaoSelecionada = null;
            buttonNovaAdocao.setDisable(true);
            buttonRemoverAdocao.setDisable(true);
        });
        
        // Listener para botão de remover
        buttonRemoverAdocao.setOnAction(event -> {
            if (adocaoSelecionada != null) {
                try {
                    if (adocaoDAO.remover(adocaoSelecionada)) {
                        observableListAdocoes.remove(adocaoSelecionada);
                        limparCampos();
                        mostrarAlerta("Sucesso", "Adoção removida com sucesso", null);
                    } else {
                        mostrarAlerta("Erro", "Erro ao remover Adoção", null);
                    }
                } catch (SQLException ex) {
                    mostrarAlerta("Erro", "Erro ao remover Adoção", ex.getMessage());
                }
            }
        });
        
        // Listener para botão de salvar
        buttonSalvar.setOnAction(event -> {
            try {
                Adocao adocao = criarAdocao();
                if (adocaoSelecionada == null) {
                    // Inserir nova Adoção
                    if (adocaoDAO.inserir(adocao)) {
                        observableListAdocoes.add(adocao);
                        limparCampos();
                        mostrarAlerta("Sucesso", "Adoção cadastrada com sucesso", null);
                    } else {
                        mostrarAlerta("Erro", "Erro ao cadastrar Adoção", null);
                    }
                } else {
                    // Atualizar Adoção existente
                    adocao.setId(adocaoSelecionada.getId());
                    if (adocaoDAO.alterar(adocao)) {
                        int index = observableListAdocoes.indexOf(adocaoSelecionada);
                        observableListAdocoes.set(index, adocao);
                        limparCampos();
                        mostrarAlerta("Sucesso", "Adoção atualizada com sucesso", null);
                    } else {
                        mostrarAlerta("Erro", "Erro ao atualizar Adoção", null);
                    }
                }
            } catch (SQLException ex) {
                mostrarAlerta("Erro", "Erro ao salvar Adoção", ex.getMessage());
            }
        });
        
        // Listener para botão de cancelar
        buttonCancelar.setOnAction(event -> {
            limparCampos();
            adocaoSelecionada = null;
            buttonNovaAdocao.setDisable(false);
            buttonRemoverAdocao.setDisable(true);
        });
    }
    
    private void preencherCampos(Adocao adocao) {
        comboBoxUsuarios.getSelectionModel().select(adocao.getUsuario());;
        tableViewPets.getSelectionModel().select(adocao.getPet());
    }
    
    private void limparCampos() {
        comboBoxUsuarios.getSelectionModel().clearSelection(); // Limpa seleção do ComboBox
        tableViewPets.getSelectionModel().clearSelection();    
    }

    private void carregarComboBoxUsuarios(){
        listUsuarios = usuarioDAO.listar();
        observableListUsuarios = FXCollections.observableArrayList(listUsuarios);
        comboBoxUsuarios.setItems(observableListUsuarios);
    }
    
    private void carregarTableViewPets(){
        tableColumnAdocaoId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnAdocaoUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        tableColumnAdocaoPet.setCellValueFactory(new PropertyValueFactory<>("pet"));
        tableColumnAdocaoData.setCellValueFactory(new PropertyValueFactory<>("data"));
        listPets = petDAO.listarTodos();
        observableListPets = FXCollections.observableArrayList(listPets);
        tableViewPets.setItems(observableListPets);
    }
    
    private Adocao criarAdocao() {
        Adocao adocao = new Adocao();
        Usuario usuarioSelecionado = (Usuario) comboBoxUsuarios.getSelectionModel().getSelectedItem();
        Pet petSelecionado = tableViewPets.getSelectionModel().getSelectedItem();
        
        if (usuarioSelecionado == null || petSelecionado == null) {
            mostrarAlerta("Erro", "Dados inválidos", "Você precisa selecionar um usuário e um pet.");
            return null;
        }

        adocao.setUsuario(usuarioSelecionado);
        adocao.setPet(petSelecionado);
        adocao.setData(LocalDate.now()); // Data atual da adoção
        return adocao;
    }
    
    private void mostrarAlerta(String titulo, String cabecalho, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

}
