import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static boolean addingToQueue = true;
    public static void main(String[] args) {
        // setup parameters
        int numThreads = 2;
        int size = 9000;
        int threshold = 100;
        double xlo = -2.4;
        double xhi = 1.5;
        double ylo = -2.4;
        double yhi = 1.5;
		String filename = "default.png";


        // parse parameters
        String argName = "";
        String argValue = "";
        for (int i = 0; i < args.length - 1; i++) {
            argName = args[i];
            argValue = args[i + 1];
            try {
               switch(argName) {
				   case "-FILE":
					   filename = argValue;
					   break;
                   case "-NUMTHREADS":
                       numThreads = Integer.parseInt(argValue);
                       break;
                   case "-SIZE":
                       size = Integer.parseInt(argValue);
                       break;
                   case "-THRESHOLD":
                       threshold = Integer.parseInt(argValue);
                       break;
                   case "-XLO":
                       xlo = Double.parseDouble(argValue);
                       break;
                   case "-XHI":
                       xhi = Double.parseDouble(argValue);
                       break;
                   case "-YLO":
                       ylo = Double.parseDouble(argValue);
                       break;
                   case "-YHI":
                       yhi= Double.parseDouble(argValue);
                       break;
               }
            } catch (Exception e) {
                System.out.println("Error parsing command line arguments");
                System.out.println(e.getLocalizedMessage());
            }
        }

        int[][] grid = new int[size][size];
        int k = 100;
        // compute the widths of our steps
        double xWidth = (xhi - xlo) / (size - 1);
        double yWidth = (yhi - ylo) / (size - 1);

        long startTime = System.currentTimeMillis();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(numThreads, grid, xlo, ylo, xWidth, yWidth, threshold);
        // split up region into k x k grid
        // inspired from pdd35, creating tasks array that the threads will pull from
        // int[] structure will hold: [xmin, xmax, ymin, ymax] of the region
        for(int x= 0; x < size; x += k){
            for(int y = 0; y < size; y += k){
                executor.addTask(new int[]{x, Math.min(x + k, size), y, Math.min(y + k, size)});
            }
        }
        addingToQueue = false;

        for(Thread t : executor.getWorkers()) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();
        float totalTime = (endTime - startTime) / 1000.0f;
        System.out.println("Total time: " + totalTime + "s");

        // generate an array of colors
        int[] colors = new int[threshold + 1];
        for (int i = 0; i < threshold; i++) {
            colors[i] = Color.HSBtoRGB(i/256f, 1, i/(i+8f));
        }

        // build the image
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if (grid[i][j] < threshold) image.setRGB(i, j, colors[grid[i][j]]);
                else image.setRGB(i, j, 0);
            }
        }

        try {
            ImageIO.write(image, "png", new File(filename));
        } catch (IOException e ){
            e.printStackTrace();
        }
    }
}
