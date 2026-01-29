package ec.com.tecnointel.soem.documeElec.tareas;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.registroInt.ParametroRegisInt;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.enterprise.concurrent.ManagedScheduledExecutorService;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@Stateless
public class ManejadorTareaEnviarHoraFija {
	
//	Este recurso se utiliza para realiza tareas en segundo plano inmediata
//	@Resource
//	ManagedExecutorService managedExecutorService;
        
//	Este recurso de utiliza para hacer tareas luego de un tiempo determinado (Timer)
//	managedScheduledExecutorService.schedule; ejecuta tareas una sola
//	managedScheduledExecutorService.scheduleAtFixedRate; ejecuta tareas periodicas
//	managedScheduledExecutorService.scheduleWithFixedDelay; ejecuta tareas periodicas
	@Resource
    ManagedScheduledExecutorService managedScheduledExecutorService;
    
    @Inject
    Instance<TareaEnviarHoraFija> tareaEnviarDocuInstance;    
    
    @Inject
    ParametroRegisInt parametroRegis;
    
    public void ejecutarTareaEnviarDocu() throws ExecutionException, InterruptedException {
    	
    	TareaEnviarHoraFija tareaEnviarDocuLote = tareaEnviarDocuInstance.get();
    	
//    	Fecha y Hora Actual
		LocalDateTime fechaHoraActual = LocalDateTime.now();
		
//    	Hora para procesar facturas		
		LocalDateTime horaProcesar = LocalDateTime.now().withHour(23).withMinute(00).withSecond(0);

//		Aumentar un dia en caso que la computadora se prenda 
//		luego de la hora en que se van a procesar las facturas
		if(fechaHoraActual.compareTo(horaProcesar) > 0)
			horaProcesar = horaProcesar.plusDays(1);

//		Activar envio Metodo 1
//		Se usa Duration para sacar la diferencia entre fechas y luego obtener los minutos
//		Duration duration = Duration.between(fechaHoraActual, horaProcesar);
//		long tiempoEspera = duration.toMinutes();
		
//		TimeUnit.DAYS.toMinutes(1); Convierte un dia a minutos que seria 1440 minutos
//		managedScheduledExecutorService.scheduleAtFixedRate(tareaEnviarDocuLote, tiempoEspera, TimeUnit.DAYS.toMinutes(1), TimeUnit.MINUTES);
//		managedScheduledExecutorService.scheduleAtFixedRate(tareaEnviarDocuLote, tiempoEspera, 1440, TimeUnit.MINUTES);

//		Activar envio Metodo 2
//		Se usa Duration para sacar la diferencia entre fechas
		long tiempoEspera = ChronoUnit.MINUTES.between(fechaHoraActual, horaProcesar);
		System.out.println("tiempo en que se lanzara el evento:" + tiempoEspera);
		System.out.println("tiempo en que se lanzara el evento:" + tiempoEspera);
		System.out.println("tiempo en que se lanzara el evento:" + tiempoEspera);
		System.out.println("tiempo en que se lanzara el evento:" + tiempoEspera);
		
//		TimeUnit.DAYS.toMinutes(1); Convierte un dia a minutos que seria 1440 minutos
//		managedScheduledExecutorService.scheduleAtFixedRate(tareaEnviarDocuLote, tiempoEspera, 1, TimeUnit.MINUTES);
		managedScheduledExecutorService.scheduleAtFixedRate(tareaEnviarDocuLote, 1, 1, TimeUnit.MINUTES);
    }
    
    public Integer buscarHoraEnvio() {
    	
    	Parametro parametro = new Parametro();
    	
//    	Cambiar el parametro con la hora que se quiere enviar las facturas
    	try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 3200);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return Integer.parseInt(parametro.getDescri());
    }
}
