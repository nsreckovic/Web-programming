package application.ticket;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Ticket implements Serializable {

    private UUID id;
    private boolean one_way;
    private String from;
    private String to;
    private Date departure_date;
    private Date return_date;
    private int version;

    public Ticket(boolean one_way, String from, String to, Date departure_date, Date return_date) {
        this.id = UUID.randomUUID();
        this.one_way = one_way;
        this.from = from;
        this.to = to;
        this.departure_date = departure_date;
        this.return_date = return_date;
        this.version = 0;
    }

    public Ticket(UUID id, boolean one_way, String from, String to, Date departure_date, Date return_date, int version) {
        this.id = id;
        this.one_way = one_way;
        this.from = from;
        this.to = to;
        this.departure_date = departure_date;
        this.return_date = return_date;
        this.version = version;
    }

    public UUID getId() {
        return id;
    }

    public boolean isOne_way() { return one_way; }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Date getDeparture_date() {
        return departure_date;
    }

    public Date getReturn_date() {
        return return_date;
    }

    public int getVersion() {
        return version;
    }

}
