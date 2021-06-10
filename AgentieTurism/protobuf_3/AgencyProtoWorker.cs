using System;
using System.Collections.Generic;
using System.Net.Sockets;
using System.Threading;
using Agency.Protocol;
using Google.Protobuf;
using services;
using Reservation = model.domain.Reservation;

namespace protobuf_3
{
    public class AgencyProtoWorker:IAgencyObserver
    {
        private IAgencyService server;
        private TcpClient connection;

        private NetworkStream stream;
        private volatile bool connected;

        public AgencyProtoWorker(IAgencyService server, TcpClient connection)
        {
            this.server = server;
            this.connection = connection;
            
            try
            {
				
                stream=connection.GetStream();
                connected=true;
            }
            catch (Exception e)
            {
                Console.WriteLine(e.StackTrace);
            }
        }


        public void ReservationAdded(Reservation reservation)
        {
	        Console.WriteLine("Update new reservation...");
	        try
	        {
		        sendResponse(ProtoUtils.createNewReservationResponse(reservation));
	        }
	        catch (Exception e)
	        {
		        Console.WriteLine(e.StackTrace);
	        }
        }

        public virtual void run()
        {
            while(connected)
            {
                try
                {
					
                    AgencyRequest request = AgencyRequest.Parser.ParseDelimitedFrom(stream);
                    AgencyResponse response =handleRequest(request);
                    if (response!=null)
                    {
                        sendResponse(response);
                    }
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.StackTrace);
                }
				
                try
                {
                    Thread.Sleep(1000);
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.StackTrace);
                }
            }
            try
            {
                stream.Close();
                connection.Close();
            }
            catch (Exception e)
            {
                Console.WriteLine("Error "+e);
            }
        }
        private AgencyResponse handleRequest(AgencyRequest request)
		{
			AgencyResponse response =null;
			AgencyRequest.Types.Type reqType=request.Type;
            switch(reqType){
                case AgencyRequest.Types.Type.Login:
			{
				Console.WriteLine("Login request ...");
				model.domain.TravelAgent user =ProtoUtils.getUser(request);
				try
                {
                    lock (server)
                    {
                        server.Login(user, this);
                    }
					return ProtoUtils.createOkResponse();
				}
				catch (ServiceException e)
				{
					connected=false;
					return ProtoUtils.createErrorResponse(e.Message);
				}
			}
                case AgencyRequest.Types.Type.Logout:
			{
				Console.WriteLine("Logout request");
                model.domain.TravelAgent user = ProtoUtils.getUser(request);
				try
				{
                    lock (server)
                    {

                        server.Logout(user, this);
                    }
					connected=false;
					return ProtoUtils.createOkResponse();

				}
				catch (ServiceException e)
				{
				   return ProtoUtils.createErrorResponse(e.Message);
				}
			}
                case AgencyRequest.Types.Type.AddUser:
			{
				Console.WriteLine("Add user request ...");
				model.domain.TravelAgent user =ProtoUtils.getUser(request);
				try
				{
					lock (server)
					{
						server.AddUser(user);
					}
					return ProtoUtils.createOkResponse();
				}
				catch (ServiceException e)
				{
					return ProtoUtils.createErrorResponse(e.Message);
				}
			}

                case AgencyRequest.Types.Type.GetAllTrips:
			{
				Console.WriteLine("Get all trips request Request ...");
				try
				{
                    IEnumerable<model.domain.Trip> trips;
                    lock (server)
                    {

                         trips = server.GetAllTrips();
                    }
					return ProtoUtils.createGetAllTripsResponse(trips);
				}
				catch (ServiceException e)
				{
					return ProtoUtils.createErrorResponse(e.Message);
				}
			}
                case AgencyRequest.Types.Type.TripsByNameHour:
                {
	                string place = request.FilterTrips.Place;
	                int min = request.FilterTrips.MinHour;
	                int max = request.FilterTrips.MaxHour;
	                Console.WriteLine("Get filtered trips request Request ...");
	                
	                try
	                {
		                IEnumerable<model.domain.Trip> trips;
		                lock (server)
		                {

			                trips = server.FindTripsByNameAndHours(place,min,max);
		                }
		                return ProtoUtils.createFilteredTripsResponse(trips);
	                }
	                catch (ServiceException e)
	                {
		                return ProtoUtils.createErrorResponse(e.Message);
	                }
                }  
                case AgencyRequest.Types.Type.AddReservation:
                {
	                Console.WriteLine("Add user request ...");
	                model.domain.Reservation reservation =ProtoUtils.getReservation(request);
	                try
	                {
		                lock (server)
		                {
			                server.AddReservation(reservation);
		                }
		                return ProtoUtils.createOkResponse();
	                }
	                catch (ServiceException e)
	                {
		                return ProtoUtils.createErrorResponse(e.Message);
	                }
                }
            }
			return response;
		}
        private void sendResponse(AgencyResponse response)
        {
            Console.WriteLine("sending response "+response);
            lock (stream)
            {
                response.WriteDelimitedTo(stream);
                stream.Flush();
            }

        }
    }
    
}