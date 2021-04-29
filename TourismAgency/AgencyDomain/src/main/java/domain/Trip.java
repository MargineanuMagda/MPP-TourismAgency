package domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.LocalTime;

@javax.persistence.Entity
@Table(name = "trips")
public class Trip extends Entity{
    @Basic
    @Column(name = "place")
    private String place;
    @Basic
    @Column(name = "transport")
    private String transport;
    @Basic
    @Column(name = "dateTrip")
    private LocalDateTime date;

    public Trip() {
    }

    @Basic
    @Column(name = "price")
    private Double price;
    @Basic
    @Column(name = "nrTickets")
    private Integer nrTickets;
    @Basic
    @Column(name = "freeTickets")
    private Integer freeTickets;

    public Trip(String place, String transport, LocalDateTime date, Double price, Integer nrTickets, Integer freeTickets) {
        this.place = place;
        this.transport = transport;
        this.date = date;
        this.price = price;
        this.nrTickets = nrTickets;
        this.freeTickets = freeTickets;
    }

    public Trip(Long tripID,String place,Integer freeTickets) {
        super();
        this.setId(tripID);
        this.place = place;
        this.transport = "";
        this.date = LocalDateTime.MIN;
        this.price = 0d;
        this.nrTickets = 0;
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
    public LocalTime getDateTime(){return date.toLocalTime();}

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
        return "Trip{" +
                "id"+getId()+
                ",  place='" + place + '\'' +
                ", transport='" + transport + '\'' +
                ", date=" + date +
                ", price=" + price +
                ", nrTickets=" + nrTickets +
                ", freeTickets=" + freeTickets +
                '}';
    }
}
