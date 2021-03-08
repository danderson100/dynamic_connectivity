import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final int trials;
    private final double[] numOpen;
    private final static double CONFIDENCE_95 = 1.96;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        this.trials = trials;
        this.numOpen = new double[trials];

        for (int i = 0; i < trials; i++) {

            Percolation perc = new Percolation(n);

            while (!perc.percolates()) {
                int randRow = StdRandom.uniform(1, n + 1);
                int randCol = StdRandom.uniform(1, n + 1);
                perc.open(randRow, randCol);
            }
            double result = (double) perc.numberOfOpenSites() / (n * n);
            numOpen[i] = result;
        }

    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(numOpen);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(numOpen);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - ((CONFIDENCE_95 * stddev()) / Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + ((CONFIDENCE_95 * stddev()) / Math.sqrt(trials));
    }

    // test client (see below)
    public static void main(String[] args) {
        int gridSize = Integer.parseInt(args[0]);
        int numTrials = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(gridSize, numTrials);
        System.out.println(stats.mean());
    }

}