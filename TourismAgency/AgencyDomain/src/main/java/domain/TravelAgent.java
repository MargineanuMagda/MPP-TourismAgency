package domain;

import javax.persistence.*;

@javax.persistence.Entity
@Table(name="users")
public class TravelAgent extends Entity{
    private String username;
    private String passwd;

    public TravelAgent(String username, String passwd) {
        this.username = username;
        this.passwd = passwd;
    }

    public TravelAgent() {
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    @Basic
    @Column(name = "parola")
    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }


    @Override
    public String toString() {
        return "TravelAgent{" +
                "username='" + username + '\'' +
                ", passwd='" + passwd + '\'' +
                '}';
    }
}
