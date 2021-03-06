import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class GroceryStore {
    // Maximum food units for grocery store
    private int smax;

    // Queue of people waiting to be served
    private int sc;

    // Minimum amount of employees for the store to be open
    private int min_sse;

    // Range of the amount of customers to be served per day
    private int ss_min;
    private int ss_max;

    // Maximum amount of customers to serve
    public int maxCustomers;

    // Current amount of food in the store
    private int currentFoodQuantity;

    // Current amount of workers in the store.
    public int currentWorkerCount;

    // Total amount of customers that have been served
    public int customersServed;

    // Queue of residents that are waiting to buy food
    public BoundedBuffer<Resident> queue;

    // Boolean flag to determine when the store is open
    public boolean open = true;

    // Binary Semaphore that acts as a mutex lock
    public Semaphore mutex = null;

    // Semaphore to determine whether we are waiting for enough workers to open the store
    public Semaphore waiting = null;

    public GroceryStore(int smax, int min_sse, int ss_min, int ss_max, int sc) {
        this.smax = smax;
        this.ss_max = ss_max;
        this.ss_min = ss_min;
        this.min_sse = min_sse;
        this.sc = sc;

        this.currentFoodQuantity = 1000;

        this.maxCustomers = ThreadLocalRandom.current().nextInt(this.ss_min, this.ss_max);
        this.currentWorkerCount = 0;

        // Semaphore to act as a mutex lock
        this.mutex = new Semaphore(1);

        // Semaphores for the line
        this.queue = new BoundedBuffer<>(this.sc);

        // Semaphore for waiting for appropriate amount of workers to arrive
        this.waiting = new Semaphore(0);

    }

    /*
    Clock out a GroceryWorker from working. Decrement that count of currentWorkers
     */
    public void clockOut(String name){
        try {
            this.mutex.acquire();
            this.currentWorkerCount -= 1;
            System.out.println(name + " is clocking out. CurrentWorkers: " + this.currentWorkerCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.mutex.release();
        }
    }

    /*
    Method to accept GroceryWorkers who are beginning to work.
    Block the GroceryWorkers until there are enough
     */
    public void wantToWork(String name) {
        System.out.println(name + " wants to work");
        try {
            this.mutex.acquire();
            this.currentWorkerCount++;
            // if we have enough workers to open, open
            if (this.currentWorkerCount == this.min_sse) {
                this.open = true;
                System.out.println("Store is open");
                this.customersServed = 0;
                // we need to call release in a loop here because we want to be able to release everybody who is
                // waiting to begin their working cycle
                for(int i = 0; i<this.currentWorkerCount; i++) {
                    this.waiting.release();
                }
            } else if (this.currentWorkerCount > this.min_sse){
                this.waiting.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.mutex.release();
        }
    }

    /*
    Puts a resident in the queue
     */
    public void getInLine(Resident resident) {
        try {
            if (this.queue.put(resident)) {
                System.out.println("Successfully added " + resident.name + " in the store queue");
            } else {
                System.out.println("Failed to add " + resident.name + " in the store queue");
                resident.waiting.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
    Remove customer from queue, and service them
     */
    public void serviceCustomer() {
        try {
            Resident r = this.queue.take();

            System.out.println("Serving: " + r.name);

            // Subtract amount of food resident is buying from the store total
            int customerAmount = r.getBuyFoodAmount();
            if (this.currentFoodQuantity < customerAmount) {
                // put customer in the back of the line (tough luck)
                this.getInLine(r);
                return;
            }

            // remove amount the customer is buying from the store
            this.currentFoodQuantity -= customerAmount;
            // give the food to the customer
            r.nf += customerAmount;
            System.out.println(r.name + " is buying " + customerAmount + " units of food. The store has " + currentFoodQuantity +  " units of food left.");

            this.customersServed++;
            if (this.customersServed == this.maxCustomers) {
                System.out.println("Served maximum amount of customers for the day");
                this.open = false;
            }

            // Allow the Resident to go back to whatever they needs to do
            r.waiting.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
    Accept an amount of food from the trucker and increment the amount of food in the store
     */
    public void acceptDelivery(Trucker trucker) {
        try {
            this.mutex.acquire();
            if (trucker.currentAmount + this.currentFoodQuantity > this.smax) {
                System.out.println("Trucker has too much food for the store to accept");
                return;
            }
            System.out.println("Store has accepted " + trucker.currentAmount + " units of food");
            this.currentFoodQuantity += trucker.currentAmount;
            trucker.currentAmount = 0;
            trucker.waiting.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.mutex.release();
        }
    }
}
