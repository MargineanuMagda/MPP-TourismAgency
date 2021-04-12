using networking.dto;
using System;

namespace networking.objProtocol
{
    
	public interface Request
	{
	}


	[Serializable]
	public class LoginRequest : Request
	{
		private AgentDTO user;

		public LoginRequest(AgentDTO user)
		{
			this.user = user;
		}

		public virtual AgentDTO User
		{
			get
			{
				return user;
			}
		}
	}

	[Serializable]
	public class LogoutRequest : Request
	{
		private AgentDTO user;

		public LogoutRequest(AgentDTO user)
		{
			this.user = user;
		}

		public virtual AgentDTO User
		{
			get
			{
				return user;
			}
		}
	}
	[Serializable]
	public class AddUserRequest : Request
	{
		private AgentDTO user;

		public AddUserRequest(AgentDTO user)
		{
			this.user = user;
		}

		public virtual AgentDTO User
		{
			get
			{
				return user;
			}
		}
	}

	[Serializable]
	public class AddReservationRequest : Request
	{
		private ReservationDTO reservation;

		public AddReservationRequest(ReservationDTO reservation)
		{
			this.reservation = reservation;
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
	public class GetFilteredTripsRequest : Request
	{
		private FilterDTO filter;

		public GetFilteredTripsRequest(FilterDTO filter)
		{
			this.filter = filter;
		}

		public virtual FilterDTO Filter
		{
			get
			{
				return filter;
			}
		}
	}
	[Serializable]
	public class GetAllTripsRequest : Request
	{


		public GetAllTripsRequest()
		{
			
		}

		
	}


}
