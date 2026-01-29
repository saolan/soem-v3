package ec.com.tecnointel.soem.egreso.registroImp;

import java.io.Serializable;

import ec.com.tecnointel.soem.egreso.modelo.PersVend;
import ec.com.tecnointel.soem.egreso.registroInt.PersVendRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;

@Stateless
public class PersVendRegisImp extends GestorRegisSoem<PersVend> implements PersVendRegisInt, Serializable {

	private static final long serialVersionUID = 126807835564259517L;

}
