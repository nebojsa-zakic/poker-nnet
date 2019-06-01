/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Label;

/**
 *
 * @author Nebojsa
 */
public class Table {
    private int pot = 0;
    private int entry;
    private CardDeck deck;
    private List<Card> boardCards;
    private Label action;
    private ArrayList<Player> players;
    private ArrayList<Player> roundPlayers;
    private int roundBet;
    
    public Table(int entry, Label action) {
        this.entry = entry;
        this.action = action;
        players = new ArrayList();
    }
    
    public void addPlayer(Player player) {
        player.setInGame(true);
        players.add(player);
    }
    
    public void removePlayer(Player player) {
        player.setInGame(false);
        players.remove(player);
    }
    
    public void updateDisplayedPlayerMoneyData() {
        players.forEach((player) -> {
            player.updateMoneyData();
        });
    }
    
    public void resetAllCards() {
        players.forEach((player) -> {
            player.hideCards();
        });
    }
    
    public void playerFold(Player player) {
        roundPlayers.remove(player);
        player.setInRound(false);
        player.setBet(0);
        action.setText(action.getText() + player.getName() + " odustaje.\n");
        System.out.println("Player folded: " + player.getName());
        if (player.isHuman()) {
            for (int i = boardCards.size(); i < 5; i++) {
                PlayerDecision.decideRaiseForAllAI(this);
                dealBoardCard();
            }
            resolveResults();
        }
        
    }
    
    public void roundStart() {
        System.out.println("New round");
        deck = new CardDeck();
        boardCards = new ArrayList();
        roundPlayers = new ArrayList();
        roundBet = entry;
        action.setText("");
        dealBoardCard();
        dealBoardCard();
        dealBoardCard();
        
        players.forEach((player) -> {
            if(player.isInGame() && player.makeBet(entry)){                    
                player.setInRound(true);
                pot += entry;
                roundPlayers.add(player);
                player.getHand().setCard1(deck.dealCard());
                player.getHand().setCard2(deck.dealCard());
            } else {
                player.setInGame(false);
                player.setInRound(false);
            }
        });
    }
    

    public boolean follow(Player player) {
        int value = roundBet - player.getBet();
        if (player.makeBet(value)) {
            pot +=value;
        } else {
            playerFold(player);
        }
        
        return true;
    }
    
    
    public boolean raise(Player player, int value) {
        if (player.makeBet(value)) {
           
           action.setText(action.getText() + player.getName() + " dize " + value + " \n");
           System.out.println("Player raised: " + player.getName() + " by " + value);
           
            pot +=value;
            roundBet += value;
            
            PlayerDecision.decideFollowForAllAI(this);
            
            return true;
        }
        
        return false;
    }
    
    public boolean isOneRemaining() {
        if (roundPlayers.size() == 1) {
            roundPlayers.get(0).setBet(0);
            roundPlayers.get(0).recievePot(pot);
            pot = 0;
            action.setText(action.getText() + "Pobedio je " + roundPlayers.get(0).getName() + "\n");
            return true;
        }
        
        return false;
    }
    
    public void resolveResults() {
        int max = 0, pScore, maxIndex = -1, index = -1;
    
        roundPlayers.forEach((player) -> {
            HandEvaluator.get().evaluateHand(player.getHand(), boardCards);
            System.out.println(player.getHand().getRank() + " " + player.getHand().getMax() + " " + player.getHand().getMin());
            player.setBet(0);
        });
        
        for(Player p1: roundPlayers) {
            pScore = 0;
            index += 1;
            for(Player p2: roundPlayers) {
                pScore += p1.getHand().compareHand(p2.getHand());
            }
            if (max < pScore){ max = pScore; maxIndex = index;}
        }
        
        if (max != 0) {
            action.setText(action.getText() + "Pobedio je " + roundPlayers.get(maxIndex).getName());

            roundPlayers.get(maxIndex).recievePot(pot);
        } else {
            roundPlayers.forEach((player) -> {
                player.recievePot(pot / roundPlayers.size());
            });
        }
        
        roundPlayers.forEach((player) -> {
            player.setBet(0);
        });
        
        pot = 0;    
    }
    
    public Card dealBoardCard() {
        Card draw = deck.dealCard();
        boardCards.add(draw);
        
        return draw;
    }
    
    public Card getBoardCard(int i) {
        return boardCards.get(i);
    }
    
    public int roundPlayerCount() {
        return roundPlayers.size();
    }
    
    public int tableCardCount() {
        return boardCards.size();
    }
    
    public int getPot() {
        return pot;
    }
    
    public int getRoundBet() {
        return roundBet;
    }
    
    public List<Card> getBoardCards() {
        return boardCards;
    }
    
    public List<Player> getRoundPlayers() {
        return roundPlayers;
    }
   
}
