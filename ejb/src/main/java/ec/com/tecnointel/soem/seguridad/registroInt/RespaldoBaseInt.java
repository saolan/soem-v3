package ec.com.tecnointel.soem.seguridad.registroInt;

import jakarta.ejb.Local;

@Local
public interface RespaldoBaseInt {

	public boolean respaldar(String host, String puerto, String usuario, String clave, String formato,
			String base, String rutaRespaldo, String rutaPgDump) throws Exception;

}
