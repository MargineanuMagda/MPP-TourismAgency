using model.domain;
using System;
using System.Collections.Generic;

using System.Data;
using log4net;


namespace persistence
{


    public class AgentDBRepository : AgentRepository
    {
        private static readonly ILog log = LogManager.GetLogger("AgentDbRepository");

        public AgentDBRepository()
        {
            log.Info("Creating AgentDBRepository");
        }

        public void delete(long id)
        {
            log.InfoFormat("Deleting travel agent with id {0}",id);
            IDbConnection con = DBUtils.getConnection();

            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "delete from login where idf=@id";
                IDbDataParameter paramId = comm.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                comm.Parameters.Add(paramId);
                var dataR = comm.ExecuteNonQuery();

                if (dataR == 0)
                    throw new RepositoryException("No agent deleted!");
                else
                    log.Info("Travel agent deleted succesfully");
            }
        }

        

        public TravelAgent findAgentsByUser(string user, string passwd)
        {
            log.InfoFormat("Entering findOne with username: {0}, password: {1}", user,passwd);
            IDbConnection con = DBUtils.getConnection();

            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "select idf,username, passw from login where username=@user and passw=@passwd";
                IDbDataParameter paramUser = comm.CreateParameter();
                paramUser.ParameterName = "@user";
                paramUser.Value = user;

                comm.Parameters.Add(paramUser);
                var paramPas = comm.CreateParameter();
                paramPas.ParameterName = "@passwd";
                paramPas.Value = passwd;
                comm.Parameters.Add(paramPas);

                using (var dataR = comm.ExecuteReader())
                {
                    if (dataR.Read())
                    {
                        long idf = dataR.GetInt64(0);
                        String username = dataR.GetString(1);
                        String passw = dataR.GetString(2);
                        TravelAgent agent = new TravelAgent(username, passw);
                        agent.ID = idf;
                        log.InfoFormat("Exiting findOne with value {0}", agent);
                        return agent;
                    }
                }
            }
            log.InfoFormat("Exiting findOne with value {0}", null);
            return null;
        }

        public IEnumerable<TravelAgent> findAll()
        {
            IDbConnection con = DBUtils.getConnection();
            IList<TravelAgent> agents = new List<TravelAgent>();
            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "select idf,username, passw from login";

                using (var dataR = comm.ExecuteReader())
                {
                    while (dataR.Read())
                    {
                        long idf = dataR.GetInt64(0);
                        String username = dataR.GetString(1);
                        String passw = dataR.GetString(2);
                        TravelAgent agent = new TravelAgent(username, passw);
                        agent.ID = idf;
                        agents.Add(agent);
                    }
                }
            }

            return agents;
        }

        public TravelAgent findOne(long id)
        {
            log.InfoFormat("Entering findOne with value {0}", id);
            IDbConnection con = DBUtils.getConnection();

            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "select idf,username, passw from login where idf=@id";
                IDbDataParameter paramId = comm.CreateParameter();
                paramId.ParameterName = "@id";
                paramId.Value = id;
                comm.Parameters.Add(paramId);

                using (var dataR = comm.ExecuteReader())
                {
                    if (dataR.Read())
                    {
                        long idf = dataR.GetInt64(0);
                        String username = dataR.GetString(1);
                        String passw = dataR.GetString(2);
                        TravelAgent agent = new TravelAgent(username, passw);
                        agent.ID = idf;
                        return agent;
                    }
                }
            }
            log.InfoFormat("Exiting findOne with value {0}", null);
            return null;
        }

        public void save(TravelAgent entity)
        {
            var con = DBUtils.getConnection();
            log.InfoFormat("Saving agent = {0}", entity);
            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "insert into login (username,passw) values (@username, @passwd);select LAST_INSERT_ID()";
                /*var paramId = comm.CreateParameter();
                paramId.ParameterName = "@idf";
                paramId.Value = entity.ID;
                comm.Parameters.Add(paramId);*/

                var paramUser = comm.CreateParameter();
                paramUser.ParameterName = "@username";
                paramUser.Value = entity.Username;
                comm.Parameters.Add(paramUser);

                var paramPas = comm.CreateParameter();
                paramPas.ParameterName = "@passwd";
                paramPas.Value = entity.Passwd;
                comm.Parameters.Add(paramPas);

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

        public void update(TravelAgent entity)
        {
            var con = DBUtils.getConnection();
            log.InfoFormat("Updating agent = {0}", entity);
            using (var comm = con.CreateCommand())
            {
                comm.CommandText = "update login set username=@username ,passw=@passwd where idf=@idf";
                var paramId = comm.CreateParameter();
                paramId.ParameterName = "@idf";
                paramId.Value = entity.ID;
                comm.Parameters.Add(paramId);

                var paramUser = comm.CreateParameter();
                paramUser.ParameterName = "@username";
                paramUser.Value = entity.Username;
                comm.Parameters.Add(paramUser);

                var paramPas = comm.CreateParameter();
                paramPas.ParameterName = "@passwd";
                paramPas.Value = entity.Passwd;
                comm.Parameters.Add(paramPas);

                var result = comm.ExecuteNonQuery();
                if (result == 0)
                    throw new RepositoryException("Update failed !");
                else {
                    log.InfoFormat("Agent was updated succesfully!");
                }
            }
        }
    }
}
