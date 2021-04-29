package utils;

import protobuffprotocol.AgencyProtoWorker;
import services.IAgencyService;

import java.net.Socket;

public class AgencyProtobuffConcurrentServer extends AbsConcurrentServer{
    private IAgencyService server;

    public AgencyProtobuffConcurrentServer(int port, IAgencyService server) {
        super(port);
        this.server = server;
        System.out.println("Agency-AgencyProtobuffConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        AgencyProtoWorker worker = new AgencyProtoWorker(server,client);
        Thread tw = new Thread(worker);
        return tw;
    }
}
