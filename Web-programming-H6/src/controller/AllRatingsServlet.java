package controller;

import model.AssistantRating;
import model.AssistantRatingsDB;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "AllRatingsServlet", urlPatterns = "/all")
public class AllRatingsServlet extends HttpServlet {

    private AssistantRatingsDB db;

    public AllRatingsServlet() {
        db = AssistantRatingsDB.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ArrayList<AssistantRating> assistantRatings = db.getAssistantRatings();

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("AllRatings.jsp");
        req.setAttribute("ratings", assistantRatings);
        requestDispatcher.forward(req, resp);
    }
}
