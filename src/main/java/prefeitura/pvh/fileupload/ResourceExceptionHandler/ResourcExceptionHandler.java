package prefeitura.pvh.fileupload.ResourceExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import prefeitura.pvh.fileupload.service.exception.ImageServiceException;


@ControllerAdvice
public class ResourcExceptionHandler {
    @ExceptionHandler(ImageServiceException.class)
    public ResponseEntity<String> handleImageServiceException(ImageServiceException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }

}