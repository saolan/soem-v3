package ec.com.saolan.soem.nomina.registroImp;

import java.io.Serializable;

import ec.com.saolan.soem.nomina.modelo.AcciPersTipo;
import ec.com.saolan.soem.nomina.registroInt.AcciPersTipoRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;

@Stateless
public class AcciPersTipoRegisImp extends GestorRegisSoem<AcciPersTipo> implements AcciPersTipoRegisInt, Serializable {

	private static final long serialVersionUID = 7778834276637742562L;

}
