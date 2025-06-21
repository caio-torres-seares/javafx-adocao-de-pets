/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adocaopets.controller;

import sockets.thread.ContadorGrupo;
import sockets.thread.LogGrupo;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author 20231si012
 */
public class FXMLAnchorPaneLogsThreadsESocketsController implements Initializable {

    @FXML
    private Label labelLogIndividual;
    @FXML
    private TableView<ContadorGrupo> tableViewLogsGrupos;
    @FXML
    private TableColumn<ContadorGrupo, Integer> tableColumnLogsPos;
    @FXML
    private TableColumn<ContadorGrupo, String> tableColumnLogsGrupo;
    @FXML
    private TableColumn<ContadorGrupo, Integer> tableColumnLogsUtilizacoes;
    @FXML
    private Label labelGrupoAtual;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableColumnLogsPos.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(tableViewLogsGrupos.getItems().indexOf(cellData.getValue()) + 1));
        tableColumnLogsGrupo.setCellValueFactory(new PropertyValueFactory<>("nomeGrupo"));
        tableColumnLogsUtilizacoes.setCellValueFactory(new PropertyValueFactory<>("quantidadeUtilizacoes"));
        new Thread(this::initiateConnection).start();
        
    }    
    
    public void initiateConnection() {
        int idGrupo = 7; // ID do grupo desejado (1-10)
        String servidor = "34.41.27.130"; // IP do servidor
        int porta = 12345;
        
        Platform.runLater(() -> labelGrupoAtual.setText("Grupo: " + idGrupo));

        try (Socket clienteSocket = new Socket(servidor, porta)) {
            System.out.println("✅ Conectado ao servidor: " + servidor + ":" + porta);
            
            // PASSO 1: Enviar ID do grupo
            ObjectOutputStream saida = new ObjectOutputStream(clienteSocket.getOutputStream());
            saida.writeObject(idGrupo);
            System.out.println("📤 Enviado ID do grupo: " + idGrupo);
            
            // PASSO 2: Receber ranking completo
            ObjectInputStream entrada = new ObjectInputStream(clienteSocket.getInputStream());
            @SuppressWarnings("unchecked")
            List<ContadorGrupo> ranking = (List<ContadorGrupo>) entrada.readObject();
            System.out.println("📥 Recebido ranking com " + ranking.size() + " grupos");
            
            // PASSO 3: Receber logs do grupo
            @SuppressWarnings("unchecked")
            List<LogGrupo> logs = (List<LogGrupo>) entrada.readObject();
            System.out.println("📥 Recebidos " + logs.size() + " logs do grupo " + idGrupo);
            
            // PASSO 4: Processar dados recebidos e atualizar a UI
            Platform.runLater(() -> atualizarRankingUI(ranking));
            atualizarLogsUI(logs);
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Erro na comunicação: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void atualizarRankingUI(List<ContadorGrupo> ranking) {
        ObservableList<ContadorGrupo> observableList = FXCollections.observableArrayList(ranking);
        tableViewLogsGrupos.setItems(observableList);
    }
    
    private void atualizarLogsUI(List<LogGrupo> logs) {
        if (logs.isEmpty()) {
            Platform.runLater(() -> labelLogIndividual.setText("Nenhum log encontrado para este grupo."));
            return;
        }

        Thread logThread = new Thread(() -> {
            try {
                while (true) {
                    for (LogGrupo log : logs) {
                        String brasilTimestamp = convertToBrasiliaTime(log.getTimestamp());
                        Platform.runLater(() -> {
                            labelLogIndividual.setText(brasilTimestamp);
                        });
                        Thread.sleep(2000);
                    }
                }
            } catch (InterruptedException e) {
                System.err.println("❌ Thread de logs interrompida: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        });
        logThread.setDaemon(true);
        logThread.start();
    }
    
    private String convertToBrasiliaTime(String timestamp) {
        try {
            // Formato que vem do servidor: "yyyy-MM-dd HH:mm:ss"
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime localDateTime = LocalDateTime.parse(timestamp, inputFormatter);
            
            // Supondo que o horário do servidor está em UTC
            ZonedDateTime utcDateTime = localDateTime.atZone(ZoneId.of("UTC"));
            
            // Convertendo para o fuso horário de Brasília
            ZonedDateTime brasiliaDateTime = utcDateTime.withZoneSameInstant(ZoneId.of("America/Sao_Paulo"));
            
            // Formato que queremos exibir na tela: "dd/MM/yyyy 'às' HH:mm:ss"
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm:ss");
            return brasiliaDateTime.format(outputFormatter);
        } catch (Exception e) {
            // Se houver qualquer erro na conversão, retorna o timestamp original
            System.err.println("❌ Não foi possível converter o horário para o fuso de Brasília: " + e.getMessage());
            return timestamp;
        }
    }
}
