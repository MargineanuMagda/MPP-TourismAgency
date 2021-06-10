package client;


import agency.services.rest.ServiceException;
import domain.Trip;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

public class UsersClient {
    public static final String URL = "http://localhost:8080/agency/trips";

    private RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e) { // server down, resource exception
            throw new ServiceException(e);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public Trip[] getAll() {
        System.out.println("Get all trips..");
        return execute(() -> restTemplate.getForObject(URL, Trip[].class));
    }

    public Trip getById(String id) {
        System.out.println("Get trip by id..");
        return execute(() -> restTemplate.getForObject(String.format("%s/%s", URL, id), Trip.class));
    }

    public Trip create(Trip user) {
        System.out.println("Create trip..");
        return execute(() -> restTemplate.postForObject(URL, user, Trip.class));
    }

    public void update(Trip user) {
        execute(() -> {
            restTemplate.put(String.format("%s/%s", URL, user.getId()), user);
            return null;
        });
    }

    public void delete(String id) {
        execute(() -> {
            restTemplate.delete(String.format("%s/%s", URL, id));
            return null;
        });
    }

}

