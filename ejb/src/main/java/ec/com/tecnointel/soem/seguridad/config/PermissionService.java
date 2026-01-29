package ec.com.tecnointel.soem.seguridad.config;

import java.util.Set;

/** Define acceso a permisos consultados desde BD. */
public interface PermissionService {
	/**
	 * @param username usuario autenticado
	 * @return conjunto de permisos (p.ej. "caja:create", "caja:read")
	 */
	Set<String> permissionsOf(String username);

	/** atajo */
	default boolean has(String username, String permission) {
		return permissionsOf(username).contains(permission);
	}
}
