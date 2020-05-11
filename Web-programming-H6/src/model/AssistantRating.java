package model;

import java.util.ArrayList;

public class AssistantRating {

    private String name;
    private ArrayList<Integer> ratings = new ArrayList<>();
    private double sum = 0;
    private double average;

    public AssistantRating(String name) {
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
        sum += rating;
        average = sum / ratings.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AssistantRating) {
            if (((AssistantRating) obj).getName().equals(this.name)) return true;
        }
        return false;
    }

    public double getAverage() {
        return average;
    }
}

