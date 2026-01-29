package ec.com.tecnointel.soem.egreso.registroImp;

import java.io.Serializable;

import ec.com.tecnointel.soem.egreso.modelo.PersCobr;
import ec.com.tecnointel.soem.egreso.registroInt.PersCobrRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;

@Stateless
public class PersCobrRegisImp extends GestorRegisSoem<PersCobr> implements PersCobrRegisInt, Serializable {

	private static final long serialVersionUID = -590373285638735221L;

}
