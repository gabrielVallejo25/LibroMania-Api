package es.gvallejo.libromaniaapi.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.util.StandardCharset;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import es.gvallejo.libromaniaapi.service.IJWTUtilityService;
import es.gvallejo.libromaniaapi.util.LibroManiaConstants;

@Service
@Transactional(readOnly = true)
public class JWTUtilityService implements IJWTUtilityService {

	@Value("classpath:jwtKeys/private_key.pem")
	private Resource privateKeyResource;

	@Value("classpath:jwtKeys/public_key.pem")
	private Resource publicKeyResource;

	@Override
	public String generateJWT(String emailUsuario)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, JOSEException {
		PrivateKey privateKey = loadPrivateKey(privateKeyResource);
		JWSSigner signer = new RSASSASigner(privateKey);
		Date now = new Date();
		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder().subject(emailUsuario).issueTime(now)
				.expirationTime(new Date(now.getTime() + LibroManiaConstants.FOUR_HOURS_IN_MILIS.intValue())).build();

		SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claimsSet);
		signedJWT.sign(signer);

		return signedJWT.serialize();
	}

	@Override
	public JWTClaimsSet parseJWT(String jwt)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, JOSEException, ParseException {
		SignedJWT signedJWT = SignedJWT.parse(jwt);
		PublicKey publicKey = loadPublicKey(publicKeyResource);
		JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey) publicKey);
		if (Boolean.FALSE.equals(signedJWT.verify(verifier))) {
			throw new JOSEException("Invalid signature");
		}
		JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
		if (claimsSet.getExpirationTime().before(new Date())) {
			throw new JOSEException("Expired token");
		}
		return claimsSet;
	}

	private PrivateKey loadPrivateKey(Resource resource)
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] keyBytes = Files.readAllBytes(Paths.get(resource.getURI()));
		String privateKeyPEM = new String(keyBytes, StandardCharset.UTF_8)
				.replace(LibroManiaConstants.BEGIN_PRIVATE_KEY, StringUtils.EMPTY)
				.replace(LibroManiaConstants.END_PRIVATE_KEY, StringUtils.EMPTY).replaceAll("\\s", StringUtils.EMPTY);
		byte[] decodedKey = Base64.getDecoder().decode(privateKeyPEM);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decodedKey));
	}

	private PublicKey loadPublicKey(Resource resource)
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] keyBytes = Files.readAllBytes(Paths.get(resource.getURI()));
		String publicKeyPEM = new String(keyBytes, StandardCharset.UTF_8)
				.replace(LibroManiaConstants.BEGIN_PUBLIC_KEY, StringUtils.EMPTY)
				.replace(LibroManiaConstants.END_PUBLIC_KEY, StringUtils.EMPTY).replaceAll("\\s", StringUtils.EMPTY);
		byte[] decodedKey = Base64.getDecoder().decode(publicKeyPEM);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));

	}
}
