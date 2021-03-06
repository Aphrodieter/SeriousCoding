/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package primsmst;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Clemens
 */
public class Vertex implements Comparable<Vertex>{
         String label;
         int key;
         Vertex parent;
         public Vertex(String label){
             this.label = label;
         }
         
         //our own equal method for comparing to vertices with each other
         public boolean equals(Vertex other){
             if (this.getLabel().equals(other.getLabel()))
                 return true;
             return false;
         }
         
         public String toString(){
             return label;
         }
         
        
         
         public void setKey(int key){
             this.key = key;
         }
         
         public void setParent(Vertex parent){
             this.parent = parent;
         }

        public Vertex getParent() {
            return parent;
        }
         
         public int getKey(){
             return key;
         }
         
         public String getLabel(){
             return label;
         }
       

    //necessary for the PriorityQueue to compare vertices with regards to their key 
    @Override
    public int compareTo(Vertex t) {
        if (key < t.key)
            return -1;
        else if (key > t.key)
            return 1;
        else return 0;
    }
    }
