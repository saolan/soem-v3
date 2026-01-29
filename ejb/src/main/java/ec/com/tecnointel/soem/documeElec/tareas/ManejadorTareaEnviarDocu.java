package ec.com.tecnointel.soem.documeElec.tareas;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.parametro.registroInt.ParametroRegisInt;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.enterprise.concurrent.ManagedScheduledExecutorService;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@Stateless
public class ManejadorTareaEnviarDocu {
	
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
    Instance<TareaEnviarDocu> tareaEnviarDocuInstance;    
    
    @Inject
    ParametroRegisInt parametroRegis;
    
    public void ejecutarTareaEnviarDocu(Object object, File archivoFirmado, Sucursal sucursal, String codDoc, String claveAcceso, 
    		String proxyIp, String proxyPuerto, String ambiente, String urlProduccion, String urlPruebas, String nombreServicio) throws ExecutionException, InterruptedException {
    	
    	TareaEnviarDocu tareaEnviarDocu = tareaEnviarDocuInstance.get();
    	
    	tareaEnviarDocu.setObject(object);
    	tareaEnviarDocu.setArchivoFirmado(archivoFirmado);
    	tareaEnviarDocu.setSucursal(sucursal);
    	tareaEnviarDocu.setCodDoc(codDoc);
    	tareaEnviarDocu.setClaveAcceso(claveAcceso);
    	tareaEnviarDocu.setProxyIp(proxyIp);
    	tareaEnviarDocu.setProxyPuerto(proxyPuerto);
    	tareaEnviarDocu.setAmbiente(ambiente);
    	tareaEnviarDocu.setUrlProduccion(urlProduccion);
    	tareaEnviarDocu.setUrlPruebas(urlPruebas);
    	tareaEnviarDocu.setNombreServicio(nombreServicio);
    	
    	Integer esperaEnvio = buscarEsperaEnvio();
    	
    	managedScheduledExecutorService.schedule(tareaEnviarDocu, esperaEnvio, TimeUnit.SECONDS);
//    	managedScheduledExecutorService.shutdown();
    	
//        for(int i=0; i<2; i++) {
//            TareaEnviarDocu tareaEnviarDocu = tareaEnviarDocuIntence.get();
//            this.managedExecutorService.submit(tareaEnviarDocu);  ejecuta la tarea
//        }
    }
    
    public Integer buscarEsperaEnvio() {
    	
    	Parametro parametro = new Parametro();
    	
    	try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 3200);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return Integer.parseInt(parametro.getDescri());
    }
}
