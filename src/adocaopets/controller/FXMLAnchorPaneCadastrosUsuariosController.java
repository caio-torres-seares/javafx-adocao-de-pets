// ALUNA: GABRIELA BENEVIDES PEREIRA MARQUES

package adocaopets.controller;

import adocaopets.model.dao.UsuarioDAO;
import adocaopets.model.database.Database;
import adocaopets.model.database.DatabaseFactory;
import adocaopets.model.domain.Usuario;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

// Add imports for logging
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author Gabriela
 */
public class FXMLAnchorPaneCadastrosUsuariosController implements Initializable {
    
    @FXML
    private TableView<Usuario> tableViewUsuarios;
    @FXML
    private TableColumn<Usuario, String> tableColumnUsuarioNome;
    @FXML
    private TableColumn<Usuario, String> tableColumnUsuarioCPF;
    @FXML
    private Button buttonInserir;
    @FXML
    private Button buttonAlterar;
    @FXML
    private Button buttonRemover;
    @FXML
    private Label labelUsuarioId;   
    @FXML
    private Label labelUsuarioNome;
    @FXML
    private Label labelUsuarioCPF;
    @FXML
    private Label labelUsuarioPetsAdotados;
    @FXML
    private Label labelUsuarioVoluntario;

    private List<Usuario> listUsuarios;
    private ObservableList<Usuario> observableListUsuarios;

    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuarioDAO.setConnection (connection);
        
        carregarTableViewUsuarios();

        // Limpando a exibição dos detalhes do Usuario
        selecionarItemTableViewUsuarios(null);

        // Listen acionado diante de quaisquer alterações na seleção de itens do TableView
        tableViewUsuarios.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewUsuarios(newValue));
    }

    public void carregarTableViewUsuarios() {
        tableColumnUsuarioNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnUsuarioCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));

        try {
            listUsuarios = usuarioDAO.listar();
            // Log the number of users fetched
            System.out.println("Number of users fetched: " + (listUsuarios != null ? listUsuarios.size() : "null list"));
        } catch (SQLException ex) {
            // Log the exception if fetching fails
            Logger.getLogger(FXMLAnchorPaneCadastrosUsuariosController.class.getName()).log(Level.SEVERE, "Error fetching users from database", ex);
            // Optionally, show an alert to the user
            listUsuarios = FXCollections.observableArrayList(); // Initialize with empty list on error
        }

        observableListUsuarios = FXCollections.observableArrayList(listUsuarios);
        tableViewUsuarios.setItems(observableListUsuarios);
    }

    public void selecionarItemTableViewUsuarios(Usuario usuario) {
        if (usuario != null) {
            labelUsuarioId.setText(String.valueOf(usuario.getId()));
            labelUsuarioNome.setText(usuario.getNome());
            labelUsuarioCPF.setText(usuario.getCpf());
            boolean res = usuario.isVoluntario();
            labelUsuarioVoluntario.setText(res ? "Sim" : "Não");
        } else {
            labelUsuarioId.setText("");
            labelUsuarioNome.setText("");
            labelUsuarioCPF.setText("");
            labelUsuarioVoluntario.setText("");
        }
    }    
    
}