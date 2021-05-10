import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

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

    public int currentWorkerCount;
    public int customersServed;
    public int maxCustomers;

    public Resident[] queue;
    public int putIn = 0, takeOut = 0;
    public boolean open = true;

    public Semaphore mutex = null;
    public Semaphore waiting = null;
    public Semaphore truckers = null;
    public Semaphore queueSize = null;
    public Semaphore queueSpaces = null;


    public GroceryStore(int smax, int min_sse, int ss_min, int ss_max) {
        this.smax = smax;
        this.ss_max = ss_max;
        this.ss_min = ss_min;
        this.min_sse = min_sse;

        this.maxCustomers = ThreadLocalRandom.current().nextInt(this.ss_min, this.ss_max);
        this.currentWorkerCount = 0;

        this.mutex = new Semaphore(1);

        // Semaphores for the line
        this.queue = new Resident[this.smax];
        this.queueSize = new Semaphore(0);
        this.queueSpaces = new Semaphore(this.smax);

        // Semaphore for waiting for appropriate amount of workers to arrive
        this.waiting = new Semaphore(0);

        this.truckers = new Semaphore(0);
    }

    public void wantToWork(String name) {
        System.out.println(name + " wants to work");
        try {
            this.mutex.acquire();
            this.currentWorkerCount++;
            if (this.currentWorkerCount >= this.min_sse) {
                this.waiting.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.mutex.release();
        }
    }

    public void getInLine(Resident resident) {
        System.out.println(resident.name + " is getting in line to shop");
        try {
            this.mutex.acquire();
            this.queueSpaces.acquire();
            queue[putIn] = resident;
            putIn = (putIn + 1) % this.smax;
            this.queueSize.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.mutex.release();
        }
    }

    public void serviceCustomer() {
        try {
            this.mutex.acquire();
            this.queueSize.acquire();
            Resident r = this.queue[takeOut];
            System.out.println("Serving: " + r.name);
            takeOut = (takeOut + 1) % this.smax;

            this.customersServed++;
            if (this.customersServed == this.maxCustomers) {
                System.out.println("Served maximum amount of customers for the day");
                this.open = false;
            }

            this.queueSpaces.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.mutex.release();
        }
    }

}
