package com.ns;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Professor implements Runnable {

    private CountDownLatch examinersInitialisedLatch;
    private Semaphore semaphore;
    private CyclicBarrier cyclicBarrier;
    private String threadName;

    private CountDownLatch defenseLatch;

    public Professor(CountDownLatch examinersInitialisedLatch) {
        this.examinersInitialisedLatch = examinersInitialisedLatch;
        semaphore = new Semaphore(2, true);
        cyclicBarrier = new CyclicBarrier(2);
        threadName = "Professor thread";
        defenseLatch = new CountDownLatch(2);
    }

    public int scoreStudent() {
        return (new Random()).nextInt(10) + 1;
    }

    @Override
    public void run() {
        Thread.currentThread().setName(threadName);
        Main.printQueue.add("Professor initialised.");
        examinersInitialisedLatch.countDown();
        while (!Thread.currentThread().isInterrupted()) ;
        Main.printQueue.add("Professor thread shutdown.");
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    public CyclicBarrier getCyclicBarrier() {
        return cyclicBarrier;
    }

    public String getThreadName() {
        return threadName;
    }

    public CountDownLatch getDefenseLatch() {
        return defenseLatch;
    }

    public void resetDefenseLatch() {
        defenseLatch = new CountDownLatch(2);
    }
}
