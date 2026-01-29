package ec.com.saolan.soem.asistencia.registroImp;

import java.io.Serializable;

import ec.com.saolan.soem.asistencia.modelo.HoraExtr;
import ec.com.saolan.soem.asistencia.registroInt.HoraExtrRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;

@Stateless
public class HoraExtraRegisImp extends GestorRegisSoem<HoraExtr>
		implements
			HoraExtrRegisInt,
			Serializable {

	private static final long serialVersionUID = 5384390689986293579L;

}
