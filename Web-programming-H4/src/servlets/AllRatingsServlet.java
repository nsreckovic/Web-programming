package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.Socket;

@WebServlet(name = "AllRatingsServlet", urlPatterns = "/allRatings")
public class AllRatingsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Socket socket = new Socket("localhost", 9000);
        BufferedReader in1 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out1 = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

        StringBuilder sb = new StringBuilder();
        sb.append("GET /all HTTP/1.1\n");
        sb.append("Host: localhost:9000\n");

        System.out.println("Sending:");
        System.out.println(sb.toString());
        out1.println(sb.toString());


        sb = new StringBuilder();

        System.out.println("\n\nResponse:");
        String line = in1.readLine();
        System.out.println(line);
        while (!(line = in1.readLine()).equals("")) {
            System.out.println(line);
        }
        System.out.println(line);
        while ((line = in1.readLine()) != null && !line.equals("}")) {
            sb.append(line);
            System.out.println(line);
        }

        System.out.println("SB:");
        System.out.println(sb.toString());
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(sb.toString(), JsonObject.class);
        JsonArray allRatings = jsonObject.getAsJsonArray("allRatings");
        System.out.println(allRatings.toString());
        resp.setStatus(200);
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();


        out.println("<html lang=\"en\">");
            out.println("<head>");
                out.println("<meta charset=\"UTF-8\">");
                out.println("<title>All ratings</title>");
                out.println("<link rel=\"stylesheet\" href=\"../css/style.css\">");
            out.println("</head>");

            out.println("<body>");
                out.println("<div id=\"rate-again\">");
                    out.println("<div class=\"container\">");

                    out.println("<h1 class=\"heading\">");
                        out.println("Average assistant");
                        out.println("<span class=\"text-primary\">ratings</span>:");
                    out.println("</h1>");

                    out.println("<br>");

                    out.println("<table class=\"table-fill\">");
                        out.println("<thead>");
                            out.println("<tr>");
                                out.println("<th>Assistant</th>");
                                out.println("<th>Average rating</th>");
                            out.println("</tr>");
                        out.println("</thead>");

                        out.println("<tbody class=\"table-hover\">");
                            int i = 0;
                            while (true) {
                                try {
                                    JsonObject assistantRating = allRatings.get(i).getAsJsonObject();
                                    String name = assistantRating.get("name").getAsString();

                                    if (name.equals("ALEKSANDAR")) {
                                        out.println("<tr>");
                                        out.println("<td>" + name + "</td>");
                                        out.println("<td>0.0</td>");
                                        out.println("</tr>");
                                        i++;
                                        continue;
                                    }

                                    JsonArray ratings = assistantRating.getAsJsonArray("ratings");
                                    int j = 0, sum = 0;

                                    while (true) {
                                        try {
                                            sum += ratings.get(j).getAsInt();
                                            j++;
                                        } catch (IndexOutOfBoundsException e) {
                                            break;
                                        }
                                    }

                                    double average = 0;
                                    if (j != 0) average = sum * 1.0 / j;

                                    average *= 100;
                                    long tmp = Math.round(average);
                                    average = (double) tmp / 100;

                                    out.println("<tr>");
                                    out.println("<td>" + name + "</td>");
                                    out.println("<td>" + average + "</td>");
                                    out.println("</tr>");

                                    i++;

                                } catch (IndexOutOfBoundsException e) {
                                    break;
                                }

                            }
                        out.println("</tbody>");
                    out.println("</table>");

                    out.println("<br>");
                    out.println("Wanna rate again? Go <a href=\"/\">here</a>.");

                    out.println("</div>");
                out.println("</div>");
            out.println("</body>");
        out.println("</html>");

        out.close();
    }
}
