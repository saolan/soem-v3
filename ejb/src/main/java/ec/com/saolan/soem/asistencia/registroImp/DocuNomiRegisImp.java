package ec.com.saolan.soem.asistencia.registroImp;

import java.io.Serializable;

import ec.com.saolan.soem.asistencia.registroInt.DocuNomiRegisInt;
import ec.com.saolan.soem.nomina.modelo.DocuNomi;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;

@Stateless
public class DocuNomiRegisImp extends GestorRegisSoem<DocuNomi> implements DocuNomiRegisInt, Serializable {

	private static final long serialVersionUID = 7778779218601661506L;

}
