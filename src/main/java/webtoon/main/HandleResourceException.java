package webtoon.main;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import webtoon.main.utils.exception.CustomHandleException;
import webtoon.main.utils.exception.ResponseDto;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class HandleResourceException {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> inValidArguments(MethodArgumentNotValidException ex) {
		ex.printStackTrace();
		Map<Object, String> errors = new HashMap<>();
		// collect errors
		for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}
		return ResponseEntity.badRequest().body(ResponseDto.of(1, errors));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> resolverException(HttpMessageNotReadableException ex) {
		return ResponseEntity.badRequest().body(ResponseDto.of(2, ex.getMessage()));
	}

	@ExceptionHandler(CustomHandleException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseDto handleError(CustomHandleException ex) {
		ex.printStackTrace();
		return ResponseDto.ofError(ex.getCode());
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseDto handleLackPermission(AccessDeniedException ex) {
		ex.printStackTrace();
		return ResponseDto.ofError(5);
	}
}
