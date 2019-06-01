/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.ArrayList;
import nnet.HandNormalizer;
import nnet.NNetwork;
import org.neuroph.core.data.DataSetRow;

/**
 *
 * @author Nebojsa
 */
public class PlayerDecision {
    
    public static void decideRaiseForAllAI(Table table) {
        System.out.println("Deciding AI raise");
        for(int i = 0; i < table.getRoundPlayers().size(); i++){
            if (!table.getRoundPlayers().get(i).isHuman()) {
                int raise = decideRaiseAI(table, table.getRoundPlayers().get(i));
                if (raise > 0 && table.raise(table.getRoundPlayers().get(i), raise)) {
                    return;
                }
            }
        }
    }
         
    public static void decideFollowForAllAI(Table table) {
        for(int i = 0; i < table.getRoundPlayers().size(); i++){
            if (!table.getRoundPlayers().get(i).isHuman()) {
                if (!decideFollowAI(table, table.getRoundPlayers().get(i))) {
                    table.playerFold(table.getRoundPlayers().get(i));
                } else {
                    table.follow(table.getRoundPlayers().get(i));
                }
            }
        }
    }
    
    private static String createRowData(Table table, Player player) {
        String boardRes = "";
        for (Card card: table.getBoardCards()) {
            boardRes += card.toString() + " ";
        }
        
        for (int i = table.tableCardCount(); i <= 5; i++) {
            boardRes += "0 0 ";
        }
        
        if(table.tableCardCount() == 5) {
            boardRes = " ";
        }
        
        return player.getHand().toString() + " " + boardRes +
               player.getMoney() + " " + table.getPot() + " " + player.getBet();
    }
    
    private static double[] getAIDecisionForPlayer(Table table, Player player) {
        double[] result = NNetwork.evaluate(new DataSetRow(HandNormalizer.parseLine(createRowData(table, player))));
        System.out.println("Name: " + player.getName());
        System.out.println("Chance: " + result[0]);
        System.out.println("Bet: " + result[1]);
        System.out.println("Hand " + player.getHand());
        return result;
    }
    
    private static int decideRaiseAI(Table table, Player player) {
       if (player.getBet() < table.getRoundBet()) {
           return -2;
       }
       HandEvaluator.get().evaluateHand(player.getHand(), table.getBoardCards());

       double[] evaluation = getAIDecisionForPlayer(table, player);
       if (evaluation[0] > 0.5) {
          return (int)(10000 * evaluation[1]);
       } else {
           return -1;
       }
          
    }
    
    private static boolean decideFollowAI(Table table, Player player) {
       if (table.getRoundBet() <= player.getBet()) {
           return true;
       }
       
       HandEvaluator.get().evaluateHand(player.getHand(), table.getBoardCards());
       double decision = getAIDecisionForPlayer(table, player)[0];

       if (0.5 < decision && player.getMoney() > (table.getRoundBet() - player.getBet())) {
           return true;
       } else {
           return false; 
       }
          
    }
    
}
