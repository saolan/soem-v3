package ec.com.saolan.soem.nomina.registroImp;

import java.io.Serializable;

import ec.com.saolan.soem.nomina.modelo.Nomina;
import ec.com.saolan.soem.nomina.registroInt.NominaRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;

@Stateless
public class NominaRegisImp extends GestorRegisSoem<Nomina> implements NominaRegisInt, Serializable {

	private static final long serialVersionUID = -5027625689615519255L;

}
