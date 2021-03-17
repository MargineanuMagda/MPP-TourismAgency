using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using AgentieTurism.domain;
namespace AgentieTurism.repository
{
    interface AgentRepository:Repository<long,TravelAgent>
    {
        TravelAgent findAgentsByUser(String user,String passwd);
    }
}
