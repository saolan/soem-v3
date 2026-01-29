package ec.com.tecnointel.soem.seguridad.config;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.security.enterprise.SecurityContext;

/**
 * Valida permisos de negocio en métodos anotados con
 * {@link RequiresPermission}.
 */
@RequiresPermission("")
@Priority(Interceptor.Priority.APPLICATION)
@Interceptor
@Dependent
public class PermissionInterceptor {

	@Inject
	PermissionService permissionService;

	@Inject
	SecurityContext securityContext;

	@AroundInvoke
	public Object check(InvocationContext ctx) throws Exception {
		RequiresPermission ann = ctx.getMethod().getAnnotation(RequiresPermission.class);
		if (ann == null) {
			ann = ctx.getTarget().getClass().getAnnotation(RequiresPermission.class);
		}
		if (ann != null) {
			String user = (securityContext != null && securityContext.getCallerPrincipal() != null)
					? securityContext.getCallerPrincipal().getName()
					: "anonymous";

			if (!permissionService.has(user, ann.value())) {
				throw new SecurityException("Acceso denegado: falta el permiso '" + ann.value() + "'");
			}
		}
		return ctx.proceed();
	}
}