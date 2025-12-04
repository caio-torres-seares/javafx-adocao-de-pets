package adocaopets.controller;

import adocaopets.model.grpc.GrpcService;
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

    private GrpcService grpcService;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        grpcService = new GrpcService();
    }

    @FXML
    public void onBuscar() {
        String condicao = textFieldCondicao.getText().trim();

        if (condicao.isEmpty()) {
            textAreaResultado.setText("Digite uma condição para buscar medicamentos.");
            return;
        }

        Thread t = new Thread(() -> {
            try {
                List<String> medicamentos = grpcService.consultarMedicamentos(condicao);

                Platform.runLater(() -> {
                    textAreaResultado.setText(String.join("\n", medicamentos));
                });

            } catch (Exception e) {
                Platform.runLater(() ->
                    textAreaResultado.setText("Erro ao buscar medicamentos:\n" + e.getMessage())
                );
            }
        });

        t.setDaemon(true);
        t.start();
    }

    public void close() {
        grpcService.close();
    } 
}
