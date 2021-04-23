import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // setup parameters
        int numThreads = 0;
        int size = 0;
        int threshold = 0;
        double xlo = 0.0;
        double xhi = 0.0;
        double ylo = 0.0;
        double yhi = 0.0;

        // parse parameters
        String argName = "";
        String argValue = "";
        for (int i = 0; i < args.length - 1; i++) {
            argName = args[i];
            argValue = args[i + 1];
            try {
               switch(argName) {
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

        // compute the widths of our steps
        double xWidth = (xhi - xlo) / (size - 1);
        double yWidth = (yhi - ylo) / (size - 1);

        int[] colors = new int[threshold + 1];
        for (int i = 0; i < threshold; i++) {
            colors[i] = Color.HSBtoRGB(i/256f, 1, i/(i+8f));
        }

        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        int iterations;
        int xCord = 0;
        // graphing is weird for final image, could start this at SIZE - 1 and decrement each iteration
        int yCord = 0;

        long startTime = System.currentTimeMillis();
        for(double i = xlo; i <= xhi; i += xWidth){
            for(double j = ylo; j <= yhi; j += yWidth){
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

                if (iterations < threshold) image.setRGB(xCord, yCord, colors[iterations]);
                else image.setRGB(xCord, yCord, 0);

                yCord += 1;
            }
            yCord = 0;
            xCord += 1;
        }

        long endTime = System.currentTimeMillis();
        float totalTime = (endTime - startTime) / 1000.0f;
        System.out.println("Total time: " + totalTime + "s");

        try {
            ImageIO.write(image, "png", new File("default.png"));
        } catch (IOException e ){
            e.printStackTrace();
        }
    }
}
