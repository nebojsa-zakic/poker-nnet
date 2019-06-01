/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

/**
 *
 * @author Nebojsa
 */
public class Hand {
    private int rank = 0;
    private int min = 16;
    private int max = 0;
    private Card card1, card2;
    
    public Hand() {}
    public Hand(int rank, int min, int max) {
        this.rank = rank;
        this.min = min;
        this.max = max;
    }
    
    public int compareHand(Hand hand2) {
        if (rank > hand2.getRank() || (rank == hand2.getRank() && max > hand2.getMax()) || (rank == hand2.getRank() && max == hand2.getMax() && min > hand2.getMin())) {
            return 1;
        }
        
        if (rank == hand2.getRank() && max == hand2.getMax() && min == hand2.getMin()) {
            return 0;
        }
        return -1;
    }
    
    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
            
    public Card getCard1() {
        return card1;
    }

    public void setCard1(Card card1) {
        this.card1 = card1;
    }

    public Card getCard2() {
        return card2;
    }

    public void setCard2(Card card2) {
        this.card2 = card2;
    }
    
    public String toString() {
        return card1 + " " + card2;
    }
}   
