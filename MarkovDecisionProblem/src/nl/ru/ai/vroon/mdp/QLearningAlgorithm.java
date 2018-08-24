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
public class QLearningAlgorithm {

    MarkovDecisionProblem mdp;
    Tuple[][] QValues;
    int counter = 0;
    private int maxIterations = 10000;
    
    Double alpha;

    public QLearningAlgorithm(MarkovDecisionProblem m) {
        mdp = m;
        mdp.setInitialState(0, 0);
    }

    public void QLearning() {
        initializeQTable(QValues);
        Boolean done = false;

        while (!done || counter >= maxIterations) {

            //loop through every state
            for (int x = 0; x < mdp.getWidth(); x++) {
                for (int y = 0; y < mdp.getHeight(); y++) {
                    Double oldVal = QValues[x][y].getValue();
                    Double newVal = oldVal + alpha*getBestActionVal(x, y);
                    QValues[x][y].setValue(newVal);

                }
            }
        }
        
        
        
    }

    private void initializeQTable(Tuple[][] QValues) {
        QValues = new Tuple[mdp.getWidth()][mdp.getHeight()];
        for (int x = 0; x < mdp.getWidth(); x++) {
            for (int y = 0; y < mdp.getHeight(); y++) {
                QValues[x][y] = new Tuple(Action.UP, 0.0);
            }
        }

    }

    private Double getBestActionVal(int x, int y) {

        return null;
    }
}
