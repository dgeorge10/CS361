public class ComputeThread implements Runnable {
    private volatile int[][] grid;
    private int threshold = 100;
    private double xlo = -2.4;
    private double xhi = 1.5;
    private double ylo = -2.4;
    private double yhi = 1.5;
    private int yStart = 0;
    private int yEnd= 0;
    private double xWidth = 0.0;
    private double yWidth = 0.0;

    // no lock is needed as we are passing the thread the region to compute and won't be accessing the same elements
    // TODO: make sure only needed params are passed
    public ComputeThread(int[][] grid, int threshold, double xlo, double xhi, double ylo, double yhi,
                         int yStart, int yEnd, double xWidth, double yWidth) {
        this.grid = grid;
        this.threshold = threshold;
        this.xlo = xlo;
        this.xhi = xhi;
        this.ylo = ylo;
        this.yhi = yhi;
        this.yStart = yStart;
        this.yEnd = yEnd;
        this.xWidth = xWidth;
        this.yWidth = yWidth;
    }

    @Override
    public void run() {
        int iterations;
        int xCord = 0;
        int yCord = this.yStart;
        for(double i = xlo; i <= this.xhi; i += xWidth){
            for(double j = ylo; j <= this.yhi; j += yWidth){
                iterations = 1;
                double zReal = i;
                double zImaginary = j;
                double zRealSquared;
                double zImaginarySquared;

                zRealSquared = zReal * zReal;
                zImaginarySquared = zImaginary * zImaginary;
                // while |z(n)| < 4 and iterations is less than the threshold
                while (zRealSquared + zImaginarySquared < 4 && iterations < threshold){
                    zImaginary = Math.abs(2*zReal*zImaginary) + j;
                    zReal = zRealSquared - zImaginarySquared + i;

                    zRealSquared = zReal * zReal;
                    zImaginarySquared = zImaginary * zImaginary;
                    iterations++;
                }

                grid[xCord][yCord] = iterations;
                yCord += 1;
            }
            yCord = this.yStart;
            xCord += 1;
        }
    }
}
