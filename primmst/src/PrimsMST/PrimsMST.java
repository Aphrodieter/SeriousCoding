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
public class PrimsMST {
    
    public Graph createGraph(){
        Graph G = new Graph();
        
        Vertex a = new Vertex("A");
        Vertex b = new Vertex("B");
        Vertex c = new Vertex("C");
        Vertex d = new Vertex("D");
        Vertex e = new Vertex("E");
        Vertex f = new Vertex("F");
        Vertex g = new Vertex("G");
        Vertex h = new Vertex("H");
        Vertex i = new Vertex("I");
        
        Edge ab = new Edge(4,a,b);
        Edge ah = new Edge(8,a,h);
        Edge bc = new Edge(8,b,c);
        Edge bh = new Edge(11,b,h);
        Edge hg = new Edge(1,h,g);
        Edge hi = new Edge(7,h,i);
        Edge ic = new Edge(2,i,c);
        Edge ig = new Edge(6,i,g);
        Edge cf = new Edge(4,c,f);
        Edge cd = new Edge(7,c,d);
        Edge df = new Edge(14,d,f);
        Edge de = new Edge(9,d,e);
        Edge fe = new Edge(10,f,e);
        Edge gf = new Edge(2,g,f);
        
        G.addEdge(ab);
        G.addEdge(ah);
        G.addEdge(bc);
        G.addEdge(bh);
        G.addEdge(hg);
        G.addEdge(hi);
        G.addEdge(ic);
        G.addEdge(ig);
        G.addEdge(cf);
        G.addEdge(cd);
        G.addEdge(df);
        G.addEdge(de);
        G.addEdge(fe);
        G.addEdge(gf);
        
        
        G.addVertex(a);
        G.addVertex(b);
        G.addVertex(c);
        G.addVertex(d);
        G.addVertex(e);
        G.addVertex(f);
        G.addVertex(g);
        G.addVertex(h);
        G.addVertex(i);
        
        G.setRoot(a);
        
        return G;
    }
    
    public void MST(Graph G){
         for (Vertex v : G.getV()){
             v.setKey(Integer.MAX_VALUE);
             v.setParent(null);
         }
         G.getRoot().setKey(0);
         
         PriorityQueue Q = new PriorityQueue(G.getV());
         
         
         int count = 0;
         Vertex u;
         while (!Q.isEmpty()){
             u = (Vertex) Q.remove();
             for (Vertex v: G.getAdjacent(u)){
                 //System.out.println(u.getLabel() + "----->" + v.getLabel());
                              count++;

                 int w = G.returnWeight(u, v);
                 if (Q.contains(v) && (w < v.getKey())){
                     v.setParent(u);
                     v.setKey(w);
                     for(int k=Q.size()/2; k >= 1; k--)
                        {
                            Q.heapify(k);
                        }
                 }
             }
         }
         
         for (Vertex v : G.getV()){
             System.out.println(v.getParent() + "-->" + v.getLabel());
         }
         System.out.println(count);
         
    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PrimsMST mst = new PrimsMST();
        Graph g = mst.createGraph();
        System.out.println(g);
        mst.MST(g);

          
        
        
    }
    
}
