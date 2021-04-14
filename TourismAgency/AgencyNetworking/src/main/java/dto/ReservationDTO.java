package dto;


import java.io.Serializable;

public class ReservationDTO implements Serializable {
    private String client;
    private String telefon;
    private Long tripID;
    private String place;
    private Integer avTickets;
    private Integer nrTick;

    public ReservationDTO(String client, String telefon, Long tripID,String place,Integer avTickets, Integer nrTick) {
        this.client = client;
        this.telefon = telefon;
        this.tripID = tripID;
        this.nrTick = nrTick;
        this.place=place;
        this.avTickets=avTickets;
    }

    public String getClient() {
        return client;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getAvTickets() {
        return avTickets;
    }

    public void setAvTickets(Integer avTickets) {
        this.avTickets = avTickets;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public Long getTripID() {
        return tripID;
    }

    public void setTripID(Long tripID) {
        this.tripID = tripID;
    }

    public Integer getNrTick() {
        return nrTick;
    }

    public void setNrTick(Integer nrTick) {
        this.nrTick = nrTick;
    }

    @Override
    public String toString() {
        return "ReservationDTO{" +
                "client='" + client + '\'' +
                ", telefon='" + telefon + '\'' +
                ", tripID=" + tripID +
                ", place='" + place + '\'' +
                ", avTickets=" + avTickets +
                ", nrTick=" + nrTick +
                '}';
    }
}
