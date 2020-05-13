package application.ticket_company;

import java.util.UUID;

public class TicketCompany {

    private UUID companyID;
    private UUID ticketID;

    public TicketCompany(UUID companyID, UUID ticketID) {
        this.companyID = companyID;
        this.ticketID = ticketID;
    }

    public UUID getCompanyID() {
        return companyID;
    }

    public UUID getTicketID() {
        return ticketID;
    }
}
