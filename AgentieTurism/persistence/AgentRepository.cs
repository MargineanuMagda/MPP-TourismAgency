using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using model.domain;
namespace persistence
{
    public interface AgentRepository:Repository<long,TravelAgent>
    {
        TravelAgent findAgentsByUser(String user,String passwd);
    }
}
