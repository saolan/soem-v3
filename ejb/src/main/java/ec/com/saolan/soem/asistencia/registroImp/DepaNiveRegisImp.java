package ec.com.saolan.soem.asistencia.registroImp;

import java.io.Serializable;

import ec.com.saolan.soem.asistencia.modelo.DepaNive;
import ec.com.saolan.soem.asistencia.registroInt.DepaNiveRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;

@Stateless
public class DepaNiveRegisImp extends GestorRegisSoem<DepaNive>
		implements
			DepaNiveRegisInt,
			Serializable {

	private static final long serialVersionUID = 4315271385782049506L;

}
