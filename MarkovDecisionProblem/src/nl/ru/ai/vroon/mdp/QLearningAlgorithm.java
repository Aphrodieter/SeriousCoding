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
    private int maxIterations = 5;
    private Random rnd;
    
    double discount = 0.9;
    Double alpha = 0.2;

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
                System.out.println(counter);


                int a = bestAction(x,y);
                double expFutValue = actionValue(a);
                QValues[x][y][a] = QValues[x][y][a] + alpha*(expFutValue - QValues[x][y][a]);

                x = mdp.getStateXPosition();
                y = mdp.getStateYPostion();





            }
            mdp.restart();
            counter++;
  
            
        }
        
        printTable();
        while (true){
            int bestAction = 0;
            double bestValue = -9999999;
            for (int a = 0; a < QValues[0][0].length; a++){
                if (QValues[mdp.getStateXPosition()][mdp.getStateYPostion()][a] > bestValue)
                    bestValue = QValues[mdp.getStateXPosition()][mdp.getStateYPostion()][a];
                    bestAction = a;
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
        }
        
        
        
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

    private Double getBestActionVal(int x, int y) {

        return null;
    }

    private int bestAction(int x, int y) {
        int[] possibleActions = getPossibleActions(x,y);
        
        //best action
//        int k = 0;
//        double bestValue = -999999999999.0;
//        for (int a : possibleActions){
//            if (QValues[x][y][a] > bestValue){
//                bestValue = QValues[x][y][a];
//                k = a;
//            }
//        }
        
        //random action
        int k = rnd.nextInt(possibleActions.length);
        return k;
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
        
        double maxAction = Arrays.stream(QValues[newX][newY]).max().getAsDouble();
        
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
}
