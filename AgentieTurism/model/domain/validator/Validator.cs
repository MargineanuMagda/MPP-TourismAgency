using System;


namespace model.domain.validator
{
    class ValidException : Exception
    {
        public ValidException(string message) : base(message)
        {
        }
    }
    interface Validator<E>
    {
        void validate(E entity);
    }
}
