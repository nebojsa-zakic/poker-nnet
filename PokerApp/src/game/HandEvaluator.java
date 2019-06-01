/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Nebojsa
 */
public class HandEvaluator {
    private static HandEvaluator evaluate;
    
    private HandEvaluator() {
    }
    
    public static HandEvaluator get() {
        if (evaluate == null) {
            evaluate = new HandEvaluator();
        }
        return evaluate;
    }
    
    public void evaluateHand(Hand hand, List board) {
        List<Card> fullList = new ArrayList<>();
        int rank = 0;
        
        fullList.add(hand.getCard1());
        fullList.add(hand.getCard2());
        fullList.addAll(board);
        HashMap<Integer, Integer> cardValues = countCardValues(fullList);
        HashMap<Integer, Integer> cardTypes = countCardTypes(fullList);
        rank = checkCombinations(cardValues, hand);
        
        if (rank < 7) {
            int temp = checkTypes(cardTypes);
            rank = temp > 0 ? temp : rank;
        }
        if (rank == 0 || rank == 6) {
            rank += checkStraight(cardValues, hand);
        }
        
        
        hand.setRank(rank);
    }
    
     private int checkStraight(HashMap<Integer, Integer> cardValues, Hand hand) {
        List<Integer> sortedKeys=new ArrayList(cardValues.keySet());
        Collections.sort(sortedKeys);
        int count = 0;
        int lastVal = -1;
        if (hand.getRank() <= 6){
            for (int key : sortedKeys) {
                if (key == 1) {
                    key = 15;
                }
                if (key - 1 == lastVal) {
                    hand.setMax(key);
                    count += 1;
                } else {
                    if (count >= 5) {
                        return 5;
                    }
                    count = 0;
                    lastVal = key;
                }
            }
        }
        if (count < 5) {
            if (sortedKeys.get(0) != 1){
                hand.setMax(sortedKeys.get(sortedKeys.size() - 1));
            } else {
                hand.setMax(15);
            }
            int i = 2;
            do{
                hand.setMin(sortedKeys.get(sortedKeys.size() - i));
            } while (hand.getMax() == sortedKeys.get(sortedKeys.size() - (i++)));
        }
        return count >= 5 ? 5 : 0;
    }
    
    private int checkCombinations(HashMap<Integer, Integer> cardValues, Hand hand) {
        int pairs = 0;
        int trips = 0;
        int quads = 0;

        for (Map.Entry<Integer, Integer> entry : cardValues.entrySet()) {
            int key = entry.getKey();
            int count = entry.getValue();
            
            if (key == 1) {
                key = 15;
            }
            
            switch(count) {
                case 2:
                    hand.setMax(hand.getMax() < key ? key : hand.getMax());
                    hand.setMin(hand.getMin() > key ? key : hand.getMin());
                    pairs += 1;
                break;
                case 3:
                    hand.setMax(hand.getMax() < key ? key : hand.getMax());
                    hand.setMin(hand.getMin() > key ? key : hand.getMin());
                    trips += 1;
                break;
                case 4:
                    
                    hand.setMax(hand.getMax() < key ? key : hand.getMax());
                    hand.setMin(hand.getMin() > key ? key : hand.getMin());
                    quads += 1;
                break;
            }
        }
        
        return calcCountResult(pairs, trips, quads);
    }
    
    private int checkTypes(HashMap<Integer, Integer> cardTypes) {  
        for (Map.Entry<Integer, Integer> entry : cardTypes.entrySet()) {
                int count = entry.getValue();
                if (count >= 5) {
                    return 6;
                }
        }
        
        return 0;
    }
        
    private int calcCountResult(int pairs, int trips, int quads) {
        if (quads == 1) {
            return 8;
        }
        
        if (trips == 1 && pairs == 1) {
            return 7;
        }
        
        if (trips == 1) {
            return 3;
        }
        
        if (pairs == 2) {
            System.out.println("Two pairs");
            return 2;
        }
        
        if (pairs == 1) {
            return 1;
        }
        
        return 0;
    }
    
    private HashMap<Integer, Integer> countCardValues(List<Card> bothCards) {
        HashMap<Integer, Integer> cards = new HashMap();
        
        bothCards.forEach((card) -> {
            if (cards.containsKey(card.getValue())) {
                cards.put(card.getValue(), cards.get(card.getValue()) + 1);
            } else {
                cards.put(card.getValue(), 1);
            }
        });
        
        return cards;
    }
    
    private HashMap<Integer, Integer> countCardTypes(List<Card> bothCards) {
        HashMap<Integer, Integer> cards = new HashMap();
        bothCards.forEach((card) -> {
            if (cards.containsKey(card.getType())) {
                cards.put(card.getType(), cards.get(card.getType()) + 1);
            } else {
                cards.put(card.getType(), 1);
            }
        });
        
        return cards;
    }
}
