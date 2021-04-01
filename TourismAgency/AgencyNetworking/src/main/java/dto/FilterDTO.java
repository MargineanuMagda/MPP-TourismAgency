package dto;

import java.io.Serializable;
import java.time.LocalTime;

public class FilterDTO implements Serializable {
    private String place;
    private LocalTime minHour;
    private LocalTime maxHour;

    public FilterDTO(String place, LocalTime minHour, LocalTime maxHour) {
        this.place = place;
        this.minHour = minHour;
        this.maxHour = maxHour;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public LocalTime getMinHour() {
        return minHour;
    }

    public void setMinHour(LocalTime minHour) {
        this.minHour = minHour;
    }

    public LocalTime getMaxHour() {
        return maxHour;
    }

    public void setMaxHour(LocalTime maxHour) {
        this.maxHour = maxHour;
    }
}
