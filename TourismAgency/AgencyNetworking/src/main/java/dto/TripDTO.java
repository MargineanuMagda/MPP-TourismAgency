package dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TripDTO implements Serializable {
    private Long tripId;
    private String place;
    private String transport;
    private LocalDateTime date;
    private Double price;
    private Integer nrTickets;
    private Integer freeTickets;

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public TripDTO(Long tripId, String place, String transport, LocalDateTime date, Double price, Integer nrTickets, Integer freeTickets) {
        this.tripId=tripId;
        this.place = place;
        this.transport = transport;
        this.date = date;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
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
        return "TripDTO{" +
                "place='" + place + '\'' +
                ", transport='" + transport + '\'' +
                ", date=" + date +
                ", price=" + price +
                ", nrTickets=" + nrTickets +
                ", freeTickets=" + freeTickets +
                '}';
    }
}
