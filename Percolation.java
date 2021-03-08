import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdRandom;

public class Percolation {

    private final int capacity;
    private final boolean[][] grid;
    private final WeightedQuickUnionUF wqufGrid;
    private final WeightedQuickUnionUF wqufFull;
    private int numOpenSites;

    private final int virtualTop;
    private final int virtualBottom;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("N must be greater than 0!");
        this.capacity = n;
        this.numOpenSites = 0;
        int gridSquared = n * n;
        this.wqufGrid = new WeightedQuickUnionUF(gridSquared + 2);
        this.wqufFull = new WeightedQuickUnionUF(gridSquared + 1);
        this.virtualTop = gridSquared;
        this.virtualBottom = gridSquared + 1;
        this.grid = new boolean[n][n];

    }

    // Test: open site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);

        int shiftRow = row - 1;
        int shiftCol = col - 1;
        int flatIndex = flattenGrid(row, col) - 1;

        // If already open, stop
        if (isOpen(row, col)) {
            return;
        }

        // Open Site
        grid[shiftRow][shiftCol] = true;
        numOpenSites++;

        if (row == 1) {  // Top Row
            wqufGrid.union(virtualTop, flatIndex);
            wqufFull.union(virtualTop, flatIndex);
        }

        if (row == capacity) {  // Bottom Row
            wqufGrid.union(virtualBottom, flatIndex);
        }

        // Check and Open Left
        if (isOnGrid(row, col - 1) && isOpen(row, col - 1)) {
            wqufGrid.union(flatIndex, flattenGrid(row, col - 1) - 1);
            wqufFull.union(flatIndex, flattenGrid(row, col - 1) - 1);
        }

        // Check and Open Right
        if (isOnGrid(row, col + 1) && isOpen(row, col + 1)) {
            wqufGrid.union(flatIndex, flattenGrid(row, col + 1) - 1);
            wqufFull.union(flatIndex, flattenGrid(row, col + 1) - 1);
        }

        // Check and Open Up
        if (isOnGrid(row - 1, col) && isOpen(row - 1, col)) {
            wqufGrid.union(flatIndex, flattenGrid(row - 1, col) - 1);
            wqufFull.union(flatIndex, flattenGrid(row - 1, col) - 1);
        }

        // Check and Open Down
        if (isOnGrid(row + 1, col) && isOpen(row + 1, col)) {
            wqufGrid.union(flatIndex, flattenGrid(row + 1, col) - 1);
            wqufFull.union(flatIndex, flattenGrid(row + 1, col) - 1);
        }

        // debug
        // runTests();
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);
        return wqufFull.find(virtualTop) == wqufFull.find(flattenGrid(row, col) - 1);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {

        int rootTop = wqufGrid.find(virtualTop);
        int rootBot = wqufGrid.find(virtualBottom);

        return rootTop == rootBot;
    }

    private boolean isOnGrid(int row, int col) {
        int shiftRow = row - 1;
        int shiftCol = col - 1;
        return (shiftRow >= 0 && shiftCol >= 0 && shiftRow < capacity && shiftCol < capacity);
    }

    private void validate(int row, int col) {
        if (!isOnGrid(row, col)) {
            throw new IllegalArgumentException("out of bounds");
        }
    }

    private int flattenGrid(int row, int col) {
        return capacity * (row - 1) + col;
    }


    // test client (optional)
    public static void main(String[] args) {
        Percolation perc = new Percolation(20);

        for (int i = 0; i < 400; i++) {

            int randRow = StdRandom.uniform(1, perc.capacity + 1);
            int randCol = StdRandom.uniform(1, perc.capacity + 1);
            System.out.println("Now opening row " + randRow + " and column " + randCol);
            perc.open(randRow, randCol);
            if (perc.percolates()) {
                System.out.println("Yay! It percolates");
                System.out.println(perc.numberOfOpenSites());
                break;
            }
        }
        if (!perc.percolates()) {
            System.out.println("Doesn't percolate");
        }

    }
}