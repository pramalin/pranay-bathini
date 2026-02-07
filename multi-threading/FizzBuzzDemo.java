class FizzBuzz {
    private final int n;
    private int current = 1;
    private volatile int turn = 4; // Start with number thread

    public FizzBuzz(int n) {
        this.n = n;
    }

    public synchronized void fizz() throws InterruptedException {
        while (current <= n) {
            while (current <= n && turn != 1) {
                wait();
            }

            if (current > n) break;

            System.out.println("Fizz");
            current++;
            turn = getNextTurn(current);
            notifyAll();
        }
    }

    public synchronized void buzz() throws InterruptedException {
        while (current <= n) {
            while (current <= n && turn != 2) {
                wait();
            }

            if (current > n) break;

            System.out.println("Buzz");
            current++;
            turn = getNextTurn(current);
            notifyAll();
        }
    }

    public synchronized void fizzbuzz() throws InterruptedException {
        while (current <= n) {
            while (current <= n && turn != 3) {
                wait();
            }

            if (current > n) break;

            System.out.println("FizzBuzz");
            current++;
            turn = getNextTurn(current);
            notifyAll();
        }
    }

    public synchronized void number() throws InterruptedException {
        while (current <= n) {
            while (current <= n && turn != 4) {
                wait();
            }

            if (current > n) break;

            System.out.println(current);
            current++;
            turn = getNextTurn(current);
            notifyAll();
        }
    }

    private int getNextTurn(int num) {
        if (num > n) return -1;

        if (num % 15 == 0) return 3; // FizzBuzz
        if (num % 3 == 0) return 1;  // Fizz
        if (num % 5 == 0) return 2;  // Buzz
        return 4;                     // Number
    }
}

public class FizzBuzzDemo {
    public static void main(String[] args) {
        FizzBuzz fizzBuzz = new FizzBuzz(25);

        Thread t1 = new Thread(() -> {
            try { fizzBuzz.fizz(); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }, "Fizz");

        Thread t2 = new Thread(() -> {
            try { fizzBuzz.buzz(); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }, "Buzz");

        Thread t3 = new Thread(() -> {
            try { fizzBuzz.fizzbuzz(); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }, "FizzBuzz");

        Thread t4 = new Thread(() -> {
            try { fizzBuzz.number(); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }, "Number");

        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}
