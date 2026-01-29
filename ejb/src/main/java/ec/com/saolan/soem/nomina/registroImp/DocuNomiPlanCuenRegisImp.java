package ec.com.saolan.soem.nomina.registroImp;

import java.io.Serializable;

import ec.com.saolan.soem.nomina.modelo.DocuNomiPlanCuen;
import ec.com.saolan.soem.nomina.registroInt.DocuNomiPlanCuenRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;

@Stateless
public class DocuNomiPlanCuenRegisImp extends GestorRegisSoem<DocuNomiPlanCuen>
		implements DocuNomiPlanCuenRegisInt, Serializable {

	private static final long serialVersionUID = -4377913863393277406L;

}
