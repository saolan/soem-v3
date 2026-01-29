package ec.com.saolan.soem.asistencia.registroImp;

import java.io.Serializable;

import ec.com.saolan.soem.asistencia.modelo.HoraPlanDeta;
import ec.com.saolan.soem.asistencia.registroInt.HoraPlanDetaRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;

@Stateless
public class HoraPlanDetaRegisImp extends GestorRegisSoem<HoraPlanDeta>
		implements
			HoraPlanDetaRegisInt,
			Serializable {

	private static final long serialVersionUID = -8116220452170004898L;

}
