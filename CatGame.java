import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.DijkstraUndirectedSP;
import edu.princeton.cs.algs4.Stack;

public class CatGame {
    EdgeWeightedGraph board;
    boolean[] marked;
    int catPosition;
    int size;
    int randNum;
    DijkstraUndirectedSP shortestPath;
    final int FREEDOM;

    public CatGame(int n) {
        size = n;
        board = new EdgeWeightedGraph(n * n + 1);
        marked = new boolean[n * n + 1];
        catPosition = index(n / 2, n / 2);
        boolean[][] hasEdge = new boolean[index(n,n)][index(n,n)];
        FREEDOM = n * n;

        //Set edges for inside vertices
        for (int row = 1; row < n - 1; row++) {
            for (int col = 1; col < n - 1; col++) {
                int v = index(row, col);
                if (!hasEdge[v][v - 1]) {
                    board.addEdge(new CatEdge(v, v - 1));
                    hasEdge[v][v - 1] = true;
                }
                if (!hasEdge[v][v + 1]) {
                    board.addEdge(new CatEdge(v, v + 1));
                    hasEdge[v][v + 1] = true;
                }
                if (!hasEdge[v][v - n - 1]) {
                    board.addEdge(new CatEdge(v, v - n - 1));
                    hasEdge[v][v - n - 1] = true;
                }
                if (!hasEdge[v][v - n]) {
                    board.addEdge(new CatEdge(v, v - n));
                    hasEdge[v][v - n] = true;
                }
                if (!hasEdge[v][v + n]) {
                    board.addEdge(new CatEdge(v, v + n));
                    hasEdge[v][v + n] = true;
                }
                if (!hasEdge[v][v + n - 1]) {
                    board.addEdge(new CatEdge(v, v + n - 1));
                    hasEdge[v][v + n - 1] = true;
                }
            }
        }
        //Set edges for outside vertices
        for (int i = 0; i < n; i++) {
            board.addEdge(new CatEdge(index(i, 0), FREEDOM));
            board.addEdge(new CatEdge(index(i, n - 1), FREEDOM));
            board.addEdge(new CatEdge(index(0, i), FREEDOM));
            board.addEdge(new CatEdge(index(n - 1, i), FREEDOM));
        }

        //Set random marked tiles at start
        randNum = (int) ((size/2)*(Math.random()));
        int row;
        int col;
        while (randNum > 0) {
            row = (int) (size*(Math.random()));
            col = (int) (size*(Math.random()));
            if (!marked(row, col) && index(row, col) != catPosition){
                markTile(row, col);
                randNum--;
            }
        }
    }
    
    //Convert row and column to array index
    private int index(int row, int col) {
        return (row * size) + col;
    }

    //Mark tile as blocked and move cat position along best path
    void markTile(int row, int col){
        marked[index(row, col)] = true;
        int v = index(row, col);
        for(Edge i : board.adj(v)){
            CatEdge e = (CatEdge) i;
            e.changeWeight();
        }
        //Only move cat after random tiles have been placed
        if(randNum ==0){
          shortestPath = new DijkstraUndirectedSP(board, catPosition);
          Stack<Edge> stack = (Stack) shortestPath.pathTo(FREEDOM);
          catPosition = (stack.pop()).other(catPosition);
        }
    }
    
    //Check if tile is marked
    boolean marked(int row, int col) {
        return marked[index(row, col)];
    }

    //Return tile where cat currently is.
    int[] getCatTile() {
        int col = catPosition % size;
        int row = catPosition / size;
        int[] result = { row, col };
        return result;
    }

    //Check if cat has escaped.
    boolean catHasEscaped() {
        return catPosition == FREEDOM;
    }

    //Check if cat has no possible moves
    boolean catIsTrapped() {
        for (Edge i : board.adj(catPosition)) {
            CatEdge e = (CatEdge) i;
            if (e.weight() != Double.POSITIVE_INFINITY)
                return false;
        }
        return true;
    }
}
