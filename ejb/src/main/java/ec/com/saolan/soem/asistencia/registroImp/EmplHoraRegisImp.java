package ec.com.saolan.soem.asistencia.registroImp;

import java.io.Serializable;

import ec.com.saolan.soem.asistencia.modelo.EmplHora;
import ec.com.saolan.soem.asistencia.registroInt.EmplHoraRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;

@Stateless
public class EmplHoraRegisImp extends GestorRegisSoem<EmplHora>
		implements
			EmplHoraRegisInt,
			Serializable {

	private static final long serialVersionUID = -3509464801025643631L;

}
