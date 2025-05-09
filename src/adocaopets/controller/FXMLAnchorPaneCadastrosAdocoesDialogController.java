package adocaopets.controller;

import adocaopets.model.dao.PetDAO;
import adocaopets.model.dao.UsuarioDAO;
import adocaopets.model.database.Database;
import adocaopets.model.database.DatabaseFactory;
import adocaopets.model.domain.Adocao;
import adocaopets.model.domain.Pet;
import adocaopets.model.domain.Usuario;
import java.net.URL;
import java.sql.Connection;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class FXMLAnchorPaneCadastrosAdocoesDialogController implements Initializable {

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
    private Button buttonConfirmar;
    @FXML
    private Button buttonCancelar;

    private Stage dialogStage;
    private Adocao adocao;
    private boolean buttonConfirmarClicked = false;

    private List<Usuario> listUsuarios;
    private List<Pet> listPets;
    private ObservableList<Usuario> observableListUsuarios;
    private ObservableList<Pet> observableListPets;

    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final PetDAO petDAO = new PetDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuarioDAO.setConnection(connection);
        petDAO.setConnection(connection);
        
        carregarComboBoxUsuarios();
        carregarTableViewPets();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setAdocao(Adocao adocao) {
        this.adocao = adocao;
        if (adocao != null) {
            comboBoxUsuarios.setValue(adocao.getUsuario());
            if (adocao.getPet() != null) {
                tableViewPets.getSelectionModel().select(adocao.getPet());
            }
        }
    }

    public boolean isButtonConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    private void carregarComboBoxUsuarios() {
        listUsuarios = usuarioDAO.listar();
        observableListUsuarios = FXCollections.observableArrayList(listUsuarios);
        comboBoxUsuarios.setItems(observableListUsuarios);
    }

    private void carregarTableViewPets() {
        columnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        columnEspecie.setCellValueFactory(new PropertyValueFactory<>("especie"));
        columnRaca.setCellValueFactory(new PropertyValueFactory<>("raca"));
        
        listPets = petDAO.listarDisponiveis();
        observableListPets = FXCollections.observableArrayList(listPets);
        tableViewPets.setItems(observableListPets);
    }

    @FXML
    public void handleButtonConfirmar() {
        Usuario usuarioSelecionado = comboBoxUsuarios.getSelectionModel().getSelectedItem();
        Pet petSelecionado = tableViewPets.getSelectionModel().getSelectedItem();

        if (usuarioSelecionado == null || petSelecionado == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Dados inválidos");
            alert.setContentText("Você precisa selecionar um usuário e um pet.");
            alert.showAndWait();
            return;
        }

        adocao.setUsuario(usuarioSelecionado);
        adocao.setPet(petSelecionado);

        buttonConfirmarClicked = true;
        dialogStage.close();
    }

    @FXML
    public void handleButtonCancelar() {
        dialogStage.close();
    }
} 