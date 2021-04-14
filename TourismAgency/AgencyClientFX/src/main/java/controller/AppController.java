package controller;

import domain.Reservation;
import domain.TravelAgent;
import domain.Trip;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import services.IAgencyObserver;
import services.IAgencyService;
import services.ServiceException;

import java.io.Serializable;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AppController extends UnicastRemoteObject implements Initializable, IAgencyObserver, Serializable {
    private IAgencyService service;
    private TravelAgent user;

    public void setService(IAgencyService serv){
        this.service=serv;
        System.out.println("Initializare service pentru AppController");

        //in caz ca nu merge initializa
        initTable1();
        //initLabels();
        initCombo();
    }
    public void setUser(TravelAgent agent){
        this.user=agent;
        initLabels();
    }

    public AppController() throws RemoteException {

        System.out.println("Constructor without params");
    }

    public AppController(IAgencyService service) throws RemoteException  {
        super();
        this.service = service;
        System.out.println("Constructor with param server ");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("INIT ");

    }

    @Override
    public void reservationAdded(Reservation reservation) {
        //initTable1();
        Platform.runLater(()->{
            refreshTables(reservation);
        });

    }

    private void refreshTables(Reservation reservation) {
        modelTrips.forEach(x->{
            //System.out.println("Rezervarea mea are id: "+reservation.getTripId().getId()+" vs rezervare din model trip id: "+x.getId());
            if(reservation.getTripId().getId().equals(x.getId())){
                //System.out.println("id egale");
                x.setFreeTickets(x.getFreeTickets()-reservation.getNrTick());
                //System.out.println("update trip "+x);
            }
        });


        tableTrip1.setItems(null);
        tableTrip1.setItems(modelTrips);
        tableTrip1.refresh();

    }

    private void initCombo() {
        tripCombo.setItems(modelTrips);
    }

    @FXML
    Label username;
    private void initLabels() {
        username.setText(user.getUsername().split("@")[0]);
        sliderMin.valueProperty().addListener((observableValue, oldValue, newValue) -> lblMin.textProperty().setValue(
                String.valueOf(newValue.intValue())));
        sliderMax.valueProperty().addListener((observableValue, oldValue, newValue) -> lblMax.textProperty().setValue(
                String.valueOf(newValue.intValue())));
        nrTickSpinner.valueProperty().addListener((observableValue, oldValue, newValue) -> priceLbl.textProperty().setValue(
                String.valueOf(tripCombo.getSelectionModel().getSelectedItem().getPrice()*newValue.intValue())));

    }


    @FXML
    TabPane tabPane;
    @FXML
    TableView<Trip> tableTrip1;
    @FXML
    TableColumn<Trip,String> place1Col;
    @FXML
    TableColumn<Trip,String> trans1Col;
    @FXML
    TableColumn<Trip,Double> price1Col;
    @FXML
    TableColumn<Trip, LocalDateTime> date1Col;
    @FXML
    TableColumn<Trip,Integer> tick1Col;
    @FXML
    TableColumn<Trip,Integer> freeTick1Col;

    ObservableList<Trip> modelTrips= FXCollections.observableArrayList();

    private void initTable1() {
        try {
            Iterable<Trip> trips = service.getAllTrips();
            List<Trip> tripList = StreamSupport.stream(trips.spliterator(), false).collect(Collectors.toList());
            modelTrips.setAll(tripList);
            place1Col.setCellValueFactory(new PropertyValueFactory<>("Place"));
            trans1Col.setCellValueFactory(new PropertyValueFactory<>("Transport"));
            price1Col.setCellValueFactory(new PropertyValueFactory<>("Price"));
            date1Col.setCellValueFactory(new PropertyValueFactory<>("Date"));
            tick1Col.setCellValueFactory(new PropertyValueFactory<>("NrTickets"));
            freeTick1Col.setCellValueFactory(new PropertyValueFactory<>("FreeTickets"));

            tableTrip1.setRowFactory(tv -> new TableRow<>() {
                @Override
                protected void updateItem(Trip item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty)
                        setStyle("");
                    else if (item.getFreeTickets() == 0)
                        setStyle("-fx-background-color:   red");

                }
            });
            tableTrip1.setItems(modelTrips);

        }catch (ServiceException er){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setContentText(er.getMessage());
            alert.showAndWait();
        }
    }


    //TAB 2
    @FXML
    TextField searchName;
    @FXML
    Slider sliderMin;
    @FXML
    Label lblMin;
    @FXML
    Slider sliderMax;
    @FXML
    Label lblMax;
    @FXML
    CheckBox nameCheck;
    @FXML
    CheckBox minCheck;
    @FXML
    CheckBox maxCheck;


    @FXML
    TableView<Trip> tableTrip2;
    @FXML
    TableColumn<Trip,String> place2Col;
    @FXML
    TableColumn<Trip,String> trans2Col;
    @FXML
    TableColumn<Trip,Double> price2Col;
    @FXML
    TableColumn<Trip, LocalDateTime> date2Col;
    @FXML
    TableColumn<Trip,Integer> tick2Col;
    @FXML
    TableColumn<Trip,Integer> freeTick2Col;


    public void searchTrips() {
        String name="";
        int minHour=0;
        int maxHour=23;
        if(nameCheck.isSelected())
            name=searchName.getText();
        if(minCheck.isSelected())
            minHour=(int)sliderMin.getValue();
        if(maxCheck.isSelected())
            maxHour=(int)sliderMax.getValue();

        System.out.println("!!!!!"+name+" "+minHour+" "+maxHour);
        Iterable<Trip> tripFiltered= null;
        try {
            tripFiltered = service.findTripsByNameAndHours(name, LocalTime.of(minHour,0),LocalTime.of(maxHour,0));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        tripFiltered.forEach(System.out::println);
        initTable2(tripFiltered);

    }

    private void initTable2(Iterable<Trip> tripFiltered) {
        List<Trip> tripList= StreamSupport.stream(tripFiltered.spliterator(), false).collect(Collectors.toList());
        ObservableList<Trip> modelTrips2= FXCollections.observableArrayList();
        modelTrips2.setAll(tripList);
        place2Col.setCellValueFactory(new PropertyValueFactory<>("Place"));
        trans2Col.setCellValueFactory(new PropertyValueFactory<>("Transport"));
        price2Col.setCellValueFactory(new PropertyValueFactory<>("Price"));
        date2Col.setCellValueFactory(new PropertyValueFactory<>("Date"));
        tick2Col.setCellValueFactory(new PropertyValueFactory<>("NrTickets"));
        freeTick2Col.setCellValueFactory(new PropertyValueFactory<>("FreeTickets"));
        tableTrip2.setItems(modelTrips2);
    }

    public void reserveTab2() {
        Trip tripToReserve=tableTrip2.getSelectionModel().getSelectedItem();
        if(tripToReserve!=null){


            tripCombo.setValue(tripToReserve);
            tripCombo.setAccessibleText(tripToReserve.toString());

            tabPane.getSelectionModel().select(2);

        }else{
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setContentText("Please select a trip first!");
            alert.showAndWait();
        }


    }

    //TAB3

    @FXML
    TextField clientTxt;
    @FXML
    TextField telTxt;
    @FXML
    Label priceLbl;
    @FXML
    ComboBox<Trip> tripCombo;
    @FXML
    Spinner<Integer> nrTickSpinner;



    @FXML
    TextField placeForm;
    @FXML
    TextField transForm;
    @FXML
    TextField priceForm;
    @FXML
    TextField dataForm;


    public void changeFormInfo() {



        Trip selected = tripCombo.getValue();
        placeForm.setText(selected.getPlace());
        transForm.setText(selected.getTransport());
        dataForm.setText(selected.getDate().toString());
        priceForm.setText(selected.getPrice().toString());


    }

    public void reserveTickets() {
        Trip toReserve = tripCombo.getValue();
        String client = clientTxt.getText();
        String tel = telTxt.getText();
        int nrTick = nrTickSpinner.getValue();
        if(!client.equals("") && !tel.equals("")&&toReserve!=null){
            try{
                Reservation r = new Reservation(client,tel,toReserve,nrTick);
                service.addReservation(r);
                //initTable1();
                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Info ");
                alert.setContentText("reservation added succesfully");
                alert.showAndWait();
            }catch (Exception e){
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error ");
                alert.setContentText("error: "+e.getMessage());
                alert.showAndWait();
            }

        }else{
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setContentText("All fields are required!");
            alert.showAndWait();
        }
    }

    public void logout() {
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Error ");
        alert.setContentText("Are you sure you want to leave?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent())
        // alert is exited, no button has been pressed.
        {
            if (result.get() == ButtonType.OK) {
                try {
                    service.logout(user, this);

                } catch (ServiceException e) {
                    System.out.println("Logout error " + e);
                }
                alert.close();

            } else if (result.get() == ButtonType.CANCEL) {
                alert.close();
            }
        }
    }


    public void handleLogout(ActionEvent actionEvent) {
        logout();
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }
}
