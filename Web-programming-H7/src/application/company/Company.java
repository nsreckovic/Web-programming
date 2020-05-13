package application.company;

import java.util.UUID;

public class Company {

    private UUID id;
    private String name;
    private String link;
    private int version;

    public Company(String id, String name, String link, int version) {
        this.id = UUID.fromString(id);
        this.name = name;
        this.link = link;
        this.version = version;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLink() { return link; }

    public int getVersion() { return version; }

}
