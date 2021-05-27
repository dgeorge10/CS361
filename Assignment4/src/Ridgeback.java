public class Ridgeback extends Dog implements Runnable {
    public Ridgeback(String name, Kennel kennel) {
        super(name, kennel);
        this.type = DogType.Ridgeback;
        new Thread(this).start();
    }
}
