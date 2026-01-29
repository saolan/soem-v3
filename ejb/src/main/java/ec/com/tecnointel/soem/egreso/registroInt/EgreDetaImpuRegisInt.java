package ec.com.tecnointel.soem.egreso.registroInt;

import ec.com.tecnointel.soem.egreso.modelo.EgreDetaImpu;
import jakarta.ejb.Local;

@Local
public interface EgreDetaImpuRegisInt {

	public Object insertar(EgreDetaImpu egreDetaImpu) throws Exception;

	public void modificar(EgreDetaImpu egreDetaImpu) throws Exception;

	public void eliminar(EgreDetaImpu egreDetaImpu) throws Exception;

	public EgreDetaImpu buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
