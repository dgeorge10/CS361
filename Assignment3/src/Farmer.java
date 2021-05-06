import java.util.concurrent.ThreadLocalRandom;

public class Farmer extends Resident implements Runnable {
    public int currentProduced = 0;
    public int currentEaten = 0;
    public int fs_min = 0;
    public int fs_max = 100;

    public Farmer(int nf, int fmin, int cfmin, int cfmax, int bfmin, int bfmax, GroceryStore store) {
        super(nf, fmin, cfmin, cfmax, bfmin, bfmax, store);

        new Thread(this).start();
    }

    public void farm(){
        int amount = ThreadLocalRandom.current().nextInt(this.fs_min, this.fs_max);
        this.currentProduced = amount;
    }

    public void eatFood(){
        int amount = ThreadLocalRandom.current().nextInt(this.cf_min, this.cf_max);
        this.currentEaten = amount;
        if (this.currentEaten < 0) this.currentEaten = 0;
    }

    public void awaitTruckerToTakeFood(){
       // wait for trucker
    }

    @Override
    public void shop(){
        if (this.nf > this.f_min) { return; }
        int amount = ThreadLocalRandom.current().nextInt(this.bf_min, this.bf_max);
        // shop for amount from store
    }

    @Override
    public void run() {
        while (true) {
            this.farm();
            this.eatFood();
            this.awaitTruckerToTakeFood();
            this.shop();
            this.nonWork();
        }
    }
}
