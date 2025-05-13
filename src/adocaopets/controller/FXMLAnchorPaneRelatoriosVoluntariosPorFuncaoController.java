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
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
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

public class FXMLAnchorPaneRelatoriosVoluntariosPorFuncaoController implements Initializable {

    @FXML
    private TableView<Voluntario> tableViewVoluntarios;
    @FXML
    private TableColumn<Voluntario, Integer> tableColumnVoluntarioId;
    @FXML
    private TableColumn<Voluntario, String> tableColumnVoluntarioNome;
    @FXML
    private TableColumn<Voluntario, Boolean> tableColumnVoluntarioAtivo;
    @FXML
    private TableColumn<Voluntario, String> tableColumnVoluntarioFuncao;
    @FXML
    private TableColumn<Voluntario, String> tableColumnVoluntarioFuncaoDescricao;
    @FXML
    private TableColumn<Voluntario, String> tableColumnVoluntarioDataCadastro;

    private Button buttonImprimir;
    @FXML
    private ComboBox comboBoxFuncoes;

    private List<Usuario> listUsuarios;
    private ObservableList<Usuario> observableListUsuarios;

    private List<FuncaoVoluntario> listFuncoes;
    private ObservableList<FuncaoVoluntario> observableListFuncoes;

    private List<Voluntario> listVoluntarios;
    private ObservableList<Voluntario> observableListVoluntarios;

    //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final VoluntarioDAO voluntarioDAO = new VoluntarioDAO();
    private final FuncaoVoluntarioDAO funcaoVoluntarioDAO = new FuncaoVoluntarioDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuarioDAO.setConnection(connection);
        voluntarioDAO.setConnection(connection);
        funcaoVoluntarioDAO.setConnection(connection);

        listFuncoes = funcaoVoluntarioDAO.listar();
        observableListFuncoes = FXCollections.observableArrayList(listFuncoes);
        comboBoxFuncoes.setItems(observableListFuncoes);
        carregarTableViewVoluntarios();
    }

    public void carregarTableViewVoluntarios() {
        // Configuração correta das colunas
        tableColumnVoluntarioId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnVoluntarioNome.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getUsuario().getNome())
        );
        tableColumnVoluntarioAtivo.setCellValueFactory(new PropertyValueFactory<>("ativo"));
        tableColumnVoluntarioDataCadastro.setCellValueFactory(new PropertyValueFactory<>("dataCadastro"));

        // Modificando para mostrar apenas a função selecionada
        tableColumnVoluntarioFuncao.setCellValueFactory(cellData -> {
            FuncaoVoluntario funcaoSelecionada = (FuncaoVoluntario) comboBoxFuncoes.getValue();
            if (funcaoSelecionada != null) {
                return new SimpleStringProperty(funcaoSelecionada.getNome());
            }
            return new SimpleStringProperty("");
        });

        tableColumnVoluntarioFuncaoDescricao.setCellValueFactory(cellData -> {
            FuncaoVoluntario funcaoSelecionada = (FuncaoVoluntario) comboBoxFuncoes.getValue();
            if (funcaoSelecionada != null) {
                return new SimpleStringProperty(funcaoSelecionada.getDescricao());
            }
            return new SimpleStringProperty("");
        });

        FuncaoVoluntario funcao = (FuncaoVoluntario) comboBoxFuncoes.getValue();
        if (funcao != null) {
            listVoluntarios = voluntarioDAO.listarPorFuncao(funcao);
            observableListVoluntarios = FXCollections.observableArrayList(listVoluntarios);
            tableViewVoluntarios.setItems(observableListVoluntarios);
        }
    }

    public void handleImprimir() throws JRException {
        HashMap<String, Object> filtro = new HashMap<>();
        FuncaoVoluntario funcaoVoluntario = (FuncaoVoluntario) comboBoxFuncoes.getValue();

        if (funcaoVoluntario != null) {
            try {
                filtro.put("id_funcao", funcaoVoluntario.getId());
                System.out.println("ID da função selecionada: " + funcaoVoluntario.getId());

                URL url = getClass().getResource("/adocaopets/relatorios/AdocaoPetsRelatorioVoluntarios.jasper");
                if (url == null) {
                    throw new JRException("Arquivo do relatório não encontrado");
                }

                JasperReport jasperReport = (JasperReport) JRLoader.loadObject(url);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, filtro, connection);

                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setTitle("Relatório de Voluntários - " + funcaoVoluntario.getNome());
                jasperViewer.setVisible(true);
            } catch (JRException ex) {
                throw new JRException("Erro ao gerar relatório: " + ex.getMessage());
            }
        }
    }

}
