

using System;

namespace model.domain
{
    [Serializable]
    public class TravelAgent :Entity<long>
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

       
    }
}
