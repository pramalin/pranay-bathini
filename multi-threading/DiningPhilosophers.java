import java.util.Random;

class Fork {
    private final int id;
    
    public Fork(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
}

class Philosopher implements Runnable {
    private final int id;
    private final Fork leftFork;
    private final Fork rightFork;
    private final Random random = new Random();
    
    public Philosopher(int id, Fork leftFork, Fork rightFork) {
        this.id = id;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                think();
                eat();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private void think() throws InterruptedException {
        System.out.println("Philosopher " + id + " is thinking");
        Thread.sleep(random.nextInt(1000));
    }
    
    private void eat() throws InterruptedException {
        // Acquire forks in order to prevent deadlock
        synchronized (leftFork) {
            System.out.println("Philosopher " + id + " picked up left fork " + leftFork.getId());
            
            synchronized (rightFork) {
                System.out.println("Philosopher " + id + " picked up right fork " + rightFork.getId());
                System.out.println("Philosopher " + id + " is EATING");
                Thread.sleep(random.nextInt(1000));
            }
            
            System.out.println("Philosopher " + id + " put down right fork " + rightFork.getId());
        }
        
        System.out.println("Philosopher " + id + " put down left fork " + leftFork.getId());
    }
}

public class DiningPhilosophers {
    public static void main(String[] args) {
        Fork[] forks = new Fork[5];
        Philosopher[] philosophers = new Philosopher[5];
        
        // Create forks
        for (int i = 0; i < 5; i++) {
            forks[i] = new Fork(i);
        }
        
        // Create philosophers with asymmetric fork ordering for the last one
        for (int i = 0; i < 4; i++) {
            philosophers[i] = new Philosopher(i, forks[i], forks[i + 1]);
        }
        
        // Last philosopher picks right fork first to break circular wait
        philosophers[4] = new Philosopher(4, forks[0], forks[4]);
        
        // Start all philosopher threads
        for (Philosopher philosopher : philosophers) {
            new Thread(philosopher).start();
        }
    }
}

