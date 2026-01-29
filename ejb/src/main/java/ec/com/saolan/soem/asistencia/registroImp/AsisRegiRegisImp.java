package ec.com.saolan.soem.asistencia.registroImp;

import java.io.Serializable;

import ec.com.saolan.soem.asistencia.modelo.AsisRegi;
import ec.com.saolan.soem.asistencia.registroInt.AsisRegiRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;

@Stateless
public class AsisRegiRegisImp extends GestorRegisSoem<AsisRegi>
		implements
			AsisRegiRegisInt,
			Serializable {

	private static final long serialVersionUID = -332560392647037166L;

}
