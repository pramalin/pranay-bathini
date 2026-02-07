public class EvenOddPrinter {
    static final String ODD_THREAD = " ODD_THREAD";
    static final String EVEN_THREAD = " EVEN_THREAD";

    public static void main(String[] args) {
        SharedPrinter2 sharedPrinter = new SharedPrinter2();
        Thread evenThread = new Thread(new Task2(sharedPrinter, true), EVEN_THREAD);
        Thread oddThread = new Thread(new Task2(sharedPrinter, false), ODD_THREAD);
        evenThread.start();
        oddThread.start();
    }
}

class Task implements Runnable {
    private SharedPrinter2 sharedPrinter;
    private boolean isEven;

    public Task(SharedPrinter2 sharedPrinter, boolean isEven) {
        this.sharedPrinter = sharedPrinter;
        this.isEven = isEven;
    }

    @Override
    public void run() {
        int number = isEven ? 2 : 1;
        while (number <= 10) {
            sharedPrinter.printNumber(number, isEven);
            number += 2;
        }
    }
}

class SharedPrinter {
    private volatile boolean isEven = true;

    synchronized void printNumber(int number, boolean isEven) {
        try {
            // Wait while it's not this thread's turn
            while (this.isEven == isEven) {
                wait();
            }

            // Print the number with thread name
            System.out.println(number + Thread.currentThread().getName());

            // Toggle the state
            this.isEven = !this.isEven;

            // Notify waiting thread
            notify();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
