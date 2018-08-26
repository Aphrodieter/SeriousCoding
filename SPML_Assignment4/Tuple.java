/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.ru.ai.vroon.mdp;

/**
 *
 * @author CLEMENS
 */
public class Tuple {

    private Action a;
    private Double value;

    public Tuple(Action a, Double v) {
        this.a = a;
        value = v;

    }

    public Action getAction() {
        return a;
    }

    public Double getValue() {
        return value;
    }
    
    public void setValue(Double v){
        value=v;
    }

}
