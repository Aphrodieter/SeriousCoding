/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.ru.ai.vroon.mdp;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author christianlammers
 */
public class ValueIterationAlgorithm {

    MarkovDecisionProblem mdp;
    Double[][] stateUtilities;
    Double[][] oldStateUtilities;

    public ValueIterationAlgorithm() {
        mdp = new MarkovDecisionProblem();
        mdp.setInitialState(0, 0);
        stateUtilities = new Double[mdp.getWidth()][mdp.getHeight()];
        oldStateUtilities = new Double[mdp.getWidth()][mdp.getHeight()];
        initStateUtility(oldStateUtilities);
        
    }

    public void valueIteration() {
        for (int x = 0; x < mdp.getWidth(); x++) {
            for (int y = 0; y < mdp.getHeight(); y++) {
                //System.out.println(oldStateUtilities[x][y]);
                System.out.println(getBestAction(x,y) + " " + x + " , " + y);
                //stateUtilities[x][y] = 
               
            }

        }

    }
    
    private Action getBestAction(int x, int y){
        Map<Double,Action> utils = new HashMap<>();
        int right = x + 1;
        int left = x - 1;
        int up = y + 1;
        int down = y - 1;
        if (right < mdp.getWidth())
            utils.put(oldStateUtilities[right][y],Action.RIGHT);
        if (left >= 0)
            utils.put(oldStateUtilities[left][y], Action.LEFT);
        if (up < mdp.getHeight())
            utils.put(oldStateUtilities[x][up], Action.UP);
        if (down >= 0)
            utils.put(oldStateUtilities[x][down], Action.DOWN);
        
        Set<Double> values = utils.keySet();
        values.remove(null);
        Action bestAction = utils.get(Collections.max(values)); 
        
        return bestAction;
    }

    /**
     * Sets all state values to 0 except for the negreward and reward state
     * which are respectively initialized with -100 and 100..
     *
     * @param stateValues
     */
    private void initStateUtility(Double[][] oldStateUtilities) {
        for (int i = 0; i < mdp.getWidth(); i++) {
            for (int j = 0; j < mdp.getHeight(); j++) {
                if (mdp.getField(i, j) == Field.EMPTY) {
                    oldStateUtilities[i][j] = 0.0;
                } else if (mdp.getField(i, j) == Field.NEGREWARD) {
                    oldStateUtilities[i][j] = -1.0;
                } else if (mdp.getField(i, j) == Field.REWARD) {
                    oldStateUtilities[i][j] = 1.0;
                }
            }

        }
    }

}
