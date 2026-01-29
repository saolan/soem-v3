package ec.com.tecnointel.soem.seguridad.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class JwtUtil {

	@Inject
	JwtConfig config;

	/** Generar un JWT firmado con la clave activa */
	public String generarToken(String usuario, Set<String> roles, Set<String> permisos) {

		SecretKey secretKey = Keys.hmacShaKeyFor(config.getActiveKey().getBytes(StandardCharsets.UTF_8));

//		return Jwts.builder().setSubject(usuario)
//				.claim("roles", roles)
//				.claim("permisos", permisos)
//				.setIssuedAt(new Date())
//				.setExpiration(new Date(System.currentTimeMillis() + config.getExpirationTime()))
//				.signWith(key, SignatureAlgorithm.HS256)
//				.compact();

		return Jwts.builder().subject(usuario).claim("roles", roles).claim("permisos", permisos).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + config.getExpirationTime())).signWith(secretKey)
				.compact();
	}

	/** Validar token con soporte para rotación de clave */
	public boolean validarToken(String token) {
		try {
			parseClaims(token, config.getActiveKey()); // intenta con la clave activa
			return true;
		} catch (JwtException e1) {
			// intenta con la clave anterior (si existe)
			return config.getPreviousKey().map(prevKey -> {
				try {
					parseClaims(token, prevKey);
					return true;
				} catch (JwtException e2) {
					return false;
				}
			}).orElse(false);
		}
	}

	/** Obtener claims si el token es válido */
	public Claims obtenerClaims(String token) {
		try {
			return parseClaims(token, config.getActiveKey());
		} catch (JwtException e1) {
			return config.getPreviousKey().map(prevKey -> parseClaims(token, prevKey))
					.orElseThrow(() -> new JwtException("Token inválido o expirado"));
		}
	}

	private Claims parseClaims(String token, String keyValue) {
		SecretKey secretkey = Keys.hmacShaKeyFor(keyValue.getBytes(StandardCharsets.UTF_8));

//		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

		return Jwts.parser().verifyWith(secretkey) // reemplaza a setSigningKey
				.build().parseSignedClaims(token) // reemplaza a parseClaimsJws
				.getPayload();
	}
}
