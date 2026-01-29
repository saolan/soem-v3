package ec.com.tecnointel.soem.parametro.registroInt;

import ec.com.tecnointel.soem.parametro.modelo.SucuCertEmis;
import jakarta.ejb.Local;

@Local
public interface SucuCertEmisRegisInt {
	
	public Object insertar(SucuCertEmis sucuCertEmis) throws Exception;

	public void modificar(SucuCertEmis sucuCertEmis) throws Exception;

	public void eliminar(SucuCertEmis sucuCertEmis) throws Exception;

	public SucuCertEmis buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
