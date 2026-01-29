package ec.com.saolan.soem.nomina.registroImp;

import java.io.Serializable;

import ec.com.saolan.soem.nomina.modelo.AcciPers;
import ec.com.saolan.soem.nomina.registroInt.AcciPersRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;

@Stateless
public class AcciPersRegisImp extends GestorRegisSoem<AcciPers> implements AcciPersRegisInt, Serializable {

	private static final long serialVersionUID = 8576902550695259207L;

}
