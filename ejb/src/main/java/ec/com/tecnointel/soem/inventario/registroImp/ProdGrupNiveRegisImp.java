package ec.com.tecnointel.soem.inventario.registroImp;

import java.io.Serializable;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.inventario.modelo.ProdGrupNive;
import ec.com.tecnointel.soem.inventario.registroInt.ProdGrupNiveRegisInt;
import jakarta.ejb.Stateless;

@Stateless
public class ProdGrupNiveRegisImp extends GestorRegisSoem<ProdGrupNive> implements ProdGrupNiveRegisInt, Serializable {

	private static final long serialVersionUID = -725719142941777727L;

}
