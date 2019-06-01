/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pokerapp;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 *
 * @author Nebojsa
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label label;
    
    @FXML 
    private void switchScreen(Stage stage, String path) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(path));    
        stage.setFullScreen(true);
        stage.getScene().setRoot(root);
    }
    
    @FXML 
    private void newGame(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        switchScreen(stage, "GameBoard.fxml");
    }
    
    @FXML 
    private void credit(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        switchScreen(stage, "Credit.fxml");
    }
    @FXML
    private void exit() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Izlaz");
        alert.setHeaderText("Da li zelite da napustite aplikaciju?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            System.exit(0);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }    
    
}
