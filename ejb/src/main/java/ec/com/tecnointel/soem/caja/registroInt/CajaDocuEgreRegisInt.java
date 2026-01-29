package ec.com.tecnointel.soem.caja.registroInt;

import ec.com.tecnointel.soem.caja.modelo.CajaDocuEgre;
import jakarta.ejb.Local;

@Local
public interface CajaDocuEgreRegisInt {

	public Object insertar(CajaDocuEgre cajaDocuEgre) throws Exception;

	public void modificar(CajaDocuEgre cajaDocuEgre) throws Exception;

	public void eliminar(CajaDocuEgre cajaDocuEgre) throws Exception;

	public CajaDocuEgre buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
