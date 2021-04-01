package domain.validators;

import domain.TravelAgent;

public class AgentValidator implements Validator<TravelAgent> {
    @Override
    public void validate(TravelAgent entity) throws ValidationException {
        if(entity.getPasswd().length()<=6)
            throw new ValidationException("Password shoul be at least 6 characters!");
    }
}
