package model;

import java.util.ArrayList;

public class AssistantRatingsDB {
    private static AssistantRatingsDB instance = null;
    private ArrayList<AssistantRating> assistantRatings;

    private AssistantRatingsDB() {
        assistantRatings = new ArrayList<>();
    }

    public synchronized void addAssistantRating(AssistantRating ar) {
        if (assistantRatings.contains(ar)) {
            String name = ar.getName();
            for (AssistantRating assistantRating : assistantRatings){
                if (assistantRating.getName().equals(name)) {
                    for (Integer rating : ar.getRatings()){
                        assistantRating.addRating(rating);
                    }
                    break;
                }
            }
        } else {
            assistantRatings.add(ar);
        }
    }

    public synchronized ArrayList<AssistantRating> getAssistantRatings() {
        return assistantRatings;
    }

    public static AssistantRatingsDB getInstance() {
        if (instance == null) instance = new AssistantRatingsDB();
        return instance;
    }
}
