package ec.com.tecnointel.soem.seguridad.registroImp;

import java.io.Serializable;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.registroInt.PersUsuaRegisInt;
import jakarta.ejb.Stateless;

@Stateless
public class PersUsuaRegisImp extends GestorRegisSoem<PersUsua> implements PersUsuaRegisInt, Serializable {

	private static final long serialVersionUID = 6881731143952295944L;

}
