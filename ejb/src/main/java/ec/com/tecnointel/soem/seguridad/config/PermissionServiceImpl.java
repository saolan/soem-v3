package ec.com.tecnointel.soem.seguridad.config;

import java.util.Map;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Implementación de ejemplo: consulta permisos desde tablas de usuarios/roles.
 * Reemplaza con tus entidades reales (Usuario, Rol, Permiso, etc.).
 */
@ApplicationScoped
public class PermissionServiceImpl implements PermissionService {

	@PersistenceContext
	EntityManager em;

//	@Override
//	public Set<String> permissionsOf(String username) {
//// TODO: Implementar consulta real a tu modelo de seguridad.
//// Se retorna un mock seguro por defecto (sólo lectura) para no abrir permisos por accidente.
//		Set<String> perms = new HashSet<>();
////		perms.add("caja:read");
//		perms.add("caja:create");
//		perms.add("caja:delete");
//		perms.add("caja:update");
//		return perms;
//	}

	// Mock por usuario: admin con create; viewer solo read
	private static final Map<String, Set<String>> PERMISSIONS_BY_USER = Map.of("anonymous",
			Set.of("caja:read", "caja:create"), "viewer", Set.of("caja:read"));

	@Override
	public Set<String> permissionsOf(String username) {
		return PERMISSIONS_BY_USER.getOrDefault(username, Set.of());
	}
}
