package repository;

import domain.TravelAgent;

public interface AgentRepository extends Repository<Long, TravelAgent> {
    TravelAgent findAgentByUserAndPassw(String username, String passwd);
}
