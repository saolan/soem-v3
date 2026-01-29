package ec.com.tecnointel.soem.seguridad.registroImp;

import java.io.Serializable;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.seguridad.modelo.Auditoria;
import ec.com.tecnointel.soem.seguridad.registroInt.AuditoriaRegisInt;
import jakarta.ejb.Stateless;

@Stateless
public class AuditoriaRegisImp extends GestorRegisSoem<Auditoria> implements AuditoriaRegisInt, Serializable {

	private static final long serialVersionUID = -5975365001724273730L;

}
