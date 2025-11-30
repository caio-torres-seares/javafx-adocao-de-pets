package adocaopets.controller;

import adocaopets.model.grpc.GrpcMedicamentos;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class FXMLAnchorPaneGrpcMedicamentosController implements Initializable {
    
    @FXML
    private TextField textFieldCondicao;

    @FXML
    private TextArea textAreaResultado;

    private GrpcMedicamentos grpcMedicamentos;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        grpcMedicamentos = new GrpcMedicamentos();
    }

    @FXML
    public void onBuscar() {
        String condicao = textFieldCondicao.getText();

        Thread t = new Thread(() -> {
            try {
                List<String> medicamentos = grpcMedicamentos.consultarMedicamentos(condicao);

                Platform.runLater(() -> {
                    textAreaResultado.setText(
                        "Medicamentos recomendados:\n" + String.join("\n", medicamentos)
                    );
                });

            } catch (Exception e) {
                Platform.runLater(() -> textAreaResultado.setText("Erro: " + e.getMessage()));
            }
        });

        t.setDaemon(true);
        t.start();
    }

    public void close() {
        grpcMedicamentos.close();
    } 
    
}
