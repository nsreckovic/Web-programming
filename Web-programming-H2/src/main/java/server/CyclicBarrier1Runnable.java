package server;

import model.Status;

import java.util.Random;

public class CyclicBarrier1Runnable implements Runnable {
    @Override
    public void run() {
        if (Server.currentActive < 6) {
            for (ServerThread s : Server.serverThreads) {
                if (s.getSeatNumber() > -1) {
                    s.getResponse().setStatus(Status.BID);
                    if (s.getSeatNumber() == Server.currentActive) {
                        s.getResponse().setStatus(Status.DRAW);
                    }
                }
            }
            Server.currentShorter = (new Random()).nextInt(6);
            System.out.println("The shorter stick is: " + Server.currentShorter);
        }
    }
}
