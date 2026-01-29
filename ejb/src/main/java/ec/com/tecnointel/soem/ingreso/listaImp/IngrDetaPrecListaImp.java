package ec.com.tecnointel.soem.ingreso.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.ingreso.listaInt.IngrDetaPrecListaInt;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDetaPrec;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.Rol;
import ec.com.tecnointel.soem.seguridad.modelo.RolPersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolPrec;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class IngrDetaPrecListaImp extends GestorListaSoem<IngrDetaPrec> implements IngrDetaPrecListaInt, Serializable {

	private static final long serialVersionUID = 466452036583778070L;

	// Busca con paginaci?n
	@Override
	public List<IngrDetaPrec> buscar(IngrDetaPrec ingrDetaPrec, Integer pagina) {

		EntityGraph<?> ingrDetaPrecGraph = this.entityManager.getEntityGraph("ingrDetaPrec.Graph");
		
		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<IngrDetaPrec> query = builder.createQuery(IngrDetaPrec.class);
		Root<IngrDetaPrec> ingrDetaPrecRoot = query.from(IngrDetaPrec.class);

		query.orderBy(builder.asc(ingrDetaPrecRoot.get("ingrDetaPrecId")));
		TypedQuery<IngrDetaPrec> consulta = this.entityManager
				.createQuery(query.select(ingrDetaPrecRoot).where(getSearchPredicates(ingrDetaPrecRoot, ingrDetaPrec)));
		consulta.setHint("jakarta.persistence.loadgraph", ingrDetaPrecGraph);

		// Si se pasa null a pagina se listan todos los datos de acuerdo a
		// parametros
		// Es decir no realiza paginaci?n
		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		// consulta.setFirstResult(pagina *
		// getFilasPagina()).setMaxResults(getFilasPagina());

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(IngrDetaPrec ingrDetaPrec) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<IngrDetaPrec> ingrDetaPrecRoot = countQuery.from(IngrDetaPrec.class);

		countQuery = countQuery.select(builder.count(ingrDetaPrecRoot))
				.where(getSearchPredicates(ingrDetaPrecRoot, ingrDetaPrec));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<IngrDetaPrec> ingrDetaPrecRoot, IngrDetaPrec ingrDetaPrec) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer ingrDetaPrecId = ingrDetaPrec.getIngrDetaPrecId();
		if (ingrDetaPrecId != null) {
			predicates.add(builder.equal(ingrDetaPrecRoot.get("ingrDetaPrecId"), ingrDetaPrecId));
		}

		Integer ingrDetaId = ingrDetaPrec.getIngrDeta().getIngrDetaId();
		if (ingrDetaId != null) {
			predicates.add(builder.equal(ingrDetaPrecRoot.get("ingrDeta").get("ingrDetaId"), ingrDetaId));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	@Override
	public List<IngrDetaPrec> filtrarIngrDetaPrec(List<IngrDetaPrec> ingrDetaPrecs, PersUsua persUsuaSesion, List<RolPrec> rolPrecs,
			Sucursal sucursal) throws Exception {
		
		RolPrec rolPrec = new RolPrec();
		
		List<Rol> roles = new ArrayList<>();
//		List<RolPrec> rolPrecs = new ArrayList<>();
		List<IngrDetaPrec> filtroProdPrecs = new ArrayList<>();
		
		Set<IngrDetaPrec> filtroProdPrecSet = new HashSet<IngrDetaPrec>();
		
//		rolPrecs = variablesSesion.getRolPrecs();
//		Sucursal sucursal = variablesSesion.getSucursal();
				
		Set<RolPersUsua> rolPersUsuas = persUsuaSesion.getRolPersUsuas();
		for (RolPersUsua rolPersUsua : rolPersUsuas) {
			roles.add(rolPersUsua.getRol());
		}
		
		for (Rol rol : roles) {
			for (IngrDetaPrec ingrDetaPrec : filtroProdPrecs) {
				rolPrec.setSucursal(sucursal);
				rolPrec.setRol(rol);
				rolPrec.setPrecio(ingrDetaPrec.getPrecio());
				if (rolPrecs.contains(rolPrec) ) {
					filtroProdPrecSet.add(ingrDetaPrec);
				}
			}
		}
	
		filtroProdPrecs.addAll(filtroProdPrecSet);
		
		return filtroProdPrecs;
	}

	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}