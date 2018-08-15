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
public class ValueIterationAlgorithm {

    MarkovDecisionProblem mdp;
    Double[][] stateUtilities;
    Double[][] oldStateUtilities;

    public ValueIterationAlgorithm() {
        mdp = new MarkovDecisionProblem();
        mdp.setInitialState(0, 0);
        initStateUtility(oldStateUtilities);
    }

    public void valueIteration() {
        for (int x = 0; x < mdp.getWidth(); x++) {
            for (int y = 0; y < mdp.getHeight(); y++) {
                stateUtilities[x][y] = 
               
            }

        }

    }

    /**
     * Sets all state values to 0 except for the negreward and reward state
     * which are respectively initialized with -100 and 100..
     *
     * @param stateValues
     */
    private void initStateUtility(Double[][] oldStateUtilities) {
        stateUtilities = new Double[mdp.getWidth()][mdp.getHeight()];
        for (int i = 0; i < oldStateUtilities.length; i++) {
            for (int j = 0; j < oldStateUtilities[i].length; j++) {
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
