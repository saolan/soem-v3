package ec.com.saolan.soem.asistencia.registroImp;

import java.io.Serializable;

import ec.com.saolan.soem.asistencia.modelo.Dispositivo;
import ec.com.saolan.soem.asistencia.registroInt.DispositivoRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;

@Stateless
public class DispositivoRegisImp extends GestorRegisSoem<Dispositivo> implements DispositivoRegisInt, Serializable {

	private static final long serialVersionUID = -2208082008103065530L;

}
