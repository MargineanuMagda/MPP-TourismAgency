using System;
using System.Collections.Generic;
using System.Globalization;
using Agency.Protocol;
using model.domain;
using proto = Agency.Protocol;
using Trip = model.domain.Trip;

namespace protobuf_3
{
    static class ProtoUtils
    {
        public static AgencyRequest createLoginRequest(model.domain.TravelAgent user)
        {
            proto.TravelAgent userDTO = new proto.TravelAgent();
            userDTO.UserId = user.ID;
            userDTO.Username = user.Username;
            userDTO.Passwd = user.Passwd;

            AgencyRequest request = new AgencyRequest();
            request.Type = AgencyRequest.Types.Type.Login;
            request.User = userDTO;

            return request;
        }

        public static AgencyRequest createLogoutRequest(model.domain.TravelAgent user)
        {
            proto.TravelAgent userDTO = new proto.TravelAgent();
            userDTO.UserId = user.ID;
            userDTO.Username = user.Username;
            userDTO.Passwd = user.Passwd;

            AgencyRequest request = new AgencyRequest();
            request.Type = AgencyRequest.Types.Type.Logout;
            request.User = userDTO;

            return request;
        }

        public static AgencyRequest createNewUserRequest(model.domain.TravelAgent user)
        {
            proto.TravelAgent userDTO = new proto.TravelAgent();
            userDTO.UserId = user.ID;
            userDTO.Username = user.Username;
            userDTO.Passwd = user.Passwd;

            AgencyRequest request = new AgencyRequest();
            request.Type = AgencyRequest.Types.Type.AddUser;
            request.User = userDTO;

            return request;
        }

        public static AgencyRequest createNewReservation(model.domain.Reservation reservation)
        {
            proto.Reservation resDTO = new proto.Reservation();
            resDTO.Client = reservation.Client;
            resDTO.Telefon = reservation.Telefon;
            resDTO.Place = reservation.Trip.Place;
            resDTO.TripID = reservation.Trip.ID;
            resDTO.AvTickets = reservation.Trip.FreeTickets;
            resDTO.NrTick = reservation.NrTickets;

            AgencyRequest request = new AgencyRequest();
            request.Type = AgencyRequest.Types.Type.AddUser;
            request.Reservation = resDTO;
            return request;
        }

        public static AgencyRequest createGetAllTripsRequest()
        {
            AgencyRequest request = new AgencyRequest();
            request.Type = AgencyRequest.Types.Type.GetAllTrips;
            return request;
        }

        public static AgencyRequest createFilteredTripsRequest(String place, int minHour, int maxHour)
        {
            Filter filDTO = new Filter();
            filDTO.Place = place;
            filDTO.MinHour = minHour;
            filDTO.MaxHour = maxHour;

            AgencyRequest request = new AgencyRequest();
            request.Type = AgencyRequest.Types.Type.TripsByNameHour;
            request.FilterTrips = filDTO;
            return request;
        }

        public static AgencyResponse createOkResponse()
        {
            AgencyResponse response = new AgencyResponse {Type = AgencyResponse.Types.Type.Ok};
            return response;
        }


        public static AgencyResponse createErrorResponse(String text)
        {
            AgencyResponse response = new AgencyResponse
            {
                Type = AgencyResponse.Types.Type.Error, Error = text
            };
            return response;
        }

        public static AgencyResponse createGetAllTripsResponse(IEnumerable<model.domain.Trip> trips)
        {
            AgencyResponse response = new AgencyResponse();
            response.Type = AgencyResponse.Types.Type.GetAllTrips;
            foreach (var t in trips)
            {
                proto.Trip tripDTO = new proto.Trip();
                tripDTO.TripId = t.ID;
                tripDTO.Place = t.Place;
                tripDTO.Transport = t.Transport;
                tripDTO.Date = t.Date.ToString("yyyy-MM-dd HH:mm:ss");
                tripDTO.Price = t.Price;
                tripDTO.NrTickets = t.NrTickets;
                tripDTO.FreeTickets = t.FreeTickets;
                response.Trips.Add(tripDTO);
            }

            return response;
        }

        public static AgencyResponse createFilteredTripsResponse(IEnumerable<model.domain.Trip> trips)
        {
            AgencyResponse response = new AgencyResponse();
            response.Type = AgencyResponse.Types.Type.TripsFiltered;
            foreach (var t in trips)
            {
                proto.Trip tripDTO = new proto.Trip();
                tripDTO.TripId = t.ID;
                tripDTO.Place = t.Place;
                tripDTO.Transport = t.Transport;
                tripDTO.Date = t.Date.ToString("yyyy-MM-dd HH:mm:ss");
                tripDTO.Price = t.Price;
                tripDTO.NrTickets = t.NrTickets;
                tripDTO.FreeTickets = t.FreeTickets;
                response.Trips.Add(tripDTO);
            }

            return response;
        }

        public static AgencyResponse createNewReservationResponse(model.domain.Reservation reservation)
        {
            proto.Reservation resDTO = new proto.Reservation();
            resDTO.Client = reservation.Client;
            resDTO.Telefon = reservation.Telefon;
            resDTO.Place = reservation.Trip.Place;
            resDTO.TripID = reservation.Trip.ID;
            resDTO.AvTickets = reservation.Trip.FreeTickets;
            resDTO.NrTick = reservation.NrTickets;

            AgencyResponse response = new AgencyResponse();
            response.Type = AgencyResponse.Types.Type.NewReservation;
            response.Reservation = resDTO;
            return response;
        }
        public static String getError(AgencyResponse response)
        {
            String errorMessage = response.Error;
            return errorMessage;
        }
        public static model.domain.TravelAgent getUser(AgencyResponse response)
        {
            model.domain.TravelAgent user = new model.domain.TravelAgent(response.User.Username, response.User.Passwd);
            user.ID = response.User.UserId;
            return user;
        }

        public static model.domain.Reservation getReservation(AgencyResponse response)
        {
            proto.Reservation resDTO = response.Reservation;
            model.domain.Trip trip = new model.domain.Trip(resDTO.TripID, resDTO.Place, resDTO.AvTickets);
            model.domain.Reservation reservation =
                new model.domain.Reservation(resDTO.Client, resDTO.Telefon, trip, resDTO.NrTick);
            return reservation;
        }

        public static IEnumerable<model.domain.Trip> getTrips(AgencyResponse response)
        {
            List<model.domain.Trip> trips = new List<Trip>();
            for (int i = 0; i < response.Trips.Count; i++)
            {
                proto.Trip tripDTO = response.Trips[i];
                CultureInfo provider = CultureInfo.InvariantCulture;  
// It throws Argument null exception  
                DateTime dateTime = DateTime.ParseExact(tripDTO.Date, "yyyy-MM-dd HH:mm:ss", provider);
                model.domain.Trip trip = new model.domain.Trip(tripDTO.Place, tripDTO.Transport, dateTime,
                    tripDTO.Price, tripDTO.NrTickets, tripDTO.FreeTickets);
                trip.ID = tripDTO.TripId;
                
                trips.Add(trip);
            }

            return trips;
        }

        public static model.domain.TravelAgent getUser(AgencyRequest request)
        {
            model.domain.TravelAgent user = new model.domain.TravelAgent(request.User.Username, request.User.Passwd);
            user.ID = request.User.UserId;
            return user;
        }

        public static model.domain.Reservation getReservation(AgencyRequest request)
        {
            proto.Reservation resDTO = request.Reservation;
            model.domain.Trip trip = new model.domain.Trip(resDTO.TripID, resDTO.Place, resDTO.AvTickets);
            model.domain.Reservation reservation =
                new model.domain.Reservation(resDTO.Client, resDTO.Telefon, trip, resDTO.NrTick);
            return reservation;
        }
        
    }
}