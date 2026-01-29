package ec.com.tecnointel.soem.ingreso.registroImp;

import java.io.Serializable;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.ingreso.modelo.ProvGrup;
import ec.com.tecnointel.soem.ingreso.registroInt.ProvGrupRegisInt;
import jakarta.ejb.Stateless;

@Stateless
public class ProvGrupRegisImp extends GestorRegisSoem<ProvGrup> implements ProvGrupRegisInt, Serializable {

	private static final long serialVersionUID = 5661023814623019368L;

}
