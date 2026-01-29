package ec.com.saolan.soem.asistencia.registroImp;

import java.io.Serializable;

import ec.com.saolan.soem.asistencia.modelo.Horario;
import ec.com.saolan.soem.asistencia.registroInt.HorarioRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;

@Stateless
public class HorarioRegisImp extends GestorRegisSoem<Horario>
		implements
			HorarioRegisInt,
			Serializable {

	private static final long serialVersionUID = -1280952879990834453L;

}
