/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Nebojsa
 */
public class CardDeck {
    private Set cards;
    
    public CardDeck() {
        cards = new HashSet<Card>();
        shuffleDeck();
    }
    
    public Card dealCard() {
        int size = cards.size();
        int item = new Random().nextInt(size);
        int i = 0;
        
        for(Object obj : cards) {
            if (i == item){
                cards.remove(obj);
                return (Card)obj;
            }
            i++;
        }
        
        return null;
    }
    
    private void shuffleDeck() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 14; j++) {
                if (j == 10) {
                    continue;
                }
                cards.add(new Card(i + 1, j + 1));
            }
        }
    }
}
