package ec.com.saolan.soem.asistencia.registroImp;

import java.io.Serializable;

import ec.com.saolan.soem.asistencia.modelo.EmplCargVaca;
import ec.com.saolan.soem.asistencia.registroInt.EmplCargVacaRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;

@Stateless
public class EmplCargVacaRegisImp extends GestorRegisSoem<EmplCargVaca>
		implements
			EmplCargVacaRegisInt,
			Serializable {

	private static final long serialVersionUID = -5967439786570747979L;

}
