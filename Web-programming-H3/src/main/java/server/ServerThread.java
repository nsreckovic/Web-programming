package server;

import com.google.gson.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ServerThread extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Gson gson = new Gson();

    private ArrayList<Quote> serverQuotes = new ArrayList<>();

    private Socket qodSocket;
    private BufferedReader qodIn;
    private PrintWriter qodOut;

    public ServerThread(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

        // In case server made too many request to API, return one of the server's quotes (random).
        Quote defaultQuote = new Quote("My default quote.", "Me", "2020-03-18");
        serverQuotes.add(defaultQuote);

        // Default port is 80
        qodSocket = new Socket("quotes.rest", 80);
        qodIn = new BufferedReader(new InputStreamReader(qodSocket.getInputStream()));
        qodOut = new PrintWriter(new OutputStreamWriter(qodSocket.getOutputStream()), true);

    }

    @Override
    public void run() {
        try {
            String request = in.readLine();

            if (request.startsWith("GET / HTTP/1.1")) {

                System.out.println("\nHTTP client GET / request:");
                do {
                    System.out.println(request);
                    request = in.readLine();
                } while (!request.trim().equals(""));

                ServerMain.qod = getQOD();

                String response = "";
                if (ServerMain.qod != null) {
                    response = "HTTP/1.1 301 Moved Permanently" + "\n";
                    response += "Location: qod" + "\n";
                } else {
                    response = "HTTP/1.1 500 Internal Server Error" + "\n\n";
                }

                System.out.println("\nServer response:\n" + response);
                out.println(response);
                in.close();
                out.close();

            } else if (request.startsWith("GET /qod HTTP/1.1")) {
                System.out.println("\nHTTP client GET /qod request:");
                do {
                    System.out.println(request);
                    request = in.readLine();
                } while (!request.trim().equals(""));

                String response = "";
                if (ServerMain.qod != null) {
                    response = "HTTP/1.1 200 OK" + "\n";
                    response += "Content-Type: text/html\n\n";
                    response += HtmlPage.returnQuotePage(ServerMain.qod);
                } else {
                    response = "HTTP/1.1 500 Internal Server Error" + "\n\n";
                }

                out.println(response);
                in.close();
                out.close();
            }
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized Quote getQOD() throws IOException {
        qodOut.println("GET /qod HTTP/1.1\n" +
                        "Host: quotes.rest\n" +
                        "Content-Type: application/json\n");

        StringBuilder sb = new StringBuilder();

        System.out.println("\nAPI Response:");
        String line = "";

        // Skip appending to sb until you reach json (content)
        while (!(line = qodIn.readLine()).equals("")) {
            System.out.println(line);
        }
        System.out.println(line);
        while (true) {
            line = qodIn.readLine();
            if (line == null) break;
            else if (line.equals("    }")) {
                System.out.println("    }");
                sb.append("    }\n");
                System.out.println("}");
                sb.append("}\n");
                break;
            } else {
                System.out.println(line);
                sb.append(line);
            }
        }

        System.out.println("\n");
        qodIn.close();
        qodOut.close();

        JsonObject jsonObject = gson.fromJson(sb.toString(), JsonObject.class);
        JsonObject contents;
        JsonArray quotes;

        // This api allows only 10 requests per hour. If that number is exceeded, api will return error object.
        // In that case the exception will be thrown, so server will return some random quote (one from serverQuotes list).
        try {
            contents = jsonObject.getAsJsonObject("contents");
            quotes = contents.getAsJsonArray("quotes");
        } catch (NullPointerException e) {
            if (!serverQuotes.isEmpty()) return serverQuotes.get((new Random()).nextInt(serverQuotes.size()));
            else return null;
        }

        JsonObject theQuote = quotes.get(0).getAsJsonObject();
        String quote = theQuote.get("quote").getAsString();
        String author = theQuote.get("author").getAsString();
        String date = theQuote.get("date").getAsString();

        Quote q = new Quote(quote, author, date);
        if (!serverQuotes.contains(q)) serverQuotes.add(q);

        return q;
    }

}
