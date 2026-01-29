package ec.com.saolan.soem.asistencia.registroImp;

import java.io.Serializable;

import ec.com.saolan.soem.asistencia.modelo.EmplHoraPlan;
import ec.com.saolan.soem.asistencia.registroInt.EmplHoraPlanRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;

@Stateless
public class EmplHoraPlanRegisImp extends GestorRegisSoem<EmplHoraPlan>
		implements
			EmplHoraPlanRegisInt,
			Serializable {

	private static final long serialVersionUID = 8232044441877298189L;

}
