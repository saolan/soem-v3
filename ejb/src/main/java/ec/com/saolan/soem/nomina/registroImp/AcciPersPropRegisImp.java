package ec.com.saolan.soem.nomina.registroImp;

import java.io.Serializable;

import ec.com.saolan.soem.nomina.modelo.AcciPersProp;
import ec.com.saolan.soem.nomina.registroInt.AcciPersPropRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;

@Stateless
public class AcciPersPropRegisImp extends GestorRegisSoem<AcciPersProp> implements AcciPersPropRegisInt, Serializable {

	private static final long serialVersionUID = 2801233637736349803L;

}
