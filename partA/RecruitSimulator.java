package partA;

public class RecruitSimulator {

    private static final int NUMBER_OF_RECRUITS = 100;

    public static void main(String[] args) throws InterruptedException {
        RecruitSynchronizer synchronizer = new RecruitSynchronizer(NUMBER_OF_RECRUITS);

        Thread[] recruitThreads = new Thread[NUMBER_OF_RECRUITS];
        for (int i = 0; i < recruitThreads.length; i++) {
            recruitThreads[i] = new Thread(() -> {
                try {
                    if (Math.random() < 0.5) {
                        synchronizer.turnLeft();
                    } else {
                        synchronizer.turnRight();
                    }

                    while (!synchronizer.isSteadyState()) {
                        Thread.sleep(1);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        for (Thread recruitThread : recruitThreads) {
            recruitThread.start();
        }

        for (Thread recruitThread : recruitThreads) {
            recruitThread.join();
        }

        System.out.println("All recruits have reached a steady state!");
    }
}

