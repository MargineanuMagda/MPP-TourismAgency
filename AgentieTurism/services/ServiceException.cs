using System;

namespace services
{
    [Serializable]
    public class ServiceException:Exception
    {

       

        public ServiceException(String msg) : base(msg) { }

        public ServiceException(String msg, Exception ex) : base(msg, ex) { }

    }
}
