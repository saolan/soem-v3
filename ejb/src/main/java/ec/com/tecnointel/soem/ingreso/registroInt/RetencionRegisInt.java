package ec.com.tecnointel.soem.ingreso.registroInt;

import ec.com.tecnointel.soem.ingreso.modelo.Retencion;
import jakarta.ejb.Local;

@Local
public interface RetencionRegisInt {

	public Object insertar(Retencion retencion) throws Exception;

	public void modificar(Retencion retencion) throws Exception;

	public void eliminar(Retencion retencion) throws Exception;

	public Retencion buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
