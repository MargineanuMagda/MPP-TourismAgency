using System;
using Newtonsoft.Json;

namespace model.domain
{
    [Serializable]
    public class Trip :Entity<long>
    {
        

        [JsonProperty("place")]
        public string Place { get; set; }
        [JsonProperty("transport")]
        public string Transport { get; set; }
        [JsonProperty("date")]
        public string Date { get; set; }
        [JsonProperty ("price")]
        public double Price { get; set; }
        [JsonProperty ("nrTickets")]
        public int NrTickets { get; set; }
        [JsonProperty ("freeTickets")]
        public int FreeTickets { get; set; }

        public Trip() { }
       
        public Trip(string place, string transport, string data, double price, int tickets, int freeTickets)
        {
            this.Place = place;
            this.Transport = transport;
            Date = data;
            this.Price = price;
            NrTickets = tickets;
            this.FreeTickets = freeTickets;
        }

        public Trip(string place, string transport, string data, double price, int tickets)
        {
            this.Place = place;
            this.Transport = transport;
            Date = data;
            this.Price = price;
            NrTickets = tickets;
            FreeTickets = tickets;
        }

        public Trip(long tripId, string place, int avTickets)
        {
            ID = tripId;
            Transport = "";
            Date = DateTime.Now.ToString("MM/dd/yyyy HH:mm:ss");
            Price = 0;
            NrTickets = avTickets;
            this.Place = place;
            FreeTickets = avTickets;
        }

        public override string ToString()
        {
            return string.Format("[Trip: id={0}, place={1}, transport={2}, date={3}, price={4}, nrTickets={5}, freeTickets={6}]", ID, Place, Transport,Date,Price,NrTickets,FreeTickets);
        }
    }
}
