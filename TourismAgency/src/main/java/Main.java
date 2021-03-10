import domain.Reservation;
import domain.TravelAgent;
import domain.Trip;
import domain.validators.*;
import repository.RepoException;
import repository.Repository;
import repository.database.AgentDbRepository;
import repository.database.ReservationDbRepository;
import repository.database.TripDbRepository;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {

        Properties props= new Properties();

        try {
            props.load(new FileReader("TourismAgency/database.config"));
        } catch (IOException e) {
            System.out.println("spectacol.config not found! "+ e.getMessage());
        }

        Validator<Trip> tripValidator=new TripValidator();
        Repository<Long, Trip> repoTrip= new TripDbRepository(props,tripValidator);
        Validator<TravelAgent> agentValidator=new AgentValidator();
        Repository<Long, TravelAgent> repoAgent= new AgentDbRepository(props,agentValidator);
        Validator<Reservation> reservationValidator=new ReservationValidator();
        Repository<Long,Reservation> repoReservation = new ReservationDbRepository(props,reservationValidator);

        Trip s= new Trip("Brasov","EliTrans",LocalDate.of(2021,06,26), LocalTime.now(),400d,120,120);
        TravelAgent agent = new TravelAgent("aa@travel","1234567");
        s.setId(13l);
        agent.setId(0L);
        try {
            //repoTrip.save(s);

            repoAgent.delete(2L);
        } catch (RepoException e) {
            e.printStackTrace();
        }
        System.out.println(repoTrip.findAll());
        System.out.println(repoAgent.findAll());
        System.out.println(repoReservation.findAll());
    }
}
