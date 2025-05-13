//ALUNO: CAIO TORRES SEARES
package adocaopets.controller;

import adocaopets.model.dao.PetDAO;
import adocaopets.model.database.Database;
import adocaopets.model.database.DatabaseFactory;
import adocaopets.model.domain.Pet;
import java.net.URL;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.jar.JarException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author Seares
 */
public class FXMLAnchorPaneRelatoriosPetsDisponiveisEAdotadosController implements Initializable {

    @FXML
    private TableView<Pet> tableViewPets;
    @FXML
    private TableColumn<Pet, Integer> tableColumnCodigo;
    @FXML
    private TableColumn<Pet, String> tableColumnNome;
    @FXML
    private TableColumn<Pet, String> tableColumnEspecie;
    @FXML
    private TableColumn<Pet, String> tableColumnRaca;
    @FXML
    private TableColumn<Pet, Integer> tableColumnIdade;
    @FXML
    private TableColumn<Pet, String> tableColumnSexo;
    @FXML
    private Button buttonImprimir;
    @FXML
    private ComboBox comboBoxEscolhaListagem;
    
    private List<Pet> listPets;
    private ObservableList<Pet> observableListPets;
    
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final PetDAO petDAO = new PetDAO();

  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        petDAO.setConnection(connection);
        
        comboBoxEscolhaListagem.getItems().addAll("Disponíveis", "Adotados");
        comboBoxEscolhaListagem.setValue("Disponíveis"); // valor padrão
        
        carregarTableViewPets("Disponíveis");
        
        comboBoxEscolhaListagem.setOnAction(event -> {
            String statusSelecionado = (String) comboBoxEscolhaListagem.getSelectionModel().getSelectedItem();
            carregarTableViewPets(statusSelecionado);
        });
    }    
    
    private void carregarTableViewPets(String status) {
        tableColumnCodigo.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnEspecie.setCellValueFactory(new PropertyValueFactory<>("especie"));
        tableColumnRaca.setCellValueFactory(new PropertyValueFactory<>("raca"));
        tableColumnIdade.setCellValueFactory(new PropertyValueFactory<>("idade"));
        tableColumnSexo.setCellValueFactory(new PropertyValueFactory<>("sexo"));
        
        if (status.equals("Disponíveis")) {
            listPets = petDAO.listarDisponiveis();
        } else {
            listPets = petDAO.listarAdotados();
        }
        
        observableListPets = FXCollections.observableArrayList(listPets);
        tableViewPets.setItems(observableListPets);
    }
    
    public void handleImprimir() throws JarException, JRException{
        HashMap filtro = new HashMap();
        
        String statusSelecionado = (String) comboBoxEscolhaListagem.getSelectionModel().getSelectedItem();
        String statusPet = statusSelecionado.equals("Disponíveis") ? "DISPONIVEL" : "ADOTADO";

        filtro.put("status_pet", statusPet);

        URL url = getClass().getResource("/adocaopets/relatorios/JAVAFXRelatoriosPetsDisponiveisEAdotados.jasper");
        JasperReport report = (JasperReport) JRLoader.loadObject(url);
        JasperPrint print = JasperFillManager.fillReport(report, filtro, connection);

        JasperViewer viewer = new JasperViewer(print, false);
        viewer.setTitle("Relatório de Pets - " + statusSelecionado);
        viewer.setVisible(true);
   }
}
