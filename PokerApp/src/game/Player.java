/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import pokerapp.PlayerUI;

/**
 *
 * @author Nebojsa
 */
public class Player {
    private String name;
    private int money;
    private int bet = 0;
    private Hand hand;
    private boolean isHuman;
    private boolean inGame = false;
    private boolean inRound = false;
    private PlayerUI ui;
    
    public Player(String name, int money, boolean isHuman) {
        this.name = name;
        this.money = money;
        this.hand = new Hand();
        this.isHuman = isHuman;
    }
    
    public void updateMoneyData() {
        ui.updatePlayerData(money, bet);
    }
    
    public boolean makeBet(int value) {
        if (money - value < 0) {
            return false;
        }
        
        money -= value;
        bet += value;
        return true;
    }
    
    public boolean isHuman() {
        return isHuman;
    }
    
    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public boolean isInRound() {
        return inRound;
    }

    public void setInRound(boolean inRound) {
        this.inRound = inRound;
    }
    
    public void recievePot(int value) {
        bet = 0;
        money += value;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }
    
    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }
    
    public void setUI(PlayerUI ui) {
        this.ui = ui;
        ui.updateName(name);
    }
    
    public void showCards() {
        if (inRound) {
            ui.showCards(hand.getCard1(), hand.getCard2());
        }
    }
    
    public void hideCards() {
        ui.hideCards();
    }
    
}
