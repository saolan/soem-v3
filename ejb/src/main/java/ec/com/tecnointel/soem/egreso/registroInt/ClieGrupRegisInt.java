package ec.com.tecnointel.soem.egreso.registroInt;

import ec.com.tecnointel.soem.egreso.modelo.ClieGrup;
import jakarta.ejb.Local;

@Local
public interface ClieGrupRegisInt {

	public Object insertar(ClieGrup clieGrup) throws Exception;

	public void modificar(ClieGrup clieGrup) throws Exception;

	public void eliminar(ClieGrup clieGrup) throws Exception;

	public ClieGrup buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
