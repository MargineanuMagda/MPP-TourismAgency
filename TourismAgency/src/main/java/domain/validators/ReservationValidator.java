package domain.validators;

import domain.Reservation;

public class ReservationValidator implements Validator<Reservation> {
    @Override
    public void validate(Reservation entity) throws ValidationException {
        if (entity.getClient().equals(""))
            throw new ValidationException("Client name should not be ne null!!\n");

        if (!entity.getClient().matches("^[a-zA-Z -]+$"))//daca contine atceva decat litere, spatii
            throw new ValidationException("Client name should contain only letters ,- and spaces\n");

        if(!entity.getTelefon().matches("^[0-9]+$"))
            throw new ValidationException("Enter a valid telephone number!\n");
        if(entity.getNrTick()<=0)
            throw new ValidationException("Number of tickets should be a positive integer!\n");
    }
}
