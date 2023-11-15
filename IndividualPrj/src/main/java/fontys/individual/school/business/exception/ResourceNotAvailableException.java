package fontys.individual.school.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ResourceNotAvailableException extends ResponseStatusException {
    public ResourceNotAvailableException(){super(HttpStatus.BAD_REQUEST,"Resource is not available");}
}

