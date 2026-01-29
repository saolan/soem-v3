package ec.com.saolan.soem.nomina.registroImp;

import java.io.Serializable;

import ec.com.saolan.soem.nomina.modelo.NomiDeta;
import ec.com.saolan.soem.nomina.registroInt.NomiDetaRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;

@Stateless
public class NomiDetaRegisImp extends GestorRegisSoem<NomiDeta> implements NomiDetaRegisInt, Serializable {

	private static final long serialVersionUID = -1505316623479642422L;

}
