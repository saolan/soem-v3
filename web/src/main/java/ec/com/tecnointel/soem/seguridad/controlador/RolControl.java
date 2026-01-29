package ec.com.tecnointel.soem.seguridad.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ec.com.tecnointel.soem.general.controlador.PaginaControl;
import ec.com.tecnointel.soem.parametro.modelo.Bodega;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.FormPago;
import ec.com.tecnointel.soem.parametro.modelo.Precio;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.seguridad.listaInt.RolBodeListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolDocuListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolFormPagoListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolPermListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolPrecListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolSucuListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.Permiso;
import ec.com.tecnointel.soem.seguridad.modelo.Rol;
import ec.com.tecnointel.soem.seguridad.modelo.RolBode;
import ec.com.tecnointel.soem.seguridad.modelo.RolDocu;
import ec.com.tecnointel.soem.seguridad.modelo.RolFormPago;
import ec.com.tecnointel.soem.seguridad.modelo.RolPerm;
import ec.com.tecnointel.soem.seguridad.modelo.RolPrec;
import ec.com.tecnointel.soem.seguridad.modelo.RolSucu;
import ec.com.tecnointel.soem.seguridad.registroInt.RolRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class RolControl extends PaginaControl implements Serializable {

	private Integer personaId;
	private Rol rol;

	List<Rol> rols;
	List<RolPerm> rolPerms;
	List<RolDocu> rolDocus;
	List<RolFormPago> rolFormPagos;
	List<RolPrec> rolPrecs;
	List<RolBode> rolBodes;
	List<RolSucu> rolSucus;

	@Inject
	RolRegisInt rolRegis;

	@Inject
	RolListaInt rolLista;
	
	@Inject
	RolPermListaInt rolPermLista;
	
	@Inject
	RolDocuListaInt rolDocuLista;
	
	@Inject
	RolFormPagoListaInt rolFormPagoLista;

	@Inject
	RolPrecListaInt rolPrecLista;
		
	@Inject
	RolBodeListaInt rolBodeLista;

	@Inject
	RolSucuListaInt rolSucuLista;

	private static final long serialVersionUID = 7132801538626254423L;

	@PostConstruct
	public void cargar() {
		rol = new Rol();
		rol.setEstado(true);
	}

	public void buscar() {

		try {
			
			rolLista.filasPagina(variablesSesion.getFilasPagina());
			
			this.rols = rolLista.buscar(this.rol, this.pagina);
			this.numeroReg = rols.size();
			this.contadorReg = rolLista.contarRegistros(rol);
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

		if (this.id == null) {
			this.rol = new Rol();
			this.rol.setEstado(true);
		} else {

			try {
				this.rol = rolRegis.buscarPorId(Rol.class, this.getId());
				this.buscarRolPerm(rol);
				this.buscarRolDocu(rol);
				this.buscarRolFormPago(rol);
				this.buscarRolPrec(rol);
				this.buscarRolBode(rol);
				this.buscarRolSucu(rol);
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
				Object id = rolRegis.insertar(rol);
				this.id = (Integer) id;
			} else {
				rolRegis.modificar(rol);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al grabar datos"));
			e.printStackTrace();
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, "Registro grabado"));

		return "explora?faces-redirect=true&rolId=" + this.getId();
	}

	public String nuevo() {
		return "/ppsj/seguridad/rol/registra?faces-redirect=true&personaId=" + this.getPersonaId();
	}
	
	public String modificar() {
		return "registra?faces-redirect=true&rolId=" + this.getId();
	}

	public String explorar() {
		return "explora?faces-redirect=true&rolId=" + this.getId();
	}

	public String eliminar() {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

		try {
			Rol rol = rolRegis.buscarPorId(Rol.class, this.getId());
			rolRegis.eliminar(rol);

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

	public List<Rol> buscarTodo() {

		List<Rol> roles = new ArrayList<>();

		try {
			roles = rolLista.buscarTodo("descri");
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar todo"));
			e.printStackTrace();
		}
		return roles;
	}
	
	public void buscarRolPerm(Rol rol){

		RolPerm rolPerm = new RolPerm();
		rolPerm.setRol(rol);
		rolPerm.setPermiso(new Permiso());
		
		try {
			this.rolPerms = rolPermLista.buscar(rolPerm, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Rol - Permiso"));
			e.printStackTrace();
		}
	}
	
	public void buscarRolDocu(Rol rol){

		RolDocu rolDocu = new RolDocu();
		rolDocu.setRol(rol);
		rolDocu.setDocumento(new Documento());
		
		try {
			this.rolDocus = rolDocuLista.buscar(rolDocu, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Rol - Documento"));
			e.printStackTrace();
		}
	}

	public void buscarRolFormPago(Rol rol){

		RolFormPago rolFormPago = new RolFormPago();
		rolFormPago.setRol(rol);
		rolFormPago.setFormPago(new FormPago());
		
		try {
			this.rolFormPagos = rolFormPagoLista.buscar(rolFormPago, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Rol - Forma Pago"));
			e.printStackTrace();
		}
	}

	public void buscarRolPrec(Rol rol){

		RolPrec rolPrec = new RolPrec();
		rolPrec.setRol(rol);
		rolPrec.setPrecio(new Precio());
		rolPrec.setSucursal(new Sucursal());
		
		try {
			this.rolPrecs = rolPrecLista.buscar(rolPrec, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Rol - Precio"));
			e.printStackTrace();
		}
	}

	public void buscarRolBode(Rol rol){

		RolBode rolBode = new RolBode();
		rolBode.setRol(rol);
		rolBode.setBodega(new Bodega());
		rolBode.setSucursal(new Sucursal());
		
		try {
			this.rolBodes = rolBodeLista.buscar(rolBode, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Rol - Bodega"));
			e.printStackTrace();
		}
		
	}

	public void buscarRolSucu(Rol rol){

		RolSucu rolSucu = new RolSucu();
		rolSucu.setRol(rol);
		rolSucu.setSucursal(new Sucursal());
		
		try {
			this.rolSucus = rolSucuLista.buscar(rolSucu, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null, "Excepcion - Error al buscar Rol - Sucursal"));
			e.printStackTrace();
		}
	}
	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public List<Rol> getRols() {
		return rols;
	}

	public void setRols(List<Rol> rols) {
		this.rols = rols;
	}

	public List<RolBode> getRolBodes() {
		return rolBodes;
	}

	public void setRolBodes(List<RolBode> rolBodes) {
		this.rolBodes = rolBodes;
	}

	public List<RolPerm> getRolPerms() {
		return rolPerms;
	}

	public void setRolPerms(List<RolPerm> rolPerms) {
		this.rolPerms = rolPerms;
	}

	public List<RolDocu> getRolDocus() {
		return rolDocus;
	}

	public void setRolDocus(List<RolDocu> rolDocus) {
		this.rolDocus = rolDocus;
	}

	public List<RolFormPago> getRolFormPagos() {
		return rolFormPagos;
	}

	public void setRolFormPagos(List<RolFormPago> rolFormPagos) {
		this.rolFormPagos = rolFormPagos;
	}

	public List<RolPrec> getRolPrecs() {
		return rolPrecs;
	}

	public void setRolPrecs(List<RolPrec> rolPrecs) {
		this.rolPrecs = rolPrecs;
	}

	public List<RolSucu> getRolSucus() {
		return rolSucus;
	}

	public void setRolSucus(List<RolSucu> rolSucus) {
		this.rolSucus = rolSucus;
	}

	public Integer getPersonaId() {
		return personaId;
	}

	public void setPersonaId(Integer personaId) {
		this.personaId = personaId;
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< NOTAS
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}