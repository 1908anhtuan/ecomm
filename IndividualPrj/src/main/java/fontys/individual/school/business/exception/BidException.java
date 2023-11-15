package fontys.individual.school.business.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BidException extends ResponseStatusException {
    public BidException(){super(HttpStatus.BAD_REQUEST,"Something wrong has happened");}
}
