
class WorkThread implements Runnable {
    int[] chunk;
    int sum = 0;
    public WorkThread(int[] chunk){
        this.chunk = chunk;
    }

    @Override
    public void run() {
       for (int i = 0; i < this.chunk.length; i++)
           this.sum += this.chunk[i];
    }
}
public class Main {

    public static void main(String[] args) {
        int[] T = {1,2,3,4,5};
        int[] B = {1,2,3,4,5};

        int numThreads = 2;
        int tSum = 0;
        int bSum = 0;
        WorkThread[] threads = new WorkThread[2];
        boolean finished = false;
        int chunkSize = 2;
        /*
            The main idea is to create a working thread that knows how to sum up any chunk that is given to it
            The driver program is responsible for dividing the work up until things are finished
            We will remove elements from T and B as they are sent to threads. When both T and B are empty,
            we know our calculations are finished.
            We store the sum as a part of the thread implementation
            Both threads can run on a chunk from either array.
         */
        while (!finished) {
            for(int i = 0; i < numThreads; i++){
                // get chunk for new thread
                // remove elements from T and B as they are calculated so we can finish when T and B are empty
                threads[i] = new WorkThread(chunk);
            }

            for(WorkThread thread : threads) {
                 Thread t = new Thread(thread);
                 t.start();
                 t.join();
            }
            tSum += threads[0].sum;
            bSum += threads[1].sum;
            if (B.length == 0 & T.length == 0) finished = true;
        }

        double ratio = tSum / bSum;
        System.out.println("Ratio: " + ratio);
    }
}
