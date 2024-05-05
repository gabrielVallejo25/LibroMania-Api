package es.gvallejo.libromaniaapi.dto.error;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LibroManiaError {
	private int status;
	private String message;
	private List<String> errors;
}
