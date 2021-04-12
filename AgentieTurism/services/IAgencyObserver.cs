
using model.domain;

namespace services
{
    public interface IAgencyObserver
    {
        void ReservationAdded(Reservation reservation);
    }
}
