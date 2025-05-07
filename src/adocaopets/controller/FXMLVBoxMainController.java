/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adocaopets.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Gabriela
 */
public class FXMLVBoxMainController implements Initializable {

    @FXML
    private MenuItem menuItemCadastrosUsuarios;
    
    @FXML
    private MenuItem menuItemCadastrosPets;
        
    @FXML
    private MenuItem menuItemProcessosAdocao;
    
    @FXML
    private MenuItem menuItemProcessosVoluntariado;
        
    @FXML
    private MenuItem menuItemGraficosAdocoesPorMes;
    
    @FXML
    private MenuItem menuItemGraficosVoluntariosPorMes;
    
    @FXML
    private MenuItem menuItemRelatoriosPetsDisponiveisAdotados;

    @FXML
    private MenuItem menuItemRelatoriosVoluntariosPorFuncao;
    
    @FXML
    private AnchorPane anchorPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    public void handleMenuItemCadastrosUsuarios() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/adocaopets/view/FXMLAnchorPaneCadastrosUsuarios.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    public void handleMenuItemCadastrosPets() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/adocaopets/view/FXMLAnchorPaneCadastrosPets.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    public void handleMenuItemProcessosAdocao() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/adocaopets/view/FXMLAnchorPaneCadastrosAdocoes.fxml"));
        anchorPane.getChildren().setAll(a);
    }
        
    @FXML
    public void handleMenuItemProcessosVoluntariado() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource(""));
        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    public void handleMenuItemGraficosAdocoesPorMes() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource(""));
        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    public void handleMenuItemGraficosVoluntariadosPorMes() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource(""));
        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    public void handleMenuItemRelatoriosPetsDisponiveisAdotados() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource(""));
        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    public void handleMenuItemRelatoriosVoluntariosPorFuncao() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource(""));
        anchorPane.getChildren().setAll(a);
    }


}

