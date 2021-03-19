package controller;

import domain.TravelAgent;
import domain.validators.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import repository.RepoException;
import service.ServiceAgency;
import service.ServiceException;


public class LoginController {
    private ServiceAgency service;
    Stage mainStage;
    public void setService(ServiceAgency serv,Stage mainStage){
        this.service=serv;
        this.mainStage=mainStage;
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
        if(!user.equals("") && !passwd.equals("")){
            try {
                service.login(user,passwd);
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/menu.fxml"));
                    Parent root = loader.load();

                    Scene scene = new Scene(root);
                    Stage primaryStage = new Stage();
                    primaryStage.setScene(scene);

                    AppController ctrl = loader.getController();
                    ctrl.setService(service,primaryStage);

                    primaryStage.setTitle("TravelPack");
                    primaryStage.show();

                    userTxt.clear();
                    passwdTxt.clear();
                }catch(Exception e){
                    Alert alert=new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error ");
                    alert.setContentText("Error app "+e);
                    alert.showAndWait();
                }
            }catch (ServiceException ex){
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error ");
                alert.setContentText("Error while starting app "+ex);
                alert.showAndWait();
            }
        }
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
                service.login(user,pass1);
            } catch (ValidationException | RepoException | ServiceException e) {
                e.printStackTrace();
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/menu.fxml"));
                Parent root = loader.load();

                Scene scene = new Scene(root);
                Stage primaryStage = new Stage();
                primaryStage.setScene(scene);

                AppController ctrl = loader.getController();
                ctrl.setService(service,primaryStage);

                primaryStage.setTitle("TravelPack");
                primaryStage.show();

                mainStage.close();
            }catch(Exception e){
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error ");
                alert.setContentText("Error app "+e);
                alert.showAndWait();
            }

        }
    }

    public void signUpTab(ActionEvent actionEvent) {
        tabPane.getSelectionModel().select(1);
    }
}
