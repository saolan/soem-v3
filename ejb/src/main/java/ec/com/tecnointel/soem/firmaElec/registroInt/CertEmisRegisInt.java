package ec.com.tecnointel.soem.firmaElec.registroInt;

import ec.com.tecnointel.soem.firmaElec.modelo.CertEmis;
import jakarta.ejb.Local;

@Local
public interface CertEmisRegisInt {
	
	public Object insertar(CertEmis certEmis) throws Exception;

	public void modificar(CertEmis certEmis) throws Exception;

	public void eliminar(CertEmis certEmis) throws Exception;

	public CertEmis buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
