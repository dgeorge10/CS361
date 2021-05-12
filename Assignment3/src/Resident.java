import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Resident {
    // Amount of food at home in pantry
    public int nf;

    // Trigger for going to the store to buy more food
    public int f_min;

    // Range for how much food is eaten in a given day
    public int cf_min;
    public int cf_max;

    // Current amount of food that was eaten for this lifecycle
    public int currentEaten = 0;

    // Range for how much food resident buys when at the shop
    public int bf_min;
    public int bf_max;

    // Maximum amount of ms to sleep
    private int s_max;

    // Name of the thread
    public String name;

    public GroceryStore store = null;

    // Semaphore to block Resident when they are waiting for another resident to transition their state
    public Semaphore waiting = null;

    public Resident(String name, int nf, int fmin, int cfmin, int cfmax, int bfmin, int bfmax, int s_max, GroceryStore store) {
        this.name = name;
        this.nf = nf;
        this.f_min = fmin;
        this.cf_min = cfmin;
        this.cf_max = cfmax;
        this.bf_min = bfmin;
        this.bf_max = bfmax;
        this.s_max = s_max;
        this.store = store;

        this.waiting = new Semaphore(0);
    }

    /*
    Eat a random amount of food between cf_min and cf_max. Decrement this amount from nf
     */
    public void eatFood() {
        int amount = ThreadLocalRandom.current().nextInt(this.cf_min, this.cf_max);
        this.currentEaten = amount;

        // ensure that the resident has enough food to eat
        if (this.nf < this.currentEaten) {
            System.out.println(this.name + " does not have enough food to eat " + this.currentEaten + " units of food");
        } else {
            this.nf -= this.currentEaten;
            System.out.println(this.name + " ate " + this.currentEaten + " units of food");
        }
    }

    /*
    Return a random amount of food to buy
     */
    public int getBuyFoodAmount() {
        return ThreadLocalRandom.current().nextInt(this.bf_min, this.bf_max);
    }

    /*
    Request to put the Resident in the shopping queue
    Only do this if their amount of food falls below a certain threshold
     */
    public void shop() {
        try {
            if (this.nf < this.f_min) {
                this.store.getInLine(this);
                this.waiting.acquire();
            } else {
                System.out.println(this.name + " is not going to the store because they have enough food");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
    Sleep... ZZZZ
     */
    public void nonWork() {
        try {
            System.out.println(this.name + " is resting");
            int ms = ThreadLocalRandom.current().nextInt(1, this.s_max);
            Thread.sleep(ms*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
