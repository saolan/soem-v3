package ec.com.tecnointel.soem.parametro.registroImp;

import java.io.Serializable;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.parametro.registroInt.PersonaRegisInt;
import jakarta.ejb.Stateless;

@Stateless
public class PersonaRegisImp extends GestorRegisSoem<Persona> implements PersonaRegisInt, Serializable {

	private static final long serialVersionUID = -5670089256147202084L;

}
