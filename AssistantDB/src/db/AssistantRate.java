package db;

import com.google.gson.*;

import java.util.ArrayList;

public class AssistantRate {

    private String name;
    private ArrayList<Integer> ratings = new ArrayList<>();

    public AssistantRate(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Integer> getRatings() {
        return ratings;
    }

    public synchronized void addRating(int rating) {
        ratings.add(rating);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AssistantRate) {
            if (((AssistantRate) obj).getName().equals(this.name)) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        JsonObject jsonObj = new JsonObject();
        JsonArray ratings = new Gson().toJsonTree(this.ratings).getAsJsonArray();
        JsonObject nameObject = new Gson().fromJson("{name:" + name + "}", JsonObject.class);

        jsonObj.add("name", nameObject);
        jsonObj.add("ratings", ratings);
        return jsonObj.toString();
    }

}

