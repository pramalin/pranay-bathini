import java.util.concurrent.Semaphore;

class OddEvenPrinter {
    private final int max;
    private final Semaphore oddSemaphore = new Semaphore(1); // Start with odd
    private final Semaphore evenSemaphore = new Semaphore(0);

    public OddEvenPrinter(int max) {
        this.max = max;
    }

    public void printOdd() {
        for (int i = 1; i <= max; i += 2) {
            try {
                oddSemaphore.acquire(); // Wait for permit
                System.out.println("Odd: " + i);
                evenSemaphore.release(); // Give permit to even thread
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void printEven() {
        for (int i = 2; i <= max; i += 2) {
            try {
                evenSemaphore.acquire(); // Wait for permit
                System.out.println("Even: " + i);
                oddSemaphore.release(); // Give permit to odd thread
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

public class OddEvenDemo {
    public static void main(String[] args) {
        OddEvenPrinter printer = new OddEvenPrinter(10);

        Thread oddThread = new Thread(printer::printOdd, "Odd-Thread");
        Thread evenThread = new Thread(printer::printEven, "Even-Thread");

        oddThread.start();
        evenThread.start();
    }
}
