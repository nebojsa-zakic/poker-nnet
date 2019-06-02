/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pokerapp;

import game.Card;
import game.CardDeck;
import game.Player;
import game.PlayerDecision;
import game.Table;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.stage.Stage;
import nnet.NNetwork;
/**
 * FXML Controller class
 *
 * @author Nebojsa
 */
public class GameBoardController implements Initializable {
    @FXML Button next;
    @FXML Label Action;
    
    @FXML Button p1c1;
    @FXML Button p1c2;
    @FXML Button p2c1;
    @FXML Button p2c2;
    @FXML Button p3c1;
    @FXML Button p3c2;
    @FXML Button p4c1;
    @FXML Button p4c2;
    @FXML Button p5c1;
    @FXML Button p5c2;
    @FXML Button bCard1;
    @FXML Button bCard2;
    @FXML Button bCard3;
    @FXML Button bCard4;
    @FXML Button bCard5;

    @FXML Label Player1_Bet;
    @FXML Label Player1_Money;
    @FXML Label Player1_Name;
    
    @FXML Button Raise_Button;
    @FXML Button Fold_Button;
    @FXML TextField Player1_Bet_Input;
            
    @FXML Label Player2_Bet;
    @FXML Label Player2_Money;
    @FXML Label Player2_Name;
    
    @FXML Label Player3_Bet;
    @FXML Label Player3_Money;
    @FXML Label Player3_Name;
    
    @FXML Label Player4_Bet;
    @FXML Label Player4_Money;
    @FXML Label Player4_Name;
    
    @FXML Label Player5_Bet;
    @FXML Label Player5_Money;
    @FXML Label Player5_Name;
    
    @FXML Label Pot_Value;
    
    Table table;
    
    Player player1 = new Player("Nebojsa", 1000, true);
    Player player2 = new Player("Marko", 1000, false);
    Player player3 = new Player("Nikola", 1000, false);
    Player player4 = new Player("Jovan", 1000, false);
    Player player5 = new Player("Milovan", 1000, false);
    CardDeck deck = new CardDeck();

    UnaryOperator<Change> filter = change -> {
        String text = change.getText();

        if (text.matches("[0-9]*")) {
            return change;
        }

        return null;
    };
    
    private void dealCards() {
        table.roundStart();
        next.setText("Dalje");
        System.out.println(table.getBoardCard(0));
        setCard(bCard1, table.getBoardCard(0));
        setCard(bCard2, table.getBoardCard(1));
        setCard(bCard3, table.getBoardCard(2));
        if (player1.isInGame()) {
            player1.showCards();
        }
        updatePlayerData();
    }
    
    @FXML
    private void openNext() {
        if (next.getText().equals("Deli")) {
            unSetField();
            dealCards();
            Fold_Button.setDisable(false);
            Raise_Button.setDisable(false);
            Player1_Bet_Input.setDisable(false);
            return;
        }
        if (!next.getText().contains("Prati") && !next.getText().equals("Zovi")) {
            PlayerDecision.decideRaiseForAllAI(table);
            openCard();
        } else {
            if (next.getText().equals("Prati")) {
                follow();
                next.setText("Dalje");
                Raise_Button.setDisable(false);
                Player1_Bet_Input.setDisable(false);
            }
        }

        
        
        decideOpen();
        updatePlayerData();
    }
    
    private void decideOpen() {
        if (table.tableCardCount() < 5) {
            table.dealBoardCard();
            setCard(table.tableCardCount() == 4 ? bCard4 : bCard5, table.getBoardCard(table.tableCardCount() - 1));
            if (table.tableCardCount() == 5) {
                next.setText("Zovi");
            }
        } else {
            player2.showCards();
            player3.showCards();
            player4.showCards();
            player5.showCards();
            
            table.resolveResults();
            
            next.setText("Deli");
            Fold_Button.setDisable(true);
            Raise_Button.setDisable(true);
            Player1_Bet_Input.setDisable(true);
        }
    }
    
    private void openCard() {
        System.out.println("Round bet: " + table.getRoundBet());
            System.out.println("P1 bet: " + player1.getBet());
            if (table.getRoundBet() > player1.getBet()) {
                next.setText("Prati " + (table.getRoundBet() - player1.getBet()));
                Raise_Button.setDisable(true);
                Player1_Bet_Input.setDisable(true);
                
            } else {
                if(table.isOneRemaining()) {
                    next.setText("Deli");
                    Fold_Button.setDisable(true);
                    Raise_Button.setDisable(true);
                    Player1_Bet_Input.setDisable(true);
                    System.out.println("TEST");
                }
            }
            
            updatePlayerData();
    }
    
    @FXML
    public void playerFold() {
        fold(player1, 1);
        next.setText("Deli");
        Fold_Button.setDisable(true);
        Raise_Button.setDisable(true);
    }
    
    @FXML
    public void playerRaise() {
        raise(player1, Player1_Bet_Input.getText());
        
        updatePlayerData();
        if (table.roundPlayerCount() == 1) {
            next.setText("Deli");
            Fold_Button.setDisable(true);
            Raise_Button.setDisable(true);
            Player1_Bet_Input.setDisable(true);
        } else {
            openCard();
        }
        
    }
    
    public void follow() {
        if(!table.follow(player1)) {
            playerFold();
        } 
    }
    
    public boolean raise(Player player, String unparsedValue) {
        int value;
        if (unparsedValue.equals("") || unparsedValue.equals("0")) {
            return false;
        } else {
            value = Integer.parseInt(unparsedValue);
            return table.raise(player, value);
        }    
    }
    
    public void fold(Player player, int index) {
        table.playerFold(player);
        if (table.isOneRemaining()) {
            next.setText("Deli");
            Fold_Button.setDisable(true);
            Raise_Button.setDisable(true);
            Player1_Bet_Input.setDisable(true);
        }
        
        updatePlayerData();
    }
    
    private void unSetField() {
        table.resetAllCards();
        
        unSetCard(bCard1);
        unSetCard(bCard2);
        unSetCard(bCard3);
        unSetCard(bCard4);
        unSetCard(bCard5);
    }
    
    private void unSetCard(Button displayCard) {
        displayCard.setStyle("-fx-background-color: #ff0000; -fx-text-fill: #fff; -fx-border-color: white;");
        displayCard.setText("");
    }
    
    private void setCard(Button displayCard, Card card) {
        displayCard.setStyle("-fx-background-color: #fff; -fx-border-color: black;");
        if (card.getType() > 2) {
            displayCard.setStyle("-fx-text-fill: #ff0000; -fx-background-color: #fff; -fx-border-color: black;");
        }
        displayCard.setText(card.toString());
    }
    
    private void updatePlayerData() {
        table.updateDisplayedPlayerMoneyData();
        Pot_Value.setText("" + table.getPot());
    }
    
    @FXML 
    private void back(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setFullScreen(true);
        stage.getScene().setRoot(root);
    }
    @FXML 
    private void reset(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("GameBoard.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setFullScreen(true);
        stage.getScene().setRoot(root);
    }
    
    
    private void initUI() {
        PlayerUI p1ui = new PlayerUI(p1c1, p1c2, Player1_Name, Player1_Money, Player1_Bet);
        PlayerUI p2ui = new PlayerUI(p2c1, p2c2, Player2_Name, Player2_Money, Player2_Bet);
        PlayerUI p3ui = new PlayerUI(p3c1, p3c2, Player3_Name, Player3_Money, Player3_Bet);
        PlayerUI p4ui = new PlayerUI(p4c1, p4c2, Player4_Name, Player4_Money, Player4_Bet);
        PlayerUI p5ui = new PlayerUI(p5c1, p5c2, Player5_Name, Player5_Money, Player5_Bet);
        
        player1.setUI(p1ui);
        player2.setUI(p2ui);
        player3.setUI(p3ui);
        player4.setUI(p4ui);
        player5.setUI(p5ui);
        
        updatePlayerData();
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        table = new Table(50, Action);
        table.addPlayer(player1);
        table.addPlayer(player2);
        table.addPlayer(player3);
        table.addPlayer(player4);
        table.addPlayer(player5);
        
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        Player1_Bet_Input.setTextFormatter(textFormatter);
        initUI();
        
        NNetwork.loadNetwork();
    }    
    
}
