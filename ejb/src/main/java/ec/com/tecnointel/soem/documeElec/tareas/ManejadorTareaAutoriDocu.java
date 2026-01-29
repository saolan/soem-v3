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
public class ManejadorTareaAutoriDocu {
	
	@Resource
    ManagedScheduledExecutorService managedScheduledExecutorService;
    
    @Inject
    Instance<TareaAutoriDocu> tareaAutoriDocuInstance;    
    
    @Inject
    ParametroRegisInt parametroRegis;
    
    public void ejecutarTareaAutoriDocu(Object object, File archivoFirmado, Sucursal sucursal, String codDoc, String claveAcceso, 
    		String proxyIp, String proxyPuerto, String ambiente, String urlProduccion, String urlPruebas, String nombreServicio) throws ExecutionException, InterruptedException {
    	
    	TareaAutoriDocu tareaAutoriDocu = tareaAutoriDocuInstance.get();
    	
    	tareaAutoriDocu.setObject(object);
    	tareaAutoriDocu.setArchivoFirmado(archivoFirmado);
    	tareaAutoriDocu.setSucursal(sucursal);
    	tareaAutoriDocu.setCodDoc(codDoc);
    	tareaAutoriDocu.setClaveAcceso(claveAcceso);
    	tareaAutoriDocu.setProxyIp(proxyIp);
    	tareaAutoriDocu.setProxyPuerto(proxyPuerto);
    	tareaAutoriDocu.setAmbiente(ambiente);
    	tareaAutoriDocu.setUrlProduccion(urlProduccion);
    	tareaAutoriDocu.setUrlPruebas(urlPruebas);
    	tareaAutoriDocu.setNombreServicio(nombreServicio);
    	tareaAutoriDocu.setRutaAutorizados(this.buscarRutaAutori());
    	tareaAutoriDocu.setRutaFirmados(this.buscarRutaFirmados());
    	
    	Integer esperaAutori = buscarEsperaAutori();
    	
    	managedScheduledExecutorService.schedule(tareaAutoriDocu, esperaAutori, TimeUnit.SECONDS);

//    	System.out.println("<<<<<<<<<<<<<<<<<<<<<<< " + LocalDateTime.now());
//        try {
//            Thread.sleep(2000); // Dormir el hilo principal para permitir que la tarea se ejecute
//            System.out.println("<<<<<<<<<<<<<<<<<<<<<<< Durmiendo");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println("<<<<<<<<<<<<<<<<<<<<<<< " + LocalDateTime.now());
    }
    
    public Integer buscarEsperaAutori() {
    	
    	Parametro parametro = new Parametro();
    	try {
			parametro = parametroRegis.buscarPorId(Parametro.class,3201);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return Integer.parseInt(parametro.getDescri());
    }
    
    public String buscarRutaFirmados() {
    	
    	Parametro parametro = new Parametro();
    	try {
			parametro = parametroRegis.buscarPorId(Parametro.class,3241);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return parametro.getDescri();
    }
    
    public String buscarRutaAutori() {
    	
    	Parametro parametro = new Parametro();
    	try {
			parametro = parametroRegis.buscarPorId(Parametro.class,3242);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return parametro.getDescri();
    }
}
