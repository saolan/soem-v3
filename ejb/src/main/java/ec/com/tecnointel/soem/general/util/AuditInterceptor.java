package ec.com.tecnointel.soem.general.util;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;


/*
 * Cuando se quiera usar este interceptor se coloca @AuditInterceptorInt
 * en la clase para que afecte a todos los metodos o en algún metodo de la clase
 * para el ejemplo esta en GestorRegisSoem
 */
@AuditInterceptorInt
@Interceptor
public class AuditInterceptor {

	@AroundInvoke
	private Object intercept(InvocationContext ic) throws Exception {
		
		System.out.println(ic.getTarget().getClass().getName());
		System.out.println(ic.getMethod().getName());

//		logger.entering(ic.getTarget().getClass().getName(), ic.getMethod().getName());
		try {
			return ic.proceed();
		} finally {

//			logger.exiting(ic.getTarget().getClass().getName(), ic.getMethod().getName());
			System.out.println(ic.getTarget().getClass().getName());
			System.out.println(ic.getMethod().getName());

			Object[] parameters = ic.getParameters();

			if (parameters.length > 0) {

				System.out.println(parameters[0].toString());
				System.out.println(parameters.toString());

			}
		}
	}
}