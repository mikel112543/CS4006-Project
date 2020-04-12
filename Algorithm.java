import java.util.*;

class Algorithm {
    private List<Node> open;
    private List<Node> closed;
    private List<Node> path;
    private String[][] matrix;
    private Node now;
    private int rStart;
    private int cStart;
    private int pathCost = 0;
    private int rEnd, cEnd;
    //ROWS GO ACROSS
    //COLUMNS GO DOWN

    class Node implements Comparable {
        public Node parent;
        public int r, c;
        public double g;
        public double h;
        public double f;

        Node(Node parent, int rPos, int cPos, double g, double h) {
            this.parent = parent;
            this.r = rPos;
            this.c = cPos;
            this.g = g;
            this.h = h;
        }

        //Compare nodes by their f values & sorting by lowest
        @Override
        public int compareTo(Object o) {
            Node that = (Node) o;
            return (int) ((int) (this.h + this.g) - (that.h + that.g));
        }

    }

    private Algorithm(String[][] matrix, int rStart, int cStart) {
        this.matrix = matrix;
        this.rStart = rStart;
        this.cStart = cStart;
        this.open = new ArrayList<>();
        this.closed = new ArrayList<>();
        this.path = new ArrayList<>();
        this.now = new Node(null, rStart, cStart, 0, manhattanDistance(this.rStart, this.cStart));
        this.open.add(this.now);
    }

    /**
     *
     * @return      Estimated distance between start and end nodes i.e h value
     */
    private double manhattanDistance(int rValue, int cValue) {
        return Math.abs( rValue - this.rEnd) + Math.abs(cValue - this.cEnd);
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
        for (String[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }

    /**
     *      Checks nodes to the left and right of current position if they satisfy
     */
    private void checkLeftToRight() {                                   //matrix[x][y+1/y-1]
        Node node;
        for(int col = -1; col <= 1; col ++) {
            node = new Node(this.now, this.now.r, this.now.c + col, this.now.g, manhattanDistance(this.now.r, this.now.c + col));
            if(this.now.c + col >= 0
                    && (col != 0)
                    && (this.now.c + col < matrix[0].length)
                    && (!findInList(this.closed, node))
                    && (!findInList(this.open, node))
                    && (!matrix[this.now.r][this.now.c + col].equals("-1"))){
                node.g = node.parent.g + 1;
                this.open.add(node);
            }
        }
        //Sort based on f values
        Collections.sort(open);
    }

    /**
     *      Check nodes above and below current position if they satisfy
     */
    private void checkDownToUp() {                                    //Matrix[x+1/x-1][y]
        Node node;
        for(int row = -1; row <= 1; row++) {
            node = new Node(this.now, this.now.r + row, this.now.c, this.now.g, manhattanDistance(this.now.r + row, this.now.c));
            if((this.now.r + row >= 0)
                    && row != 0
                    && (this.now.r + row < matrix.length)                       //Within boundaries
                    && (!matrix[this.now.r+ row][this.now.c].equals("-1"))      //Check position isn't blocked
                    && (!findInList(this.closed, node))                         //Check its not already in lists
                    && (!findInList(this.open, node))) {
                node.g = node.parent.g + 1;
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
    private void findShortestPath(int rEnd, int cEnd) {
        this.rEnd = rEnd;
        this.cEnd = cEnd;                                           //this.closed.add(this.now);
        checkLeftToRight();
        checkDownToUp();
        while ((this.now.r != this.rEnd) && (this.now.c != this.cEnd)) {
            this.now = this.open.get(0);    //Move to lowest f score node
            matrix[this.now.r][this.now.c] = "X";
            this.open.remove(0);
            this.closed.add(this.now);
            this.path.add(this.now);
            pathCost++;
            checkDownToUp();
            checkLeftToRight();
        }
    }

    public static void main(String[] args) {
        /*String[][] matrix = {
                            {  "0", "0",  "0",  "0", },
                            {  "0", "-1",  "0",  "0", },
                            {  "0",  "0",  "0",  "0", },
                            { "0",   "0",   "0", "0", },
                            {  "0",  "0",  "0", "0", },
                        };
                        */
        String[][] matrix = new String[5][5];
        for (String[] strings : matrix) {
            Arrays.fill(strings, "0");
        }

        Scanner setup = new Scanner(System.in);
        System.out.println("Input row number start coordinate: ");
        int rStart = setup.nextInt();
        System.out.println("Input column number start coordinate: ");
        int cStart = setup.nextInt();
        System.out.println("Input row number end coordinate: ");
        int rEnd = setup.nextInt();
        System.out.println("Input column number end coordinate: ");
        int cEnd = setup.nextInt();
        Algorithm algorithm = new Algorithm(matrix, rStart, cStart);
        algorithm.findShortestPath(rEnd, cEnd);
        System.out.println("Total cost: " + algorithm.path.size());
        matrix[algorithm.rStart][algorithm.cStart] = "5";
        matrix [algorithm.rEnd][algorithm.cEnd] = "6";
        algorithm.printMatrix();
    }
}
