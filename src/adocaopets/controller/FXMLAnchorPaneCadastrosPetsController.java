/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adocaopets.controller;

import adocaopets.model.dao.PetDAO;
import adocaopets.model.database.Database;
import adocaopets.model.database.DatabaseFactory;
import adocaopets.model.domain.Pet;
import adocaopets.model.domain.SexoPetEnum;
import adocaopets.model.domain.StatusPetEnum;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author Seares
 */
public class FXMLAnchorPaneCadastrosPetsController implements Initializable {

    @FXML
    private TableView<Pet> tableViewPets;

    @FXML
    private Label labelPetId;

    @FXML
    private TextField textFieldNome;

    @FXML
    private ComboBox<String> comboBoxEspecie;

    @FXML
    private TextField textFieldRaca;

    @FXML
    private Spinner<Integer> spinnerIdade;

    @FXML
    private RadioButton radioMacho;

    @FXML
    private RadioButton radioFemea;

    @FXML
    private RadioButton radioStatusPetDisponivel;

    @FXML
    private RadioButton radioStatusPetIndisponivel;

    @FXML
    private RadioButton radioStatusPetAdotado;

    @FXML
    private Button buttonInserir;

    @FXML
    private Button buttonRemover;

    @FXML
    private Button buttonSalvar;

    @FXML
    private Button buttonCancelar;

    private ObservableList<Pet> listaPets;
    private Pet petSelecionado;

    private ToggleGroup toggleGroupSexo;
    private ToggleGroup toggleGroupStatusPet;

    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final PetDAO petDAO = new PetDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        petDAO.setConnection(connection);

        // Configurar lista de pets
        listaPets = FXCollections.observableArrayList();
        tableViewPets.setItems(listaPets);

        // Configurar combo box de espécies
        comboBoxEspecie.getItems().addAll("Cachorro", "Gato", "Pássaro", "Outro");

        // Configurar spinner de idade
        spinnerIdade.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99, 1));

        // Configurar grupo de radio buttons para Sexo
        toggleGroupSexo = new ToggleGroup();
        radioMacho.setToggleGroup(toggleGroupSexo);
        radioFemea.setToggleGroup(toggleGroupSexo);

        // Configurar grupo de radio buttons para Status 
        toggleGroupStatusPet = new ToggleGroup();
        radioStatusPetDisponivel.setToggleGroup(toggleGroupStatusPet);
        radioStatusPetIndisponivel.setToggleGroup(toggleGroupStatusPet);
        radioStatusPetAdotado.setToggleGroup(toggleGroupStatusPet);
        // Carregar dados iniciais
        carregarDados();

        // Configurar listeners
        configurarListeners();

    }

    private void carregarDados() {
        listaPets.clear();
        listaPets.addAll(petDAO.listarTodos());
    }

    private void configurarListeners() {
        // Listener para seleção na tabela
        tableViewPets.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        petSelecionado = newValue;
                        preencherCampos(newValue);
                        buttonInserir.setDisable(true);
                        buttonRemover.setDisable(false);
                    } else {
                        limparCampos();
                        buttonInserir.setDisable(false);
                        buttonRemover.setDisable(true);
                    }
                }
        );

        // Listener para botão de inserir
        buttonInserir.setOnAction(event -> {
            limparCampos();
            petSelecionado = null;
            buttonInserir.setDisable(true);
            buttonRemover.setDisable(true);
        });

        // Listener para botão de remover
        buttonRemover.setOnAction(event -> {
            if (petSelecionado != null) {
                try {
                    if (petDAO.remover(petSelecionado)) {
                        listaPets.remove(petSelecionado);
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
            if (!validarEntradaDeDados()) {
                return;
            }
            try {
                Pet pet = criarPetDosCampos();
                if (petSelecionado == null) {
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
                    pet.setId(petSelecionado.getId());
                    if (petDAO.alterar(pet)) {
                        int index = listaPets.indexOf(petSelecionado);
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
            petSelecionado = null;
            buttonInserir.setDisable(false);
            buttonRemover.setDisable(true);
        });
    }

    private void preencherCampos(Pet pet) {
        labelPetId.setText(String.valueOf(pet.getId()));
        textFieldNome.setText(pet.getNome());
        comboBoxEspecie.setValue(pet.getEspecie());
        textFieldRaca.setText(pet.getRaca());
        spinnerIdade.getValueFactory().setValue(pet.getIdade());

        if (pet.getSexo() == SexoPetEnum.MASCULINO) {
            radioMacho.setSelected(true);
        } else {
            radioFemea.setSelected(true);
        }

        if (pet.getStatus() == StatusPetEnum.ADOTADO) {
            radioStatusPetAdotado.setSelected(true);
            setarRadioButtonAtivo(radioStatusPetAdotado);
        } else if (pet.getStatus() == StatusPetEnum.DISPONIVEL) {
            radioStatusPetDisponivel.setSelected(true);
            setarRadioButtonAtivo(radioStatusPetDisponivel);
        } else {
            radioStatusPetIndisponivel.setSelected(true);
            setarRadioButtonAtivo(radioStatusPetIndisponivel);
        }
    }

    private void setarRadioButtonAtivo(RadioButton button) {
        // Desativa os botões do Status pois não quero que permita a alteração deles
        radioStatusPetAdotado.setDisable(true);
        radioStatusPetDisponivel.setDisable(true);
        radioStatusPetIndisponivel.setDisable(true);
        // Após isso, seto o botão passado como ativo pois é o botão que corresponde ao status atual do pet
        if (button != null) {
            button.setDisable(false);
        }
    }

    private void limparCampos() {
        labelPetId.setText("");
        textFieldNome.clear();
        comboBoxEspecie.getSelectionModel().clearSelection();
        textFieldRaca.clear();
        spinnerIdade.getValueFactory().setValue(1);
        toggleGroupSexo.selectToggle(null);
        toggleGroupStatusPet.selectToggle(null);
        setarRadioButtonAtivo(null);
    }

    private Pet criarPetDosCampos() {
        Pet pet = new Pet();
        pet.setNome(textFieldNome.getText());
        pet.setEspecie(comboBoxEspecie.getValue());
        pet.setRaca(textFieldRaca.getText());
        pet.setIdade(spinnerIdade.getValue());

        RadioButton radioSelecionado = (RadioButton) toggleGroupSexo.getSelectedToggle();
        if (radioSelecionado != null) {
            pet.setSexo(SexoPetEnum.fromValorBanco(radioSelecionado.getUserData().toString()));
        }

        pet.setStatus(StatusPetEnum.DISPONIVEL);
        return pet;
    }

    private void mostrarAlerta(String titulo, String cabecalho, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        if (textFieldNome.getText() == null || textFieldNome.getText().trim().isEmpty()) {
            errorMessage += "Nome inválido!\n";
        }
        if (comboBoxEspecie.getValue() == null || comboBoxEspecie.getValue().isEmpty()) {
            errorMessage += "Selecione uma espécie!\n";
        }
        if (textFieldRaca.getText() == null || textFieldRaca.getText().trim().isEmpty()) {
            errorMessage += "Raça inválida!\n";
        }
        if (toggleGroupSexo.getSelectedToggle() == null) {
            errorMessage += "Selecione o sexo do pet!\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campos inválidos, por favor, corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
}
