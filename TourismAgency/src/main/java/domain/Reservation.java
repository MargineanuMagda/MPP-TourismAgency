package domain;

public class Reservation extends Entity<Long>{
    private String client;
    private String telefon;
    private Long tripId;
    private Integer nrTick;

    public Reservation(String client, String telefon, Long tripId, Integer nrTick) {
        this.client = client;
        this.telefon = telefon;
        this.tripId = tripId;
        this.nrTick = nrTick;
    }

    public String getClient() {
        return client;
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

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public Integer getNrTick() {
        return nrTick;
    }

    public void setNrTick(Integer nrTick) {
        this.nrTick = nrTick;
    }
}
