using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace networking.dto
{
    [Serializable]
    public class TripDTO
    {
        public long Id { get; set; }
        public string Place { get; set; }
        public string Transport { get; set; }
        public DateTime Data { get; set; }
        public double Price { get; set; }
        public int Tickets { get; set; }
        public int FreeTickets { get; set; }


        public TripDTO(long tripId, string place, string transport, DateTime data, double price, int tickets, int freeTickets)
        {
            Id = tripId;
            Place = place;
            Transport = transport;
            Data = data;
            Price = price;
            Tickets = tickets;
            FreeTickets = freeTickets;
        }
    }
}
