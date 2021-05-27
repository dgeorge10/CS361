public class Doodle extends Dog implements Runnable {
    public Doodle(String name, Kennel kennel) {
        super(name, kennel);
        this.type = DogType.Doodle;
        new Thread(this).start();
    }
}
