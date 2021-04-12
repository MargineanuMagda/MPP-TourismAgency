

namespace model.domain.validator
{
    class TripValidator : Validator<Trip>
    {
        public void validate(Trip entity)
        {
            if (entity.Place=="")
                throw new ValidException("Destination name should not be ne null!!\n");

            if (entity.Transport=="")
                throw new ValidException("Transport company should not be ne null!!\n");

            if (entity.Price < 0)
                throw new ValidException("Price should not be negative!\n");
            if (entity.Tickets <= 0)
                throw new ValidException("Number of tickets should be a positive integer!\n");
        }
    }
}
