package ec.com.saolan.soem.nomina.registroImp;

import java.io.Serializable;

import ec.com.saolan.soem.nomina.modelo.AcciPersAuto;
import ec.com.saolan.soem.nomina.registroInt.AcciPersAutoRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;

@Stateless
public class AcciPersAutoRegisImp extends GestorRegisSoem<AcciPersAuto> implements AcciPersAutoRegisInt, Serializable {

	private static final long serialVersionUID = 7514157026094814206L;

}
