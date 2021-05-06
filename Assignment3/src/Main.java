public class Main {
    public static void main(String[] args) {
        final int numFarmers = 5;
        final int numGroceryWorkers = 10;
        final int numTruckers = 8;
        int nf = 0;
        int fmin = 10;
        int cfmin = 0;
        int cfmax = 5;
        int bfmin = 0;
        int bfmax = 5;

        GroceryStore store = new GroceryStore();

        for (int i = 0; i < numFarmers; i++) {
            new Farmer(nf, fmin, cfmin, cfmax, bfmin, bfmax, store);
        }

        for (int i = 0; i < numGroceryWorkers; i++) {
            new GroceryWorker(nf, fmin, cfmin, cfmax, bfmin, bfmax, store);
        }

        for (int i = 0; i < numTruckers; i++) {
            new Trucker(nf, fmin, cfmin, cfmax, bfmin, bfmax, store);
        }

    }
}
