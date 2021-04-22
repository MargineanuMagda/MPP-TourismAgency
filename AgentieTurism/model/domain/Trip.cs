using System;

namespace model.domain
{
    [Serializable]
    public class Trip :Entity<long>
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

        public Trip(long tripId, string place, int avTickets)
        {
            ID = tripId;
            Transport = "";
            Data = DateTime.Now;
            Price = 0;
            Tickets = avTickets;
            Place = place;
            FreeTickets = avTickets;
        }

        public override string ToString()
        {
            return "ID trip: "+ ID+ "Place:"+Place+" Transport:"+Transport+" Data:" +Data+" Price:"+Price+" Tickets:"+Tickets+" Free Tickets: "+FreeTickets;
        }
    }
}
