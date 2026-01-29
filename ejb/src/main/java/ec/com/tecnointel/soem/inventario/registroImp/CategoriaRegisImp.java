package ec.com.tecnointel.soem.inventario.registroImp;

import java.io.Serializable;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.inventario.modelo.Categoria;
import ec.com.tecnointel.soem.inventario.registroInt.CategoriaRegisInt;
import jakarta.ejb.Stateless;

@Stateless
public class CategoriaRegisImp extends GestorRegisSoem<Categoria> implements CategoriaRegisInt, Serializable {

	private static final long serialVersionUID = -5502649997510542961L;

}
