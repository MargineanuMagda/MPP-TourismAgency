package repository;

import domain.Trip;

import java.time.LocalDate;
import java.time.LocalTime;

public interface TripRepository extends Repository<Long, Trip>{
    Iterable<Trip> findTripsByDate(LocalDate minDate, LocalDate maxDate);
    Iterable<Trip> findTripsByName(String name);
    Iterable<Trip> findTripsByNameAndHours(String name , LocalTime minTime,LocalTime maxTime);
}
