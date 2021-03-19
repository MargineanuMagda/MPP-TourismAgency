import domain.Reservation;
import domain.TravelAgent;
import domain.Trip;
import domain.validators.*;
import repository.*;
import repository.database.AgentDbRepository;
import repository.database.ReservationDbRepository;
import repository.database.TripDbRepository;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {

        Properties props= new Properties();

        try {
            //props.load(new FileReader("database.config"));
            props.load(new FileReader("D:\\Facultate\\AN2SEM2\\MPP\\Laborator\\AgentieTurism\\TourismAgency\\database.config"));

        } catch (IOException e) {
            System.out.println("database.config not found! "+ e.getMessage());
        }

        Validator<Trip> tripValidator=new TripValidator();
        TripRepository repoTrip= new TripDbRepository(props,tripValidator);
        Validator<TravelAgent> agentValidator=new AgentValidator();
        AgentRepository repoAgent= new AgentDbRepository(props,agentValidator);
        Validator<Reservation> reservationValidator=new ReservationValidator();
        ReservationRepository repoReservation = new ReservationDbRepository(props,reservationValidator);

        Trip s= new Trip("Budapesta","ATrans", LocalDateTime.of(LocalDate.of(2021, 9,1), LocalTime.now()),700d,70,70);

        s.setId(3L);
        //TRIP ADD,UPDATE,DELETE
        /*try {
            repoTrip.save(s);
            s.setFreeTickets(50);
            repoTrip.update(s);
        } catch (ValidationException e) {
            e.printStackTrace();
        } catch (RepoException e) {
            e.printStackTrace();
        }*/

        //find trips by name and hours
        //repoTrip.findTripsByNameAndHours("Brasov",LocalTime.of(0,0),LocalTime.of(23,0)).forEach(System.out::println);

        //find one
        //System.out.println(repoTrip.findOne(2l));

        TravelAgent agent = new TravelAgent("ion@travel","1234567");
        agent.setId(0L);
        /*try {
            repoAgent.save(agent);
            agent.setUsername("andrei@travel");
            repoAgent.update(agent);
            repoAgent.delete(agent.getId());
        } catch (ValidationException e) {
            e.printStackTrace();
        } catch (RepoException e) {
            e.printStackTrace();
        }*/

        System.out.println(repoAgent.findAgentByUserAndPassw("magdalena@travel","1234567"));
        repoAgent.findAll().forEach(System.out::println);

        //RESERVATION REPO CRUD
        //add reservation
        Reservation r = new Reservation("Margineanu Magdalena","0749 779 473",s,4);
        r.setId(4L);
        //repoReservation.findAll().forEach(System.out::println);
        try {
            //repoReservation.save(r);

            r.setNrTick(7);
            repoReservation.update(r);
            //repoAgent.delete(2L);
        } catch (RepoException | ValidationException e) {
            e.printStackTrace();
        }


        //delete reservation
        /*try {
            repoReservation.delete(5l);
        } catch (RepoException e) {
            e.printStackTrace();
        }*/

        //find reservation by client
       repoReservation.findReservationByClient("Pop").forEach(System.out::println);

        //find reservation by trip id
        repoReservation.findReservationByTripID(3L).forEach(System.out::println);

        /*System.out.println(repoTrip.findAll());

        repoReservation.findAll().forEach(System.out::println);*/
    }
}
