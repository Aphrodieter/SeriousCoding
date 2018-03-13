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
public class PrimsMST {
               
    static int count = 0; //runtime measure
    //method for creating a graph with the exact wishes of the user
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
    
    //main algorithm for computing a MST
    public void MST(Graph G){
        List<Vertex> path = new ArrayList<>();
         for (Vertex v : G.getV()){
             v.setKey(Integer.MAX_VALUE);
             v.setParent(null);
         }
         G.getRoot().setKey(0);
         
         PriorityQueue Q = new PriorityQueue(G.getV());
         
         count = 0;
         Vertex u;
         while (!Q.isEmpty()){
             u = (Vertex) Q.remove();
             path.add(u);
             for (Vertex v: G.getAdjacent(u)){
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
         
         //alternative way of getting the final MST path
//         for (Vertex v : G.getV()){
//             System.out.println(v.getParent() + "-->" + v.getLabel());
//         }

        String p = "";
        for (Vertex v: path){
            p += v.getLabel() + "\n"; 
        }
        System.out.println("MST path : \n" + p);
        System.out.println("Edges considered: " + count);
         
    }
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrayList<String> measures = new ArrayList<>();
        ArrayList<String> ver = new ArrayList<>();
        ArrayList<String> edg = new ArrayList<>();
        ArrayList<String> run = new ArrayList<>();

        for (int v = 4; v < 10; v++){
            for (int e = v-1; e <= v*(v-1)/2; e++){
            RandomGraph rg = new RandomGraph(v,e);
            Graph g = rg.generateGraph();
            //System.out.println(g);
            PrimsMST mst = new PrimsMST();
            mst.MST(g);
            measures.add("Vertices: " + v + "\n" +
                    "Edges: " + e + "\n" + 
                    "Runtime: " + count + "\n" +
                    "---------------------------" + "\n");
            ver.add(v + " ");
            edg.add(e + " ");
            run.add(count + " ");
        }
            //System.out.println(measures);
            System.out.println(ver);
            System.out.println(edg);
            System.out.println(run);
        }
          
        
    }
    
}
