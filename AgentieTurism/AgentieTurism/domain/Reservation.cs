using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AgentieTurism.domain
{
    class Reservation:Entity<long>
    {
        public string Client { get; set; }
        public string Telefon { get; set; }
        public Trip Trip { get; set; }
        public int NrTickets { get; set; }

        public Reservation(string client, string telefon, Trip tripId, int nrTickets)
        {
            Client = client;
            Telefon = telefon;
            Trip = tripId;
            NrTickets = nrTickets;
        }

        public override string ToString()
        {
            return "Id: "+ID+"Client "+Client+" Telefon:"+Telefon+"Trip : "+Trip.Place+" Number of tickets: "+NrTickets;
        }

        public override bool Equals(object obj)
        {
            return base.Equals(obj);
        }

        public override int GetHashCode()
        {
            return base.GetHashCode();
        }
    }
}
