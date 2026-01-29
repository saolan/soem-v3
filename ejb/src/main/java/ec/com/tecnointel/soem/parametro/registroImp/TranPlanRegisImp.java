package ec.com.tecnointel.soem.parametro.registroImp;

import java.io.Serializable;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.parametro.modelo.TranPlan;
import ec.com.tecnointel.soem.parametro.registroInt.TranPlanRegisInt;
import jakarta.ejb.Stateless;

@Stateless
public class TranPlanRegisImp extends GestorRegisSoem<TranPlan> implements TranPlanRegisInt, Serializable {

	private static final long serialVersionUID = 8990556919019885507L;

}
