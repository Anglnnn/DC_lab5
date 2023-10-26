package partB;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.atomic.AtomicInteger;

public class FourThreadStringChanger {

    private static final int NUM_THREADS = 4;
    private static final int NUM_LINES = 3;

    private static final char[] ALLOWED_CHARS = {'A', 'B', 'C', 'D'};

    private static final ReentrantLock[] lineLocks = new ReentrantLock[NUM_LINES + 1];
    private static final Condition[] lineConditions = new Condition[NUM_LINES + 1];
    private static final AtomicInteger[] lineABCounts = new AtomicInteger[NUM_LINES + 1];

    public static void main(String[] args) {
        for (int i = 0; i < NUM_LINES + 1; i++) {
            lineLocks[i] = new ReentrantLock();
            lineConditions[i] = lineLocks[i].newCondition();
            lineABCounts[i] = new AtomicInteger();
        }

        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(new LineChanger(i));
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("All threads have finished!");
    }

    private static class LineChanger implements Runnable {

        private final int lineIndex;

        public LineChanger(int lineIndex) {
            this.lineIndex = lineIndex;
        }

        @Override
        public void run() {
            while (true) {
                lineLocks[lineIndex].lock();

                try {
                    boolean allLinesEqual = lineABCounts[lineIndex].get() == lineABCounts[(lineIndex + 1) % lineABCounts.length].get()
                            && lineABCounts[lineIndex].get() == lineABCounts[(lineIndex + 2) % lineABCounts.length].get();

                    if (allLinesEqual) {
                        return;
                    }

                    int randomIndex = (int) (Math.random() * ALLOWED_CHARS.length);
                    char newChar = ALLOWED_CHARS[(ALLOWED_CHARS[randomIndex] == 'A' ? 'C' : 'B')];
                    lineABCounts[lineIndex].addAndGet(newChar == 'A' ? 1 : -1);

                    lineConditions[lineIndex].signalAll();
                } finally {
                    lineLocks[lineIndex].unlock();
                }

                try {
                    lineConditions[lineIndex].await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
