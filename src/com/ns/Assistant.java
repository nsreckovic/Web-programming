package com.ns;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class Assistant implements Runnable {

    private CountDownLatch examinersInitialisedLatch;
    private Semaphore semaphore;
    private String threadName;

    public Assistant(CountDownLatch examinersInitialisedLatch){
        this.examinersInitialisedLatch = examinersInitialisedLatch;
        semaphore = new Semaphore(1);
        threadName = "Assistant thread";
    }

    public int scoreStudent(){
        return (new Random()).nextInt(10) + 1;
    }

    @Override
    public void run() {
        Thread.currentThread().setName(threadName);
        Main.printQueue.add("Assistant initialised.");
        examinersInitialisedLatch.countDown();
        while(!Thread.currentThread().isInterrupted());
        Main.printQueue.add("Assistant thread shutdown.");
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    public String getThreadName() {
        return threadName;
    }

}
