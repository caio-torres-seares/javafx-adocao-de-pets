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
import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
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
    private Button buttonInserir;
    @FXML
    private Button buttonAlterar;
    @FXML
    private Button buttonRemover;
    @FXML
    private Label labelAdocaoId;
    @FXML
    private Label labelAdocaoData;
    @FXML
    private Label labelAdocaoUsuario;
    @FXML
    private Label labelAdocaoPet;

    private List<Adocao> listAdocoes;
    private ObservableList<Adocao> observableListAdocoes;

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
        adocaoDAO.setConnection(connection);

        carregarTableViewAdocoes();

        // Limpando a exibição dos detalhes da adoção
        selecionarItemTableViewAdocoes(null);

        // Listen acionado diante de quaisquer alterações na seleção de itens do TableView
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

    public void selecionarItemTableViewAdocoes(Adocao adocao) {
        if (adocao != null) {
            labelAdocaoId.setText(String.valueOf(adocao.getId()));
            labelAdocaoData.setText(String.valueOf(adocao.getData()));
            labelAdocaoUsuario.setText(adocao.getUsuario().toString());
            labelAdocaoPet.setText(adocao.getPet().toString());
        } else {
            labelAdocaoId.setText("");
            labelAdocaoData.setText("");
            labelAdocaoUsuario.setText("");
            labelAdocaoPet.setText("");
        }
    }

    @FXML
    public void handleButtonInserir() throws IOException {
        Adocao adocao = new Adocao();
        adocao.setData(LocalDate.now());
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosAdocoesDialog(adocao);
        if (buttonConfirmarClicked) {
            try {
                connection.setAutoCommit(false);
                if (adocaoDAO.inserir(adocao)) {
                    if (!marcarPetAdotado(adocao.getPet())) {
                        connection.rollback();
                        mostrarAlerta("Erro", "Não foi possível marcar o pet como adotado", null);
                        return;
                    }
                    connection.commit();
                    carregarTableViewAdocoes();
                } else {
                    connection.rollback();
                    mostrarAlerta("Erro", "Erro ao cadastrar Adoção", null);
                }
            } catch (SQLException ex) {
                try {
                    connection.rollback();
                } catch (SQLException ex1) {
                    Logger.getLogger(FXMLAnchorPaneCadastrosAdocoesController.class.getName()).log(Level.SEVERE, null, ex1);
                }
                Logger.getLogger(FXMLAnchorPaneCadastrosAdocoesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    public void handleButtonAlterar() throws IOException {
        Adocao adocao = tableViewAdocoes.getSelectionModel().getSelectedItem();
        if (adocao != null) {
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosAdocoesDialog(adocao);
            if (buttonConfirmarClicked) {
                try {
                    connection.setAutoCommit(false);
                    if (adocaoDAO.alterar(adocao)) {
                        connection.commit();
                        carregarTableViewAdocoes();
                    } else {
                        connection.rollback();
                        mostrarAlerta("Erro", "Erro ao atualizar Adoção", null);
                    }
                } catch (SQLException ex) {
                    try {
                        connection.rollback();
                    } catch (SQLException ex1) {
                        Logger.getLogger(FXMLAnchorPaneCadastrosAdocoesController.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                    Logger.getLogger(FXMLAnchorPaneCadastrosAdocoesController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha uma adoção na Tabela!");
            alert.show();
        }
    }

    @FXML
    public void handleButtonRemover() throws IOException, SQLException {
        Adocao adocao = tableViewAdocoes.getSelectionModel().getSelectedItem();
        if (adocao != null) {
            try {
                connection.setAutoCommit(false);
                if (adocaoDAO.remover(adocao)) {
                    connection.commit();
                    carregarTableViewAdocoes();
                } else {
                    connection.rollback();
                    mostrarAlerta("Erro", "Erro ao remover Adoção", null);
                }
            } catch (SQLException ex) {
                try {
                    connection.rollback();
                } catch (SQLException ex1) {
                    Logger.getLogger(FXMLAnchorPaneCadastrosAdocoesController.class.getName()).log(Level.SEVERE, null, ex1);
                }
                Logger.getLogger(FXMLAnchorPaneCadastrosAdocoesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha uma adoção na Tabela!");
            alert.show();
        }
    }

    public boolean showFXMLAnchorPaneCadastrosAdocoesDialog(Adocao adocao) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastrosAdocoesDialogController.class.getResource("/adocaopets/view/FXMLAnchorPaneCadastrosAdocoesDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Registro de Adoções");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        FXMLAnchorPaneCadastrosAdocoesDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setAdocao(adocao);

        dialogStage.showAndWait();

        return controller.isButtonConfirmarClicked();
    }

    private void mostrarAlerta(String titulo, String cabecalho, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private boolean marcarPetAdotado(Pet pet) throws SQLException {
        pet.setStatus(StatusPetEnum.ADOTADO);
        return petDAO.alterar(pet);
    }
}
