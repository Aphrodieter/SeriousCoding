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
 public class Graph{
         List<Vertex> V = new ArrayList<>();
         List<Edge> E = new ArrayList<>();
         Vertex root;
         
         public void addVertex(Vertex v){
             V.add(v);
         }
         
         public void addEdge(Edge e){
             E.add(e);
         }

        public Vertex getRoot() {
            return root;
        }

        public void setRoot(Vertex root) {
            this.root = root;
        }

        public List<Vertex> getV() {
            return V;
        }
         
         @Override
         public String toString(){
             String s = "";
            
             return s;
         }
         
         public ArrayList<Vertex> getAdjacent(Vertex v){
             ArrayList<Vertex> adjacent = new ArrayList();
             for (Edge e : E){
                 if (e.getStart().equals(v))
                     adjacent.add(e.getDest());
                 else if (e.getDest().equals(v))
                     adjacent.add(e.getStart());
                     
             }
             return adjacent;
         }
         
         public int returnWeight(Vertex a, Vertex b){
             for (Edge e: E){
                 if (e.getStart().equals(a) && e.getDest().equals(b) ||
                         e.getStart().equals(b) && e.getDest().equals(a))
                     return e.getWeight();
             }
             System.out.println("Edge doesn't exist");
             return -1;
         }
    }
