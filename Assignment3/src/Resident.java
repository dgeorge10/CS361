public abstract class Resident {
    // Amount of food at home in pantry
    public int nf;

    // Trigger for going to the store to buy more food
    public int f_min;

    // Range for how much food is eaten in a given day
    public int cf_min;
    public int cf_max;

    // Range for how much food resident buys when at the shop
    public int bf_min;
    public int bf_max;

    public String name;

    public GroceryStore store = null;

    public Resident(String name, int nf, int fmin, int cfmin, int cfmax, int bfmin, int bfmax, GroceryStore store) {
        this.name = name;
        this.nf = nf;
        this.f_min = fmin;
        this.cf_min = cfmin;
        this.cf_max = cfmax;
        this.bf_min = bfmin;
        this.bf_max = bfmax;
        this.store = store;
    }

    public void eatFood() {}

    public void shop() {
        this.store.getInLine(this);
    }

    public void nonWork() {
        try {
            System.out.println(this.name + " is resting");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
