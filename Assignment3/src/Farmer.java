import java.util.concurrent.ThreadLocalRandom;

public class Farmer extends Resident implements Runnable {
    public int currentProduced = 0;
    public int currentEaten = 0;
    public int fs_min = 0;
    public int fs_max = 100;

    public Farmer(String name, int nf, int fmin, int cfmin, int cfmax, int bfmin, int bfmax, GroceryStore store) {
        super(name, nf, fmin, cfmin, cfmax, bfmin, bfmax, store);
        new Thread(this, "").start();
    }

    public void farm(){
        int amount = ThreadLocalRandom.current().nextInt(this.fs_min, this.fs_max);
        this.currentProduced = amount;
        //System.out.println(this.name + " generated " + this.currentProduced+ " units of food");
    }

    public void eatFood(){
        int amount = ThreadLocalRandom.current().nextInt(this.cf_min, this.cf_max);
        this.currentEaten = amount;
        if (this.currentEaten < 0) this.currentEaten = 0;
        //System.out.println(this.name + " ate " + this.currentEaten + " units of food");
    }

    public void awaitTruckerToTakeFood(){
        // wait for trucker
        System.out.println(this.name + " is waiting for trucker");
    }

    public void shopForFood() {
        if (this.nf < this.f_min) { this.shop(); }
    }

    @Override
    public void run() {
        while (true) {
            this.farm();
            this.eatFood();
            //this.awaitTruckerToTakeFood();
            this.shopForFood();
            this.nonWork();
        }
    }
}
