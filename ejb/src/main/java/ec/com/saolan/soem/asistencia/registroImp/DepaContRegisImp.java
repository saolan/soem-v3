package ec.com.saolan.soem.asistencia.registroImp;

import java.io.Serializable;

import ec.com.saolan.soem.asistencia.modelo.DepaCont;
import ec.com.saolan.soem.asistencia.registroInt.DepaContRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;

@Stateless
public class DepaContRegisImp extends GestorRegisSoem<DepaCont>
		implements
			DepaContRegisInt,
			Serializable {

	private static final long serialVersionUID = 1523299633495139778L;

}
