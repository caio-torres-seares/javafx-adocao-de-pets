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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

public class FXMLAnchorPaneCadastrosUsuariosController implements Initializable {
    
    // Componentes da tabela
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
    private Button buttonAlterar;
    @FXML
    private Button buttonRemover;
    @FXML
    private Button buttonCancelar;
    @FXML
    private Button buttonSalvar;
    
    // Campos do formulário
    @FXML
    private Label labelUsuarioId;
    @FXML
    private TextField textFieldUsuarioNome;
    @FXML
    private TextField textFieldUsuarioCPF;
    @FXML
    private ComboBox<String> comboBoxUsuarioTipo;
    @FXML
    private RadioButton radioUsuarioVoluntarioSim;
    @FXML
    private RadioButton radioUsuarioVoluntarioNao;
    
    private ToggleGroup toggleGroupVoluntario;
    
    private List<Usuario> listUsuarios;
    private ObservableList<Usuario> observableListUsuarios;

    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            usuarioDAO.setConnection(connection);
            
            // Configurar ToggleGroup para os RadioButtons
            toggleGroupVoluntario = new ToggleGroup();
            radioUsuarioVoluntarioSim.setToggleGroup(toggleGroupVoluntario);
            radioUsuarioVoluntarioNao.setToggleGroup(toggleGroupVoluntario);
            
            // Carregar dados
            carregarTableViewUsuarios();
            
            // Configurar listener para seleção na tabela
            tableViewUsuarios.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewUsuarios(newValue));
            
        } catch (Exception ex) {
            Logger.getLogger(FXMLAnchorPaneCadastrosUsuariosController.class.getName())
                .log(Level.SEVERE, "Erro ao inicializar", ex);
        }
    }

    public void carregarTableViewUsuarios() {
        try {
            tableColumnUsuarioNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
            tableColumnUsuarioCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));

            listUsuarios = usuarioDAO.listar();
            observableListUsuarios = FXCollections.observableArrayList(listUsuarios);
            tableViewUsuarios.setItems(observableListUsuarios);
            
        } catch (SQLException ex) {
            Logger.getLogger(FXMLAnchorPaneCadastrosUsuariosController.class.getName())
                .log(Level.SEVERE, "Erro ao carregar usuários", ex);
        }
    }

    public void selecionarItemTableViewUsuarios(Usuario usuario) {
        if (usuario != null) {
            labelUsuarioId.setText(String.valueOf(usuario.getId()));
            textFieldUsuarioNome.setText(usuario.getNome());
            textFieldUsuarioCPF.setText(usuario.getCpf());
            
            // Configurar RadioButton
            if (usuario.isVoluntario()) {
                toggleGroupVoluntario.selectToggle(radioUsuarioVoluntarioSim);
            } else {
                toggleGroupVoluntario.selectToggle(radioUsuarioVoluntarioNao);
            }
        } else {
            labelUsuarioId.setText("");
            textFieldUsuarioNome.clear();
            textFieldUsuarioCPF.clear();
            toggleGroupVoluntario.selectToggle(null);
        }
    }
}