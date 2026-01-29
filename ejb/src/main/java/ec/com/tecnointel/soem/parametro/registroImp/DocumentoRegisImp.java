package ec.com.tecnointel.soem.parametro.registroImp;

import java.io.Serializable;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.registroInt.DocumentoRegisInt;
import jakarta.ejb.Stateless;

@Stateless
public class DocumentoRegisImp extends GestorRegisSoem<Documento> implements DocumentoRegisInt, Serializable {

	private static final long serialVersionUID = -6223289371939221027L;

}
