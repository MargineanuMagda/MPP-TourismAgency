package repository.database;

import domain.Trip;
import repository.TripRepository;

import java.time.LocalDate;
import java.time.LocalTime;

public class TripDbRepository implements TripRepository {

    @Override
    public Trip findOne(Long aLong) {
        return null;
    }

    @Override
    public Iterable<Trip> findAll() {
        return null;
    }

    @Override
    public Trip save(Trip entity) {
        return null;
    }

    @Override
    public Trip delete(Long aLong) {
        return null;
    }

    @Override
    public Trip update(Trip entity) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Iterable<Trip> findTripsByDate(LocalDate minDate, LocalDate maxDate) {
        return null;
    }

    @Override
    public Iterable<Trip> findTripsByName(String name) {
        return null;
    }

    @Override
    public Iterable<Trip> findTripsByNameAndHours(String name, LocalTime minTime, LocalTime maxTime) {
        return null;
    }
}
