package domain.validators;

import domain.Trip;

public class TripValidator implements Validator<Trip> {
    @Override
    public void validate(Trip entity) throws ValidationException {

        if (entity.getPlace().equals(""))
            throw new ValidationException("Destination name should not be ne null!!\n");

        if (entity.getTransport().equals(""))
            throw new ValidationException("Transport company should not be ne null!!\n");

        if(entity.getPrice()<0)
            throw new ValidationException("Price should not be negative!\n");
        if(entity.getNrTickets()<=0)
            throw new ValidationException("Number of tickets should be a positive integer!\n");
    }

}
