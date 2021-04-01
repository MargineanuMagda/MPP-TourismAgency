package domain;

public class Reservation extends Entity<Long>{
    private String client;
    private String telefon;
    private Trip trip;
    private Integer nrTick;

    public Reservation(String client, String telefon, Trip trip, Integer nrTick) {
        this.client = client;
        this.telefon = telefon;
        this.trip = trip;
        this.nrTick = nrTick;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "client='" + client + '\'' +
                ", telefon='" + telefon + '\'' +
                ", tripId=" + trip +
                ", nrTick=" + nrTick +
                '}';
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

    public Trip getTripId() {
        return trip;
    }

    public void setTripId(Trip tripId) {
        this.trip = tripId;
    }

    public Integer getNrTick() {
        return nrTick;
    }

    public void setNrTick(Integer nrTick) {
        this.nrTick = nrTick;
    }
}
