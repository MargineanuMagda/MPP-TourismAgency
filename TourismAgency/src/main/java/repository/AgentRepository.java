package repository;

import domain.TravelAgent;

import java.time.LocalDate;

public interface AgentRepository extends Repository<Long, TravelAgent> {
    TravelAgent findAgentByUserAndPassw(String username, String passwd);
}
