package com.ns;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static AtomicInteger sumOfScores = new AtomicInteger();
    public static AtomicInteger noStudents = new AtomicInteger();
    public static AtomicBoolean active = new AtomicBoolean(true);

    public static long initialisationTime;

    public static ConcurrentLinkedQueue<String> printQueue = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) throws InterruptedException {
        System.out.print("Please enter number of students: ");
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        sc.close();

        // Initialising examiners, using CountDownLatch to make sure they are both initialised before program starts
        CountDownLatch examinersInitialisedLatch = new CountDownLatch(2);
        Professor professor = new Professor(examinersInitialisedLatch);
        Assistant assistant = new Assistant(examinersInitialisedLatch);

        ExecutorService examinersExecutorService = Executors.newFixedThreadPool(2);
        examinersExecutorService.execute(professor);
        examinersExecutorService.execute(assistant);

        // Waiting for all examiners to initialise
        examinersInitialisedLatch.await();
        Main.printQueue.add("All examiners initialised. Defense time starts now.\n");

        // Initialising defense start time
        initialisationTime = System.currentTimeMillis();

        // Initialising students
        ScheduledExecutorService studentsExecutorService = Executors.newScheduledThreadPool(N);

        for (int i = 0; i < N; i++) {
            long defenseTime = (new Random()).nextInt(500) + 500;
            long retentionTime = (new Random()).nextInt(1000);
            String studentName = "Student " + (i + 1);
            Student student = new Student(studentName, professor, assistant, defenseTime);
            studentsExecutorService.schedule(student, retentionTime, TimeUnit.MILLISECONDS);
        }

        // Total defense time -> 5s = 5000ms
        Thread.sleep(5000);

        // Defense finished, shutting everything down
        Main.active.set(false);
        Main.printQueue.add("");
        studentsExecutorService.shutdownNow();
        examinersExecutorService.shutdownNow();

        DecimalFormat df = new DecimalFormat("0.00");
        Main.printQueue.add("\nStudents examined: " + noStudents.intValue());
        Main.printQueue.add("Scores total: " + sumOfScores.intValue());
        Main.printQueue.add("Average: " + df.format(sumOfScores.doubleValue() / noStudents.doubleValue()));

        Iterator<String> printQueueIterator = printQueue.iterator();
        while (printQueueIterator.hasNext()) {
            String msg = printQueueIterator.next();
            System.out.println(msg);
        }

    }
}
