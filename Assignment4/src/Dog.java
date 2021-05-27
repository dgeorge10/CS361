import java.util.concurrent.ThreadLocalRandom;

/**
 * Enum of Dog types
 */
enum DogType {
    None,
    Ridgeback,
    Coonhound,
    Doodle
}

public abstract class Dog implements Runnable {
    String name = "";
    DogType type = DogType.None;
    Kennel kennel;
    private final long startTime = System.currentTimeMillis();

    final static int outsideMax = 3;
    final static int eatMax = 10;

    public Dog(String name, Kennel kennel) {
        this.name = name;
        this.kennel = kennel;
    }

    /**
     * Helper method to get the lifespan of a thread
     * @return age() - ms
     */
    protected final long age() {
       return System.currentTimeMillis() - startTime;
    }

    /**
     * Sleep to simulate eating
     */
    protected void eat() {
        System.out.println(this.name + " is eating");
        try {
            int seconds = ThreadLocalRandom.current().nextInt(1, this.eatMax);
            System.out.println(this.name + " is eating for " + seconds + " seconds");
            Thread.sleep(1000* seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.name + " is done eating");
    }

    /**
     * Simulate being outside by sleeping for a random amount of time
     */
    public void outside() {
        try {
            int seconds = ThreadLocalRandom.current().nextInt(1, this.outsideMax);
            System.out.println(this.name + " is outside for " + seconds + " seconds");
            Thread.sleep(seconds*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Run the lifecycle of a dog
     * 1. go outside
     * 2. get in line in kennel
     * 3. eat
     */
    @Override
    public void run() {
        while(true) {
            this.outside();
            this.kennel.beginGetInLine(this);
            this.kennel.endGetInLine(this);
            this.kennel.beginEating(this);
            this.kennel.endEating(this);
        }
    }
}
