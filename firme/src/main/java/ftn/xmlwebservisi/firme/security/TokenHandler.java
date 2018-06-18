package ftn.xmlwebservisi.firme.security;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenHandler {

	private final String SECRET_KEY = "myownsecretkey";
	private final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
	private final long EXPIRES_IN = 600000; // 10 minutes

	public TokenHandler() {
	}

	public String generateToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRES_IN))
				.signWith(SIGNATURE_ALGORITHM, SECRET_KEY).compact();
	}

	public Claims getClaims(String authToken) {
		Claims claims;
		try {
			claims = Jwts.parser()
						.setSigningKey(SECRET_KEY)
						.parseClaimsJws(authToken)
						.getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}

	public String getUsername(String authToken) {
		String username;
		try {
			final Claims claims = getClaims(authToken);
			username = claims.getSubject();
		} catch (Exception e) {
			username = null;
		}
		return username;
	}
}
