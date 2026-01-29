package ec.com.tecnointel.soem.ingreso.registroInt;

import ec.com.tecnointel.soem.ingreso.modelo.ProvGrup;
import jakarta.ejb.Local;

@Local
public interface ProvGrupRegisInt {

	public Object insertar(ProvGrup provGrup) throws Exception;

	public void modificar(ProvGrup provGrup) throws Exception;

	public void eliminar(ProvGrup provGrup) throws Exception;

	public ProvGrup buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
