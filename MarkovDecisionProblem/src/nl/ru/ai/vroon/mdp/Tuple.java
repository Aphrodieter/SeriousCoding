/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.ru.ai.vroon.mdp;

/**
 *
 * @author christianlammers
 */
public class Tuple<Action, Value> {

    public final Action a;
    public final Value v;

    public Tuple(Action a, Value v) {
        this.a = a;
        this.v = v;
    }
    
    
}
