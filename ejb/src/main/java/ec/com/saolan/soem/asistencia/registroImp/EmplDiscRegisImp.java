package ec.com.saolan.soem.asistencia.registroImp;

import java.io.Serializable;

import ec.com.saolan.soem.asistencia.modelo.EmplDisc;
import ec.com.saolan.soem.asistencia.registroInt.EmplDiscRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;

@Stateless
public class EmplDiscRegisImp extends GestorRegisSoem<EmplDisc>
		implements
			EmplDiscRegisInt,
			Serializable {

	private static final long serialVersionUID = 8390764546244092595L;

}
