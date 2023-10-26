package partA;

import java.util.concurrent.Semaphore;

public class RecruitSynchronizer {

    private final Semaphore semaphore;
    private final int numberOfRecruits;
    private final boolean[] recruitDirections;

    public RecruitSynchronizer(int numberOfRecruits) {
        this.numberOfRecruits = numberOfRecruits;
        this.semaphore = new Semaphore(1);
        this.recruitDirections = new boolean[numberOfRecruits + 20];
    }

    public void turnLeft() throws InterruptedException {
        semaphore.acquire();
        if (Thread.currentThread().getId() >= recruitDirections.length) {
            throw new ArrayIndexOutOfBoundsException("Index out of bounds");
        }

        recruitDirections[(int) Thread.currentThread().getId()] = true;
        semaphore.release();
    }

    public void turnRight() throws InterruptedException {
        semaphore.acquire();
        if (Thread.currentThread().getId() >= recruitDirections.length) {
            throw new ArrayIndexOutOfBoundsException("Index out of bounds");
        }

        recruitDirections[(int) Thread.currentThread().getId()] = false;
        semaphore.release();
    }

    public boolean isSteadyState() throws InterruptedException {
        semaphore.acquire();

        boolean isSteadyState = true;
        for (int i = 0; i < numberOfRecruits; i++) {
            if (recruitDirections[i] != recruitDirections[0]) {
                isSteadyState = false;
                break;
            }
        }

        semaphore.release();
        return isSteadyState;
    }
}
