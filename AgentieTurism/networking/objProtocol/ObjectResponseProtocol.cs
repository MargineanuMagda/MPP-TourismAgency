using networking.dto;
using System;
using System.Collections.Generic;

namespace networking.objProtocol
{
    class ObjectResponseProtocol
    {
    }

	public interface Response
	{
	}

	[Serializable]
	public class OkResponse : Response
	{

	}

	[Serializable]
	public class ErrorResponse : Response
	{
		private string message;

		public ErrorResponse(string message)
		{
			this.message = message;
		}

		public virtual string Message
		{
			get
			{
				return message;
			}
		}
	}
	[Serializable]
	public class UserLoggedInResponse : Response
	{
		private AgentDTO agent;

		public UserLoggedInResponse(AgentDTO user)
		{
			this.agent = user;
		}

		public virtual AgentDTO Agent
		{
			get
			{
				return agent;
			}
		}
	}

	[Serializable]
	public class UserLoggedOutResponse : Response
	{
		private AgentDTO friend;

		public UserLoggedOutResponse(AgentDTO friend)
		{
			this.friend = friend;
		}

		public virtual AgentDTO Friend
		{
			get
			{
				return friend;
			}
		}
	}

	[Serializable]
	public class NewUserResponse : Response
	{
		private AgentDTO agent;

		public NewUserResponse(AgentDTO user)
		{
			this.agent = user;
		}

		public virtual AgentDTO Agent
		{
			get
			{
				return agent;
			}
		}
	}
	public interface UpdateResponse : Response
	{
	}
	[Serializable]
	public class NewReservationResponse : UpdateResponse
	{
		private ReservationDTO reservation;

		public NewReservationResponse(ReservationDTO newReservation)
		{
			this.reservation = newReservation;
		}

		public virtual ReservationDTO Reservation
		{
			get
			{
				return reservation;
			}
		}
	}

	[Serializable]
	public class GetAllTripsResponse : Response
	{
		private IEnumerable<TripDTO> trips;

		public GetAllTripsResponse(IEnumerable<TripDTO> trips)
		{
			this.trips = trips;
		}

		public virtual IEnumerable<TripDTO> Trips
		{
			get
			{
				return trips;
			}
		}
	}

	[Serializable]
	public class GetFilteredTripsResponse : Response
	{
		private IEnumerable<TripDTO> trips;

		public GetFilteredTripsResponse(IEnumerable<TripDTO> trips)
		{
			this.trips = trips;
		}

		public virtual IEnumerable<TripDTO> Trips
		{
			get
			{
				return trips;
			}
		}
	}

}
