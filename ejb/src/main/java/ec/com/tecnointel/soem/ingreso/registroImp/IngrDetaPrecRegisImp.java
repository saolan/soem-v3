package ec.com.tecnointel.soem.ingreso.registroImp;

import java.io.Serializable;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDetaPrec;
import ec.com.tecnointel.soem.ingreso.registroInt.IngrDetaPrecRegisInt;
import jakarta.ejb.Stateless;

@Stateless
public class IngrDetaPrecRegisImp extends GestorRegisSoem<IngrDetaPrec> implements IngrDetaPrecRegisInt, Serializable {

	private static final long serialVersionUID = 5376766809757520942L;

}
