package application.ticket;

import application.company.Company;
import application.company.CompanyRepository;
import application.ticket_company.TicketCompany;
import application.ticket_company.TicketCompanyRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TicketService {

    public TicketDto addTicket(TicketDto ticketDto) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");

        Ticket ticket = null;
        try {
            if (ticketDto.getReturn_date() == null || ticketDto.getReturn_date().isEmpty()) {
                ticket = new Ticket(ticketDto.isOne_way(),
                        ticketDto.getFrom(),
                        ticketDto.getTo(),
                        dateFormat.parse(ticketDto.getDeparture_date()),
                        null);
            } else {
                ticket = new Ticket(ticketDto.isOne_way(),
                        ticketDto.getFrom(),
                        ticketDto.getTo(),
                        dateFormat.parse(ticketDto.getDeparture_date()),
                        dateFormat.parse(ticketDto.getReturn_date()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Ticket t = TicketRepository.addTicket(ticket);
        Company c = CompanyRepository.getCompanyByName(ticketDto.getCompany());
        if (c == null || t == null) return null;
        TicketCompany tc = TicketCompanyRepository.addTC(new TicketCompany(c.getId(), t.getId()));
        if (tc == null) return null;

        TicketDto tDto = makeTicketDto(t, c);
        System.out.println(tDto);
        return tDto;
    }

    public List<TicketDto> getAllTickets() {
        List<TicketCompany> ticket_companies = TicketCompanyRepository.getAllTCs();
        List<TicketDto> ticketDtos = new ArrayList<>();

        for (TicketCompany tc : ticket_companies) {
            Ticket t = TicketRepository.getTicketByID(tc.getTicketID());
            Company c = CompanyRepository.getCompanyByID(tc.getCompanyID());
            TicketDto ticketDto = makeTicketDto(t, c);
            if (ticketDto != null) ticketDtos.add(ticketDto);
        }

        return ticketDtos;
    }

    public List<TicketDto> getOneWayTickets() {
        List<TicketCompany> ticket_companies = TicketCompanyRepository.getAllTCs();
        List<TicketDto> ticketDtos = new ArrayList<>();

        for (TicketCompany tc : ticket_companies) {
            Ticket t = TicketRepository.getTicketByID(tc.getTicketID());
            if (t.isOne_way()) {
                Company c = CompanyRepository.getCompanyByID(tc.getCompanyID());
                TicketDto ticketDto = makeTicketDto(t, c);
                if (ticketDto != null) ticketDtos.add(ticketDto);
            }
        }

        return ticketDtos;
    }

    public List<TicketDto> getRoundTripTickets() {
        List<TicketCompany> ticket_companies = TicketCompanyRepository.getAllTCs();
        List<TicketDto> ticketDtos = new ArrayList<>();

        for (TicketCompany tc : ticket_companies) {
            Ticket t = TicketRepository.getTicketByID(tc.getTicketID());
            if (!t.isOne_way()) {
                Company c = CompanyRepository.getCompanyByID(tc.getCompanyID());
                TicketDto ticketDto = makeTicketDto(t, c);
                if (ticketDto != null) ticketDtos.add(ticketDto);
            }
        }

        return ticketDtos;
    }

    private TicketDto makeTicketDto(Ticket t, Company c) {
        if (t == null || c == null) return null;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");

        TicketDto tDto = new TicketDto();
            tDto.setOne_way(t.isOne_way());
            tDto.setFrom(t.getFrom());
            tDto.setTo(t.getTo());
            tDto.setDeparture_date(dateFormat.format(t.getDeparture_date()));
            if (t.getReturn_date() == null) tDto.setReturn_date("");
            else tDto.setReturn_date(dateFormat.format(t.getReturn_date()));
            tDto.setCompany(c.getName());
            tDto.setCompany_link(c.getLink());

        return tDto;
    }
}
