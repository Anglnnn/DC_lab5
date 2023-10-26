package partA;

public class RecruitSimulator {

    private static final int NUMBER_OF_RECRUITS = 100;

    public static void main(String[] args) throws InterruptedException {
        RecruitSynchronizer synchronizer = new RecruitSynchronizer(NUMBER_OF_RECRUITS);

        // Create a separate thread for each recruit
        Thread[] recruitThreads = new Thread[NUMBER_OF_RECRUITS];
        for (int i = 0; i < recruitThreads.length; i++) {
            recruitThreads[i] = new Thread(() -> {
                try {
                    // Turn left or right depending on the command
                    if (Math.random() < 0.5) {
                        synchronizer.turnLeft();
                    } else {
                        synchronizer.turnRight();
                    }

                    // Wait for all other recruits to turn
                    while (!synchronizer.isSteadyState()) {
                        Thread.sleep(1);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        // Start all of the recruit threads
        for (Thread recruitThread : recruitThreads) {
            recruitThread.start();
        }

        // Wait for all of the recruit threads to finish
        for (Thread recruitThread : recruitThreads) {
            recruitThread.join();
        }

        // All recruits have reached a steady state
        System.out.println("All recruits have reached a steady state!");
    }
}

