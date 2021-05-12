import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) {
        final int numFarmers = 10;
        final int numGroceryWorkers = 10;
        final int numTruckers = 4;
        int nf = 0;
        int fmin = 10;
        int cfmin = 0;
        int cfmax = 10;
        int bfmin = 2;
        int bfmax = 20;

        int smax = 10000;
        int sc = 2;
        int min_sse = 4;
        int ss_max = 20;
        int ss_min = 15;

        int kmax = 500;

        int s_max = 5;

        GroceryStore store = new GroceryStore(smax, min_sse, ss_min, ss_max, sc);

        //BlockingQueue<Farmer> waitingFarmers = new LinkedBlockingQueue<Farmer>(); //new Farmer[numFarmers];
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
