package ec.com.tecnointel.soem.seguridad.registroImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.Rol;
import ec.com.tecnointel.soem.seguridad.modelo.RolDocu;
import ec.com.tecnointel.soem.seguridad.modelo.RolPersUsua;
import ec.com.tecnointel.soem.seguridad.registroInt.RolDocuRegisInt;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;

@Stateless
public class RolDocuRegisImp extends GestorRegisSoem<RolDocu> implements RolDocuRegisInt, Serializable {

	private static final long serialVersionUID = -7086631680899061090L;

	@Override
	public RolDocu buscarPorId(Class<?> entidad, Integer id) {

		EntityGraph<?> rolDocuGraph = this.entityManager.getEntityGraph("rolDocu.Graph");
		Map<String, Object> hints = new HashMap<String, Object>();
		hints.put("jakarta.persistence.loadgraph", rolDocuGraph);

		return (RolDocu) entityManager.find(entidad, id, hints);
	}
	
	@Override
	public RolDocu buscarRolDocuPermisos(List<RolDocu> rolDocus, Documento documento, PersUsua persUsua) {

		RolDocu rolDocu = new RolDocu();

		List<Rol> roles = new ArrayList<>();

		Set<RolPersUsua> rolPersUsuas = persUsua.getRolPersUsuas();
		for (RolPersUsua rolPersUsua : rolPersUsuas) {
			roles.add(rolPersUsua.getRol());
		}

		for (Rol rol : roles) {
			for (RolDocu rolDocuRecorrer : rolDocus) {
				if (rolDocuRecorrer.getDocumento().equals(documento) && rolDocuRecorrer.getRol().equals(rol)) {
					rolDocu = rolDocuRecorrer;
					break;
				}
			}
		}

		return rolDocu;
	}

}
