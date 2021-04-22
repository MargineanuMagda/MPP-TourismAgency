
using System;

namespace model.domain
{
    
   [Serializable]
    public class Entity<Id>
    {
        
        public Id ID { get; set; }
    }
}
