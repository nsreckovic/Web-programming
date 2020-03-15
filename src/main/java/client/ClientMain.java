package client;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientMain {

    private static ScheduledExecutorService executorService;

    private static int clientThreadCounter = 1;

    public static void main(String[] args) throws IOException {
        System.out.print("Please enter number of players: ");
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        sc.close();

        executorService = Executors.newScheduledThreadPool(N);

        for (int i = 0; i < N; i++) {
            int delay = (new Random()).nextInt(1000);
            executorService.schedule(new ClientThread("Client T" + clientThreadCounter++), delay, TimeUnit.MILLISECONDS);
        }

        executorService.shutdown();
    }

}
