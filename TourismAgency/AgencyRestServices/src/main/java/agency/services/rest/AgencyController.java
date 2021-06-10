package agency.services.rest;

import domain.Trip;
import domain.validators.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.RepoException;
import repository.TripRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@CrossOrigin
@RestController
@RequestMapping("/agency/trips")
public class AgencyController {
    private static final String template = "Hello, %s!";
    @Autowired
    private TripRepository tripRepository;

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value=" USER", defaultValue="World") String name) {
        return String.format(template, name);
    }
    @RequestMapping( method= RequestMethod.GET)
    public Trip[] getAll(){
        System.out.println("Get all trips..");
        List<Trip> tripList =  StreamSupport.stream(tripRepository.findAll().spliterator(), false).collect(Collectors.toList());
        return tripList.toArray(new Trip[tripList.size()]);}
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable String id){

        System.out.println("Get trip by id ..");
        Trip user=tripRepository.findOne(Long.parseLong(id));
        System.out.println("FOUND TRIP: "+user);
        if (user==null)
            return new ResponseEntity<>("Trip not found", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Trip>(user, HttpStatus.OK);
    }
    //@CrossOrigin(origins = "http://localhost:3000/")
    @RequestMapping(method = RequestMethod.POST)
    public Trip create(@RequestBody Trip trip){
        try {
            System.out.println("trip to save: "+trip);
            tripRepository.save(trip);
            System.out.println("trip: "+trip+" was added");
            return trip;
        } catch (ValidationException|RepoException  ex) {
            System.out.println("Trip addition fail..");
            ex.printStackTrace();
            //return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return null;

    }
    @RequestMapping(value = "/{id}",method =  RequestMethod.PUT)
    public Trip update(@RequestBody Trip trip){
        try {
            Trip tripAdded = tripRepository.update(trip);
            return tripAdded;
        } catch (ValidationException|RepoException  ex) {
            System.out.println("Trip update fail..");
            ex.printStackTrace();
            //return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return null;

    }
    @RequestMapping(value="/{id}", method= RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable String id){
        System.out.println("Deleting trip ... "+id);
        try {
            tripRepository.delete(Long.parseLong(id));
            return new ResponseEntity<Trip>(HttpStatus.OK);
        }catch ( RepoException ex){
            System.out.println("Ctrl Delete trip exception");
            return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping("/{id}/tripName")
    public String name(@PathVariable String id){
        Trip p = tripRepository.findOne(Long.parseLong(id));


        return p.getPlace();
    }
    @ExceptionHandler(RepoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userRepoError(RepoException e) {
        return e.getMessage();
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userValidError(ValidationException e) {
        return e.getMessage();
    }
}
