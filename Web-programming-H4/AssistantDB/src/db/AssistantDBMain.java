package db;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class AssistantDBMain {
    private static int port = 9000;

    protected static ArrayList<AssistantRate> assistantRates = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("AssistantDB is listening on port: " + port + "\n");

            while (true) {
                Socket socket = serverSocket.accept();
                AssistantDBThread dbThread = new AssistantDBThread(socket);
                dbThread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected synchronized static void addAssistantRate(AssistantRate ar) {
        assistantRates.add(ar);
    }

    public synchronized static ArrayList<AssistantRate> getAssistantRates() {
        return assistantRates;
    }
}

