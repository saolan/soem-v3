package ec.com.saolan.soem.asistencia.registroImp;

import java.io.Serializable;

import ec.com.saolan.soem.asistencia.modelo.FeriRecu;
import ec.com.saolan.soem.asistencia.registroInt.FeriRecuRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;

@Stateless
public class FeriRecuRegisImp extends GestorRegisSoem<FeriRecu>
		implements
			FeriRecuRegisInt,
			Serializable {

	private static final long serialVersionUID = -3324403998634498018L;

}
