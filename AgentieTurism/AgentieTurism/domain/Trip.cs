using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AgentieTurism.domain
{
    class Trip:Entity<long>
    {
        public string Place { get; set; }
        public string Transport { get; set; }
        public DateTime Data { get; set; }
        public double Price { get; set; }
        public int Tickets { get; set; }
        public int FreeTickets { get; set; }

       
        public Trip(string place, string transport, DateTime data, double price, int tickets, int freeTickets)
        {
            Place = place;
            Transport = transport;
            Data = data;
            Price = price;
            Tickets = tickets;
            FreeTickets = freeTickets;
        }

        public Trip(string place, string transport, DateTime data, double price, int tickets)
        {
            Place = place;
            Transport = transport;
            Data = data;
            Price = price;
            Tickets = tickets;
            FreeTickets = tickets;
        }

        public override string ToString()
        {
            return "ID trip: "+ base.ID+ "Place:"+Place+" Transport:"+Transport+" Data:" +Data+" Price:"+Price+" Tickets:"+Tickets+" Free Tickets: "+FreeTickets;
        }
    }
}
