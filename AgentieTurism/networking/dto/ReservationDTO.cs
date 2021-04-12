using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace networking.dto
{
    [Serializable]
    public class ReservationDTO
    {
        public String Client { get; set; }
        public String Telefon { get; set; }
        public long TripID { get; set; }
        public String Place { get; set; }
        public int AvTickets { get; set; }
        public int NrTick { get; set; }

        public ReservationDTO(string client, string telefon, long tripID, string place, int avTickets, int nrTick)
        {
            Client = client;
            Telefon = telefon;
            TripID = tripID;
            Place = place;
            AvTickets = avTickets;
            NrTick = nrTick;
        }
    }
}
