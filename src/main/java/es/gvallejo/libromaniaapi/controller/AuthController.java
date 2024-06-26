package es.gvallejo.libromaniaapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@PostMapping("/login")
	public ResponseEntity<String> login() {
		return new ResponseEntity<>("OK", HttpStatus.OK);
	}

}
