/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PrimsMST;

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
       

         
//         public boolean equals(Vertex other){
//             return this.getLabel().equals(other.getLabel());
//         }
    @Override
    public int compareTo(Vertex t) {
        if (key < t.key)
            return -1;
        else if (key > t.key)
            return 1;
        else return 0;
    }
    }
