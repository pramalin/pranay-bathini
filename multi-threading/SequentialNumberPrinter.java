public class SequentialNumberPrinter {
    static final String ONE = " ONE_THREAD";
    static final String TWO = " TWO_THREAD";
    static final String THREE = " THREE_THREAD";

    public static void main(String[] args) {
        SharedPrinter2 sharedPrinter = new SharedPrinter2();
        Thread oneThread = new Thread(new Task2(sharedPrinter, true), ONE);
        Thread twoThread = new Thread(new Task2(sharedPrinter, false), TWO);
        Thread threeThread = new Thread(new Task2(sharedPrinter, null), THREE);
        oneThread.start();
        twoThread.start();
        threeThread.start();
    }
}

class Task2 implements Runnable {
    private SharedPrinter2 sharedPrinter;
    private Boolean isTurn;

    public Task2(SharedPrinter2 sharedPrinter, Boolean isTurn) {
        this.sharedPrinter = sharedPrinter;
        this.isTurn = isTurn;
    }

    @Override
    public void run() {
        int number = isTurn != null ? (isTurn ? 1 : 2) : 3;
        while (number <= 10) {
            sharedPrinter.printNumber(number, isTurn);
            number += 3;
        }
    }
}

class SharedPrinter2 {
    private volatile Boolean isTurn = true; // Start with thread 1

    synchronized void printNumber(int number, Boolean isTurn) {
        try {
            // Wait until it's this thread's turn
            while (this.isTurn != isTurn) {
                wait();
            }

            // Print the number with thread name
            System.out.println(number + Thread.currentThread().getName());

            // Cycle through the states: true -> false -> null -> true
            if (this.isTurn != null && this.isTurn) {
                this.isTurn = false;      // Thread 1 -> Thread 2
            } else if (this.isTurn != null && !this.isTurn) {
                this.isTurn = null;       // Thread 2 -> Thread 3
            } else {
                this.isTurn = true;        // Thread 3 -> Thread 1
            }

            // Wake up all waiting threads
            notifyAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
