import java.util.concurrent.Semaphore;

/**
 * Custom implementation of a thread-safe Bounded Buffer. This is adopted from the slide deck in week5 part2
 * The BoundedBuffer will represent a FIFO queue, and use Semaphores as locks to ensure threadsafe operation
 * The BoundedBuffer queue is using a LinkedList with a pointer to the head and tail. This is done so that
 * adding items to the queue can be as easy as changing what the tail is and removing items from the queue
 * is accessing the head of the LinkedList
 */
public class BoundedBuffer<E> {

    static class Node<E> {
        E data;
        Node<E> next;
        public Node(E data) {
           this.data = data;
        }
    }

    private int capacity;
    private int size;
    private Semaphore sizeLock;
    private Semaphore putIn;
    private Semaphore takeOut;

    // keep track of tail of list for easy insertion
    private Node<E> head;
    private Node<E> tail;

    public BoundedBuffer() { this(Integer.MAX_VALUE); }

    public BoundedBuffer(int capacity) {
        if (size >= 0) {
            this.tail = this.head = new Node<E>(null);
            this.size = 0;
            this.capacity = capacity;
            this.putIn = new Semaphore(1);
            this.takeOut = new Semaphore(1);
            this.sizeLock = new Semaphore(1);
        }
    }

    /**
     * Method to add a new item to the queue
     * @param item to put on the queue
     * @throws InterruptedException
     */
    public void put(E item) throws InterruptedException {
        Node<E> newNode = new Node<>(item);
        try {
            // wait for the put in lock
            this.putIn.acquire();

            //TODO
            if(this.size == this.capacity) {
            }

            this.sizeLock.acquire();
            if(this.size < this.capacity) {
                this.tail = this.tail.next = newNode;
                this.size += 1;
            }
            this.sizeLock.release();
        } finally {
            this.putIn.release();
        }
    }

    /**
     * Method to take the first item that is in the BoundedBuffere queue
     * @return E The first item in the queue
     * @throws InterruptedException
     */
    public E take() throws InterruptedException {
        E element;
        try {
            this.takeOut.acquire();
            Node<E> h = this.head;
            Node<E> first = h.next;
            this.head = first;
            element = first.data;
            first.data = null;
            this.sizeLock.acquire();
            this.size -= 1;
            this.takeOut.release();
        } finally {
            this.sizeLock.release();
        }
        return element;
    }
}
