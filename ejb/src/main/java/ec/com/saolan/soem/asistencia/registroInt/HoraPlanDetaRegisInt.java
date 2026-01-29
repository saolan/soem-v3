package ec.com.saolan.soem.asistencia.registroInt;

import ec.com.saolan.soem.asistencia.modelo.HoraPlanDeta;
import jakarta.ejb.Local;

@Local
public interface HoraPlanDetaRegisInt {

	public Object insertar(HoraPlanDeta horaPlanDeta) throws Exception;

	public void modificar(HoraPlanDeta horaPlanDeta) throws Exception;

	public void eliminar(HoraPlanDeta horaPlanDeta) throws Exception;

	public HoraPlanDeta buscarPorId(Class<?> entidad, Integer id) throws Exception;

}
