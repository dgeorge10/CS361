public class GroceryWorker extends Resident implements Runnable {

    public GroceryWorker(String name, int nf, int fmin, int cfmin, int cfmax, int bfmin, int bfmax, GroceryStore store) {
        super(name, nf, fmin, cfmin, cfmax, bfmin, bfmax, store);
        new Thread(this).start();
    }

    public void beginWorking() {
        this.store.wantToWork(this.name);
        try {
            this.store.waiting.acquire();
            System.out.println(this.name + " is working");
            System.out.println(this.store.currentWorkerCount + " current workers");
            this.store.waiting.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void serviceCustomers() {
        while(this.store.open) {
            System.out.println(this.name + " is attempting to service a customer");
            this.store.serviceCustomer();
        }
    }

    @Override
    public void run() {
        while (true) {
            this.beginWorking();
            this.serviceCustomers();
        }
    }
}
