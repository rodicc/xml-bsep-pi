package ftn.xmlwebservisi.firme.xmlSignatureAndEncryption;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.UUID;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenService {
	private final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.RS256;
	private final long EXPIRES_IN = 300000; // 5m

	public String generateToken(String subject, PrivateKey privateKey) {
		return Jwts.builder()
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRES_IN))
				.setId(UUID.randomUUID().toString())
				.signWith(SIGNATURE_ALGORITHM, privateKey)
				.compact();
	}
	
	public Claims getClaims(String token, PublicKey publicKey) {
		Claims claims;
		try {
			claims = Jwts.parser()
						.setSigningKey(publicKey)
						.parseClaimsJws(token)
						.getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}
	
	
	
	public void testToken(String subject, PrivateKey privateKey, PublicKey publicKey) {
		String token = generateToken(subject, privateKey);
		System.out.println("*************************");
		System.out.println(token);
		System.out.println("*************************");
		System.out.println("-----BEGIN PUBLIC KEY-----");
		System.out.println(publicKey.toString());
		System.out.println("-----END PUBLIC KEY-----");
		System.out.println("-----BEGIN RSA PRIVATE KEY-----");
		System.out.println(privateKey.toString());
		System.out.println("-----END RSA PRIVATE KEY-----");
		Claims claims = getClaims(token, publicKey);
		if(claims != null) {
			
			System.out.println(claims.getSubject());
			System.out.println(claims.getIssuedAt().toString());
			System.out.println(claims.getExpiration().toString());
			System.out.println(claims.getId());
		}
	}
}
