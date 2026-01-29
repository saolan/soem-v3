package ec.com.tecnointel.soem.caja.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.caja.listaInt.CajaListaInt;
import ec.com.tecnointel.soem.caja.listaInt.CajaPeriListaInt;
import ec.com.tecnointel.soem.caja.listaInt.PerifericoListaInt;
import ec.com.tecnointel.soem.caja.modelo.Caja;
import ec.com.tecnointel.soem.caja.modelo.CajaPeri;
import ec.com.tecnointel.soem.caja.modelo.Periferico;
import ec.com.tecnointel.soem.caja.registroInt.CajaPeriRegisInt;
import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.seguridad.listaInt.RolSucuListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolSucu;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class CajaPeriControl extends PaginaControl implements Serializable {

	private Integer cajaId;
	private String paginaRuta;
	
	private CajaPeri cajaPeri;

	private Set<Sucursal> sucursals;
	private List<CajaPeri> cajaPeris;
	private List<Caja> cajas;
	private List<Periferico> perifericos;

	@Inject
	CajaPeriRegisInt cajaPeriRegis;

	@Inject
	CajaPeriListaInt cajaPeriLista;
	
	@Inject
	CajaListaInt cajaLista;

	@Inject
	PerifericoListaInt perifericoLista;
	
	@Inject
	RolSucuListaInt rolSucuLista;
	
	private static final long serialVersionUID = 394244802395632174L;

	@PostConstruct
	public void cargar() {
		cajaPeri = new CajaPeri();
		cajaPeri.setCaja(new Caja());
		cajaPeri.setPeriferico(new Periferico());
		
		sucursals = new HashSet<>();
		
		this.buscarRolSucus();

	}

	public void buscar() {

		try {
			
			cajaPeriLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.cajaPeris = cajaPeriLista.buscar(cajaPeri, this.pagina);
			this.numeroReg = cajaPeris.size();
			this.contadorReg = cajaPeriLista.contarRegistros(cajaPeri);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar datos"));
			e.printStackTrace();
		}
	}

	public void recuperar() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.isPostback() && facesContext.isValidationFailed()) {
			return;
		}
		
		this.getCajaPeri().getCaja().setCajaId(cajaId);
		this.buscarCajas();
		this.buscarPerifericos();

		if (this.id == null) {
			this.cajaPeri = new CajaPeri();
			this.cajaPeri.setAcceso(true);
		} else {

			try {
				this.cajaPeri = cajaPeriRegis.buscarPorId(CajaPeri.class, this.getId());
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
				Object id = cajaPeriRegis.insertar(cajaPeri);
				this.id = (Integer) id;
			} else {
				cajaPeriRegis.modificar(cajaPeri);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		if (this.getCajaId() != null){
			return paginaRuta + "?faces-redirect=true&cajaId=" + this.getCajaId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}

	}
	
	public String nuevo() {
		
		if (this.getCajaId() != null){
			return "/ppsj/caja/cajaPeri/registra?faces-redirect=true&cajaId=" + this.getCajaId() + "&paginaRuta=" + this.getPaginaRuta();			
		} else {
			return "/ppsj/caja/cajaPeri/registra?faces-redirect=true&paginaRuta=" + this.getPaginaRuta(); 
		}

	}
	
	public String modificar() {
		if (this.getCajaId() != null){
			return "/ppsj/caja/cajaPeri/registra?faces-redirect=true&cajaPeriId=" + this.getId() + "&cajaId=" + this.getCajaId() + "&paginaRuta=" + this.getPaginaRuta();	
		} else {
			return "/ppsj/caja/cajaPeri/registra?faces-redirect=true&cajaPeriId=" + this.getId() + "&paginaRuta=" + this.getPaginaRuta();	
		}
	}

	public String explorar() {
		return "explora?faces-redirect=true&cajaPeriId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			CajaPeri cajaPeri = cajaPeriRegis.buscarPorId(CajaPeri.class, this.getId());
			cajaPeriRegis.eliminar(cajaPeri);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Excepcion - Error al eliminar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro eliminado"));

		if (this.getCajaId() != null){
			return paginaRuta + "?faces-redirect=true&cajaId=" + this.getCajaId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}

	}
	
	public String cancelar(){
		
		if (this.getCajaId() != null){
			return paginaRuta + "?faces-redirect=true&cajaId=" + this.getCajaId();			
		} else {
			return paginaRuta + "?faces-redirect=true";			
		}

	}
	
	public List<CajaPeri> buscarTodo() {

		List<CajaPeri> cajaPeris = new ArrayList<>();

		try {
			cajaPeris = cajaPeriLista.buscarTodo("cajaPeriId");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return cajaPeris;
	}
	
	public void buscarCajas() {

		this.getCajaPeri().getCaja().setEstado(true);
		
		try {
			this.cajas = cajaLista.buscar(this.getCajaPeri().getCaja(), null, this.sucursals);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Bodega - Error al buscar cajas"));
			e.printStackTrace();
		}
	}
	
	public void buscarPerifericos() {

		try {
			this.perifericos = perifericoLista.buscar(this.getCajaPeri().getPeriferico(), null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion Buscar Bodega - Error al buscar Perifericos"));
			e.printStackTrace();
		}
	}
	
	public void buscarRolSucus() {
		
		List<RolSucu> rolSucus = new ArrayList<>();
		
		PersUsua persUsuaSesion = (PersUsua) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get("persUsua");
		
		try {
			rolSucus = rolSucuLista.buscar(persUsuaSesion.getRolPersUsuas());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion - Error al buscar sucursales del rol"));
			e.printStackTrace();
		}
			
		for (RolSucu rolSucu : rolSucus) {
			this.sucursals.add(rolSucu.getSucursal());
		}
	}
	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public CajaPeri getCajaPeri() {
		return cajaPeri;
	}

	public void setCajaPeri(CajaPeri cajaPeri) {
		this.cajaPeri = cajaPeri;
	}

	public List<CajaPeri> getCajaPeris() {
		return cajaPeris;
	}

	public void setCajaPeris(List<CajaPeri> cajaPeris) {
		this.cajaPeris = cajaPeris;
	}

	public Integer getCajaId() {
		return cajaId;
	}

	public void setCajaId(Integer cajaId) {
		this.cajaId = cajaId;
	}

	public List<Caja> getCajas() {
		return cajas;
	}

	public void setCajas(List<Caja> cajas) {
		this.cajas = cajas;
	}

	public List<Periferico> getPerifericos() {
		return perifericos;
	}

	public void setPerifericos(List<Periferico> perifericos) {
		this.perifericos = perifericos;
	}

	public String getPaginaRuta() {
		return paginaRuta;
	}

	public void setPaginaRuta(String paginaRuta) {
		this.paginaRuta = paginaRuta;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// Estas 3 Lineas llaman al metodo cuando se abra la pagina es similar a
	// @PostConstructor
	// <f:metadata>
	// <f:event type="preRenderView"
	// listener='#{categoriaControlador.buscarTodo}' />
	// </f:metadata>

	// public void buscarTodo() {
	// try {
	// this.categorias =
	// categoriaConsultaInterface.categoriaConsultar(this.getCategoria());
	// } catch (Exception e) {
	// // TODOs Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

}