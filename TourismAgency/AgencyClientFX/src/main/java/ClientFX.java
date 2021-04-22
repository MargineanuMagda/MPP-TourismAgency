import controller.AppController;
import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import rpcProtocol.AgencyServicesRpcProxy;
import services.IAgencyService;
import java.io.IOException;
import java.util.Properties;

public class ClientFX extends Application {


    /*private static int defaultPort = 55555;
    private static String defaultServer = "localhost";*/

    @Override
    public void start(Stage primaryStage) throws Exception {

        /*System.out.println("In start");
        Properties clientProps = new Properties();

        try {
            clientProps.load(ClientFX.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatclient.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("agency.server.host", defaultServer);
        int serverPort = defaultPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("agency.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IAgencyService server = new AgencyServicesRpcProxy(serverIP, serverPort);*/

        //Spring
        ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-client.xml");
        IAgencyService server=(IAgencyService)factory.getBean("agencyService");
        System.out.println("Obtained a reference to remote chat server");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();

        System.out.println("aaa");
       LoginController ctrl = loader.<LoginController>getController();
       ctrl.setService(server);

       FXMLLoader cloader = new FXMLLoader(
                getClass().getClassLoader().getResource("menu.fxml"));
        Parent croot=cloader.load();

        System.out.println("bbb");


        AppController appCtrl =
                cloader.<AppController>getController();
        appCtrl.setService(server);

        ctrl.setAppController(appCtrl);
        ctrl.setParent(croot);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("TravelPack");
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
