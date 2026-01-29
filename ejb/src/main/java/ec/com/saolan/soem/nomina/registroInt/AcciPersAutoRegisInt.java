package ec.com.saolan.soem.nomina.registroInt;

import ec.com.saolan.soem.nomina.modelo.AcciPersAuto;
import jakarta.ejb.Local;

@Local
public interface AcciPersAutoRegisInt {

	public Object insertar(AcciPersAuto acciPersAuto) throws Exception;

	public void modificar(AcciPersAuto acciPersAuto) throws Exception;

	public void eliminar(AcciPersAuto acciPersAuto) throws Exception;

	public AcciPersAuto buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
