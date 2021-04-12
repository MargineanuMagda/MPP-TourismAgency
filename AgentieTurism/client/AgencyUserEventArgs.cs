using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace client
{
   
    public enum AgencyUserEvent
    {
        ReservationAdded
    };
    public class AgencyUserEventArgs : EventArgs
    {
        private readonly AgencyUserEvent userEvent;
        private readonly Object data;

        public AgencyUserEventArgs(AgencyUserEvent userEvent, object data)
        {
            this.userEvent = userEvent;
            this.data = data;
        }

        public AgencyUserEvent UserEventType
        {
            get { return userEvent; }
        }

        public object Data
        {
            get { return data; }
        }
    }
}
