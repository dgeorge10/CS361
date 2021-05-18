public class Trucker extends Resident implements Runnable {

    // Maximum amount of food the Trucker's truck can hold
    private int kmax;

    // Current amount of food that the trucker has
    public int currentAmount = 0;

    // Shared queue with Truckers to determine which farmers are waiting for a trucker to arrive
    private BoundedBuffer<Farmer> waitingFarmers;

    public Trucker(String name, int nf, int fmin, int cfmin, int cfmax, int bfmin, int bfmax, int kmax, int s_max,
                   GroceryStore store, BoundedBuffer<Farmer> waitingFarmers) {
        super(name, nf, fmin, cfmin, cfmax, bfmin, bfmax, s_max, store);
        this.kmax = kmax;
        this.waitingFarmers = waitingFarmers;
        new Thread(this).start();
    }

    /*
    Goes as follows:
    1. Take the first farmer off of the queue
    2. Get the amount of food to puck up
    3. If the trucker can pickup this amount of food, accept it, remove the amount from the farmer
    4. If the trucker cannot pick up the entire amount, pick up as much that fills the truck
    5. Unblock the farmer from waiting for this interaction
     */
    public void pickupFood() {
        try {
            System.out.println(this.name + " is waiting for a farmer to help");
            Farmer f = this.waitingFarmers.take();
            System.out.println(this.name + " is helping " + f.name);
            int amount = f.getFoodForTrucker();
            System.out.println(f.name + " attempting to have " + amount + " units picked up by " + this.name);
            // if the truck has enough room for the current amount of food accept it
            // else accept as much food as can fit in the truck
            if (this.kmax > this.currentAmount + amount) {
                this.currentAmount += amount;
                f.nf -= amount;
            } else {
                f.nf -= (this.kmax = this.currentAmount);
                this.currentAmount = this.kmax;
                System.out.println("Not enough room on truck.");
            }
            System.out.println(this.name + " has " + this.currentAmount + " units of food in their truck");
            f.waiting.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
    Pass the food along to the grocery store, block until a GroceryWorker has accepted the delivery
     */
    public void deliverFood() {
        try {
            this.store.acceptDelivery(this);
            this.waiting.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
    Lifecycle of a Trucker
    1. Eat food
    2. Pickup food from farmer
    3. Deliver food to grocery store
    4. Shop for food
    5. Sleep
     */
    @Override
    public void run() {
        while (true) {
            this.eatFood();
            this.pickupFood();
            this.deliverFood();
            this.shop();
            this.nonWork();
        }
    }
}
