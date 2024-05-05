package es.gvallejo.libromaniaapi.security;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;

import es.gvallejo.libromaniaapi.service.IJWTUtilityService;
import es.gvallejo.libromaniaapi.util.LibroManiaConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthorizationFilter extends OncePerRequestFilter {
	
	@Autowired
	private IJWTUtilityService jwtUtilityService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String header = request.getHeader(LibroManiaConstants.AUTHORIZATION);
		if (StringUtils.isBlank(header)
				|| Boolean.FALSE.equals(header.startsWith(LibroManiaConstants.BEARER.concat(StringUtils.SPACE)))) {
			filterChain.doFilter(request, response);
			return;
		}
		String token = header.substring(7);
		try {
			JWTClaimsSet claimsSet = jwtUtilityService.parseJWT(token);

			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					claimsSet.getSubject(), null, Collections.emptyList());

			SecurityContextHolder.getContext().setAuthentication(authenticationToken);

		} catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException | ParseException | JOSEException e) {
			throw new RuntimeException(e);
		}
		filterChain.doFilter(request, response);

	}

}
