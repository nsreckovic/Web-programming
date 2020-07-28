export default class Reservation {
    constructor(departure_ticket_ID, return_ticket_ID, user_ID, date) {
        this.departure_ticket_ID = departure_ticket_ID;
        this.return_ticket_ID = return_ticket_ID;
        this.user_ID = user_ID;
        this.date = date;
    }
}