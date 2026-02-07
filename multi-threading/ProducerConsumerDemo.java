import java.util.LinkedList;
import java.util.Queue;

class ProducerConsumer {
    private final Queue<Integer> queue = new LinkedList<>();
    private final int capacity;

    public ProducerConsumer(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void produce(int item) throws InterruptedException {
        // Wait while queue is full
        while (queue.size() == capacity) {
            System.out.println("Queue is full. Producer waiting...");
            wait(); // Release lock and wait
        }

        queue.add(item);
        System.out.println("Produced: " + item + " | Queue size: " + queue.size());

        // Notify consumers that item is available
        notifyAll();
    }

    public synchronized int consume() throws InterruptedException {
        // Wait while queue is empty
        while (queue.isEmpty()) {
            System.out.println("Queue is empty. Consumer waiting...");
            wait(); // Release lock and wait
        }

        int item = queue.poll();
        System.out.println("Consumed: " + item + " | Queue size: " + queue.size());

        // Notify producers that space is available
        notifyAll();

        return item;
    }
}

class Producer implements Runnable {
    private final ProducerConsumer pc;
    private int item = 0;

    public Producer(ProducerConsumer pc) {
        this.pc = pc;
    }

    @Override
    public void run() {
        try {
            while (true) {
                pc.produce(++item);
                Thread.sleep(500); // Simulate production time
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Consumer implements Runnable {
    private final ProducerConsumer pc;

    public Consumer(ProducerConsumer pc) {
        this.pc = pc;
    }

    @Override
    public void run() {
        try {
            while (true) {
                pc.consume();
                Thread.sleep(1000); // Simulate consumption time
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class ProducerConsumerDemo {
    public static void main(String[] args) {
        ProducerConsumer pc = new ProducerConsumer(5);

        Thread producerThread = new Thread(new Producer(pc), "Producer");
        Thread consumerThread = new Thread(new Consumer(pc), "Consumer");

        producerThread.start();
        consumerThread.start();
    }
}
