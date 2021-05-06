import java.util.concurrent.Semaphore;

public class GroceryStore {
    // Maximum food units for grocery store
    private int smax;

    // Maximum amount of customers allowed in store at a time
    private int sc;

    // Minimum amount of employees for the store to be open
    private int min_sse;

    // Range of the amount of customers to be served per day
    private int ss_min;
    private int ss_max;

    private Semaphore working = null;
    private Semaphore truckers = null;
    public GroceryStore() {
        this.working = new Semaphore(0);
        this.truckers = new Semaphore(0);
    }
}
