using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace networking.dto
{
    
	[Serializable]
	public class AgentDTO
	{
		public long UserId { get; set; }
		public string Username { get; set; }
		public string Passwd { get; set; }

		public AgentDTO(long id) : this(id, "","")
		{
		}

		public AgentDTO(long id,string user, string passwd)
		{
			this.UserId = id;
			this.Username = user;
			this.Passwd = passwd;
		}

		
	}
}
