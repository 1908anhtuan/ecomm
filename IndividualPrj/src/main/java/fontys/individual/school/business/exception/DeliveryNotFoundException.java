package fontys.individual.school.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DeliveryNotFoundException extends ResponseStatusException {
    public DeliveryNotFoundException(){super(HttpStatus.BAD_REQUEST,"Delivery not found");}

}
