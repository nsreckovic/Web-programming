package server;

import model.Status;

public class CyclicBarrier2Runnable implements Runnable {
    @Override
    public void run() {

        for (ServerThread s : Server.serverThreads) {
            if (s.getSeatNumber() > -1) {
                if (s.getSeatNumber() != Server.currentActive) {
                    if (s.getGuess() == Server.currentActiveShorter) s.setPoints(s.getPoints() + 1);
                } else {
                    if (Server.currentActiveShorter) {
                        s.setActive(false);
                        s.setPoints(-1);
                        for (int i = 0; i < Server.tableSeats; i++) {
                            if (Server.players[i].equals(s.getClientID())) {
                                Server.players[i] = null;
                                break;
                            }
                        }
                        System.out.println("Client drew the shorter stick. Relasing semaphore.\n");
                        break;
                    }
                }
            }
        }


        Server.currentIteration++;
        Server.currentActive++;
        System.out.println("");

        if (Server.currentActiveShorter) {
            for (ServerThread s : Server.serverThreads) {
                if (s.getSeatNumber() > -1) {
                    s.setPoints(0);
                }
            }
            Server.currentActive = 0;
        }

        if (Server.currentActive >= 6 || Server.currentIteration >= Server.maxRounds) {
            System.out.println("\n[Server T0]: ---------------------------------------- GAME OVER ----------------------------------------\n\n");
            if (Server.currentActive == 6) System.out.println("The program is completed due to game completion.");
            else if (Server.currentIteration == Server.maxRounds) {
                System.out.println("The program is completed because the maximum number of rounds has been completed (" + Server.currentIteration + ").");
            }
            for (ServerThread s : Server.serverThreads) {
                s.setActive(false);
                s.getResponse().setStatus(Status.FINISHED);
            }
            int max = -1;
            ServerThread bestThread = null;
            for (ServerThread s : Server.serverThreads) {
                if (s.getPoints() > max) {
                    max = s.getPoints();
                    bestThread = s;
                }
            }
            System.out.println("\n[Server T0]: Winner with most points (" + bestThread.getPoints() + ") is client [" + bestThread.getClientID() + "] with thread [" + bestThread.getSName() + "].");

            System.out.println("\n[Server T0]: Statistics:");
            for (ServerThread s : Server.serverThreads) {
                if (s.getClientID() != null) System.out.println("[Server T0]: [" + s.getSName() + "]: -> Client: [" + s.getClientID() + "] - " + s.getPoints() + " points");
            }
            System.out.println("\n\n");
        } else {
            if (Server.currentActiveShorter) Server.seatSemaphore.release();
        }

    }
}
