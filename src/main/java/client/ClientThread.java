package client;

import com.google.gson.Gson;
import model.Request;
import model.Response;
import model.Status;

import java.io.*;
import java.net.Socket;
import java.util.Random;
import java.util.UUID;

public class ClientThread implements Runnable {
    private String name;
    private String id;
    private Socket socket;
    private BufferedWriter out;
    private BufferedReader in;
    private Gson gson;

    private int seatNumber;
    private boolean seated = false;

    private Response response;
    private Request request;

    private boolean active = false;

    public ClientThread(String name) throws IOException {
        this.name = name;
        socket = new Socket("localhost", 9000);
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        gson = new Gson();
        id = UUID.randomUUID().toString();

        response = new Response();
        request = new Request();
    }

    public void run() {
        active = true;

        request.setStatus(Status.CONNECT);
        request.setData(id);
        request();

        while (active) {
            response();

            if (response.getStatus().equals(Status.CONNECTED)) {
                seatNumber = ((Double) response.getData()).intValue();
                print("Recieved CONNECTED status for client [" + id + "]. Client took a seat " + seatNumber + ".");
                request.setStatus(Status.READY);
                request();

            } else if (response.getStatus().equals(Status.TIMEOUT)) {
                print("Recieved TIMEOUT status for client [" + id + "]");
                active = false;

            }  else if (response.getStatus().equals(Status.BID)) {
                boolean guess = (new Random()).nextBoolean();
                request.setStatus(Status.BID);
                request.setData(guess);
                print("Recieved BID status for client [" + id + "]. Client bid " + guess + ".");
                request();

            } else if (response.getStatus().equals(Status.DRAW)) {
                int draw = (new Random()).nextInt(6);
                request.setStatus(Status.DREW);
                request.setData(draw);
                print("Recieved DRAW status for client [" + id + "]. Client drew " + draw + ".");
                request();

            } else if (response.getStatus().equals(Status.FINISHED)){
                print("Recieved FINISHED status for client [" + id + "]");
                active = false;

            }

        }

        print("finnished -> [" + id + "]");

    }

    private Request request() {
        try {
            out.write(gson.toJson(request));
            out.newLine();
            out.flush();
            return request;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Response response() {
        try {
            String json = in.readLine();
            //System.out.println(json);
            response = gson.fromJson(json, Response.class);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void print(String msg) {
        System.out.println("[" + name + "]: " + msg);
    }

}
