package ec.com.tecnointel.soem.ingreso.registroImp;

import java.io.Serializable;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDetaImpu;
import ec.com.tecnointel.soem.ingreso.registroInt.IngrDetaImpuRegisInt;
import jakarta.ejb.Stateless;

@Stateless
public class IngrDetaImpuRegisImp extends GestorRegisSoem<IngrDetaImpu> implements IngrDetaImpuRegisInt, Serializable {

	private static final long serialVersionUID = 2518140117697232645L;

}
