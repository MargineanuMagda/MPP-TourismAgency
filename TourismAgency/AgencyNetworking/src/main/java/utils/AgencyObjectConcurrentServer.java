package utils;

import objectProtocol.AgencyClientObjectWorker;
import services.IAgencyService;

import java.net.Socket;

public class AgencyObjectConcurrentServer extends AbsConcurrentServer{
    private IAgencyService agencyServer;

    public AgencyObjectConcurrentServer(int port,IAgencyService agencyServer) {
        super(port);
        this.agencyServer=agencyServer;
        System.out.println("Agency-AgencyObjectConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        AgencyClientObjectWorker worker = new AgencyClientObjectWorker(agencyServer,client);
        Thread tw = new Thread(worker);
        return tw;
    }
}
