import java.util.ArrayList;
import java.util.LinkedList;

public class Kennel {
    private LinkedList<Dog> queue;
    private int numReaders = 0;
    private int numWaitingReaders = 0;
    private int numWriters = 0;
    private int numWaitingWriters = 0;
    private long startWaitingReadersTime = 0;
    private boolean okToWrite = true;

    public Kennel() {
        System.out.println("Kennel built");
        this.queue = new LinkedList<>();
    }

    /**
     * Helper method to print current queue
     */
    private synchronized void printQueue() {
        System.out.println(this.queue.toString());
    }

    /**
     * Synchronized method to try to put a dog in the queue
     * @param dog Current dog trying to get in line
     */
    public synchronized void beginGetInLine(Dog dog) {
        System.out.println(dog.name + " wants to get in line for the kennel");
        if (this.numReaders > 0 || this.numWriters > 0) {
            this.numWaitingWriters++;
            this.okToWrite = false;
            while(!this.okToWrite){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.numWaitingWriters--;
            }
            this.okToWrite = false;
            this.numWriters++;
        }
        // We have successfully gained access to the queue and can add the current dog
        this.queue.addLast(dog);
        this.printQueue();
    }

    /**
     * Finish adding the dog to the queue by notifying all other waiting threads
     * @param dog that was just added to the queue
     */
    public synchronized void endGetInLine(Dog dog) {
        this.numWriters--;
        this.okToWrite = this.numWaitingReaders == 0;
        this.startWaitingReadersTime = dog.age();
        notifyAll();
    }

    /**
     * Synchronized method to begin eating for a dog. This method ensure the following:
     * 1. only dogs of the same breed get through
     * 2. only one Doodle at a time gets through
     * @param dog
     */
    public synchronized void beginEating(Dog dog) {
        long readerArrivalTime = 0;
        if (this.numWaitingWriters > 0 || this.numWaitingReaders > 0 || this.queue.isEmpty()) {
            System.out.println(dog.name + " is waiting to eat");
            this.numWaitingReaders++;
            readerArrivalTime = dog.age();
            while (readerArrivalTime >= this.startWaitingReadersTime) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.numWaitingReaders--;
        }
        this.numReaders++;

        // Once we have access to the queue, pop all dogs off the queue that can eat
        boolean selectingDogs = true;
        ArrayList<Dog> currentDogs = new ArrayList<>();
        while(selectingDogs && !this.queue.isEmpty()) {
            Dog doggo = this.queue.pop();
            currentDogs.add(doggo);
            // continue popping if there are more dogs, and the next dog is the same as the one we just popped
            // if we previously popped a Doodle, then don't continue
            selectingDogs = !this.queue.isEmpty() && doggo.type != DogType.Doodle && this.queue.getFirst().type == doggo.type;
        }

        // make all of the dogs that were let in to the kennel eat
        if (currentDogs.size() > 0) {
            System.out.println(currentDogs.size() + " dogs of type " + currentDogs.get(0).type +" currently eating");
            for (Dog doggo: currentDogs) {
                doggo.eat();
            }
        }
    }

    /**
     * Finish eating for a dog by waking all waiting threads that were trying to eat
     * @param dog
     */
    public synchronized void endEating(Dog dog) {
        this.numReaders--;
        this.okToWrite = this.numReaders == 0;
        if (okToWrite) notifyAll();
    }
}
