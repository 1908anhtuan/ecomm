package fontys.individual.school.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AdvertNotFoundException extends ResponseStatusException {
    public AdvertNotFoundException(){super(HttpStatus.BAD_REQUEST,"Advert not found");}
}
