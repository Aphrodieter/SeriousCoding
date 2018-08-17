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
import java.util.stream.Collectors;

/**
 *
 * @author christianlammers
 */
public class ValueIterationAlgorithm {

    MarkovDecisionProblem mdp;
    
    Double discount;
    Double sigma = 0.1;
    int counter = 0; 

    public ValueIterationAlgorithm() {
        discount = 1.0;
        mdp = new MarkovDecisionProblem();
        mdp.setInitialState(0, 0);
        

    }

    public void valueIteration() {
        Map<Action, Double> utils;
        Map<Double, Action> reversedUtils;
        Double[][] stateUtilities = new Double[mdp.getWidth()][mdp.getHeight()];
        Double[][] oldStateUtilities = new Double[mdp.getWidth()][mdp.getHeight()];
        initStateUtility(oldStateUtilities);
        Boolean done = false;

        while (!done) {
            
            for (int x = 0; x < mdp.getWidth(); x++) {
                for (int y = 0; y < mdp.getHeight(); y++) {
                    //System.out.println(oldStateUtilities[x][y]);
                    //if (mdp.getField(x, y) != Field.OBSTACLE) {
                        utils = getUtils(x, y, oldStateUtilities);
                        Action bestAction = getBestAction(utils);
//                    reversedUtils = reverseMap(utils);
//                    Set<Double> values = reversedUtils.keySet();
//                    values.remove(null);
//                    Action bestAction = reversedUtils.get(Collections.max(values));
//                    utils = reversedUtils.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
                        //System.out.println(getBestAction(x, y) + " " + x + " , " + y);
                        Double reward = mdp.getReward(x,y);
                        stateUtilities[x][y] = mdp.getpPerform() * (reward + discount * utils.get(bestAction))
                                    + mdp.getSideStep() / 2 * (reward + discount * utils.get(Action.nextAction(bestAction)))
                                    + mdp.getSideStep() / 2 * (reward + discount * utils.get(Action.previousAction(bestAction)))
                                    + mdp.getpBackstep() * (reward + discount * utils.get(Action.backAction(bestAction)))
                                    + mdp.getPNoStep() * (reward + discount * oldStateUtilities[x][y]);
                    
                            
                        
                    }
                //}

            }
            done = checkDifference(stateUtilities, oldStateUtilities,sigma);
            oldStateUtilities = duplicate(stateUtilities);
            System.out.println(counter);
            counter++;
        }

        for (int x = 0; x < mdp.getWidth(); x++) {
            for (int y = 0; y < mdp.getHeight(); y++) {
                System.out.println("x: " + x + "y: " + y + " val: " + stateUtilities[x][y]);
            }
        }

    }

    private Map getUtils(int x, int y, Double[][] oldStateUtilities) {
        Map<Action, Double> utils = new LinkedHashMap<>();
        Double thisState = oldStateUtilities[x][y];
        int right = x + 1;
        int left = x - 1;
        int up = y + 1;
        int down = y - 1;
        if (right < mdp.getWidth()) {
            Double rightState = oldStateUtilities[right][y];
            if (rightState != null) {
                utils.put(Action.RIGHT, rightState);
            } else {
                utils.put(Action.RIGHT, thisState);
            }

        } else {
            utils.put(Action.RIGHT, thisState);
        }
        if (left >= 0) {
            Double leftState = oldStateUtilities[left][y];
            if (leftState != null) {
                utils.put(Action.LEFT, leftState);
            } else {
                utils.put(Action.LEFT, thisState);
            }
        } else {
            utils.put(Action.LEFT, thisState);
        }
        if (up < mdp.getHeight()) {
            Double upState = oldStateUtilities[x][up];
            if (upState != null) {
                utils.put(Action.UP, upState);
            } else {
                utils.put(Action.UP, thisState);
            }

        } else {
            utils.put(Action.UP, thisState);
        }
        if (down >= 0) {
            Double downState = oldStateUtilities[x][down];
            if (downState != null) {
                utils.put(Action.DOWN, downState);
            } else {
                utils.put(Action.DOWN, thisState);
            }
        } else {
            utils.put(Action.DOWN, thisState);
        }

        return utils;
    }

    /**
     * Sets all state values to 0 except for the negreward and reward state
     * which are respectively initialized with -100 and 100..
     *
     * @param stateValues
     */
    private void initStateUtility(Double[][] oldStateUtilities) {
//        for (int i = 0; i < mdp.getWidth(); i++) {
//            for (int j = 0; j < mdp.getHeight(); j++) {
//                if (mdp.getField(i, j) == Field.EMPTY) {
//                    oldStateUtilities[i][j] = 0.0;
//                } else if (mdp.getField(i, j) == Field.NEGREWARD) {
//                    oldStateUtilities[i][j] = -100.0;
//                } else if (mdp.getField(i, j) == Field.REWARD) {
//                    oldStateUtilities[i][j] = 100.0;
//                }
//            }
//
//        }
    for (int i = 0; i < mdp.getWidth(); i++) {
            for (int j = 0; j < mdp.getHeight(); j++) {
                oldStateUtilities[i][j] = 0.0;
            }
    }
    }

    private Map reverseMap(Map<Action, Double> utils) {
        Map<Double, Action> mapInversed = utils.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        return mapInversed;
    }

    private Action getBestAction(Map<Action, Double> utils) {
        Double bestValue = -10000000.0;
        Action bestAction = null;
        List<Double> values = new ArrayList(utils.values());
        List<Action> actions = new ArrayList(utils.keySet());
        for (int i = 0; i < utils.size(); i++) {
            Double value = values.get(i);
            if (value > bestValue) {
                bestValue = value;
                bestAction = actions.get(i);
            }

        }
        return bestAction;
    }

    private Boolean checkDifference(Double[][] stateUtilities, Double[][] oldStateUtilities,Double sigma) {
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

    private Double[][] duplicate(Double[][] stateUtilities) {
        Double[][] clone = new Double[stateUtilities.length][stateUtilities[0].length];
        for (int i = 0; i < stateUtilities.length; i++){
            for (int j = 0; j < stateUtilities[0].length; j++)
            clone[i][j] = stateUtilities[i][j];
        }
        return clone;
    }

    

}
