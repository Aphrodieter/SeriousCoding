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
    
    public RandomGraph(int nrV, int nrE){
        assert nrE >= nrV -1 : "number of edges to low";
        assert nrE <= (nrV*(nrV-1))/2 : "number of edges to high";
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
   

    private ArrayList<Vertex> generateVertices() {

        ArrayList<Vertex> vertices = new ArrayList();
        
        for (int i = 0; i < amountV; i++) {
            vertices.add(new Vertex(alphabet[i] + ""));

        }
        return vertices;
    }
    
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
        
        int remainEdges = amountE - MINEDGES;
        
        //add random edges till amount of edges is satisfied
        while (remainEdges > 0){
            start = v.get(rnd.nextInt(v.size()));
            dest = v.get(rnd.nextInt(v.size()));
            
            if (!start.equals(dest) && !existsEdge(start,dest)){
                w = rnd.nextInt(MAXWEIGHT -1 ) + 1;
                e.add(new Edge(w,start,dest));
                remainEdges--;
            }
        }
        
        
        
        
        g.setEdgeList(e);
        g.setVertList(v);
        
        
        return g;
    }
    
    public ArrayList<Vertex> deepCopy(ArrayList<Vertex> v){
        ArrayList<Vertex> vCopy = new ArrayList<>();
        for (Vertex ve : v){
            vCopy.add(ve);
            
        }
        return vCopy;
    }

    private char[] createAlphabet() {
        char[] alphabet = new char[26]; // new array// new array// new array// new array

        for (char ch = 'A'; ch <= 'Z'; ++ch)// fills alphabet array with the alphabet
        {
            alphabet[ch - 'A'] = ch;
        }
        return alphabet;
    }

    private boolean allConnected(ArrayList<Edge> edges) {
        
        for (Edge e : edges){
            
        }
        return true;
    }

    private boolean existsEdge(Vertex start, Vertex dest) {
        for (Edge ed: e){
            if (start.equals(ed.getStart()) && dest.equals(ed.getDest()) ||
                    start.equals(ed.getDest()) && dest.equals(ed.getStart()))
                return true;
            
        }
        return false;
    }

    
}
