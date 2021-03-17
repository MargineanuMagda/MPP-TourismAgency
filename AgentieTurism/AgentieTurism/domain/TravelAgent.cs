using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AgentieTurism.domain
{
    class TravelAgent:Entity<long>
    {
        public string Username { get; set; }
        public string Passwd { get; set; }

        public TravelAgent(string username, string passwd)
        {
            Username = username;
            Passwd = passwd;
        }

        public override string ToString()
        {
            return "Username: "+Username+" Password: "+Passwd;
        }

        public override bool Equals(object obj)
        {
            return base.Equals(obj);
        }

        public override int GetHashCode()
        {
            return base.GetHashCode();
        }
    }
}
