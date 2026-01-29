package ec.com.tecnointel.soem.seguridad.registroImp;

import java.io.Serializable;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.seguridad.modelo.Secuencial;
import ec.com.tecnointel.soem.seguridad.registroInt.SecuencialRegisInt;
import jakarta.ejb.Stateless;

@Stateless
public class SecuencialRegisImp extends GestorRegisSoem<Secuencial> implements SecuencialRegisInt, Serializable {

	private static final long serialVersionUID = 3514301024757476669L;

}
