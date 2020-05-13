package application.company;

import java.io.Serializable;

public class CompanyDto implements Serializable {

    private String name;
    private String link;

    public CompanyDto(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return "CompanyDto{" +
                "name='" + name + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
