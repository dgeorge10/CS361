public class GroceryWorker extends Resident implements Runnable {

    public GroceryWorker(int nf, int fmin, int cfmin, int cfmax, int bfmin, int bfmax, GroceryStore store) {
        super(nf, fmin, cfmin, cfmax, bfmin, bfmax, store);
        new Thread(this).start();
    }

    public void waitForEmployees() {}

    public void serviceCustomer() {}

    @Override
    public void run() {
        while (true) {
            this.waitForEmployees();
            this.serviceCustomer();
        }
    }
}
