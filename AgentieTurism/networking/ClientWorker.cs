using System;
using System.Collections.Generic;
using System.Net.Sockets;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;

using System.Threading;
using model.domain;
using networking.dto;
using networking.objProtocol;
using services;
namespace networking
{
	public class ClientWorker : IAgencyObserver
	{
		private IAgencyService server;
		private TcpClient connection;

		private NetworkStream stream;
		private IFormatter formatter;
		private volatile bool _connected;
		public ClientWorker(IAgencyService server, TcpClient connection)
		{
			this.server = server;
			this.connection = connection;
			try
			{

				stream = connection.GetStream();
				formatter = new BinaryFormatter();
				_connected = true;
			}
			catch (Exception e)
			{
				Console.WriteLine(e.StackTrace);
			}
		}

		public virtual void run()
		{
			while (_connected)
			{
				try
				{
					object request = formatter.Deserialize(stream);
					object response = handleRequest((Request)request);
					if (response != null)
					{
						sendResponse((Response)response);
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
				Console.WriteLine("Error " + e);
			}
		}

		public void ReservationAdded(Reservation reservation)
		{
			ReservationDTO res_dto = DTOUtils.getDTO(reservation);
			Console.WriteLine("Reservation received  " + res_dto);
			try
			{
				sendResponse(new NewReservationResponse(res_dto));
			}
			catch (Exception e)
			{
				throw new ServiceException("Sending error: " + e);
			}
		}

		private Response handleRequest(Request request)
		{
			Response response = null;
			if (request is LoginRequest)
			{
				Console.WriteLine("Login request ...");
				LoginRequest logReq = (LoginRequest)request;
				AgentDTO udto = logReq.User;
				TravelAgent user = DTOUtils.getFromDTO(udto);
				try
				{
					lock (server)
					{
						server.Login(user, this);
					}
					return new OkResponse();
				}
				catch (ServiceException e)
				{
					_connected = false;
					return new ErrorResponse(e.Message);
				}
			}
			if (request is LogoutRequest)
			{
				Console.WriteLine("Logout request");
				LogoutRequest logReq = (LogoutRequest)request;
				AgentDTO udto = logReq.User;
				TravelAgent user = DTOUtils.getFromDTO(udto);
				try
				{
					lock (server)
					{

						server.Logout(user, this);
					}
					_connected = false;
					return new OkResponse();

				}
				catch (ServiceException e)
				{
					return new ErrorResponse(e.Message);
				}
			}
			if (request is AddUserRequest)
			{
				Console.WriteLine("AddUserRequest ...");
				AddUserRequest newUserReq = (AddUserRequest)request;
				AgentDTO userDto = newUserReq.User;
				TravelAgent user = DTOUtils.getFromDTO(userDto);
				try
				{
					lock (server)
					{
						server.AddUser(user);
					}
					return new OkResponse();
				}
				catch (ServiceException e)
				{
					return new ErrorResponse(e.Message);
				}
			}

			if (request is AddReservationRequest)
			{
				Console.WriteLine("AddReservationRequest ...");
				AddReservationRequest resReq = (AddReservationRequest)request;
				ReservationDTO rdto = resReq.Reservation;
				Reservation reserv = DTOUtils.getFromDTO(rdto);
				try
				{
					lock (server)
					{
						server.AddReservation(reserv);
					}
					return new OkResponse();
				}
				catch (ServiceException e)
				{
					return new ErrorResponse(e.Message);
				}
			}
			if (request is GetFilteredTripsRequest)
			{
				Console.WriteLine("GetFilteredTripsRequest Request ...");
				GetFilteredTripsRequest filterReq = (GetFilteredTripsRequest)request;
				FilterDTO filter_dto = filterReq.Filter;

				try
				{
					IEnumerable<Trip> trips;
					lock (server)
					{

						trips = server.FindTripsByNameAndHours(filter_dto.Place, filter_dto.MinHour, filter_dto.MaxHour);
					}
					IEnumerable<TripDTO> tripsDTO = DTOUtils.getDTO(trips);
					return new GetFilteredTripsResponse(tripsDTO);
				}
				catch (ServiceException e)
				{
					return new ErrorResponse(e.Message);
				}
			}

			if (request is GetAllTripsRequest)
			{
				Console.WriteLine("GetAllTripsRequest Request ...");
				//GetAllTripsRequest filterReq = (GetAllTripsRequest)request;


				try
				{
					IEnumerable<Trip> trips;
					lock (server)
					{

						trips = server.GetAllTrips();
					}
					IEnumerable<TripDTO> tripsDTO = DTOUtils.getDTO(trips);
					return new GetAllTripsResponse(tripsDTO);
				}
				catch (ServiceException e)
				{
					return new ErrorResponse(e.Message);
				}
			}
			return response;
		}

		private void sendResponse(Response response)
		{
			Console.WriteLine("sending response " + response);
			lock (stream)
			{
				formatter.Serialize(stream, response);
				stream.Flush();
			}
			


		}
	}
}
