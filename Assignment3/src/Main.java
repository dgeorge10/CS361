import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) {
        int numFarmers = 8;
        int numGroceryWorkers = 4;
        int numTruckers = 4;
        int nf = 0;
        int fmin = 30;
        int cfmin = 0;
        int cfmax = 10;
        int bfmin = 2;
        int bfmax = 20;

        int smax = 10000;
        int sc = 2;
        int min_sse = 1;
        int ss_max = 20;
        int ss_min = 15;

        int kmax = 500;

        int s_max = 5;

        String argName = "";
        String argValue = "";

        for (int i = 0; i < args.length - 1; i++) {
            argName = args[i];
            argValue = args[i+1];

            try {
                switch (argName) {
                    case "-f":
                        numFarmers = Integer.parseInt(argValue);
                        break;
                    case "-g":
                        numGroceryWorkers = Integer.parseInt(argValue);
                        break;
                    case "-t":
                        numTruckers = Integer.parseInt(argValue);
                        break;
                    case "-nf":
                        nf = Integer.parseInt(argValue);
                        break;
                    case "-fmin":
                        fmin = Integer.parseInt(argValue);
                        break;
                    case "-cfmin":
                        cfmin = Integer.parseInt(argValue);
                        break;
                    case "-bfmin":
                        bfmin = Integer.parseInt(argValue);
                        break;
                    case "-bfmax":
                        bfmax = Integer.parseInt(argValue);
                        break;
                    case "-smax":
                        smax = Integer.parseInt(argValue);
                        break;
                    case "-sc":
                        sc = Integer.parseInt(argValue);
                        break;
                    case "-min_sse":
                        min_sse = Integer.parseInt(argValue);
                        break;
                    case "-ss_min":
                        ss_min = Integer.parseInt(argValue);
                        break;
                    case "-ss_max":
                        ss_max = Integer.parseInt(argValue);
                        break;
                    case "-kmax":
                        kmax = Integer.parseInt(argValue);
                        break;
                    case "s_max":
                        s_max = Integer.parseInt(argValue);
                        break;
                }
            } catch (Exception ex) {
                System.out.println("Error parsing command line arguments.");
                System.out.println(ex.getLocalizedMessage());
            }
        }

        GroceryStore store = new GroceryStore(smax, min_sse, ss_min, ss_max, sc);

        BoundedBuffer<Farmer> waitingFarmers = new BoundedBuffer<>();

        for (int i = 0; i < numFarmers; i++) {
            System.out.println("Starting Farmer" + i);
            new Farmer("Farmer"+i, nf, fmin, cfmin, cfmax, bfmin, bfmax, s_max, store, waitingFarmers);
        }

        for (int i = 0; i < numGroceryWorkers; i++) {
            System.out.println("Starting GroceryWorker" + i);
            new GroceryWorker("GroceryWorker"+i, nf, fmin, cfmin, cfmax, bfmin, bfmax, s_max, store);
        }

        for (int i = 0; i < numTruckers; i++) {
            new Trucker("Trucker"+i, nf, fmin, cfmin, cfmax, bfmin, bfmax, kmax, s_max, store, waitingFarmers);
        }

        System.out.println("All threads started");
    }
}
