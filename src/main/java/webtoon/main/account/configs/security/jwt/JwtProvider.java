package webtoon.main.account.configs.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.io.Serializable;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Function;

public class JwtProvider implements Serializable {
	private static final long serialVersionUID = -2550185165626007488L;
	public static final Long JWT_TOKEN_VALIDITY = 1800L; // 30 mins

	private final String secretKey; // secret key

	public JwtProvider( String secret) throws Exception {
		if (secret == null)
			throw new Exception("jwt secrete must not be null.");
		else if (secret.length() < 20)
			throw new Exception("jwt secrete must greater than 20 characters.");
		this.secretKey = Keys.class.getName() + SignatureAlgorithm.HS512
				+ Base64.getEncoder().encodeToString(secret.getBytes());
	}

	// Get token expired date
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	// Get token's claim
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	// Get all claims from token
	private Claims getAllClaimsFromToken(String token) {
		return (Claims) Jwts.parserBuilder()
				.setSigningKey(Keys.hmacShaKeyFor(Base64.getEncoder().encode(secretKey.getBytes()))).build()
				.parse(token).getBody();
	}

	// Check if token expired
	public Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(Calendar.getInstance().getTime());
	}

	// Generate new token with username
	public String generateToken(String username, long invalidTime) {
		return doGenerateToken(username, invalidTime == 0 ? JWT_TOKEN_VALIDITY : invalidTime);
	}

	// Generate new token
	private String doGenerateToken(String subject, long timeAvail) {
		return Jwts.builder().setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + timeAvail * 1000))
				.signWith(Keys.hmacShaKeyFor(Base64.getEncoder().encode(this.secretKey.getBytes()))).compact();
	}

	// Get username from token
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

}
