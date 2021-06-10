package start;

import agency.services.rest.ServiceException;
import client.UsersClient;
import domain.Trip;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

public class StartRestClient {
    private final static UsersClient usersClient=new UsersClient();
    public static void main(String[] args) {
        RestTemplate restTemplate=new RestTemplate();
        Trip trip = new Trip("Israel","EliTrans", LocalDateTime.of(2022,7,28,10,0),400d,100,15);
        trip.setId(0L);
        try{
            /*show(()-> System.out.println(usersClient.create(trip)));
            show(()->{
                Trip[] res=usersClient.getAll();
                for(Trip u:res){
                    System.out.println(u.getPlace()+" "+u.getDate()+" "+u.getPrice()+" "+u.getFreeTickets());
                }
            });*/
            Trip tripUpdated= usersClient.getById("3");
            String place = tripUpdated.getPlace();
            tripUpdated.setPlace(place+"Updated");

            show(()->usersClient.update(tripUpdated));
            show(()->{
                Trip[] res=usersClient.getAll();
                for(Trip u:res){
                    System.out.println(u.getPlace()+" "+u.getDate()+" "+u.getPrice()+" "+u.getFreeTickets());
                }
            });
            trip.setId(100L);
            show(()->usersClient.delete(trip.getId().toString()));
            show(()->{
                Trip[] res=usersClient.getAll();
                for(Trip u:res){
                    System.out.println(u.getPlace()+" "+u.getDate()+" "+u.getPrice()+" "+u.getFreeTickets());
                }
            });
        }catch(RestClientException ex){
            System.out.println("Exception ... "+ex.getMessage());
        }

    }



    private static void show(Runnable task) {
        try {
            task.run();
        } catch (ServiceException e) {
            //  LOG.error("Service exception", e);
            System.out.println("Service exception"+ e);
        }
    }
}

