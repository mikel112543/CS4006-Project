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
        public double f;

        Node(Node parent, int rPos, int cPos, double g, double h, double f) {
            this.parent = parent;
            this.r = rPos;
            this.c = cPos;
            this.g = g;
            this.h = h;
            this.f = h + g;
        }

        public double getF() {
            return f;
        }

        //Compare nodes by their f values & sorting by lowest
        @Override
        public int compareTo(Object o) {
            Node that = (Node) o;
            return (int) ((int) this.getF() - that.getF());
        }

    }

    private Algorithm(int[][] matrix, int rStart, int cStart) {
        this.matrix = matrix;
        this.rStart = rStart;
        this.cStart = cStart;
        this.open = new ArrayList<>();
        this.closed = new ArrayList<>();
        this.path = new ArrayList<>();
        this.now = new Node(null, rStart, cStart, 0, 0, 0);
    }

    /**
     *
     * @return      Estimated distance between start and end nodes i.e h value
     */
    private double manhattanDistance() {
        return Math.abs(this.now.r - this.rEnd) + Math.abs(this.now.c - this.cEnd);
    }

    /**
     *
     * @param list  specified list to check for nodes
     * @param node  specified node to check for
     * @return      true if node exists in list
     */
    private boolean findInList(List<Node> list, Node node) {
        for (Node value : list) {
            if (value.r == node.r && value.c == node.c) {
                return true;
            }
        }
        return false;
    }

    /**
     * Prints matrix in grid format
     */
    private void printMatrix() {
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }

    /**
     *      Checks nodes to the left and right of current position if they satisfy
     */
    private void checkUpToDown() {                                   //matrix[x][y+1/y-1]
        Node node;
        for(int col = -1; col <= 1; col ++) {
            node = new Node(this.now, this.now.r, this.now.c + col, this.now.g, manhattanDistance(), this.now.f);
            if((this.now.c + col >= 0)
                    && (col != 0)
                    && (this.now.c + col < matrix[0].length)
                    && (!findInList(this.closed, node))
                    && (!findInList(this.open, node))
                    && (matrix[this.now.r][this.now.c + col] != -1)) {
                node.g = node.parent.g + 1;
                node.g += matrix[this.now.r][this.now.c + col];
                this.open.add(node);
            }
        }
        //Sort based on f values
        Collections.sort(open);
    }

    /**
     *      Check nodes above and below current position if they satisfy
     */
    private void checkLeftToRight() {                                    //Matrix[x+1/x-1][y]
        Node node;
        for(int row = -1; row <= 1; row++) {
            node = new Node(this.now, this.now.r + row, this.now.c, this.now.g, manhattanDistance(), this.now.f);
            if((this.now.r + row >= 0)
                    && row != 0
                    && (this.now.r + row < matrix.length)             //Within boundaries
                    && (matrix[this.now.r + row][this.now.c] != -1)   //Check position isn't blocked
                    && (!findInList(this.closed, node))               //Check its not already in lists
                    && (!findInList(this.open, node))) {
                node.g = node.parent.g + 1;
                //node.g += matrix[this.now.r + row][this.now.c];
                this.open.add(node);
            }
        }
        Collections.sort(open);
    }

    /**
     *
     * @param rEnd      user specified end point in row
     * @param cEnd      user specified end point in column
     * @return          Path size i.e the number of moves it took to get to end point
     */
    private int findShortestPath(int rEnd, int cEnd) {
        this.rEnd = rEnd;
        this.cEnd = cEnd;                                           //this.closed.add(this.now);
        checkLeftToRight();
        checkUpToDown();
        while ((this.now.r != this.rEnd) && (this.now.c != this.cEnd)) {
            this.now = this.open.get(0);                            //Move to lowest f score node
            this.open.clear();
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
        Scanner setup = new Scanner(System.in);
        System.out.println("Input x  start coordinate: ");
        int rStart = setup.nextInt();
        System.out.println("Input y start coordinate: ");
        int cStart = setup.nextInt();
        System.out.println("Input x end coordinate: ");
        int rEnd = setup.nextInt();
        System.out.println("Input y end coordinate: ");
        int cEnd = setup.nextInt();
        Algorithm algorithm = new Algorithm(matrix, rStart, cStart);
        algorithm.findShortestPath(rEnd, cEnd);
        algorithm.printMatrix();
        System.out.println("Total cost: " + algorithm.path.get(algorithm.path.size()));
        matrix[algorithm.rStart][algorithm.cStart] = 5;
        matrix [algorithm.rEnd][algorithm.cEnd] = 6;
        algorithm.printMatrix();
    }
}
