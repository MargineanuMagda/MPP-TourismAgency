using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AgentieTurism.domain.validator
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
