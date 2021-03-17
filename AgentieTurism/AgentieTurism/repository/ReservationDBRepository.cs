using AgentieTurism.domain;
using System;
using System.Collections.Generic;
using System.Data;
using log4net;

namespace AgentieTurism.repository
{
    class ReservationDBRepository : ReservationRepository
    {
        private static ILog log = LogManager.GetLogger("ReservationDBRepository");

        public ReservationDBRepository()
        {
            log.Info("Creating Reservation DB Repository");
        }

        public void delete(long id)
        {
            log.InfoFormat("Deleting reservation with id {0}", id);
            IDbConnection con = DBUtils.getConnection();

            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "delete from reservation where idr=@id";
                IDbDataParameter paramId = comm.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                comm.Parameters.Add(paramId);
                var dataR = comm.ExecuteNonQuery();

                if (dataR == 0)
                    throw new RepositoryException("No reservation deleted!");
                else
                    log.Info("Reservation was deleted succesfully");
            }
        }

        public IEnumerable<Reservation> findAll()
        {
            IDbConnection con = DBUtils.getConnection();
            IList<Reservation> resList = new List<Reservation>();
            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "select a.idr,a.client,a.telefon,a.tickets,b.idT,b.place,b.transport,b.price,b.tickets,b.freetickets,b.datat from reservation a inner join trip b on a.tripid=b.idT";

                using (var dataR = comm.ExecuteReader())
                {
                    while (dataR.Read())
                    {
                        long idR = dataR.GetInt64(0);
                        string client = dataR.GetString(1);
                        string telefon = dataR.GetString(2);
                        int tickReserved = dataR.GetInt32(3);

                        long idT = dataR.GetInt64(4);
                        String place = dataR.GetString(5);
                        String transport = dataR.GetString(6);
                        double price = dataR.GetDouble(7);
                        int tickets = dataR.GetInt32(8);
                        int freeTickets = dataR.GetInt32(9);
                        DateTime data = dataR.GetDateTime(10);
                        Trip trip = new Trip(place, transport, data, price, tickets, freeTickets);
                        trip.ID = idT;
                        Reservation reservation = new Reservation(client, telefon, trip, tickReserved);
                        reservation.ID = idR;

                        resList.Add(reservation);
                    }
                }
            }

            return resList;
        }

        public Reservation findOne(long id)
        {
            log.InfoFormat("Entering findOne with value {0}", id);
            IDbConnection con = DBUtils.getConnection();

            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "select a.idr,a.client,a.telefon,a.tickets,b.idT,b.place,b.transport,b.price,b.tickets,b.freetickets,b.datat" +
                    " from reservation a" +
                    " inner join trip b on a.tripid=b.idT" +
                    " where idr=@id";
                IDbDataParameter paramId = comm.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                comm.Parameters.Add(paramId);

                using (var dataR = comm.ExecuteReader())
                {
                    if (dataR.Read())
                    {
                        long idR = dataR.GetInt64(0);
                        string client = dataR.GetString(1);
                        string telefon = dataR.GetString(2);
                        int tickReserved = dataR.GetInt32(3);

                        long idT = dataR.GetInt64(4);
                        String place = dataR.GetString(5);
                        String transport = dataR.GetString(6);
                        double price = dataR.GetDouble(7);
                        int tickets = dataR.GetInt32(8);
                        int freeTickets = dataR.GetInt32(9);
                        DateTime data = dataR.GetDateTime(10);
                        Trip trip = new Trip(place, transport, data, price, tickets, freeTickets);
                        trip.ID = idT;
                        Reservation reservation = new Reservation(client, telefon, trip, tickReserved);
                        reservation.ID = idR;
                        log.InfoFormat("Exiting findOne with value {0}", reservation);
                        return reservation;
                    }
                }
            }
            log.InfoFormat("Exiting findOne with value {0}", null);
            return null;
        }

        public IEnumerable<Reservation> findReservationByClient(string client1)
        {
            IDbConnection con = DBUtils.getConnection();
            log.InfoFormat("Finding reservation by client name: {0}",client1);
            IList<Reservation> resList = new List<Reservation>();
            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "select a.idr,a.client,a.telefon,a.tickets,b.idT,b.place,b.transport,b.price,b.tickets,b.freetickets,b.datat " +
                    "from reservation a " +
                    "inner join trip b " +
                    "on a.tripid=b.idT " +
                    "where a.client like @client";

                IDbDataParameter paramClient = comm.CreateParameter();
                paramClient.ParameterName = "@client";
                paramClient.Value = "%"+client1+"%";
                comm.Parameters.Add(paramClient);

                using (var dataR = comm.ExecuteReader())
                {
                    while (dataR.Read())
                    {
                        long idR = dataR.GetInt64(0);
                        string client = dataR.GetString(1);
                        string telefon = dataR.GetString(2);
                        int tickReserved = dataR.GetInt32(3);

                        long idT = dataR.GetInt64(4);
                        String place = dataR.GetString(5);
                        String transport = dataR.GetString(6);
                        double price = dataR.GetDouble(7);
                        int tickets = dataR.GetInt32(8);
                        int freeTickets = dataR.GetInt32(9);
                        DateTime data = dataR.GetDateTime(10);
                        Trip trip = new Trip(place, transport, data, price, tickets, freeTickets);
                        trip.ID = idT;
                        Reservation reservation = new Reservation(client, telefon, trip, tickReserved);
                        reservation.ID = idR;

                        resList.Add(reservation);
                    }
                }
            }

            return resList;
        }

        public IEnumerable<Reservation> findReservationByTripID(long tripID)
        {
            IDbConnection con = DBUtils.getConnection();
            log.InfoFormat("Finding reservation by trip ID: {0}", tripID);
            IList<Reservation> resList = new List<Reservation>();
            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "select a.idr,a.client,a.telefon,a.tickets,b.idT,b.place,b.transport,b.price,b.tickets,b.freetickets,b.datat " +
                    "from reservation a " +
                    "inner join trip b " +
                    "on a.tripid=b.idT " +
                    "where b.idT = @id";
                IDbDataParameter paramId = comm.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = tripID;
                comm.Parameters.Add(paramId);

                using (var dataR = comm.ExecuteReader())
                {
                    while (dataR.Read())
                    {
                        long idR = dataR.GetInt64(0);
                        string client = dataR.GetString(1);
                        string telefon = dataR.GetString(2);
                        int tickReserved = dataR.GetInt32(3);

                        long idT = dataR.GetInt64(4);
                        String place = dataR.GetString(5);
                        String transport = dataR.GetString(6);
                        double price = dataR.GetDouble(7);
                        int tickets = dataR.GetInt32(8);
                        int freeTickets = dataR.GetInt32(9);
                        DateTime data = dataR.GetDateTime(10);
                        Trip trip = new Trip(place, transport, data, price, tickets, freeTickets);
                        trip.ID = idT;
                        Reservation reservation = new Reservation(client, telefon, trip, tickReserved);
                        reservation.ID = idR;

                        resList.Add(reservation);
                    }
                }
            }

            return resList;
        }

        public void save(Reservation entity)
        {
            var con = DBUtils.getConnection();
            log.InfoFormat("Saving reservation = {0}", entity);
            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "insert into reservation (client,telefon,tripid,tickets) values (@client, @telefon,@tripId,@tick);select LAST_INSERT_ID()";
                /*var paramId = comm.CreateParameter();
                paramId.ParameterName = "@idf";
                paramId.Value = entity.ID;
                comm.Parameters.Add(paramId);*/

                var param1 = comm.CreateParameter();
                param1.ParameterName = "@client";
                param1.Value = entity.Client;
                comm.Parameters.Add(param1);

                var param2 = comm.CreateParameter();
                param2.ParameterName = "@telefon";
                param2.Value = entity.Telefon;
                comm.Parameters.Add(param2);

                var param3 = comm.CreateParameter();
                param3.ParameterName = "@tripid";
                param3.Value = entity.Trip.ID;
                comm.Parameters.Add(param3);

                var param4 = comm.CreateParameter();
                param4.ParameterName = "@tick";
                param4.Value = entity.NrTickets;
                comm.Parameters.Add(param4);

                

                using (var dataR = comm.ExecuteReader())
                {
                    if (dataR.Read())
                    {
                        long idf = dataR.GetInt64(0);

                        Console.WriteLine("LAST ID INSERTED: {0}", idf);
                        entity.ID = idf;
                        log.InfoFormat("Reservation was added succesfully}");
                    }
                }

            }
        }

        public void update(Reservation entity)
        {
            var con = DBUtils.getConnection();
            log.InfoFormat("Updating agent = {0}", entity);
            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "update reservation set client=@client,telefon=@telefon,tripid=@tripid,tickets=@tick  where idr=@id";
                var paramId = comm.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = entity.ID;
                comm.Parameters.Add(paramId);

                var param1 = comm.CreateParameter();
                param1.ParameterName = "@client";
                param1.Value = entity.Client;
                comm.Parameters.Add(param1);

                var param2 = comm.CreateParameter();
                param2.ParameterName = "@telefon";
                param2.Value = entity.Telefon;
                comm.Parameters.Add(param2);

                var param3 = comm.CreateParameter();
                param3.ParameterName = "@tripid";
                param3.Value = entity.Trip.ID;
                comm.Parameters.Add(param3);

                var param4 = comm.CreateParameter();
                param4.ParameterName = "@tick";
                param4.Value = entity.NrTickets;
                comm.Parameters.Add(param4);

                var dataR = comm.ExecuteNonQuery();

                if (dataR == 0)
                    throw new RepositoryException("No reservation updated!");
                else
                    log.Info("Reservation was updated succesfully");

            }
        }
    }
}
