package adocaopets.controller;

import adocaopets.model.grpc.GrpcMedicamentos;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

public class FXMLAnchorPaneGrpcGetTeamMembersController implements Initializable {

    @FXML
    private TextArea textAreaTeam;

    private GrpcMedicamentos grpc;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        grpc = new GrpcMedicamentos();
    }    

    @FXML
    private void onBuscarTeam() {
        Thread t = new Thread(() -> {
            try {
                List<String> membros = grpc.getTeamMembers();

                Platform.runLater(() -> {
                    textAreaTeam.setText(
                        String.join("\n", membros)
                    );

                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    textAreaTeam.setText("Erro ao buscar integrantes:\n" + e.getMessage());
                });
            }
        });

        t.setDaemon(true);
        t.start();
    }
}
