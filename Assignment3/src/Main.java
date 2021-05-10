public class Main {
    public static void main(String[] args) {
        final int numFarmers = 5;
        final int numGroceryWorkers = 10;
        final int numTruckers = 8;
        int nf = 0;
        int fmin = 10;
        int cfmin = 0;
        int cfmax = 10;
        int bfmin = 0;
        int bfmax = 20;

        int smax = 300;
        int min_sse = 5;
        int ss_max = 20;
        int ss_min = 15;

        GroceryStore store = new GroceryStore(smax, min_sse, ss_min, ss_max);

        for (int i = 0; i < numFarmers; i++) {
            System.out.println("Starting Farmer" + i);
            new Farmer("Farmer"+i, nf, fmin, cfmin, cfmax, bfmin, bfmax, store);
        }

        for (int i = 0; i < numGroceryWorkers; i++) {
            System.out.println("Starting GroceryWorker" + i);
            new GroceryWorker("GroceryWorker"+i, nf, fmin, cfmin, cfmax, bfmin, bfmax, store);
        }

        //for (int i = 0; i < numTruckers; i++) {
        //    new Trucker(nf, fmin, cfmin, cfmax, bfmin, bfmax, store);
        //}

        System.out.println("All threads started");

    }
}
