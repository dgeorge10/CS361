import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class Farmer extends Resident implements Runnable {
    // Range of values for amount of food farmed
    public int fs_min = 0;
    public int fs_max = 100;

    // Shared queue with Truckers to determine which farmers are waiting for a trucker to arrive
    private final BlockingQueue<Farmer> waitingFarmers;

    public Farmer(String name, int nf, int fmin, int cfmin, int cfmax, int bfmin, int bfmax, int s_max,
                  GroceryStore store, BlockingQueue<Farmer> waitingFarmers) {
        super(name, nf, fmin, cfmin, cfmax, bfmin, bfmax, s_max, store);
        this.waitingFarmers = waitingFarmers;
        new Thread(this, "").start();
    }

    /*
    Generate an amount of food for the farmer. Increment nf by this amount
     */
    public void farm(){
        int amount = ThreadLocalRandom.current().nextInt(this.fs_min, this.fs_max);
        this.nf += amount;
        System.out.println(this.name + " generated " + amount + " units of food");
    }

    /*
    Get amount of food that the trucker will pick up from the farmer
    The amount of food that was eaten will have already been subtracted from nf
     */
    public int getFoodForTrucker() {
        this.currentEaten = 0;
        return this.nf;
    }

    /*
    Put this farmer on the waitingFarmers queue
    Block until the Trucker is finished working with the farmer
     */
    public void awaitTruckerToTakeFood(){
        System.out.println(this.name + " is waiting for trucker");
        try {
            this.waitingFarmers.put(this);
            this.waiting.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void shopForFood() {
        this.shop();
    }

    /*
    Life cycle of a farmer:
    1. Farm
    2. Eat food
    3. Await trucker to pickup food
    4. Shop for food
    5. Sleep
     */
    @Override
    public void run() {
        while (true) {
            this.farm();
            this.eatFood();
            this.awaitTruckerToTakeFood();
            this.shopForFood();
            this.nonWork();
        }
    }
}
