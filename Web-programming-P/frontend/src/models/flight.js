export default class Flight {
    constructor(id, airline, departure_airport_ID, arrival_airport_ID) {
        this.id = id;
        this.airline = airline;
        this.departure_airport_ID = departure_airport_ID;
        this.arrival_airport_ID = arrival_airport_ID;
    }
}