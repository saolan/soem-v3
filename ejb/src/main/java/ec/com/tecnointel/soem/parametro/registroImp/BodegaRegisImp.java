package ec.com.tecnointel.soem.parametro.registroImp;

import java.io.Serializable;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.parametro.modelo.Bodega;
import ec.com.tecnointel.soem.parametro.registroInt.BodegaRegisInt;
import jakarta.ejb.Stateless;

@Stateless
public class BodegaRegisImp extends GestorRegisSoem<Bodega> implements BodegaRegisInt, Serializable {

	private static final long serialVersionUID = -6970583961871574850L;

}
