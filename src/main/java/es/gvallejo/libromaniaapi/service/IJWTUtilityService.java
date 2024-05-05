package es.gvallejo.libromaniaapi.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;

public interface IJWTUtilityService {
	// Función que genera el token con los claims (contiene en ellos el email del
	// usuario) y la firma
	String generateJWT(String emailUsuario)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, JOSEException;

	// Función que valida la vericidad del token
	JWTClaimsSet parseJWT(String jwt)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, JOSEException, ParseException;
}
