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
public class Card {
    private int type;
    private int value;
    
    public Card(int type, int value) {
        this.type = type;
        this.value = value;
    }
    
    public void setValue(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }

    public int getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        char symbol = ' ';
        String val = "";
        switch(type) { 
            case 1: symbol = 'P'; break;
            case 2: symbol = 'C'; break;
            case 3: symbol = 'T'; break;
            case 4: symbol = 'H'; break;
        }
        
        switch(value) {
            case 1: val = "A"; break;
            case 12: val = "J"; break;
            case 13: val = "Q"; break;
            case 14: val = "K"; break;
            default: val = value + "";
        }
        
        return val + " " + symbol;
    }
    
}
