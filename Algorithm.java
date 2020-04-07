import java.util.*;

class Algorithm {
    private List<Node> open;
    private List<Node> closed;
    private List<Node> path;
    private int[][] matrix;
    private Node now;
    private int rStart;
    private int cStart;
    private int rEnd, cEnd;
    //ROWS GO ACROSS
    //COLUMNS GO DOWN

    class Node implements Comparable {
        public Node parent;
        public int r, c;
        public double g;
        public double h;

        Node(Node parent, int rPos, int cPos, double g, double h) {
            this.parent = parent;
            this.r = rPos;
            this.c = cPos;
            this.g = g;
            this.h = h;
        }

        //Compare nodes by their f values
        @Override
        public int compareTo(Object o) {
            Node that = (Node) o;
            return (int) ((this.g + this.h) - (that.g + that.h));
        }
    }

    private Algorithm(int[][] matrix, int rStart, int cStart) {
        this.matrix = matrix;
        this.rStart = rStart;
        this.cStart = cStart;
        this.open = new ArrayList<>();
        this.closed = new ArrayList<>();
        this.path = new ArrayList<>();
        this.now = new Node(null, rStart, cStart, 0, 0);
    }

    private double manhattanDistance() {
        return Math.abs(this.now.r - this.rEnd) + Math.abs(this.now.c - this.cEnd);
    }

    private boolean findInList(List<Node> list, Node node) {
        for (Node value : list) {
            if (value.r == node.r && value.c == node.c) {
                return true;
            }
        }
        return false;
    }


    private void printMatrix() {
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }

    private void checkLeftToRight() {        //matrix[x][y+1/y-1]
        Node node;
        for(int col = -1; col <= 1; col ++) {
            node = new Node(this.now, this.now.r, this.now.c + col, this.now.g, manhattanDistance());
            if((this.now.c + col >= 0)
                    && col != 0
                    && (this.now.c + col < matrix[0].length)
                    && (matrix[this.now.r][this.now.c + col] != -1)
                    && (!findInList(this.closed, node))
                    && (!findInList(this.open, node))) {
                node.g = node.parent.g + 1;
                node.g += matrix[this.now.r][this.now.c + col];
                this.open.add(node);
            }
        }
        Collections.sort(open);     //Sort based on f values
    }

    private void checkUpToDown() {          //Matrix[x+1/x-1][y]
        Node node;
        for(int row = -1; row <= 1; row++) {
            node = new Node(this.now, this.now.r + row, this.now.c, this.now.g, manhattanDistance());
            if((this.now.r + row >= 0)
                    && row != 0
                    && (this.now.r + row < matrix.length)       //Within boundaries
                    && (matrix[this.now.r + row][this.now.c] != -1)   //Check position isn't blocked
                    && (!findInList(this.closed, node))     //Check its not already in lists
                    && (!findInList(this.open, node))) {
                node.g = node.parent.g + 1;
                node.g += matrix[this.now.r + row][this.now.c];
                this.open.add(node);
            }
        }
        Collections.sort(open);
    }

    private int findShortestPath(int rEnd, int cEnd) {
        this.rEnd = rEnd;
        this.cEnd = cEnd;                               //this.closed.add(this.now);
        checkLeftToRight();
        checkUpToDown();
        while((this.now.r != this.rEnd) && (this.now.c != this.cEnd)) {
            this.now = this.open.get(0);
            this.path.add(this.now);
            this.closed.add(this.now);
            checkUpToDown();
            checkLeftToRight();
        }
        return this.path.size();
    }

    public static void main(String[] args) {
        int[][] matrix = {  {  0, -1,  0,  0, },
                            {  0, -1,  0,  0, },
                            {  0,  -1,  0,  0, },
                            { 0,   0,   0, 0, },
                            {  0,  -1,  0, -1, },
                        };

        Algorithm algorithm = new Algorithm(matrix, 2, 3);
        int test = algorithm.findShortestPath(2, 0);
        algorithm.printMatrix();
        System.out.println("Total cost: ");
        matrix[algorithm.rStart][algorithm.cStart] = 5;
        matrix [algorithm.rEnd][algorithm.cEnd] = 6;
        algorithm.printMatrix();
    }
}

       /* int[][] matrix = new int[8][8];

    public void matrixFill() {
        for(int r = 0; r < matrix.length; r++) {
            for(int c = 0; c < matrix[r].length; c++) {
                matrix[r][c] = (int) (Math.random() * 5);
            }
        }
    }

    public void printMatrix() {
        for(int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
        //for(int r = 0; r < matrix.length; r++) {
            //for (int c = 0; c < matrix[r].length; c++) {
                //System.out.println(matrix[r][c] + "\t");
                //System.out.println(Arrays.deepToString(matrix));
            //}
        //}
    }

    public static void main(String[] args) {
        Algorithm algorithm = new Algorithm();
        algorithm.matrixFill();
        algorithm.printMatrix();
    }
}
        */
