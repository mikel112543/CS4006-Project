/**
 * @author - Michael Danaher - 18221726
 * @author - Aaron Foster - 18232086
 * @author - Jamie McNamara - 18250599
 * @author - John Maguire - 18250076
 */

import java.util.*;

class Algorithm {
    private List<Node> open;        //Hold open nodes
    private List<Node> closed;      //Hold closed nodes
    private List<Node> path;        //Record nodes moved to
    private final String[][] matrix;
    private Node now;
    private int rStart;             //row start point
    private int cStart;             //column start point
    private int rEnd;               //row end point
    private int cEnd;               //column end point
    //ROWS GO DOWN
    //COLUMNS GO ACROSS

    static class Node implements Comparable {
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

        //Compare nodes by their f values & sorting by lowest
        @Override
        public int compareTo(Object o) {
            Node that = (Node) o;
            return (int) ((int) (this.h + this.g) - (that.h + that.g));
        }

    }

    private Algorithm(String[][] matrix) {
        this.matrix = matrix;
    }

    /**
     * @param matrix Matrix used to find the shortest path
     * @param rStart User specified start row
     * @param cStart User specified start column
     */
    private Algorithm(String[][] matrix, int rStart, int cStart) {
        this.matrix = matrix;
        this.rStart = rStart;
        this.cStart = cStart;
        this.open = new ArrayList<>();
        this.closed = new ArrayList<>();
        this.path = new ArrayList<>();
        this.now = new Node(null, rStart, cStart, 0, manhattanDistance(this.rStart, this.cStart));
        this.closed.add(this.now);
    }

    /**
     * @return Estimated distance between start and end nodes i.e h value
     */
    private double manhattanDistance(int rValue, int cValue) {
        return Math.abs(rValue - this.rEnd) + Math.abs(cValue - this.cEnd);
    }

    /**
     * @param list specified list to check for nodes
     * @param node specified node to check for
     * @return true if node exists in list
     */
    private boolean findInList(List<Node> list, Node node) {            //Find nodes in specific list based off r and c value
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
     * Checks nodes to the left and right of current position if they satisfy
     */
    private void checkLeftToRight() {                                   //matrix[x][y+1/y-1]
        Node node;
        for (int col = -1; col <= 1; col++) {
            node = new Node(this.now, this.now.r, this.now.c + col, this.now.g, manhattanDistance(this.now.r, this.now.c + col));
            if (this.now.c + col >= 0
                    && (now.c + col < matrix[0].length)
                    && (!findInList(this.closed, node))
                    && (!findInList(this.open, node))
                    && (!matrix[this.now.r][this.now.c + col].equals("X"))) {
                node.g = node.parent.g + 1;
                this.open.add(node);
            }
        }
        //Sort based on f values
        Collections.sort(open);
    }

    /**
     * Check nodes above and below current position if they satisfy
     */
    private void checkDownToUp() {                                              //matrix[x+1/x-1][y]
        Node node;
        for (int row = -1; row <= 1; row++) {
            node = new Node(this.now, this.now.r + row, this.now.c, this.now.g, manhattanDistance(this.now.r + row, this.now.c));
            if ((this.now.r + row >= 0)
                    && (this.now.r + row < matrix.length)                       //Within boundaries
                    && (!matrix[this.now.r + row][this.now.c].equals("X"))      //Check position isn't blocked
                    && (!findInList(this.closed, node))                         //Check its not already in lists
                    && (!findInList(this.open, node))) {
                node.g = node.parent.g + 1;
                this.open.add(node);
            }
        }
        //Sort based on f values
        Collections.sort(open);
    }

    /**
     * @param rEnd user specified end point in row
     * @param cEnd user specified end point in column
     */
    private void findShortestPath(int rEnd, int cEnd) {
        this.rEnd = rEnd;
        this.cEnd = cEnd;                                           //this.closed.add(this.now);
        checkLeftToRight();
        checkDownToUp();
        while (!(this.now.r == rEnd && this.now.c == cEnd)) {
            this.now = this.open.get(0);                            //Move to lowest f score node
            matrix[this.now.r][this.now.c] = " ";
            this.closed.add(this.now);                              //Add recently moved space to closed list
            this.path.add(this.now);                                //Add recently moved space to path list to keep track
            this.open.clear();                                      //Reset open list
            checkDownToUp();
            checkLeftToRight();
        }
    }

    /**
     * @param randRow starting row point for letter shaped obstacle placement
     * @param randCol starting column for letter shaped obstacle placement
     */
    private void createT(int randRow, int randCol) {
        for (int i = 0; i < 3; i++) {
            matrix[randRow][randCol + i] = "X";                 //Place "X" in designated spots
        }
        for (int j = 0; j < 3; j++) {
            matrix[randRow + j][randCol + 1] = "X";
        }
    }

    /**
     * @param randRow starting row point for letter shaped obstacle placement
     * @param randCol starting column for letter shaped obstacle placement
     */
    private void createL(int randRow, int randCol) {
        for (int i = 0; i < 2; i++) {
            matrix[randRow][randCol + i] = "X";
        }
        for (int j = 0; j < 4; j++) {
            matrix[randRow - j][randCol] = "X";
        }
    }

    /**
     * @param randRow starting row point for letter shaped obstacle placement
     * @param randCol starting column for letter shaped obstacle placement
     */
    private void createI(int randRow, int randCol) {
        for (int i = 0; i < 5; i++) {
            matrix[randRow + i][randCol] = "X";
        }
    }

    /**
     * Randomly picks 1 of 3 shapes and checks if it fits in random location.
     * If not, a new location is chosen and checked.
     */
    private void randomShape() {
        Random r = new Random();
        int randCol;
        int randRow;
        int randLetter = r.nextInt(3);

        boolean valid = false;


        switch (randLetter) {
            case 0: //T
                while (!valid) {
                    randCol = r.nextInt(8);                                             //Random point in matrix to place object
                    randRow = r.nextInt(8);
                    if (randCol + 2 < matrix[0].length && randRow + 2 < matrix.length) {        //Check shape can fit in designated spot
                        createT(randRow, randCol);
                        valid = true;
                    }
                }
                break;
            case 1: //L
                while (!valid) {
                    randCol = r.nextInt(8);                                        //Random point in matrix to place object
                    randRow = r.nextInt(8);
                    if (randCol + 1 < matrix[0].length && randRow - 3 < matrix.length) {
                        createL(randRow, randCol);
                        valid = true;
                    }
                }
                break;
            case 2: //I
                while (!valid) {
                    randCol = r.nextInt(8);                                        //Random point in matrix to place object
                    randRow = r.nextInt(8);
                    if (randRow + 4 < matrix.length) {
                        createI(randRow, randCol);
                        valid = true;
                    }
                }
                break;
        }
    }

    /**
     * @param coordinate user inputed coordinate
     * @return true if  1 <= coordinate <= 8 else false
     */
    private boolean isValid(int coordinate) {
        if (coordinate > 8 || coordinate < 1)  {
            System.out.println("Sorry incorrect input please try again");
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        String[][] matrix = new String[8][8];
        for (String[] strings : matrix) {
            Arrays.fill(strings, "0");
        }
        Algorithm algorithm = new Algorithm(matrix);
        algorithm.randomShape();
        algorithm.printMatrix();

        Scanner setup = new Scanner(System.in);
        System.out.println("Input column number start coordinate: (1-8)");
        int cStart = setup.nextInt();
        while (!(algorithm.isValid(cStart))) {       // re-input if isValid returns false
            cStart = setup.nextInt();
        }
        System.out.println("Input row number start coordinate: (1-8)");
        int rStart = setup.nextInt();
        while (!(algorithm.isValid(rStart))) {       // re-input if isValid returns false
            rStart = setup.nextInt();
        }
        System.out.println("Input column number end coordinate: (1-8)");
        int cEnd = setup.nextInt();
        while (!(algorithm.isValid(cEnd))) {     // re-input if isValid returns false
            cEnd = setup.nextInt();
        }
        System.out.println("Input row number end coordinate: (1-8)");
        int rEnd = setup.nextInt();
        while (!(algorithm.isValid(rEnd))) {     // re-input if isValid returns false
            rEnd = setup.nextInt();
        }
        cStart -= 1;       //start matrix at one
        rStart -= 1;
        cEnd -= 1;
        rEnd -= 1;
        algorithm = new Algorithm(matrix, rStart, cStart);
        System.out.println(" ");
        algorithm.findShortestPath(rEnd, cEnd);
        matrix[algorithm.rStart][algorithm.cStart] = "S";
        matrix[algorithm.rEnd][algorithm.cEnd] = "F";
        System.out.println("Total cost: " + algorithm.path.size());
        algorithm.printMatrix();
    }
}       /*
            SAMPLE MATRIX
         1, 2, 3, 4, 5, 6, 7, 8
      1 [0, 0, 0, 0, 0, 0, 0, 0]
      2 [0, 0, 0, 0, 0, 0, 0, 0]
      3 [0, 0, 0, 0, 0, 0, 0, 0]
      4 [0, 0, 0, 0, 0, 0, 0, 0]
      5 [0, 0, 0, 0, 0, 0, 0, 0]
      6 [0, 0, 0, 0, 0, 0, 0, 0]
      7 [0, 0, 0, 0, 0, 0, 0, 0]
      8 [0, 0, 0, 0, 0, 0, 0, 0]
        */

