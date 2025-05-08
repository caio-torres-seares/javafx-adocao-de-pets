/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adocaopets.controller;

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
    private ComboBox comboBoxUsuarios;
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
                    if (petDAO.remover(adocaoSelecionada)) {
                        listaPets.remove(adocaoSelecionada);
                        limparCampos();
                        mostrarAlerta("Sucesso", "Pet removido com sucesso", null);
                    } else {
                        mostrarAlerta("Erro", "Erro ao remover pet", null);
                    }
                } catch (SQLException ex) {
                    mostrarAlerta("Erro", "Erro ao remover pet", ex.getMessage());
                }
            }
        });
        
        // Listener para botão de salvar
        buttonSalvar.setOnAction(event -> {
            try {
                Pet pet = criarPetDosCampos();
                if (adocaoSelecionada == null) {
                    // Inserir novo pet
                    if (petDAO.inserir(pet)) {
                        listaPets.add(pet);
                        limparCampos();
                        mostrarAlerta("Sucesso", "Pet cadastrado com sucesso", null);
                    } else {
                        mostrarAlerta("Erro", "Erro ao cadastrar pet", null);
                    }
                } else {
                    // Atualizar pet existente
                    pet.setId(adocaoSelecionada.getId());
                    if (petDAO.alterar(pet)) {
                        int index = listaPets.indexOf(adocaoSelecionada);
                        listaPets.set(index, pet);
                        limparCampos();
                        mostrarAlerta("Sucesso", "Pet atualizado com sucesso", null);
                    } else {
                        mostrarAlerta("Erro", "Erro ao atualizar pet", null);
                    }
                }
            } catch (SQLException ex) {
                mostrarAlerta("Erro", "Erro ao salvar pet", ex.getMessage());
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

    public void carregarComboBoxUsuarios(){
        listUsuarios = usuarioDAO.listar();
        observableListUsuarios = FXCollections.observableArrayList(listUsuarios);
        comboBoxUsuarios.setItems(observableListUsuarios);
    }
    
    public void carregarTableViewPets(){
        tableColumnAdocaoId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnAdocaoUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        tableColumnAdocaoPet.setCellValueFactory(new PropertyValueFactory<>("pet"));
        tableColumnAdocaoData.setCellValueFactory(new PropertyValueFactory<>("data"));
        listPets = petDAO.listarTodos();
        observableListPets = FXCollections.observableArrayList(listPets);
        tableViewPets.setItems(observableListPets);
    }
    
    private void mostrarAlerta(String titulo, String cabecalho, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

}
