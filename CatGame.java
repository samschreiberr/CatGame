import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.DijkstraUndirectedSP;
import java.util.Random;

public class CatGame{
    EdgeWeightedGraph board;
    boolean[] marked;
    int catPosition;
    int size;
    DijkstraUndirectedSP shortestPath;

    public CatGame(int n){
        size = n;
        board = new EdgeWeightedGraph(n*n + 1);
        marked = new boolean[n*n + 1];
        catPosition = index(n/2, n/2);

        for(int row = 1; row < n-1; row++){
            for (int col = 1; col < n -1; col++){
                int v = index(row, col);
                board.addEdge(new CatEdge(v, v - 1));
                board.addEdge(new CatEdge(v, v + 1));
                board.addEdge(new CatEdge(v, v - n - 1));
                board.addEdge(new CatEdge(v, v - n));
                board.addEdge(new CatEdge(v, v + n));
                board.addEdge(new CatEdge(v, v + n -1));
            }
        }
        Random rand = new Random();
        int num = rand.nextInt(n /2);
        while(num > 0){
            int row = rand.nextInt(n * n);
            int col = rand.nextInt(n * n);
            while (marked(row, col)){
                row = rand.nextInt(n * n);
                col = rand.nextInt(n * n);
            }
            markTile(row, col);
            num --;
        }
    }
    private int index(int row, int col){
        return (row*size) + col;
    }
    void markTile(int row, int col){
        marked[index(row, col)] = true;
        int v = index(row, col);
        for(Edge i : board.adj(v)){
            CatEdge e = (CatEdge) i;
            e.changeWeight();
        }

        shortestPath = new DijkstraUndirectedSP(board, catPosition);
    }
    boolean marked(int row, int col){
        return marked[index(row, col)];
    }
    int[] getCatTile(){
        int col = catPosition % size;
        int row = catPosition / size;
        int[] result = {row, col};
        return result;
    }
    //boolean catHasEscaped(){}
    boolean catIsTrapped(){
        for(Edge i : board.adj(catPosition)){
            CatEdge e = (CatEdge) i;
            if(e.weight() == 1) return false;
        }
        return true;
    }
}
