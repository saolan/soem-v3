package ec.com.tecnointel.soem.egreso.registroImp;

import java.io.Serializable;

import ec.com.tecnointel.soem.egreso.modelo.EgreNota;
import ec.com.tecnointel.soem.egreso.registroInt.EgreNotaRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;

@Stateless
public class EgreNotaRegisImp extends GestorRegisSoem<EgreNota> implements EgreNotaRegisInt, Serializable {

	private static final long serialVersionUID = 1466344413291336036L;

}
