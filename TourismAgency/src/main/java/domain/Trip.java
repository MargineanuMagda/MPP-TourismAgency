package domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class Trip extends Entity<Long>{
    private String place;
    private String transport;
    private LocalDate date;

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    private LocalTime departureTime;
    private Double price;
    private Integer nrTickets;
    private Integer freeTickets;

    public Trip(String place, String transport, LocalDate date, LocalTime departureTime, Double price, Integer nrTickets, Integer freeTickets) {
        this.place = place;
        this.transport = transport;
        this.date = date;
        this.departureTime = departureTime;
        this.price = price;
        this.nrTickets = nrTickets;
        this.freeTickets = freeTickets;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getNrTickets() {
        return nrTickets;
    }

    public void setNrTickets(Integer nrTickets) {
        this.nrTickets = nrTickets;
    }

    public Integer getFreeTickets() {
        return freeTickets;
    }

    public void setFreeTickets(Integer freeTickets) {
        this.freeTickets = freeTickets;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id"+getId()+
                ",  place='" + place + '\'' +
                ", transport='" + transport + '\'' +
                ", date=" + date +
                ", departureTime=" + departureTime +
                ", price=" + price +
                ", nrTickets=" + nrTickets +
                ", freeTickets=" + freeTickets +
                '}';
    }
}
