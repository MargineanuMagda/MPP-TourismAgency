package objectProtocol;

import dto.AgentDTO;


public class LoginRequest implements Request {
    private AgentDTO user;

    public LoginRequest(AgentDTO user) {
        this.user = user;
    }

    public AgentDTO getUser() {
        return user;
    }
}
