using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using AgentieTurism.domain;
using AgentieTurism.repository;

using System.IO;
using System.Configuration;
using log4net;
using log4net.Config;

namespace AgentieTurism
{
    class Program
    {
        static void Main(string[] args)
        {

            Trip t = new Trip("Cluj", "BTrans", DateTime.Now, 100, 25);
            t.ID = 1;
            Console.WriteLine(t);
            TravelAgent agent = new TravelAgent("aaa", "123");
            agent.ID = 1;
            Console.WriteLine(agent);
            Reservation reservation = new Reservation("Magda", "0711 111 1111", t, 3);
            reservation.ID = 1;
            Console.WriteLine(reservation);

            XmlConfigurator.Configure(new FileInfo(ConfigurationSettings.AppSettings["log4net-config-file"]));

            TripRepository repoTrips = new TripDBRepository();
            /*repoTrips.save(t);*/
            /*Console.WriteLine(t.ID);
            t.ID = 5;
            t.Place = "Bucuresti";
            repoTrips.update(t);*/
            //repoTrips.findTripsByNameAndHours("uj",4,23).ToList().ForEach(x => Console.WriteLine(x));
            repoTrips.findAll().ToList().ForEach(x => Console.WriteLine(x));
            AgentRepository repoAgents = new AgentDBRepository();
            TravelAgent agent1 = new TravelAgent("mihai@turism", "1234567");
            agent1.ID = 11;
            repoAgents.update(agent1);
            //Console.WriteLine(repoAgents.findAgentsByUser("magda@turism","1234567"));
            repoAgents.findAll().ToList().ForEach(x => Console.WriteLine(x));

            ReservationRepository repoReservation = new ReservationDBRepository();
            Trip t1 = repoTrips.findOne(7);
            Reservation r = new Reservation("Pop Ana", "0722 222 999", t1, 3);
            r.ID = 8;
            //repoReservation.update(r);
            //repoReservation.findReservationByTripID(1).ToList().ForEach(x => Console.WriteLine(x));
            //repoReservation.findReservationByClient("Ana").ToList().ForEach(x => Console.WriteLine(x));
            Console.WriteLine(repoReservation.findOne(2));

        }
    }
}
