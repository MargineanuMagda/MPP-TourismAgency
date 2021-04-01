package utils;

import rpcProtocol.AgencyClientRpcReflectionWorker;
import services.IAgencyService;

import java.net.Socket;

public class AgencyRpcConcurrentServer extends AbsConcurrentServer{
    private IAgencyService agencyServer;
    public AgencyRpcConcurrentServer(int port, IAgencyService agencyServer) {
        super(port);
        this.agencyServer = agencyServer;
        System.out.println("Agency-AgencyRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        AgencyClientRpcReflectionWorker worker=new AgencyClientRpcReflectionWorker(agencyServer, client);

        Thread tw=new Thread(worker);
        return tw;
    }
    @Override
    public void stop(){
        System.out.println("Stoppong services...");
    }
}
