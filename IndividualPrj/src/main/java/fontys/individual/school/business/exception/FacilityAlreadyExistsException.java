package fontys.individual.school.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FacilityAlreadyExistsException extends ResponseStatusException {
    public FacilityAlreadyExistsException(){super(HttpStatus.BAD_REQUEST,"Facility already exists");}
}
