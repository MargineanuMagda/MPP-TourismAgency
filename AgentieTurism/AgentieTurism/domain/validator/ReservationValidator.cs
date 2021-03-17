using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AgentieTurism.domain.validator
{
    class ReservationValidator : Validator<Reservation>
    {
        public void validate(Reservation entity)
        {
            if (entity.Client=="")
                throw new ValidException("Client name should not be ne null!!\n");

            if (!entity.Client.Contains("^[a-zA-Z -]+$"))//daca contine atceva decat litere, spatii
                throw new ValidException("Client name should contain only letters ,- and spaces\n");

            if (!entity.Telefon.Contains("^[0-9]+$"))
                throw new ValidException("Enter a valid telephone number!\n");
            if (entity.NrTickets <= 0)
                throw new ValidException("Number of tickets should be a positive integer!\n");
        }
    }
}
