package ec.com.tecnointel.soem.contabilidad.registroInt;

import java.util.List;

import ec.com.tecnointel.soem.contabilidad.modelo.TranPlantilla;
import jakarta.ejb.Local;

@Local
public interface TransaccionGestionInt {
	
	public Integer gestionarTranPlantilla(List<TranPlantilla> tranPlantillas) throws Exception;

	public Integer anularTransaccion(Integer transaccionId) throws Exception;

}

