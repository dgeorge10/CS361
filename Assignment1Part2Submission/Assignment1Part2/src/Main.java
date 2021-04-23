import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws InterruptedException {
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

        // split up the range of y values for a thread
        double yRange = (yhi - ylo) / numThreads;
        int yCordRange = size / numThreads;
        int[][] grid = new int[size][size];

        // compute the widths of our steps
        double xWidth = (xhi - xlo) / (size - 1);
        double yWidth = (yhi - ylo) / (size - 1);

        long startTime = System.currentTimeMillis();
        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i ++) {
            ComputeThread ct = new ComputeThread(grid, threshold,
                                xlo, xhi,
                                ylo + (yRange * i),
                                ylo + (yRange * (i+1)),
                                (yCordRange * i),
                                (yCordRange * (i+1)),
                                xWidth,
                                yWidth);
            Thread t = new Thread(ct, "ComputeThread:" + i);
            t.start();
            threads[i] = t;
        }

        // join all the threads to the main thread;
        for (Thread t: threads) {
            t.join();
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
