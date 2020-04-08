package db;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.Socket;

public class AssistantDBThread extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Gson gson = new Gson();

    public AssistantDBThread(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
    }

    @Override
    public void run() {
        try {
            String line = in.readLine();
            System.out.println("New request:");
            System.out.println(line);

            StringBuilder sb = new StringBuilder();

            if (line != null) {

                switch (line) {
                    case "POST /add HTTP/1.1":
                        while (!(line = in.readLine()).equals("")) {
                            // System.out.println(line);
                        }
                        System.out.println(line);
                        while ((line = in.readLine()) != null && !line.equals("}")) {
                            // System.out.println(line);
                            sb.append(line);
                        }
                        System.out.println(line);
                        sb.append(line);

                        AssistantRateDto arDto = gson.fromJson(sb.toString(), AssistantRateDto.class);
                        AssistantRate arating = new AssistantRate(arDto.getName());

                        if (!AssistantDBMain.getAssistantRates().contains(arating)) {
                            arating.addRating(arDto.getRating());
                            AssistantDBMain.addAssistantRate(arating);

                        } else {
                            for (AssistantRate ar : AssistantDBMain.getAssistantRates()) {
                                if (ar.getName().equals(arDto.getName())){
                                    ar.addRating(arDto.getRating());
                                    break;
                                }
                            }
                        }

                        sb = new StringBuilder();
                        sb.append("HTTP/1.1 200 OK\n");
                        sb.append("Host: localhost:8080\n");
                        out.println(sb.toString());

                        break;

                    case "GET /all HTTP/1.1":
                        sb = new StringBuilder();
                        sb.append("HTTP/1.1 200 OK\n");
                        sb.append("Host: localhost:8080\n");
                        sb.append("Content-Type: application/json\n");
                        sb.append("\n");

                        JsonObject jsonObj = new JsonObject();
                        JsonArray allRatings = new Gson().toJsonTree(AssistantDBMain.getAssistantRates()).getAsJsonArray();
                        jsonObj.add("allRatings", allRatings);

                        sb.append(jsonObj.toString());

                        out.println(sb.toString());
                        break;

                }

            }
            in.close();
            out.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
