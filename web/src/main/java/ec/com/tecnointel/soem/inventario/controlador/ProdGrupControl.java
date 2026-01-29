package ec.com.tecnointel.soem.inventario.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.contabilidad.modelo.PlanCuen;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.inventario.listaInt.ProdGrupListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdGrupNiveListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdGrupPlanCuenListaInt;
import ec.com.tecnointel.soem.inventario.modelo.ProdGrup;
import ec.com.tecnointel.soem.inventario.modelo.ProdGrupNive;
import ec.com.tecnointel.soem.inventario.modelo.ProdGrupPlanCuen;
import ec.com.tecnointel.soem.inventario.registroInt.ProdGrupPlanCuenRegisInt;
import ec.com.tecnointel.soem.inventario.registroInt.ProdGrupRegisInt;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class ProdGrupControl extends PaginaControl implements Serializable {

	private ProdGrup prodGrup;

	private ProdGrup prodGrupSele;

	List<ProdGrup> prodGrups;
	List<ProdGrupNive> prodGrupNives;
	List<ProdGrupPlanCuen> prodGrupPlanCuens;
	List<Parametro> parametros;

	@Inject
	ProdGrupRegisInt prodGrupRegis;

	@Inject
	ProdGrupListaInt prodGrupLista;

	@Inject
	ProdGrupNiveListaInt prodGrupNiveLista;

	@Inject
	ProdGrupPlanCuenListaInt prodGrupPlanCuenLista;

	@Inject
	ProdGrupPlanCuenRegisInt prodGrupPlanCuenRegis;

	private static final long serialVersionUID = 3880706252415847400L;

	@PostConstruct
	public void cargar() {

		this.parametros = new ArrayList<>();

		prodGrup = new ProdGrup();
		prodGrup.setEstado(true);

	}

	public void buscar() {

		try {

			prodGrupLista.filasPagina(variablesSesion.getFilasPagina());

			this.prodGrups = prodGrupLista.buscar(prodGrup, this.pagina);
			this.numeroReg = prodGrups.size();
			this.contadorReg = prodGrupLista.contarRegistros(prodGrup);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
	}

//	Buscar todos los grupos de productos para poder seleccionar 
//	de que grupo se va a realizar la copia de cuentas contables
	public void buscarProdGrupTodos() {

		ProdGrup prodGrup = new ProdGrup();
		prodGrup.setEstado(true);

		try {

			prodGrupLista.filasPagina(variablesSesion.getFilasPagina());

			this.prodGrups = prodGrupLista.buscar(prodGrup, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar grupos"));
			e.printStackTrace();
		}
	}

	public void buscarProdGrupPlanCuens(ProdGrup prodGrup) {

		ProdGrupPlanCuen prodGrupPlanCuen = new ProdGrupPlanCuen();
		prodGrupPlanCuen.setProdGrup(prodGrup);
		prodGrupPlanCuen.setPlanCuen(new PlanCuen());

		try {
			this.prodGrupPlanCuens = prodGrupPlanCuenLista.buscar(prodGrupPlanCuen, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar Producto Grupo Plan Cuenta"));
			e.printStackTrace();
		}
	}

	public void buscarProdGrupTipos() {

		Parametro parametro = new Parametro();

		List<Parametro> parametros = new ArrayList<>();

		parametro.setCodigo("Inventario-GrupoTipo");
		parametro.setEstado(true);

		try {
			parametros = parametroLista.buscar(parametro, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepción- Error al buscar parametro Inventario-GrupoTipo"));
			e.printStackTrace();
		}

		String[] tipoGrup;
//		Se recorre la lista y se busca el valor predeterminado
//		PCM = Predeterminado en compras; PVN = Predeterminado en ventas; PCV = Predetermindado en compras y ventas; PFL = Predeterminado false
//		Tambien se quita de la descripcion del parametro el valor que determina cual es el predeterminado
		for (Parametro parametroFiltro : parametros) {
			tipoGrup = parametroFiltro.getDescri().split(";");
			if (!tipoGrup[0].equals("Todo")) {
				parametroFiltro.setDescri(tipoGrup[0]);
				this.parametros.add(parametroFiltro);
			}
		}
	}

	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() && facesContext.isValidationFailed()) {
			return;
		}

		this.buscarProdGrupTipos();
		this.buscarProdGrupNives();

		if (this.id == null) {
			this.prodGrup = new ProdGrup();
			this.prodGrup.setEstado(true);
		} else {

			try {
				this.prodGrup = prodGrupRegis.buscarPorId(ProdGrup.class, this.getId());
				this.buscarProdGrupPlanCuens(prodGrup);
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Id"));
				e.printStackTrace();
			}
		}
	}

	public String grabar() {

		// Los mensajes tienen RequestScope y se borran al navegar a otro pagina
		// Con este comando se guarda los mensages de confirmaci�n en navegacion
		// entre paginas
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			if (this.id == null) {
				Object id = prodGrupRegis.insertar(prodGrup);
				this.id = (Integer) id;
			} else {
				prodGrupRegis.modificar(prodGrup);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		return "explora?faces-redirect=true&prodGrupId=" + this.getId();
	}

	public String modificar() {
		return "registra?faces-redirect=true&prodGrupId=" + this.getId();

	}

	public String explorar() {
		return "explora?faces-redirect=true&prodGrupId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			ProdGrup prodGrup = prodGrupRegis.buscarPorId(ProdGrup.class, this.getId());
			prodGrupRegis.eliminar(prodGrup);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		return "lista?faces-redirect=true";
	}

	public List<ProdGrup> buscarTodo() {

		List<ProdGrup> prodGrups = new ArrayList<>();

		try {
			prodGrups = prodGrupLista.buscarTodo("prodGrupId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return prodGrups;
	}

	public void buscarProdGrupNives() {

		this.getProdGrup().setProdGrupNive(new ProdGrupNive());

		try {
			this.prodGrupNives = prodGrupNiveLista.buscar(this.getProdGrup().getProdGrupNive(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar niveles del grupo de productos"));
			e.printStackTrace();
		}

	}

//	Copia cuentas contables de un grupo ya creado al nuevo grupo
//	para no estar ingresando uno por uno
	public void copiarPgpcs() {

		List<ProdGrupPlanCuen> pgpcs = crearPgpcs();
		grabarPgpcs(pgpcs);
		
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Cuentas contables copiadas"));

	}

	public List<ProdGrupPlanCuen> crearPgpcs() {

		PlanCuen planCuen = new PlanCuen();
		List<ProdGrupPlanCuen> pgpcs = new ArrayList<ProdGrupPlanCuen>();

		ProdGrupPlanCuen pgpcBuscar = new ProdGrupPlanCuen();
		pgpcBuscar.setPlanCuen(planCuen);
		pgpcBuscar.setProdGrup(prodGrupSele);

		buscarProdGrupPlanCuens(prodGrupSele);

		for (ProdGrupPlanCuen prodGrupPlanCuen : prodGrupPlanCuens) {

			ProdGrupPlanCuen pgpc = new ProdGrupPlanCuen();
			pgpc.setProdGrup(prodGrup);
			pgpc.setPlanCuen(prodGrupPlanCuen.getPlanCuen());
			pgpc.setTipoTran(prodGrupPlanCuen.getTipoTran());
			pgpc.setDocumento(prodGrupPlanCuen.getDocumento());
			pgpcs.add(pgpc);
		}

		return pgpcs;
	}

	public void grabarPgpcs(List<ProdGrupPlanCuen> pgpcs) {

		try {
			for (ProdGrupPlanCuen prodGrupPlanCuen : pgpcs) {

				prodGrupPlanCuenRegis.insertar(prodGrupPlanCuen);

			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion grabo cuentas contables para el nuevo grupo"));
			e.printStackTrace();
		}
	}

	// <<<<<<<<<<<<<<<<<<<<< GETTER & SETTER >>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<< GETTER & SETTER >>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<< GETTER & SETTER >>>>>>>>>>>>>>>>>>>>>

	public ProdGrup getProdGrup() {
		return prodGrup;
	}

	public void setProdGrup(ProdGrup prodGrup) {
		this.prodGrup = prodGrup;
	}

	public List<ProdGrup> getProdGrups() {
		return prodGrups;
	}

	public void setProdGrups(List<ProdGrup> prodGrups) {
		this.prodGrups = prodGrups;
	}

	public List<ProdGrupNive> getProdGrupNives() {
		return prodGrupNives;
	}

	public void setProdGrupNives(List<ProdGrupNive> prodGrupNives) {
		this.prodGrupNives = prodGrupNives;
	}

	public List<ProdGrupPlanCuen> getProdGrupPlanCuens() {
		return prodGrupPlanCuens;
	}

	public void setProdGrupPlanCuens(List<ProdGrupPlanCuen> prodGrupPlanCuens) {
		this.prodGrupPlanCuens = prodGrupPlanCuens;
	}

	public List<Parametro> getParametros() {
		return parametros;
	}

	public void setParametros(List<Parametro> parametros) {
		this.parametros = parametros;
	}

	public ProdGrup getProdGrupSele() {
		return prodGrupSele;
	}

	public void setProdGrupSele(ProdGrup prodGrupSele) {
		this.prodGrupSele = prodGrupSele;
	}

}