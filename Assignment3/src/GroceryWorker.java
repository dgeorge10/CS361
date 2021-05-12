public class GroceryWorker extends Resident implements Runnable {

    public GroceryWorker(String name, int nf, int fmin, int cfmin, int cfmax, int bfmin, int bfmax, int s_max, GroceryStore store) {
        super(name, nf, fmin, cfmin, cfmax, bfmin, bfmax, s_max, store);
        new Thread(this).start();
    }

    /*
    Function to begin working. This will block until the appropriate amount of workers arrive at the store
     */
    public void beginWorking() {
        this.store.wantToWork(this.name);
        try {
            this.store.waiting.acquire();
            System.out.println(this.name + " is working");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
    This is the working loop for the grocery store workers
    While the store is open, constantly service customers
    When the store closes, clock out from work
     */
    public void serviceCustomers() {
        while(this.store.open) {
            System.out.println(this.name + " is attempting to service a customer");
            this.store.serviceCustomer();
        }
        this.store.clockOut(this.name);
        System.out.println(this.name + " is done working");
    }

    /*
    Lifecycle of a GroceryWorker
    It is a assumed that grocery store workers will get food while they are on the shift
    1. Begin working
    2. Service customers
    3. Sleep
     */
    @Override
    public void run() {
        while (true) {
            this.eatFood();
            this.beginWorking();
            this.serviceCustomers();
            this.shop();
            this.nonWork();
        }
    }
}
