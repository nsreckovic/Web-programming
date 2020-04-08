package db;

public class AssistantRateDto {

    private String name;
    private int rating;

    public AssistantRateDto(String name, int rating) {
        this.name = name;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public int getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return "{\n" +
                "\tname: \"" + name + "\",\n" +
                "\trating: " + rating + "\n" +
                "}";
    }
}

