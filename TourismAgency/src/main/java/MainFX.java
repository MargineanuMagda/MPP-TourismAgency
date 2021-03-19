import controller.LoginController;
import domain.Reservation;
import domain.TravelAgent;
import domain.Trip;
import domain.validators.AgentValidator;
import domain.validators.ReservationValidator;
import domain.validators.TripValidator;
import domain.validators.Validator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import repository.AgentRepository;
import repository.ReservationRepository;
import repository.TripRepository;
import repository.database.AgentDbRepository;
import repository.database.ReservationDbRepository;
import repository.database.TripDbRepository;
import service.ServiceAgency;
import service.ServiceException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MainFX extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            LoginController ctrl = loader.getController();
            ctrl.setService(getService(),primaryStage);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("TravelPack");
            primaryStage.show();
        }catch(Exception e){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setContentText("Error while starting app "+e);
            alert.showAndWait();
        }
    }

    public static void main(String[] args) { launch(args);}

        static ServiceAgency getService()throws ServiceException{
            Properties props= new Properties();

            try {
                props.load(new FileReader("database.config"));
                //props.load(new FileReader("D:\\Facultate\\AN2SEM2\\MPP\\Laborator\\AgentieTurism\\TourismAgency\\database.config"));

            } catch (IOException e) {
                System.out.println("database.config not found! "+ e.getMessage());
            }
            Validator<Trip> tripValidator=new TripValidator();
            TripRepository repoTrip= new TripDbRepository(props,tripValidator);
            Validator<TravelAgent> agentValidator=new AgentValidator();
            AgentRepository repoAgent= new AgentDbRepository(props,agentValidator);
            Validator<Reservation> reservationValidator=new ReservationValidator();
            ReservationRepository repoReservation = new ReservationDbRepository(props,reservationValidator);

            ServiceAgency service = new ServiceAgency(repoTrip,repoAgent,repoReservation);
            return service;
        }

}
