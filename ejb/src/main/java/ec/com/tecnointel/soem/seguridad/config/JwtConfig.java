package ec.com.tecnointel.soem.seguridad.config;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JwtConfig {
	
	@ConfigProperty(name = "jwt.secret.key.active")
	private Optional<String> activeKey;

	@ConfigProperty(name = "jwt.secret.key.previous")
	private Optional<String> previousKey;

	@ConfigProperty(name = "jwt.expiration.ms", defaultValue = "3600000")
	private long expirationTime;

	public String getActiveKey() {
		String key = activeKey
				.orElseThrow(() -> new IllegalStateException("No se ha configurado jwt.secret.key.active"));
		validarLongitud(key);
		return key;
	}

	public Optional<String> getPreviousKey() {
		previousKey.ifPresent(this::validarLongitudSilenciosa);
		return previousKey;
	}

	public long getExpirationTime() {
		return expirationTime;
	}

	private void validarLongitud(String key) {
		if (key.getBytes(StandardCharsets.UTF_8).length < 32) {
			throw new IllegalArgumentException("La clave JWT debe tener al menos 32 bytes");
		}
	}

	private void validarLongitudSilenciosa(String key) {
		if (key != null && key.getBytes(StandardCharsets.UTF_8).length < 32) {
			System.err.println("Advertencia: jwt.secret.key.previous es demasiado corta y será ignorada");
		}
	}
}
