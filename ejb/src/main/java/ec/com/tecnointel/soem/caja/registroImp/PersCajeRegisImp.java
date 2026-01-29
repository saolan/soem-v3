package ec.com.tecnointel.soem.caja.registroImp;

import java.io.Serializable;

import ec.com.tecnointel.soem.caja.modelo.PersCaje;
import ec.com.tecnointel.soem.caja.registroInt.PersCajeRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;

@Stateless
public class PersCajeRegisImp extends GestorRegisSoem<PersCaje> implements PersCajeRegisInt, Serializable {

	private static final long serialVersionUID = 7033457945358445034L;

}
