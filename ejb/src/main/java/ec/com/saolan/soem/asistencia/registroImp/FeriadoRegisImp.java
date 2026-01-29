package ec.com.saolan.soem.asistencia.registroImp;

import java.io.Serializable;

import ec.com.saolan.soem.asistencia.modelo.Feriado;
import ec.com.saolan.soem.asistencia.registroInt.FeriadoRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;

@Stateless
public class FeriadoRegisImp extends GestorRegisSoem<Feriado>
		implements
			FeriadoRegisInt,
			Serializable {

	private static final long serialVersionUID = -8312935729969221595L;

}
