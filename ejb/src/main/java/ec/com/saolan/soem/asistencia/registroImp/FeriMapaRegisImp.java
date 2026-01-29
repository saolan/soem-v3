package ec.com.saolan.soem.asistencia.registroImp;

import java.io.Serializable;

import ec.com.saolan.soem.asistencia.modelo.FeriMapa;
import ec.com.saolan.soem.asistencia.registroInt.FeriMapaRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;

@Stateless
public class FeriMapaRegisImp extends GestorRegisSoem<FeriMapa>
		implements
			FeriMapaRegisInt,
			Serializable {

	private static final long serialVersionUID = 1996221526491958405L;

}
