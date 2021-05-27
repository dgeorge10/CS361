public class Main {
    public static void main(String[] args) {
        int numRidgebacks = 3;
        int numCoonhounds = 3;
        int numDoodles = 3;

        String argName = "";
        String argValue = "";

        for(int i = 0; i < args.length - 1; i ++) {
            argName = args[i];
            argValue = args[i+1];
            try {
                switch (argName) {
                    case "-r":
                        numRidgebacks = Integer.parseInt(argValue);
                        break;
                    case "-c":
                        numCoonhounds = Integer.parseInt(argValue);
                        break;
                    case "-d":
                        numDoodles = Integer.parseInt(argValue);
                        break;
                }
            } catch (Exception e) {
                System.out.println("Failed to parse command line arguments");
                e.printStackTrace();
            }
        }
        Kennel kennel = new Kennel();

        for (int i = 0; i < numRidgebacks; i++) {
            System.out.println("Starting Ridgeback"+i);
            new Ridgeback("Ridgeback"+i, kennel);
        }

        for (int i = 0; i < numCoonhounds; i++) {
            System.out.println("Starting Coonhound"+i);
            new Coonhound("Coonhound"+i, kennel);
        }

        for (int i = 0; i < numDoodles; i++) {
            System.out.println("Starting Doodle"+i);
            new Doodle("Doodle"+i, kennel);
        }

        System.out.println("All dogs started");
    }
}
