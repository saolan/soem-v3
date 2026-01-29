package ec.com.saolan.soem.nomina.registroImp;

import java.io.Serializable;

import ec.com.saolan.soem.nomina.modelo.AcciPersActu;
import ec.com.saolan.soem.nomina.registroInt.AcciPersActuRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;

@Stateless
public class AcciPersActuRegisImp extends GestorRegisSoem<AcciPersActu> implements AcciPersActuRegisInt, Serializable {

	private static final long serialVersionUID = -6954691462997270659L;
	
}
