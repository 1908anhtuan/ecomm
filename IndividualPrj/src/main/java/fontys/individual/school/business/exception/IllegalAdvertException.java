package fontys.individual.school.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class IllegalAdvertException extends ResponseStatusException {
    public IllegalAdvertException(){super(HttpStatus.BAD_REQUEST,"Please try again later");}
}
