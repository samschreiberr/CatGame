import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.Edge;

public class CatEdge extends Edge{
    private double weight;
    public CatEdge(int v, int w){
        super(v, w, 1);
    }
    public void changeWeight(){
        weight = Double.POSITIVE_INFINITY;
    }
    public double weight(){
        return weight;
    }
}