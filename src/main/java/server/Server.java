package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class Server implements Runnable {
    private ServerSocket serverSocket;
    private int port = 9000;

    protected static int tableSeats = 6;
    protected static String[] players;

    protected static int maxRounds;
    protected static int currentIteration = 0;

    protected static int currentActive = 0;                 // the one that draws
    protected static boolean currentActiveShorter = false;  // is the one which drew shorter
    protected static int currentShorter;                    // what stick is the shortest one

    private int serverThreadCounter = 1;
    protected static ArrayList<ServerThread> serverThreads = new ArrayList<>();

    protected static Semaphore seatSemaphore = new Semaphore(6);
    protected static CyclicBarrier cyclicBarrier1 = new CyclicBarrier(6, new CyclicBarrier1Runnable());
    protected static CyclicBarrier cyclicBarrier2 = new CyclicBarrier(6, new CyclicBarrier2Runnable());

    public Server() throws IOException {
        serverSocket = new ServerSocket(port);
        players = new String[tableSeats];
    }

    public void run() {
        System.out.print("[Server T0]: Please enter maximum number of rounds: ");
        Scanner sc = new Scanner(System.in);
        maxRounds = sc.nextInt();
        sc.close();

        System.out.println("\n[Server T0]: Server is listening on port: " + port + "\n");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread("Server T" + serverThreadCounter++, socket, cyclicBarrier1, cyclicBarrier2);
                serverThreads.add(serverThread);
                serverThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized int takeSeat(String playerId){
        for (int i = 0; i < tableSeats; i++) {
            if(players[i] == null){
                players[i] = playerId;
                return i;
            }
        }
        return -1;
    }

}
