using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AgentieTurism.domain.validator
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
