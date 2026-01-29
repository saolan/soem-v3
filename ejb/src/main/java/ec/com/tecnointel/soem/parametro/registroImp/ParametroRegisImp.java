package ec.com.tecnointel.soem.parametro.registroImp;

import java.io.Serializable;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.registroInt.ParametroRegisInt;
import jakarta.ejb.Stateless;

@Stateless
public class ParametroRegisImp extends GestorRegisSoem<Parametro> implements ParametroRegisInt, Serializable {

	private static final long serialVersionUID = -6428128937326020947L;

}
