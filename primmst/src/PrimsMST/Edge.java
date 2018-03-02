/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PrimsMST;

/**
 *
 * @author Clemens
 */
public class Edge{
         int weight;
         Vertex destination;
         Vertex start;
         
         public Edge(int w, Vertex start, Vertex dest){
             weight = w;
             destination = dest;
             this.start = start;
         }
         
         public int getWeight(){
             return weight;
         }
         
         public Vertex getDest(){
             return destination;
         }

    public Vertex getStart() {
        return start;
    }
    }
