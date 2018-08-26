/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.ru.ai.vroon.mdp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author christianlammers
 */
public class QLearningAlgorithm {

    MarkovDecisionProblem mdp;
    double[][][] QValues;
    int counter = 0;
    private int maxIterations = 10000;
    private Random rnd;
    
    double discount = 0.9;
    double alpha = 0.2;
    
    double eps = 1.0;
    double eps_min = 0.01;
    double decay = 0.005;

    public QLearningAlgorithm(MarkovDecisionProblem m) {
        mdp = m;
        mdp.setInitialState(0, 0);
        rnd = new Random();
    }

    public void QLearning() {
        initializeQTable();
        
        for (int iter = 0; iter < maxIterations; iter++){
            
            int x = mdp.getStateXPosition();
            int y = mdp.getStateYPostion();
            while (mdp.getField(x, y) != Field.REWARD){
                try
                {Thread.sleep(0);}
                catch (Exception e)
                {e.printStackTrace();}
                
                
                
                
                
                int a = bestAction(x,y);
                double expFutValue = actionValue(a);
                double q = QValues[x][y][a];
                QValues[x][y][a] = q + alpha*(expFutValue - q);

                x = mdp.getStateXPosition();
                y = mdp.getStateYPostion();

                eps += -decay;
                if (eps < eps_min)
                    eps = eps_min;
            }
            mdp.restart(); 
        }
        
        printTable();
        
        mdp.setShowProgress(true);
        mdp.setWaittime(500);
        applyPolicy();
        
        
        
    }

    private void initializeQTable() {
        QValues = new double[mdp.getWidth()][mdp.getHeight()][4];
        for (int x = 0; x < mdp.getWidth(); x++) {
            for (int y = 0; y < mdp.getHeight(); y++) {
                for (int z = 0; z < QValues[0][0].length;z++)
                QValues[x][y][z] = 0.0; 
            }
        }

    }

   

    private int bestAction(int x, int y) {
        float exploitation = rnd.nextFloat();

        int[] possibleActions = getPossibleActions(x,y);
        int k = 0;

        if (exploitation > eps){ //exploitation{
        
        //best action
        double bestValue = -999999999999.0;
        for (int a : possibleActions){
            if (QValues[x][y][a] > bestValue){
                bestValue = QValues[x][y][a];
                k = a;
            }
        }
        return k;
    }
        else { //exploration
            k = rnd.nextInt(possibleActions.length);
            return possibleActions[k];
        }
        
        //random action
        //possibleActions[k];
    }

    private double actionValue(int a) {
        double reward = 0;
        switch(a){
            case 0: reward = mdp.performAction(Action.UP);
            break;
            case 1: reward = mdp.performAction(Action.RIGHT);
            break;
            case 2: reward = mdp.performAction(Action.DOWN);
            break;
            case 3: reward = mdp.performAction(Action.LEFT);
        }
        
        int newX = mdp.getStateXPosition();
        int newY = mdp.getStateYPostion();
        
        double maxAction = Arrays.stream(QValues[newX][newY]).max().getAsDouble();//getBestFutureAction(newX, newY);
        //System.out.println("x: " + newX + " y: " + newY + " value: " + maxAction);
        
        return reward + discount*maxAction;
    }
    
    //public double getExpectedValue(int x, int y)

    private void printTable() {
        for (int i = 0; i < QValues.length; i++){
            for (int j =0; j < QValues[0].length; j++){
                for (int z = 0; z < QValues[0][0].length;z++){
                    System.out.println("Value for " + "x: " + i + " y: " + j + " action: " + z + " : " + QValues[i][j][z]);
                }
            }
        }
    }

    private int[] getPossibleActions(int x, int y) {
        ArrayList<Integer> possibleActions = new ArrayList<>();
        for (int a = 0; a < QValues[0][0].length; a++){
            if (isPossible(x,y,a))
                possibleActions.add(a);
        }
        return possibleActions.stream().mapToInt(i->i).toArray();
    }

    private boolean isPossible(int x,int y,int a) {
        switch(a){
            case 0: return y+1 < mdp.getHeight() && mdp.getField(x, y+1) != Field.OBSTACLE;
            case 1: return x+1 < mdp.getWidth() && mdp.getField(x+1, y) != Field.OBSTACLE;
            case 2: return y-1 >= 0 && mdp.getField(x, y-1) != Field.OBSTACLE;
            case 3: return x-1 >= 0 && mdp.getField(x-1, y) != Field.OBSTACLE;
        }
        return true;
    }

    private double getBestFutureAction(int x, int y) {
        double[] actionValues = new double[4];
        actionValues[0] = QValues[x][y][0] * mdp.getpPerform() + QValues[x][y][1] * mdp.getPSideStep()/2 + QValues[x][y][2] * mdp.getpBackstep() + QValues[x][y][3] * mdp.getPSideStep()/2;
        actionValues[1] = QValues[x][y][1] * mdp.getpPerform() + QValues[x][y][0] * mdp.getPSideStep()/2 + QValues[x][y][2] * mdp.getPSideStep()/2 + QValues[x][y][3] * mdp.getpBackstep();
        actionValues[2] = QValues[x][y][2] * mdp.getpPerform() + QValues[x][y][1] * mdp.getPSideStep()/2 + QValues[x][y][3] * mdp.getPSideStep()/2 + QValues[x][y][0] * mdp.getpBackstep();
        actionValues[3] = QValues[x][y][3] * mdp.getpPerform() + QValues[x][y][0] * mdp.getPSideStep()/2 + QValues[x][y][2] * mdp.getPSideStep()/2 + QValues[x][y][1] * mdp.getpBackstep();
        
        return Arrays.stream(actionValues).max().getAsDouble();
    }

    private void applyPolicy() {
        int x = mdp.getStateXPosition();
        int y = mdp.getStateYPostion();
        while (mdp.getField(x, y) != Field.REWARD && mdp.getField(x, y) != Field.NEGREWARD){
            int bestAction = 0;
            double bestValue = -9999999;
            int[] possibleActions = getPossibleActions(x,y);
            for (int a : possibleActions){
                if (QValues[x][y][a] > bestValue){
                    bestValue = QValues[x][y][a];
                    bestAction = a;
                }
            }
            switch (bestAction){
                case 0: mdp.performAction(Action.UP);
                break;
                case 1: mdp.performAction(Action.RIGHT);
                break;
                case 2: mdp.performAction(Action.DOWN);
                break;
                case 3: mdp.performAction(Action.LEFT);
                break;
            }
            
            x = mdp.getStateXPosition();
            y = mdp.getStateYPostion();
        }
    }
}
