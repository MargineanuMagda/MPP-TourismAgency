using System;
using System.Collections.Generic;
using System.Threading;
using System.Net.Sockets;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using model.domain;
using services;
using networking.objProtocol;
using networking.dto;

namespace networking
{
    public class ServerProxy : IAgencyService
    {
        private string host;
        private int port;

        private IAgencyObserver client;

        private NetworkStream stream;

        private IFormatter formatter;
        private TcpClient connection;

        private Queue<Response> responses;
        private volatile bool finished;
        private EventWaitHandle _waitHandle;
        public ServerProxy(string host, int port)
        {
            this.host = host;
            this.port = port;
            responses = new Queue<Response>();
            //initializeConnection();
        }
        private void initializeConnection()
        {
            try
            {
                connection = new TcpClient(host, port);
                Console.WriteLine("host: "+host+"port: "+port.ToString());
                stream = connection.GetStream();
                formatter = new BinaryFormatter();
                finished = false;
                _waitHandle = new AutoResetEvent(false);
                startReader();
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }
        private void startReader()
        {
            Thread tw = new Thread(Run);
            tw.Start();
        }
        public void AddReservation(Reservation reservation)
        {
            ReservationDTO mdto = DTOUtils.getDTO(reservation);
            sendRequest(new AddReservationRequest(mdto));
            Response response = readResponse();
            if (response is ErrorResponse)
            {
                ErrorResponse err = (ErrorResponse)response;
                throw new ServiceException(err.Message);
            }
        }

        public void AddUser(TravelAgent agent)
        {
            AgentDTO udto = DTOUtils.getDTO(agent);
            sendRequest(new AddUserRequest(udto));
            Response response = readResponse();
            if (response is ErrorResponse)
            {
                ErrorResponse err = (ErrorResponse)response;
                throw new ServiceException(err.Message);
            }
        }

        public IEnumerable<Trip> FindTripsByNameAndHours(string name, int minHout, int maxHour)
        {
            FilterDTO filter_dto = new FilterDTO(name,minHout,maxHour);
            sendRequest(new GetFilteredTripsRequest(filter_dto));
            Response response = readResponse();
            if (response is ErrorResponse)
            {
                ErrorResponse err = (ErrorResponse)response;
                throw new ServiceException(err.Message);
            }
            GetFilteredTripsResponse resp = (GetFilteredTripsResponse)response;
            IEnumerable<TripDTO> tripsDTO = resp.Trips;
            IEnumerable<Trip> trips = DTOUtils.getFromDTO(tripsDTO);
            return trips;
        } 

        public IEnumerable<Trip> GetAllTrips()
        {
            
            sendRequest(new GetAllTripsRequest());
            Response response = readResponse();
            if (response is ErrorResponse)
            {
                ErrorResponse err = (ErrorResponse)response;
                throw new ServiceException(err.Message);
            }
            GetAllTripsResponse resp = (GetAllTripsResponse)response;
            IEnumerable<TripDTO> tripsDTO = resp.Trips;
            IEnumerable<Trip> trips = DTOUtils.getFromDTO(tripsDTO);
            return trips;
        }

        public void Login(TravelAgent user, IAgencyObserver client)
        {
            initializeConnection();
            AgentDTO udto = DTOUtils.getDTO(user);
            sendRequest(new LoginRequest(udto));
            Response response = readResponse();
            if (response is OkResponse)
            {
                this.client = client;
                return;
            }
            if (response is ErrorResponse)
            {
                ErrorResponse err = (ErrorResponse)response;
                closeConnection();
                throw new ServiceException(err.Message);
            }
        }

        public void Logout(TravelAgent user, IAgencyObserver client)
        {
            AgentDTO udto = DTOUtils.getDTO(user);
            sendRequest(new LogoutRequest(udto));
            Response response = readResponse();
            closeConnection();
            if (response is ErrorResponse)
            {
                ErrorResponse err = (ErrorResponse)response;
                throw new ServiceException(err.Message);
            }
        }

        private void closeConnection()
        {
            finished = true;
            try
            {
                stream.Close();
                //output.close();
                connection.Close();
                _waitHandle.Close();
                client = null;
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }

        }

        private void sendRequest(Request request)
        {
            try
            {
                lock (stream)
                {
                    formatter.Serialize(stream, request);
                                    stream.Flush();
                }
                
            }
            catch (Exception e)
            {
                throw new ServiceException("Error sending object " + e);
            }

        }

        private Response readResponse()
        {
            Response response = null;
            try
            {
                _waitHandle.WaitOne();
                lock (responses)
                {
                    //Monitor.Wait(responses); 
                    response = responses.Dequeue();

                }


            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }
            return response;
        }

        public virtual void Run()
        {
            while (!finished)
            {
                try
                {
                    object response = formatter.Deserialize(stream);
                    Console.WriteLine("response received " + response);
                    if (response is UpdateResponse)
                    {
                        handleUpdate((UpdateResponse)response);
                    }
                    else
                    {

                        lock (responses)
                        {


                            responses.Enqueue((Response)response);

                        }
                        _waitHandle.Set();
                    }
                }
                catch (Exception e)
                {
                    Console.WriteLine("Reading error " + e);
                }

            }
        }

        private void handleUpdate(UpdateResponse update)
        {


                NewReservationResponse rUpd = (NewReservationResponse)update;
                Reservation reservation = DTOUtils.getFromDTO(rUpd.Reservation);
                Console.WriteLine("Friend logged in " + reservation);
                try
                {
                    client.ReservationAdded(reservation);
                }
                catch (ServiceException e)
                {
                    Console.WriteLine(e.StackTrace);
                }
           
            
        }
    }
}
