package ec.com.tecnointel.soem.firmaElec.registroInt;

import ec.com.tecnointel.soem.parametro.modelo.SucuCertEmis;
import jakarta.ejb.Local;

@Local
public interface FirmarArchivoInt {
	
	void signBes(SucuCertEmis sucuCertEmis, String nombreArchivo, String rutaGenerados, String rutaFirmado) throws Exception;
	
}
