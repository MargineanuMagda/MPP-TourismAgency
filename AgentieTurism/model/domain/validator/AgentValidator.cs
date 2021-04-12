

namespace model.domain.validator
{
    class AgentValidator : Validator<TravelAgent>
    {
        public void validate(TravelAgent entity)
        {
            if (entity.Passwd.Length <= 6)
                throw new ValidException("Password shoul be at least 6 characters!");
        }
    }
}
