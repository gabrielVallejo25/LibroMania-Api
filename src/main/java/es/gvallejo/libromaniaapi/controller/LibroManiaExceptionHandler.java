package es.gvallejo.libromaniaapi.controller;

import java.util.Arrays;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.gvallejo.libromaniaapi.dto.error.LibroManiaError;
import es.gvallejo.libromaniaapi.dto.exception.LibroManiaException;

@ControllerAdvice
@RestController
@Configuration
public class LibroManiaExceptionHandler {

	@ExceptionHandler(value = { LibroManiaException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<LibroManiaError> handleLoginException(Exception ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new LibroManiaError(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), Arrays.asList(ex.getMessage())));
	}

}
