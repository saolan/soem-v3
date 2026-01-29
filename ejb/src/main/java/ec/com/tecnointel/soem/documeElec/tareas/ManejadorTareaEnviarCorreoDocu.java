package ec.com.tecnointel.soem.documeElec.tareas;

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
public class ManejadorTareaEnviarCorreoDocu {
	
	@Resource
    ManagedScheduledExecutorService managedScheduledExecutorService;
    
    @Inject
    Instance<TareaEnviarCorreoDocu> tareaEnviarCorreoDocuInstance;    
    
    @Inject
    ParametroRegisInt parametroRegis;
    
    public void ejecutarTareaEnviarCorreoDocu(String destinatarios, String claveAcceso) throws ExecutionException, InterruptedException {
    	
    	TareaEnviarCorreoDocu tareaEnviarCorreoDocu = tareaEnviarCorreoDocuInstance.get();
    	
    	tareaEnviarCorreoDocu.setDestinatarios(destinatarios);
    	tareaEnviarCorreoDocu.setClaveAcceso(claveAcceso);
    	
    	Integer esperaEnvio = buscarEsperaEnvio();
    	
    	managedScheduledExecutorService.schedule(tareaEnviarCorreoDocu, esperaEnvio, TimeUnit.SECONDS);

    }
    
    public Integer buscarEsperaEnvio() {
    	
    	Parametro parametro = new Parametro();
    	
    	try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 3202);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return Integer.parseInt(parametro.getDescri());
    }
}
