public class Coonhound extends Dog implements Runnable {
    public Coonhound(String name, Kennel kennel) {
        super(name, kennel);
        this.type = DogType.Coonhound;
        new Thread(this).start();
    }
}
