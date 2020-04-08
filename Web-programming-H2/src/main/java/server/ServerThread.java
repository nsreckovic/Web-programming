package server;

import com.google.gson.Gson;
import model.Request;
import model.Response;
import model.Status;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ServerThread extends Thread {
    private String name;
    private Socket socket;
    private BufferedWriter out;
    private BufferedReader in;
    private Gson gson;

    private CyclicBarrier cyclicBarrier1;
    private CyclicBarrier cyclicBarrier2;

    private String clientID;
    private int seatNumber = -1;
    private int points = 0;

    private Response response;
    private Request request;

    private boolean active = false;


    private boolean guess = false;


    public ServerThread(String name, Socket socket, CyclicBarrier cyclicBarrier1, CyclicBarrier cyclicBarrier2) throws IOException {
        this.name = name;
        this.socket = socket;
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        this.gson = new Gson();

        response = new Response();
        request = new Request();

        this.cyclicBarrier1 = cyclicBarrier1;
        this.cyclicBarrier2 = cyclicBarrier2;
    }

    public void run() {
        active = true;

        try {

            while (active) {
                request();

                if (request.getStatus().equals(Status.CONNECT)) {
                    clientID = (String) request.getData();
                    //print("Client [" + clientID + "] connected.");

                    if (!Server.seatSemaphore.tryAcquire(2000, TimeUnit.MILLISECONDS)) {
                        response.setStatus(Status.TIMEOUT);
                        response();
                        print("Client waited for too long on semaphore. Finnishing.");
                        return;
                    }

                    seatNumber = Server.takeSeat(clientID);
                    if (seatNumber != -1) {
                        response.setStatus(Status.CONNECTED);
                        response.setData(seatNumber);
                        response();
                    }

                } else if (request.getStatus().equals(Status.READY)) {

                    while (active) {
                        if (!firstBarrier()) {
                            removeUser();
                            response();
                            return;
                        }
                        response();

                        request();

                        if (request.getStatus().equals(Status.BID)) {
                            guess = (Boolean) request.getData();
                            print("Client: [" + clientID + "] bid " + guess);
                            if (!seccondBarrier()) break;

                        } else if (request.getStatus().equals(Status.DREW)) {
                            int clientDrew = ((Double) request.getData()).intValue();
                            if (clientDrew == Server.currentShorter) {
                                print("Client: [" + clientID + "] drew the shorter stick - " + clientDrew);
                                Server.currentActiveShorter = true;
                            } else {
                                print("Client: [" + clientID + "] drew stick " + clientDrew);
                                Server.currentActiveShorter = false;
                            }
                            if (!seccondBarrier()) break;

                        }
                    }
                    response.setStatus(Status.FINISHED);


                }

            }
            response();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean firstBarrier() {
        try {
            cyclicBarrier1.await(2000, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            print("Waiting for too long on 1st barrier.");
            response.setStatus(Status.TIMEOUT);
            active = false;
            return false;
        } catch (BrokenBarrierException e) {
            print("Barrier exception: Waiting for too long on 1st barrier.");
            response.setStatus(Status.TIMEOUT);
            active = false;
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean seccondBarrier() {
        try {
            cyclicBarrier2.await(2000, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            print("Waiting for too long on 2nd barrier.");
            response.setStatus(Status.TIMEOUT);
            active = false;
            return false;
        } catch (BrokenBarrierException e) {
            print("Barrier exception: Waiting for too long on 2nd barrier.");
            response.setStatus(Status.TIMEOUT);
            active = false;
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    private synchronized void removeUser() {
        for (int i = 0; i < Server.tableSeats; i++) {
            if (Server.players[i] != null && Server.players[i].equals(clientID)) {
                Server.players[i] = null;
                break;
            }
        }
        Server.seatSemaphore.release();
    }

    private Response response() {
        try {
            out.write(gson.toJson(response));
            out.newLine();
            out.flush();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Request request() {
        try {
            String json = in.readLine();
            //System.out.println(json);
            request = gson.fromJson(json, Request.class);
            return request;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void print(String msg) {
        System.out.println("[" + name + "]: " + msg);
    }

    public String getSName() {
        return this.name;
    }

    public void setSName(String name) {
        this.name = name;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public BufferedWriter getOut() {
        return out;
    }

    public void setOut(BufferedWriter out) {
        this.out = out;
    }

    public BufferedReader getIn() {
        return in;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public CyclicBarrier getCyclicBarrier1() {
        return cyclicBarrier1;
    }

    public void setCyclicBarrier1(CyclicBarrier cyclicBarrier1) {
        this.cyclicBarrier1 = cyclicBarrier1;
    }

    public CyclicBarrier getCyclicBarrier2() {
        return cyclicBarrier2;
    }

    public void setCyclicBarrier2(CyclicBarrier cyclicBarrier2) {
        this.cyclicBarrier2 = cyclicBarrier2;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean getGuess() {
        return guess;
    }

    public void setGuess(boolean guess) {
        this.guess = guess;
    }
}
