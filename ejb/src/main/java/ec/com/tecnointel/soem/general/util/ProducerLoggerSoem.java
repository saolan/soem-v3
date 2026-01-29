package ec.com.tecnointel.soem.general.util;

import java.util.logging.Logger;

import ec.com.tecnointel.soem.general.interfac.LoggerSoem;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;

public class ProducerLoggerSoem {

	@Produces
	@LoggerSoem
	public Logger produceLog(InjectionPoint injectionPoint) {
		return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
	}

}
