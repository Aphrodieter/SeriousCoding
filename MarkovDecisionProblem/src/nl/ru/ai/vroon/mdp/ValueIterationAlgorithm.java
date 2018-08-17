/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.ru.ai.vroon.mdp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author christianlammers
 */
public class ValueIterationAlgorithm {

    MarkovDecisionProblem mdp;

    Double discount = 1.0;
    Double sigma = 0.0000001;
    int counter = 0;
    private int maxIterations = 10000;

    private Action[][] policy;

    public ValueIterationAlgorithm() {
        mdp = new MarkovDecisionProblem(10, 20);
        mdp.setInitialState(0, 0);
        policy = new Action[mdp.getWidth()][mdp.getHeight()];

    }

    /*
    The core value Iteration algorithm
     */
    public void valueIteration() {
        Map<Action, Double> utils;
        Double[][] stateUtilities = new Double[mdp.getWidth()][mdp.getHeight()];
        Double[][] oldStateUtilities = new Double[mdp.getWidth()][mdp.getHeight()];
        initStateUtility(oldStateUtilities);
        Boolean done = false;
        
        while (!done || counter >= maxIterations) {
            
            //loop through every state
            for (int x = 0; x < mdp.getWidth(); x++) {
                for (int y = 0; y < mdp.getHeight(); y++) {
                    double reward = mdp.getReward(x, y);
                    if (reward != mdp.getNoReward()) { //if the current state is a sink or an obstacle
                        stateUtilities[x][y] = reward;
                    } else {
                        
                        utils = getUtils(x, y, oldStateUtilities); // returns map of actions and their values
                        Tuple bestAction = getBestAction(utils, oldStateUtilities[x][y]); // returns the best action and the best value based on utils
                        stateUtilities[x][y] = reward + bestAction.getValue() * discount; // gives the current state a new expected value (bellmann equation)
                        
                        policy[x][y] = bestAction.getAction(); //adds the best action for every state to the list.
                    }
                    
                }

                
            }
            done = checkDifference(stateUtilities, oldStateUtilities, sigma);
            oldStateUtilities = duplicate(stateUtilities);
            System.out.println(counter);
            counter++;
        }
        
        for (int x = 0; x < mdp.getWidth(); x++) {
            for (int y = 0; y < mdp.getHeight(); y++) {
                System.out.println("(" + x + "," + y + ")" + " : " + stateUtilities[x][y] + " Action: " + policy[x][y]);
                
            }
        }
        
    }

    /*
    returns a HashMap of Actions and their corresponding expected values originating from the position x y.
     */
    private Map getUtils(int x, int y, Double[][] oldStateUtilities) {
        Map<Action, Double> utils = new LinkedHashMap<>();
        Double thisState = oldStateUtilities[x][y];
        int right = x + 1;
        int left = x - 1;
        int up = y + 1;
        int down = y - 1;

        if (right < mdp.getWidth()) {
            utils.put(Action.RIGHT, oldStateUtilities[right][y]);
        } else {
            utils.put(Action.RIGHT, thisState);
        }

        if (left >= 0) {
            utils.put(Action.LEFT, oldStateUtilities[left][y]);
        } else {
            utils.put(Action.LEFT, thisState);
        }

        if (up < mdp.getHeight()) {
            utils.put(Action.UP, oldStateUtilities[x][up]);
        } else {
            utils.put(Action.UP, thisState);
        }

        if (down >= 0) {
            utils.put(Action.DOWN, oldStateUtilities[x][down]);
        } else {
            utils.put(Action.DOWN, thisState);
        }

        return utils;
    }

    /**
     * Sets all expected values to 0.0
     *
     * @param stateValues
     */
    private void initStateUtility(Double[][] oldStateUtilities) {

        for (int i = 0; i < mdp.getWidth(); i++) {
            for (int j = 0; j < mdp.getHeight(); j++) {
                oldStateUtilities[i][j] = 0.0;
            }
        }
    }

    /*
    returns the best Action and the corresponding expected value for a state.
    
     */
    private Tuple getBestAction(Map<Action, Double> utils, Double valueOfThisState) {
        Double[] actionValues = new Double[4];

        actionValues[0] = utils.get(Action.UP) * mdp.getpPerform()
                + utils.get(Action.nextAction(Action.UP)) * mdp.getPSideStep() / 2
                + utils.get(Action.previousAction(Action.UP)) * mdp.getPSideStep() / 2
                + utils.get(Action.backAction(Action.UP)) * mdp.getpBackstep()
                + valueOfThisState * mdp.getPNoStep();
        actionValues[1] = utils.get(Action.RIGHT) * mdp.getpPerform()
                + utils.get(Action.nextAction(Action.RIGHT)) * mdp.getPSideStep() / 2
                + utils.get(Action.previousAction(Action.RIGHT)) * mdp.getPSideStep() / 2
                + utils.get(Action.backAction(Action.RIGHT)) * mdp.getpBackstep()
                + valueOfThisState * mdp.getPNoStep();
        actionValues[2] = utils.get(Action.DOWN) * mdp.getpPerform()
                + utils.get(Action.nextAction(Action.DOWN)) * mdp.getPSideStep() / 2
                + utils.get(Action.previousAction(Action.DOWN)) * mdp.getPSideStep() / 2
                + utils.get(Action.backAction(Action.DOWN)) * mdp.getpBackstep()
                + valueOfThisState * mdp.getPNoStep();
        actionValues[3] = utils.get(Action.LEFT) * mdp.getpPerform()
                + utils.get(Action.nextAction(Action.LEFT)) * mdp.getPSideStep() / 2
                + utils.get(Action.previousAction(Action.LEFT)) * mdp.getPSideStep() / 2
                + utils.get(Action.backAction(Action.LEFT)) * mdp.getpBackstep()
                + valueOfThisState * mdp.getPNoStep();

        Double bestValue = -999999999999.0;
        int bestIndex = 0;
        for (int i = 0; i < actionValues.length; i++) {
            if (actionValues[i] > bestValue) {
                bestValue = actionValues[i];
                bestIndex = i;
            }
        }

        Action bestAction = null;
        switch (bestIndex) {
            case 0:
                bestAction = Action.UP;
                break;
            case 1:
                bestAction = Action.RIGHT;
                break;
            case 2:
                bestAction = Action.DOWN;
                break;
            case 3:
                bestAction = Action.LEFT;
        }

        return new Tuple(bestAction, bestValue);
    }

    /*
    returns true if the difference between the current expected value and the previous expected value is less than sigma for all states.
     */
    private Boolean checkDifference(Double[][] stateUtilities, Double[][] oldStateUtilities, Double sigma) {
        Double difference = 0.0;
        for (int x = 0; x < mdp.getWidth(); x++) {
            for (int y = 0; y < mdp.getHeight(); y++) {
                difference = stateUtilities[x][y] - oldStateUtilities[x][y];
                if (Math.abs(difference) > sigma) {
                    return false;
                }

            }
        }
        return true;
    }

    /*
    deep copy of utility array
     */
    private Double[][] duplicate(Double[][] stateUtilities) {
        Double[][] clone = new Double[stateUtilities.length][stateUtilities[0].length];
        for (int i = 0; i < stateUtilities.length; i++) {
            for (int j = 0; j < stateUtilities[0].length; j++) {
                clone[i][j] = stateUtilities[i][j];
            }
        }
        return clone;
    }

    public Action getPolicy(int x, int y) {
        return policy[x][y];
    }

    /**
     * the agent performs the moves following the computed policy.
     */
    public void ApplyPolicy() {
        int xPos = 0;
        int yPos = 0;
        while (true) {
            xPos = mdp.getStateXPosition();
            yPos = mdp.getStateYPostion();
            if (getPolicy(xPos, yPos) != null) {
                mdp.performAction(getPolicy(xPos, yPos));
            } else {
                break;
            }
        }
        System.out.println("finished");
    }

}
