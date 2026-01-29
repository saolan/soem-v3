package ec.com.tecnointel.soem.general.controlador;

import java.io.Serializable;
import java.util.BitSet;
import java.util.List;
import java.util.logging.Logger;

import ec.com.tecnointel.soem.general.interfac.LoggerSoem;
import ec.com.tecnointel.soem.parametro.listaInt.ParametroListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.registroInt.ParametroRegisInt;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

public class PaginaControl implements Serializable {

	// Id de la entidad que se va a manipular
	protected Integer id;

	// Numero actual de pagina en el paginador
	protected Integer pagina = 0;

	// Contador total de registros
	protected long contadorReg;

	// Numero de registros que trae la consulta en la paginaci�n
	protected int numeroReg;

	// Grabar los roles con sus respectivos permisos
	// Trae el valor de VariablesSesion se procesa en @PostConstructor de cada
	// controlador
	protected BitSet rolPermiso = new BitSet(400);

	protected String formatoReporte = "pdf";
	protected List<Parametro> formatoReportes;

	protected VariablesSesion variablesSesion;

	@Inject
	protected ParametroRegisInt parametroRegis;

	@Inject
	protected ParametroListaInt parametroLista;

	@Inject
	protected GeneraJasperReportes generaJasperReportes;

	@Inject
	@LoggerSoem
	Logger logger;

	private static final long serialVersionUID = -7037705232541508763L;

	@PostConstruct
	public void buscarFormatoReportes() {

		variablesSesion = new VariablesSesion();

		Parametro parametro = new Parametro();

		variablesSesion = (VariablesSesion) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("variablesSesion");

		parametro.setCodigo("Parametro-FormatoReporte");
		parametro.setEstado(true);

		try {
			formatoReportes = parametroLista.buscar(parametro, null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
							"Excepcion - Error al buscar formato reportes"));
			e.printStackTrace();
		}

		this.rolPermiso = variablesSesion.getRolPermiso();
	}

	public String iniciarPagina() {
		this.pagina = 0;
		return null;
	}

	public String formatoPredet() {
		return "pdf";
	}

	public String nuevo() {
		return "registra?faces-redirect=true";
	}

	// Numero de filas en cada pagina
	public int getFilasPagina() {
		return variablesSesion.getFilasPagina();
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< GETTER & SETTER
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPagina() {
		return pagina;
	}

	public void setPagina(Integer pagina) {
		this.pagina = pagina;
	}

	public long getContadorReg() {
		return contadorReg;
	}

	public void setContadorReg(long contadorReg) {
		this.contadorReg = contadorReg;
	}

	public int getNumeroReg() {
		return numeroReg;
	}

	public void setNumeroReg(int numeroReg) {
		this.numeroReg = numeroReg;
	}

	public BitSet getRolPermiso() {
		return rolPermiso;
	}

	public void setRolPermiso(BitSet rolPermiso) {
		this.rolPermiso = rolPermiso;
	}

	public String getFormatoReporte() {
		return formatoReporte;
	}

	public void setFormatoReporte(String formatoReporte) {
		this.formatoReporte = formatoReporte;
	}

	public List<Parametro> getFormatoReportes() {
		return formatoReportes;
	}

	public void setFormatoReportes(List<Parametro> formatoReportes) {
		this.formatoReportes = formatoReportes;
	}

	public void setVariablesSesion(VariablesSesion variablesSesion) {
		this.variablesSesion = variablesSesion;
	}

	public VariablesSesion getVariablesSesion() {
		return variablesSesion;
	}

}
