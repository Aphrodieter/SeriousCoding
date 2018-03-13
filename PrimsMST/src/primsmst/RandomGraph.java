/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package primsmst;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author christianlammers
 */
public class RandomGraph {

    private char[] alphabet;
    private Random rnd;
    private final int MAXWEIGHT = 40;
    private final int MAXVERTICES = 26;
    private final int MAXEDGES;
    private final int MINEDGES;
    int n ;
    ArrayList<Edge> e;
    ArrayList<Vertex> v;
    int amountV;
    int amountE;
    
    //general constructor for purely random results
    public RandomGraph() {
        alphabet = createAlphabet();
        rnd = new Random();
        e = new ArrayList<>();
        amountV = rnd.nextInt(MAXVERTICES);
        v = generateVertices();
        n = v.size();
        
        MAXEDGES = (n * (n-1))/2;
        MINEDGES = n-1;
        amountE = rnd.nextInt(MAXEDGES-MINEDGES)+MINEDGES;

    }
    
    //Constructor for the case where you know exactly how many vertices/edges you need
    public RandomGraph(int nrV, int nrE){
        assert nrE >= nrV -1 : "number of edges to low";
        assert nrE <= (nrV*(nrV-1))/2 : "number of edges to high";
        assert nrV <= MAXVERTICES : "Number of vertices may not exeed " + MAXVERTICES;
        amountV = nrV;
        amountE = nrE;
        alphabet = createAlphabet();
        rnd = new Random();
        e = new ArrayList<>();
        v = generateVertices();
        n = v.size();
        MAXEDGES = (n * (n-1))/2;
        MINEDGES = n-1;
        
    }
   

    //generates amountV Vertices and puts them in a list
    private ArrayList<Vertex> generateVertices() {

        ArrayList<Vertex> vertices = new ArrayList();
        
        for (int i = 0; i < amountV; i++) {
            vertices.add(new Vertex(alphabet[i] + ""));

        }
        return vertices;
    }
    
    //main function for generating random graph
    public Graph generateGraph(){
        Graph g = new Graph();
        int indRoot = rnd.nextInt(v.size());
        Vertex root = v.get(indRoot);
        int w = 0;
        
        ArrayList<Vertex> vCopy = deepCopy(v);
        Vertex start,dest;
        start = root;
        vCopy.remove(indRoot);
        
        //connect all vertices randomly but safely with each other so that all Vertices are connected with each other and every vertex has its own start/dest
        while (!vCopy.isEmpty()){
            int indDest = rnd.nextInt(vCopy.size());
            dest = vCopy.get(indDest);
            w = rnd.nextInt(MAXWEIGHT - 1) + 1;
            e.add(new Edge(w,start,dest));
            start = dest;
            vCopy.remove(indDest);
        }
        
        //put all edges in a list which are still possible
        ArrayList<Edge> leftEdges = getLeftEdges();
        
        int remainEdges = amountE - MINEDGES;
        //add random edges till amount of edges is satisfied
        for (int i = 0; i < remainEdges; i++){
            int ind = rnd.nextInt(leftEdges.size());
            e.add(leftEdges.get(ind));
            leftEdges.remove(ind);
        }
        
        //alternative method of generating random edges which could result in a better performance in some cases
//        while (remainEdges > 0){
//            start = v.get(rnd.nextInt(v.size()));
//            dest = v.get(rnd.nextInt(v.size()));
//            
//            if (!start.equals(dest) && !existsEdge(start,dest)){
//                w = rnd.nextInt(MAXWEIGHT -1 ) + 1;
//                e.add(new Edge(w,start,dest));
//                remainEdges--;
//            }
//        }
        g.setEdgeList(e);
        g.setVertList(v);
        g.setRoot(root);
        
        return g;
    }
    
    //copies every element of the list v into another list vCopy
    private ArrayList<Vertex> deepCopy(ArrayList<Vertex> v){
        ArrayList<Vertex> vCopy = new ArrayList<>();
        for (Vertex ve : v){
            vCopy.add(ve);
            
        }
        return vCopy;
    }

    //method for creating a list filled with the alphabet
    private char[] createAlphabet() {
        char[] alphabet = new char[26];

        for (char ch = 'A'; ch <= 'Z'; ++ch)
        {
            alphabet[ch - 'A'] = ch;
        }
        return alphabet;
    }

    //checks both the general edge list "e" and the given list "edges" 
    //wether start and dest form an edge which is already existant in eather one of them
    private boolean existsEdge(Vertex start, Vertex dest, List<Edge> edges) {
        for (Edge ed: edges){
            if (start.equals(ed.getStart()) && dest.equals(ed.getDest()) ||
                    start.equals(ed.getDest()) && dest.equals(ed.getStart())){
                return true;
            } 
        }
        for (Edge ed: e){
            if (start.equals(ed.getStart()) && dest.equals(ed.getDest()) ||
                    start.equals(ed.getDest()) && dest.equals(ed.getStart())){
                return true;
        }
        
    }
                return false;
    }

    //calculates which edges are still possible to be added to the edge list e
    private ArrayList<Edge> getLeftEdges() {
        int w =0;
        ArrayList<Edge> leftEdges = new ArrayList<>();
        for (int i = 0; i < v.size(); i++){
            for (int j = 0; j < v.size(); j++){
                if (i != j && !existsEdge(v.get(i),v.get(j),leftEdges)){
                    w = rnd.nextInt(MAXWEIGHT -1 ) + 1;
                    leftEdges.add( new Edge(w,v.get(i),v.get(j)));
                }
            }
        } 
        return leftEdges;
    }

}