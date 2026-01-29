package ec.com.saolan.soem.asistencia.registroImp;

import java.io.Serializable;

import ec.com.saolan.soem.asistencia.modelo.AuseRecu;
import ec.com.saolan.soem.asistencia.registroInt.AuseRecuRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;

@Stateless
public class AuseRecuRegisImp extends GestorRegisSoem<AuseRecu>
		implements
			AuseRecuRegisInt,
			Serializable {

	private static final long serialVersionUID = -7177983236651110722L;

}
