package servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.net.Socket;


@WebServlet(name = "RateAssistantServlet", urlPatterns = "/rateAssistant")
public class RateAssistantServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher view = req.getRequestDispatcher("/html/RatingFinnish.html");
        view.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        int rating = Integer.parseInt(req.getParameter("rating"));
        AssistantRateDto ardto = new AssistantRateDto(name.toUpperCase(), rating);

        Socket socket = new Socket("localhost", 9000);
        BufferedReader in1 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out1 = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

        StringBuilder sb = new StringBuilder();
        sb.append("POST /add HTTP/1.1\n");
        sb.append("Host: localhost:9000\n");
        sb.append("Content-Type: application/json\n");
        sb.append("\n");
        sb.append(ardto.toString());

        System.out.println("Sending:");
        System.out.println(sb.toString());
        out1.println(sb.toString());

        String response = in1.readLine();
        if (response.contains("200 OK")) resp.setStatus(200);
        System.out.println("Response: " + response);

        in1.close();
        out1.close();

        RequestDispatcher view = req.getRequestDispatcher("/html/RatingFinnish.html");
        view.forward(req, resp);

    }

}
