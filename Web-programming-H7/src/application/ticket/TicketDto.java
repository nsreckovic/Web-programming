package application.ticket;

import java.io.Serializable;

public class TicketDto implements Serializable {

    private boolean one_way;
    private String from;
    private String to;
    private String departure_date;
    private String return_date;
    private String company;
    private String company_link;

    public boolean isOne_way() {
        return one_way;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getDeparture_date() {
        return departure_date;
    }

    public String getReturn_date() {
        return return_date;
    }

    public String getCompany() {
        return company;
    }

    public String getCompany_link() { return company_link; }

    public void setOne_way(boolean one_way) {
        this.one_way = one_way;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setDeparture_date(String departure_date) {
        this.departure_date = departure_date;
    }

    public void setReturn_date(String return_date) {
        this.return_date = return_date;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setCompany_link(String company_link) { this.company_link = company_link; }


    @Override
    public String toString() {
        return "TicketDto{" +
                "one_way=" + one_way +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", departure_date='" + departure_date + '\'' +
                ", return_date='" + return_date + '\'' +
                ", company='" + company + '\'' +
                ", company_link='" + company_link + '\'' +
                '}';
    }
}
