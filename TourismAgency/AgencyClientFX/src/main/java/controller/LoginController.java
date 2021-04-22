package controller;

import domain.TravelAgent;
import domain.validators.ValidationException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import repository.RepoException;
import services.IAgencyService;
import services.ServiceException;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class LoginController implements Serializable {

    private IAgencyService service;
    private AppController appCtrl;
    private TravelAgent ctrUser;
    Parent mainAppParent;



    public void setService(IAgencyService serv){
        this.service=serv;
    }
    public void setParent(Parent parent){
        this.mainAppParent=parent;
    }
    public void setAppController(AppController appCtrl){
        this.appCtrl=appCtrl;
    }
    @FXML
    TabPane tabPane;
    @FXML
    TextField userTxt;
    @FXML
    TextField passwdTxt;
    @FXML
    TextField newUsername;
    @FXML
    TextField newPass1;
    @FXML
    TextField newPass2;


    public void login(ActionEvent actionEvent) {
        String user=userTxt.getText();
        String passwd=passwdTxt.getText();
        TravelAgent agent = new TravelAgent(user,passwd);
        agent.setId(100l);
        if(!user.equals("") && !passwd.equals("")){
            try {

                service.login(agent,appCtrl);
                appCtrl.setUser(agent);
                ctrUser=agent;
                Stage stage = new Stage();
                stage.setTitle("Tourism agency for "+ctrUser.getUsername());
                stage.setScene(new Scene(mainAppParent));

                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        appCtrl.logout();
                        System.exit(0);
                    }
                });
                stage.show();
                appCtrl.setUser(ctrUser);
                ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

            }catch (ServiceException ex){
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error ");
                alert.setContentText("Error while starting app "+ex);
                alert.showAndWait();
            }
        }
    }
    public void setUser(TravelAgent user) {
        this.ctrUser = user;
    }

    public void signUp(ActionEvent actionEvent) {
        String user= newUsername.getText();
        String pass1 = newPass1.getText();
        String pass2 = newPass2.getText();
        if(!pass1.equals(pass2)){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setContentText("Password should be the same");
            alert.showAndWait();
        }
        else{
            TravelAgent newUser= new TravelAgent(user,pass1);
            try {
                service.addUser(newUser);
                ctrUser=newUser;
                service.login(newUser,appCtrl);
                appCtrl.setUser(newUser);
                Stage stage = new Stage();
                stage.setTitle("Tourism agency for "+ctrUser.getUsername());
                stage.setScene(new Scene(mainAppParent));

                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        appCtrl.logout();
                        System.exit(0);
                    }
                });
                stage.show();
                appCtrl.setUser(ctrUser);
                ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

            }catch (ServiceException ex){
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error ");
                alert.setContentText("Error while starting app "+ex);
                alert.showAndWait();
            } catch (RepoException e) {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("REPO Error ");
                alert.setContentText("This account can t be created "+e);
                alert.showAndWait();
            } catch (ValidationException e) {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Validation Error ");
                alert.setContentText("This account can t be created"+e);
                alert.showAndWait();
            }



        }
    }

    public void signUpTab(ActionEvent actionEvent) {
        tabPane.getSelectionModel().select(1);
    }
}
