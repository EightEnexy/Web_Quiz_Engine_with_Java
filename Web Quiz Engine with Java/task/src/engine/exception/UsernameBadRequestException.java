package engine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UsernameBadRequestException extends RuntimeException {
    public UsernameBadRequestException(String msg){
        super(msg);
    }
}
