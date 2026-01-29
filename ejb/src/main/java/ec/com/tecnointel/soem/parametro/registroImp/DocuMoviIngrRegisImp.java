package ec.com.tecnointel.soem.parametro.registroImp;

import java.io.Serializable;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.parametro.modelo.DocuMoviIngr;
import ec.com.tecnointel.soem.parametro.registroInt.DocuMoviIngrRegisInt;
import jakarta.ejb.Stateless;

@Stateless
public class DocuMoviIngrRegisImp extends GestorRegisSoem<DocuMoviIngr> implements DocuMoviIngrRegisInt, Serializable {

	private static final long serialVersionUID = 6360285181976747899L;

}
