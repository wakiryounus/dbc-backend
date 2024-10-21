package k.wakir.dbcbackend.exception;

import k.wakir.dbcbackend.model.ServerMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class ValidationExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ServerMessage> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = Objects.requireNonNull(ex.getBindingResult()
                        .getFieldError()).getDefaultMessage();
        return new ResponseEntity<>(new ServerMessage(errorMessage, null), HttpStatus.BAD_REQUEST);
    }
}
