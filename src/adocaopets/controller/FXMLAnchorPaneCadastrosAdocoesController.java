//ALUNO: CAIO TORRES SEARES
package adocaopets.controller;

import adocaopets.model.dao.AdocaoDAO;
import adocaopets.model.dao.PetDAO;
import adocaopets.model.dao.UsuarioDAO;
import adocaopets.model.database.Database;
import adocaopets.model.database.DatabaseFactory;
import adocaopets.model.domain.Adocao;
import adocaopets.model.domain.Pet;
import adocaopets.model.domain.StatusPetEnum;
import adocaopets.model.domain.Usuario;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private Button buttonCancelar;
    @FXML
    private Button buttonSalvar;
    @FXML
    private Button buttonRemoverPetTableView;
    @FXML
    private Button buttonAdicionarPetTableView;
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

    private List<Adocao> listAdocoes;
    private ObservableList<Adocao> observableListAdocoes;
    private List<Pet> listPets;
    private ObservableList<Pet> observableListPets;
    private List<Usuario> listUsuarios;
    private ObservableList<Usuario> observableListUsuarios;

    //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final AdocaoDAO adocaoDAO = new AdocaoDAO();
    private final PetDAO petDAO = new PetDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        adocaoDAO.setConnection(connection);
        petDAO.setConnection(connection);
        usuarioDAO.setConnection(connection);

        carregarTableViewAdocoes();
        carregarTableViewPets();
        carregarComboBoxUsuarios();
        
        selecionarItemTableViewAdocoes(null);

        // Listeners
        tableViewAdocoes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewAdocoes(newValue));
    }

    public void carregarTableViewAdocoes() {
        tableColumnAdocaoId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnAdocaoUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        tableColumnAdocaoPet.setCellValueFactory(new PropertyValueFactory<>("pet"));
        tableColumnAdocaoData.setCellValueFactory(new PropertyValueFactory<>("data"));

        listAdocoes = adocaoDAO.listar();
        observableListAdocoes = FXCollections.observableArrayList(listAdocoes);
        tableViewAdocoes.setItems(observableListAdocoes);
    }

    public void carregarTableViewPets() {
        columnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        columnEspecie.setCellValueFactory(new PropertyValueFactory<>("especie"));
        columnRaca.setCellValueFactory(new PropertyValueFactory<>("raca"));

        listPets = petDAO.listarDisponiveis();
        observableListPets = FXCollections.observableArrayList(listPets);
        tableViewPets.setItems(observableListPets);
    }

    public void carregarComboBoxUsuarios() {
        listUsuarios = usuarioDAO.listar();
        observableListUsuarios = FXCollections.observableArrayList(listUsuarios);
        comboBoxUsuarios.setItems(observableListUsuarios);
    }

    public void selecionarItemTableViewAdocoes(Adocao adocao) {
        if (adocao != null) {
            comboBoxUsuarios.getSelectionModel().select(adocao.getUsuario());
            
            // Buscando pet no dao para ter certeza que está vindo certo
            Pet pet = petDAO.buscar(adocao.getPet());
            
            // Define a TableView apenas com o pet da adoção
            ObservableList<Pet> petAdocao = FXCollections.observableArrayList(pet);
            tableViewPets.setItems(petAdocao);
            
            buttonRemoverPetTableView.setDisable(false);
            
            //labelAdocaoData.setText(adocao.getData().toString());
        } else {
            limparLabels();
        }
    }
    
    public void handleButtonRemoverPetTableView(){
        
        // Habilita o botão de adição e remove o de remoção
        buttonAdicionarPetTableView.setDisable(false);
        buttonRemoverPetTableView.setDisable(true);
    }

    private void limparLabels() {
        // Restaura os dados completos
        comboBoxUsuarios.getSelectionModel().clearSelection();
        comboBoxUsuarios.setValue(null);

        tableViewPets.getSelectionModel().clearSelection();
        tableViewPets.setItems(observableListPets);
    }

    @FXML
    public void handleButtonSalvar() {
        try {
            connection.setAutoCommit(false);

            Usuario usuario = comboBoxUsuarios.getSelectionModel().getSelectedItem();
            Pet pet = tableViewPets.getSelectionModel().getSelectedItem();

            if (validarCampos(usuario, pet)) {
                Adocao novaAdocao = new Adocao();
                novaAdocao.setUsuario(usuario);
                novaAdocao.setPet(pet);
                novaAdocao.setData(LocalDate.now());

                adocaoDAO.inserir(novaAdocao);
                atualizarStatusPet(pet, StatusPetEnum.ADOTADO);

                connection.commit();
                mostrarAlerta(Alert.AlertType.INFORMATION, "Adoção Salva!", null, "A adoção foi salva com sucesso!");
                atualizarTabelas();
                limparCampos();
            }
        } catch (SQLException ex) {
            rollbackTransacao(ex);
            mostrarAlerta(Alert.AlertType.ERROR, "Erro ao Salvar", "Ocorreu um erro ao salvar adoção!", "Não foi possível salvar a adoção.");
        }
    }
    
    @FXML
    public void handleButtonLimparSelecao(){
        limparLabels();
        tableViewAdocoes.getSelectionModel().clearSelection();
        buttonAdicionarPetTableView.setDisable(true);
        buttonRemoverPetTableView.setDisable(true);
        // Senti que esse método e o handleButtonCancelar ficaram iguais, mas 
    }

    @FXML
    public void handleButtonRemoverAdocao() {
        Adocao adocao = tableViewAdocoes.getSelectionModel().getSelectedItem();
        if (adocao != null) {
            try {
                System.out.println("Tentando remover...");
                connection.setAutoCommit(false);

                // 1. Primeiro remove a adoção
                adocaoDAO.remover(adocao);
                
                // 2. Depois atualiza o status do pet
                // Buscando pet no dao para ter certeza que está vindo certo
                Pet pet = petDAO.buscar(adocao.getPet()); // Garante dados completos
                atualizarStatusPet(pet, StatusPetEnum.DISPONIVEL);

                connection.commit();
                mostrarAlerta(Alert.AlertType.INFORMATION, "Adoção Removida", null, "A adoção foi removida com sucesso!");
                atualizarTabelas();
                
                
            } catch (SQLException ex) {
                rollbackTransacao(ex);
                mostrarAlerta(Alert.AlertType.ERROR, "Erro ao Remover", "Ocorreu um erro ao remover a adoção", 
                "Não foi possível remover a adoção!");
            }
        }
    }
    
    private boolean validarCampos(Usuario usuario, Pet pet) {
        if (usuario == null || pet == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Obrigatórios", "Seleção necessária", 
                "Por favor, selecione um usuário e um pet!");
            return false;
        }
        return true;
    }

    
    private void atualizarStatusPet(Pet pet, StatusPetEnum status) throws SQLException {
        pet.setStatus(status);
        petDAO.alterar(pet);
    }

    private void atualizarTabelas() {
        carregarTableViewAdocoes();
        carregarTableViewPets();
    }

    private void limparCampos() {
        comboBoxUsuarios.getSelectionModel().clearSelection();
        tableViewPets.getSelectionModel().clearSelection();
    }


  
    private void rollbackTransacao(SQLException ex) {
        try {
            connection.rollback();
        } catch (SQLException ex1) {
            Logger.getLogger(FXMLAnchorPaneCadastrosAdocoesController.class.getName())
                .log(Level.SEVERE, "Erro no rollback", ex1);
        }
    }

    private void mostrarAlerta(Alert.AlertType tipoAlerta, String titulo, String cabecalho, String mensagem) {
        Alert alert = new Alert(tipoAlerta);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}