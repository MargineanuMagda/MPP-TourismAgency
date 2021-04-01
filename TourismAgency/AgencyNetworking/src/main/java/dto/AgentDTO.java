package dto;

import java.io.Serializable;

public class AgentDTO implements Serializable {
    private long userId;
    private String username;
    private String passwd;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public AgentDTO(long userId, String username, String passwd) {
        this.userId=userId;
        this.username = username;
        this.passwd = passwd;
    }

    @Override
    public String toString() {
        return "AgentDTO{" +
                "username='" + username + '\'' +
                ", passwd='" + passwd + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
