package es.gvallejo.libromaniaapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/echo")
public class EchoController {
	@GetMapping
	public ResponseEntity<String> echo() {
		return new ResponseEntity<>("OK", HttpStatus.OK);
	}

}