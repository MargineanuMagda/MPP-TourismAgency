using AgentieTurism.domain;
using System;
using System.Collections.Generic;
using System.Data;
using log4net;

namespace AgentieTurism.repository
{
    class TripDBRepository : TripRepository
    {
        private static ILog log = LogManager.GetLogger("TripDBRepository");
        

        public TripDBRepository()
        {
            log.Info("Creating Trip DB Repository");
        }

        public void delete(long id)
        {
            log.InfoFormat("Deleting trip with id {0}", id);
            IDbConnection con = DBUtils.getConnection();

            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "delete from trip where idT=@id";
                IDbDataParameter paramId = comm.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                comm.Parameters.Add(paramId);
                var dataR = comm.ExecuteNonQuery();

                if (dataR == 0)
                    throw new RepositoryException("No trip deleted!");
                else
                    log.Info("Trip was deleted succesfully");
            }
        }

        public IEnumerable<Trip> findAll()
        {
            IDbConnection con = DBUtils.getConnection();
            IList<Trip> trips = new List<Trip>();
            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "select idT,place,transport,price,tickets,freetickets,datat from trip";

                using (var dataR = comm.ExecuteReader())
                {
                    while (dataR.Read())
                    {
                        long idT = dataR.GetInt64(0);
                        String place = dataR.GetString(1);
                        String transport = dataR.GetString(2);
                        double price = dataR.GetDouble(3);
                        int tickets = dataR.GetInt32(4);
                        int freeTickets = dataR.GetInt32(5);
                        DateTime data = dataR.GetDateTime(6);
                        Trip trip = new Trip(place, transport, data, price, tickets, freeTickets);
                        trip.ID = idT;
                        trips.Add(trip);
                    }
                }
            }

            return trips;
        }

        public Trip findOne(long id)
        {
            log.InfoFormat("Entering findOne with value {0}", id);
            IDbConnection con = DBUtils.getConnection();

            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "select idT,place,transport,price,tickets,freetickets,datat from trip where idT=@id";
                IDbDataParameter paramId = comm.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                comm.Parameters.Add(paramId);

                using (var dataR = comm.ExecuteReader())
                {
                    if (dataR.Read())
                    {
                        long idT = dataR.GetInt64(0);
                        String place = dataR.GetString(1);
                        String transport = dataR.GetString(2);
                        double price = dataR.GetDouble(3);
                        int tickets= dataR.GetInt32(4);
                        int freeTickets = dataR.GetInt32(5);
                        DateTime data = dataR.GetDateTime(6);


                        Trip trip = new Trip(place, transport, data, price, tickets, freeTickets);
                        trip.ID = idT;
                        log.InfoFormat("Exiting findOne with value {0}", trip);
                        return trip;
                    }
                }
            }
            log.InfoFormat("Exiting findOne with value {0}", null);
            return null;
        }

        public IEnumerable<Trip> findTripsByDate(DateTime minDate, DateTime maxDate)
        {
            IDbConnection con = DBUtils.getConnection();
            IList<Trip> trips = new List<Trip>();
            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "select idT,place,transport,price,tickets,freetickets,datat from trip where datat between @minDate and @maxDate";
                IDbDataParameter param1 = comm.CreateParameter();
                param1.ParameterName = "@minDate";
                param1.Value = minDate;
                comm.Parameters.Add(param1);
                IDbDataParameter param2 = comm.CreateParameter();
                param2.ParameterName = "@maxDate";
                param2.Value = maxDate;
                comm.Parameters.Add(param2);

                using (var dataR = comm.ExecuteReader())
                {
                    while (dataR.Read())
                    {
                        long idT = dataR.GetInt64(0);
                        String place = dataR.GetString(1);
                        String transport = dataR.GetString(2);
                        double price = dataR.GetDouble(3);
                        int tickets = dataR.GetInt32(4);
                        int freeTickets = dataR.GetInt32(5);
                        DateTime data = dataR.GetDateTime(6);
                        Trip trip = new Trip(place, transport, data, price, tickets, freeTickets);
                        trip.ID = idT;
                        trips.Add(trip);
                    }
                }
            }

            return trips;
        }

        public IEnumerable<Trip> findTripsByName(string name)
        {
            throw new NotImplementedException();
        }

        public IEnumerable<Trip> findTripsByNameAndHours(string name, int minDate, int maxDate)
        {
            IDbConnection con = DBUtils.getConnection();
            log.InfoFormat("Finding trips with name like: {0}, and hours between {1}->{2}", name,minDate,maxDate);
            IList<Trip> trips = new List<Trip>();
            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "select idT,place,transport,price,tickets,freetickets,datat from trip where hour(datat) between @minDate and @maxDate and place like @name";
                IDbDataParameter param1 = comm.CreateParameter();
                param1.ParameterName = "@minDate";
                param1.Value = minDate;
                comm.Parameters.Add(param1);
                IDbDataParameter param2 = comm.CreateParameter();
                param2.ParameterName = "@maxDate";
                param2.Value = maxDate;
                comm.Parameters.Add(param2);
                IDbDataParameter param3 = comm.CreateParameter();
                param3.ParameterName = "@name";
                param3.Value = "%"+name+"%";
                comm.Parameters.Add(param3);

                using (var dataR = comm.ExecuteReader())
                {
                    while (dataR.Read())
                    {
                        long idT = dataR.GetInt64(0);
                        String place = dataR.GetString(1);
                        String transport = dataR.GetString(2);
                        double price = dataR.GetDouble(3);
                        int tickets = dataR.GetInt32(4);
                        int freeTickets = dataR.GetInt32(5);
                        DateTime data = dataR.GetDateTime(6);
                        Trip trip = new Trip(place, transport, data, price, tickets, freeTickets);
                        trip.ID = idT;
                        trips.Add(trip);
                    }
                }
            }

            return trips;
        }

        public void save(Trip entity)
        {
            var con = DBUtils.getConnection();
            log.InfoFormat("Saving trip = {0}", entity);
            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "insert into trip (place,transport,price,tickets,freetickets,datat) values (@place, @trans,@price,@tick,@freeTick,@data);select LAST_INSERT_ID()";
                /*var paramId = comm.CreateParameter();
                paramId.ParameterName = "@idf";
                paramId.Value = entity.ID;
                comm.Parameters.Add(paramId);*/

                var param1 = comm.CreateParameter();
                param1.ParameterName = "@place";
                param1.Value = entity.Place;
                comm.Parameters.Add(param1);

                var param2 = comm.CreateParameter();
                param2.ParameterName = "@trans";
                param2.Value = entity.Transport;
                comm.Parameters.Add(param2);

                var param3 = comm.CreateParameter();
                param3.ParameterName = "@price";
                param3.Value = entity.Price;
                comm.Parameters.Add(param3);

                var param4 = comm.CreateParameter();
                param4.ParameterName = "@tick";
                param4.Value = entity.Tickets;
                comm.Parameters.Add(param4);

                var param5 = comm.CreateParameter();
                param5.ParameterName = "@freeTick";
                param5.Value = entity.FreeTickets;
                comm.Parameters.Add(param5);

                var param6 = comm.CreateParameter();
                param6.ParameterName = "@data";
                param6.Value = entity.Data;
                comm.Parameters.Add(param6);

                using (var dataR = comm.ExecuteReader())
                {
                    if (dataR.Read())
                    {
                        long idf = dataR.GetInt64(0);

                        Console.WriteLine("LAST ID INSERTED: {0}", idf);
                        entity.ID = idf;
                        log.InfoFormat("Agent was added succesfully}");
                    }
                }

            }
        }

        public void update(Trip entity)
        {
            var con = DBUtils.getConnection();
            log.InfoFormat("Updating trip = {0}", entity);
            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "update trip set place=@place,transport=@trans,price=@price,tickets=@tick,freetickets=@freeTick,datat=@data  where idT=@id";
                var paramId = comm.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = entity.ID;
                comm.Parameters.Add(paramId);

                var param1 = comm.CreateParameter();
                param1.ParameterName = "@place";
                param1.Value = entity.Place;
                comm.Parameters.Add(param1);

                var param2 = comm.CreateParameter();
                param2.ParameterName = "@trans";
                param2.Value = entity.Transport;
                comm.Parameters.Add(param2);

                var param3 = comm.CreateParameter();
                param3.ParameterName = "@price";
                param3.Value = entity.Price;
                comm.Parameters.Add(param3);

                var param4 = comm.CreateParameter();
                param4.ParameterName = "@tick";
                param4.Value = entity.Tickets;
                comm.Parameters.Add(param4);

                var param5 = comm.CreateParameter();
                param5.ParameterName = "@freeTick";
                param5.Value = entity.FreeTickets;
                comm.Parameters.Add(param5);

                var param6 = comm.CreateParameter();
                param6.ParameterName = "@data";
                param6.Value = entity.Data;
                comm.Parameters.Add(param6);

                var dataR = comm.ExecuteNonQuery();

                if (dataR == 0)
                    throw new RepositoryException("No trip updated!");
                else
                    log.Info("Trip was updated succesfully");

            }
        }
    }
}
