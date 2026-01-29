package ec.com.tecnointel.soem.caja.registroInt;

import ec.com.tecnointel.soem.caja.modelo.SaliArch;
import jakarta.ejb.Local;

@Local
public interface SaliArchRegisInt {

	public Object insertar(SaliArch saliArch) throws Exception;

	public void modificar(SaliArch saliArch) throws Exception;

	public void eliminar(SaliArch saliArch) throws Exception;

	public SaliArch buscarPorId(Class<?> entidad, Integer id) throws Exception;
	
}
