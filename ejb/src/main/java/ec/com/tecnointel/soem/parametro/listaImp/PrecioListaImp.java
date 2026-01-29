package ec.com.tecnointel.soem.parametro.listaImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.general.util.GestorListaSoem;
import ec.com.tecnointel.soem.parametro.listaInt.PrecioListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Precio;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.Rol;
import ec.com.tecnointel.soem.seguridad.modelo.RolPersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolPrec;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class PrecioListaImp extends GestorListaSoem<Precio> implements PrecioListaInt, Serializable {

	private static final long serialVersionUID = -5664208789862014921L;

	// Busca con paginaci�n
	@Override
	public List<Precio> buscar(Precio precio, Integer pagina) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Precio> query = builder.createQuery(Precio.class);
		Root<Precio> precioRoot = query.from(Precio.class);

		query.orderBy(builder.asc(precioRoot.get("descri")));
		TypedQuery<Precio> consulta = this.entityManager
				.createQuery(query.select(precioRoot).where(getSearchPredicates(precioRoot, precio)));

		// Si se pasa null a pagina se listan todos los datos de acuerdo a
		// parametros
		// Es decir no realiza paginaci�n
		if (pagina != null) {
			consulta.setFirstResult(pagina * filasPagina).setMaxResults(filasPagina);
		}

		// consulta.setFirstResult(pagina *
		// getFilasPagina()).setMaxResults(getFilasPagina());

		return consulta.getResultList();
	}

	@Override
	public long contarRegistros(Precio precio) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
		Root<Precio> precioRoot = countQuery.from(Precio.class);

		countQuery = countQuery.select(builder.count(precioRoot)).where(getSearchPredicates(precioRoot, precio));
		return this.entityManager.createQuery(countQuery).getSingleResult();

	}

	private Predicate[] getSearchPredicates(Root<Precio> precioRoot, Precio precio) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		Integer precioId = precio.getPrecioId();
		if (precioId != null) {
			predicates.add(builder.equal(precioRoot.<Integer> get("precioId"), precioId));
		}
		
		String codigo = precio.getCodigo();
		if (codigo != null && !"".equals(codigo)) {
			predicates.add(builder.equal(builder.lower(precioRoot.<String> get("codigo")), codigo.toLowerCase() ));
		}
		String descri = precio.getDescri();
		if (descri != null && !"".equals(descri)) {
			predicates.add(builder.like(builder.lower(precioRoot.<String> get("descri")),
					'%' + descri.toLowerCase() + '%'));
		}
		
		boolean estado = precio.isEstado();
		predicates.add(builder.equal(precioRoot.get("estado"), estado));

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	@Override
	public void imprimir(Precio precio) throws Exception {
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	@Override
	public List<Precio> filtrarPrecios(List<Precio> precios, PersUsua persUsuaSesion, List<RolPrec> rolPrecs, Sucursal sucursal) throws Exception {
		
		RolPrec rolPrec = new RolPrec();
		
		List<Rol> roles = new ArrayList<>();
//		List<RolPrec> rolPrecs = new ArrayList<>();
		List<Precio> filtroPrecios = new ArrayList<>();
		
		Set<Precio> filtroPrecioSet = new HashSet<Precio>();
		
//		rolPrecs = variablesSesion.getRolPrecs();
//		Sucursal sucursal = variablesSesion.getSucursal();
				
		Set<RolPersUsua> rolPersUsuas = persUsuaSesion.getRolPersUsuas();
		for (RolPersUsua rolPersUsua : rolPersUsuas) {
			roles.add(rolPersUsua.getRol());
		}
		
		for (Rol rol : roles) {
			for (Precio precio : precios) {
				rolPrec.setSucursal(sucursal);
				rolPrec.setRol(rol);
				rolPrec.setPrecio(precio);
				if (rolPrecs.contains(rolPrec) ) {
					filtroPrecioSet.add(precio);
				}
			}
		}
	
		filtroPrecios.addAll(filtroPrecioSet);
		
		return filtroPrecios;
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