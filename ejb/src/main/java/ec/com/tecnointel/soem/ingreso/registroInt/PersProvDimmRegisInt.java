package ec.com.tecnointel.soem.ingreso.registroInt;

import ec.com.tecnointel.soem.ingreso.modelo.PersProvDimm;
import jakarta.ejb.Local;

@Local
public interface PersProvDimmRegisInt {

	public Object insertar(PersProvDimm persProvDimm) throws Exception;

	public void modificar(PersProvDimm persProvDimm) throws Exception;

	public void eliminar(PersProvDimm persProvDimm) throws Exception;

	public PersProvDimm buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
