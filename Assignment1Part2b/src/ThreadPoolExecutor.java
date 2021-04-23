import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// adapted from https://www.javacodemonk.com/implement-custom-thread-pool-in-java-without-executor-framework-ca10e61d
public class ThreadPoolExecutor {
    private final BlockingQueue<int[]> tasks;
    private final Thread[] workers;
    private int[][] grid;
    private double xlo;
    private double ylo;
    private double xWidth;
    private double yWidth;
    private int threshold;

    public ThreadPoolExecutor(int numThreads, int[][] grid, double xlo, double ylo, double xWidth, double yWidth, int threshold){
        this.workers = new Thread[numThreads];
        this.tasks = new LinkedBlockingQueue<>();
        this.grid = grid;
        this.xlo = xlo;
        this.ylo = ylo;
        this.xWidth = xWidth;
        this.yWidth = yWidth;
        this.threshold = threshold;

        for (int i = 0; i < numThreads; i++) {
            Worker w = new Worker("worker" + i);
            workers[i] = w;
            w.start();
        }
    }

    public Thread[] getWorkers() { return this.workers; }

    /*
        parameters will come in the form: int[]: [xmin, xmax, ymin, ymax] for a region to execute on
     */
    public void addTask(int[] parameters){
        tasks.add(parameters);
    }

    class Worker extends Thread {
        public Worker(String name) {
            super(name);
        }

        public void run() {
            int[] currentTask;

            while (!(tasks.isEmpty() && !Main.addingToQueue)){
                try{
                    currentTask = tasks.poll();
                    if (currentTask != null){
                        for (int i = currentTask[0]; i < currentTask[1]; i++) {
                            for (int j = currentTask[2]; j < currentTask[3]; j++) {
                                grid[i][j] = compute(xlo + xWidth * i, ylo + yWidth * j, threshold);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public int compute(double xlo, double ylo, int threshold) {
            int iterations = 1;
            double zReal = xlo;
            double zImaginary = ylo;
            double zRealSquared = zReal * zReal;
            double zImaginarySquared = zImaginary * zImaginary;
            // while |z(n)| < 4 and iterations is less than the threshold
            while (zRealSquared + zImaginarySquared < 4 && iterations < threshold) {
                zImaginary = Math.abs(2 * zReal * zImaginary) + ylo;
                zReal = zRealSquared - zImaginarySquared + xlo;

                zRealSquared = zReal * zReal;
                zImaginarySquared = zImaginary * zImaginary;
                iterations++;
            }
            return iterations;
        }
    }
}
