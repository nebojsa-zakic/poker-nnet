/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pokerapp;

import game.Card;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 *
 * @author user777
 */
public class PlayerUI {
    private final static String HIDDEN_STYLE = "-fx-background-color: #ff0000; -fx-text-fill: #fff; -fx-border-color: white;";
    private final static String RED_STYLE = "-fx-text-fill: #ff0000; -fx-background-color: #fff; -fx-border-color: black;";
    private final static String BLACK_STYLE = "-fx-background-color: #fff; -fx-border-color: black;";
    
    private Button card1;
    private Button card2;

    private Label name;
    private Label money;
    private Label bet;
    
    public PlayerUI(Button card1, Button card2, Label name, Label money, Label bet) {
        this.card1 = card1;
        this.card2 = card2;
        this.name = name;
        this.money = money;
        this.bet = bet;
    }
    
    
    public void updateName(String nameValue) {
        name.setText(nameValue);
    }
    
    public void updatePlayerData(int moneyValue, int betValue) {
        money.setText("Novac: " + moneyValue);
        bet.setText("Ulog: " + betValue);
    }
    
    public void hideCards() {
        card1.setStyle(HIDDEN_STYLE);
        card1.setText("");
        card2.setStyle(HIDDEN_STYLE);
        card2.setText("");        
    }
    
    public void showCards(Card c1, Card c2) {
        card1.setStyle(BLACK_STYLE);
        if (c1.getType() > 2) {
            card1.setStyle(RED_STYLE);
        }
        card1.setText(c1.toString());
        
        card2.setStyle(BLACK_STYLE);
        if (c2.getType() > 2) {
            card2.setStyle(RED_STYLE);
        }
        card2.setText(c2.toString());
    }
    
    public Button getCard1() {
        return card1;
    }

    public void setCard1(Button card1) {
        this.card1 = card1;
    }

    public Button getCard2() {
        return card2;
    }

    public void setCard2(Button card2) {
        this.card2 = card2;
    }
    
    
    
}
