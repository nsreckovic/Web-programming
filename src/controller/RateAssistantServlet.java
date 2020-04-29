package controller;

import model.AssistantRating;
import model.AssistantRatingsDB;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


@WebServlet(name = "RateAssistantServlet", urlPatterns = "/rateAssistant")
public class RateAssistantServlet extends HttpServlet {

    private AssistantRatingsDB db;

    public RateAssistantServlet() {
        db = AssistantRatingsDB.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher view = req.getRequestDispatcher("index.jsp");
        view.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        int rating = Integer.parseInt(req.getParameter("rating"));
        AssistantRating ar = new AssistantRating(name.toUpperCase());
        ar.addRating(rating);

        db.addAssistantRating(ar);

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("RatingFinished.jsp");
        requestDispatcher.forward(req, resp);
    }

}
