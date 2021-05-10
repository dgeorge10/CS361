public class Trucker extends Resident implements Runnable {

    // Maximum amount of food the Trucker's truck can hold
    private int kmax;

    public Trucker(String name, int nf, int fmin, int cfmin, int cfmax, int bfmin, int bfmax, GroceryStore store) {
        super(name, nf, fmin, cfmin, cfmax, bfmin, bfmax, store);
        new Thread(this).start();
    }

    public void pickupFood() {}
    public void deliverFood() {}

    @Override
    public void run() {
        while (true) {
            this.eatFood();
            this.pickupFood();
            this.deliverFood();
            this.nonWork();
        }
    }
}
