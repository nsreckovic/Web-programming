package com.ns;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Student implements Runnable {
    private long arrivalTime;
    private long defenseStartedTime = -1;
    private long defenseTime;
    private Professor professor;
    private Assistant assistant;
    private boolean finished = false;
    private int score = 0;
    private String name;
    private String examiner;

    boolean flag = true;

    public Student(String name, Professor professor, Assistant assistant, long defenseTime) {
        this.name = name;
        this.professor = professor;
        this.assistant = assistant;
        this.defenseTime = defenseTime;
    }

    @Override
    public void run() {
        arrivalTime = System.currentTimeMillis();

        if (arrivalTime + defenseTime - Main.initialisationTime >= 5000) return;

        while (Main.active.get() && !finished) {

            if (professor.getSemaphore().tryAcquire() && Main.active.get()) {
                try {
                    professor.getCyclicBarrier().await(1000, TimeUnit.MILLISECONDS);
                    defenseStartedTime = System.currentTimeMillis();

                } catch (InterruptedException e) {
                    //Main.waitingQueue.add("[" + name + "]: Barrier await professor interrupt! No time to finnish defense. -> " + (System.currentTimeMillis() - Main.initialisationTime));
                    return;

                } catch (TimeoutException e) {
                    //Main.waitingQueue.add("[" + name + "]: Timeout await professor excception! Can't wait no more. -> " + (System.currentTimeMillis() - Main.initialisationTime));
                    professor.getSemaphore().release();
                    tryAssistant();
                    continue;

                } catch (BrokenBarrierException e) {
                    if (flag) {
                        //Main.waitingQueue.add("Barrier exception: " + name + " - " + (System.currentTimeMillis() - Main.initialisationTime));
                        flag = false;
                    }
                    professor.getSemaphore().release();
                    tryAssistant();
                    continue;
                }

                try {
                    Thread.sleep(defenseTime);

                } catch (InterruptedException e) {
                    //Main.waitingQueue.add("[" + name + "]: Sleeping professor interrupt! No time to finnish defense. -> " + (System.currentTimeMillis() - Main.initialisationTime));
                    return;

                }

                professor.getDefenseLatch().countDown();

                try {
                    professor.getDefenseLatch().await();

                } catch (InterruptedException e) {
                    //Main.waitingQueue.add("[" + name  + " - " + (arrivalTime - Main.initialisationTime) + "]: Awaiting other student interrupt! No time to finnish defense. -> " + (System.currentTimeMillis() - Main.initialisationTime));
                    return;

                }

                if (professor.getDefenseLatch().getCount() == 0) professor.resetDefenseLatch();

                professor.getSemaphore().release();

                score = professor.scoreStudent();
                finished = true;
                examiner = professor.getThreadName();

            }

            if (!tryAssistant()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    //Main.waitingQueue.add("Ending sleep interrupt! No time to finnish student defense.");
                    return;
                }
            }

        }

        if (finished) {
            Main.sumOfScores.addAndGet(score);
            Main.noStudents.incrementAndGet();
            arrivalTime -= Main.initialisationTime;
            defenseStartedTime -= Main.initialisationTime;

            Main.printQueue.add("Thread: " + name +
                    " | Examiner: " + examiner +
                    " | Arrival time: " + arrivalTime + "ms" +
                    " | Defense started time: " + defenseStartedTime + "ms" +
                    " | Defense time: " + defenseTime + "ms" +
                    " | Score: " + score);
        }

    }

    private boolean tryAssistant() {
        if (assistant.getSemaphore().tryAcquire() && !finished && Main.active.get()) {
            try {
                defenseStartedTime = System.currentTimeMillis();
                Thread.sleep(defenseTime);
                score = assistant.scoreStudent();
                finished = true;
                examiner = assistant.getThreadName();
                assistant.getSemaphore().release();
                return true;

            } catch (InterruptedException e) {
                //Main.waitingQueue.add("Assistant interrupt! No time to finnish student defense.");
                return false;
            }

        }
        return false;
    }


}
