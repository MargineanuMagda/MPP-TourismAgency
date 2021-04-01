package objectProtocol;

import domain.TravelAgent;
import dto.AgentDTO;


public class LogoutRequest implements Request {
    private AgentDTO user;

    public LogoutRequest(AgentDTO user) {
        this.user = user;
    }

    public AgentDTO getUser() {
        return user;
    }
}
