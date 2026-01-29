package ec.com.tecnointel.soem.general.controlador;

import java.util.BitSet;
import java.util.List;

import ec.com.tecnointel.soem.egreso.modelo.PersClie;
import ec.com.tecnointel.soem.egreso.modelo.PersCobr;
import ec.com.tecnointel.soem.egreso.modelo.PersVend;
import ec.com.tecnointel.soem.parametro.modelo.SucuBode;
import ec.com.tecnointel.soem.parametro.modelo.SucuPrec;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolBode;
import ec.com.tecnointel.soem.seguridad.modelo.RolDocu;
import ec.com.tecnointel.soem.seguridad.modelo.RolFormPago;
import ec.com.tecnointel.soem.seguridad.modelo.RolPerm;
import ec.com.tecnointel.soem.seguridad.modelo.RolPrec;
import ec.com.tecnointel.soem.seguridad.modelo.RolSucu;
import jakarta.annotation.PostConstruct;

public class VariablesSesion {

	private int filasPagina;
	private int filasProductosIngreso;
	private int filasProductosEgreso;
	private int filasClientesEgreso;
	private int columnasProductos;
	private int tiempoEmisionDocuElec;
	private int diasAnulacionDocuElec;
	
	private boolean activarConsultaPrecio;
	private boolean activarCodigoBarra;
	private boolean activarDescri;
	private boolean activarPrecio;
	private boolean activarImagen;	

	private boolean activarIngrDetaCodigoBarra;
	private boolean activarIngrDetaDescri;
	private boolean activarIngrDetaPrecio;
	
	private boolean activarIngrDetaCostoUlti;
	private boolean activarIngrDetaCostoProm;
	
	private boolean mostrarCodigoIngrDeta;
	private boolean mostrarCodigoEgreDeta;

	private boolean activarMesa;
	private boolean acumularCantidad;
	private boolean anulaFactConsFina;
	
	private BitSet rolPermiso = new BitSet(400);
	
	private StringBuilder parametrosSmtp = new StringBuilder();
	private StringBuilder usuarioCorreo = new StringBuilder();
	private String rutaImagenCabecera;
	private String rutaImagenPie;
	private String rutaCertificado;
	private String direccionesAdicionales;
	
//	Codigos Impuestos SRI
	private String codigoIva;
	private String codigoIce;
	private String codigoIrbpnr;
	
	private String codigoReteIva;
	private String codigoReteRenta;
	private String codigoReteIsd;
	private Integer dimmIdIvaActual;
//	Fin Codigos Impuestos SRI
	
	//	Parámetros facturación electrónica
	String token;
	Integer esperaAuto;
	Boolean proxyActivar;
	String proxyIp;
	String proxyPuerto;
	String proxyUsuario;
	String proxyClave;
	String urlProduccion;
	String urlPruebas;
	String rutaCertificadoSerWebSri;
	String codiClav;
	String rutaGenerados;
	String rutaFirmados;
	String rutaEnviados;
	String rutaDevueltos;
	String rutaAutorizados;
	String rutaNoAutorizados;
	String rutaDescargados;
	String leyenda1;
	String leyenda2;
	String msgInfoAdicional;
	
	private String factElecVentaAuto;
	private String factElecCompraAuto;
//	Fin Parámetros facturación electrónica

	String ventaDetaAlCargar;
	String ventaDetaAlSeleProd1;
	String ventaDetaAlSeleProd2;

	private String generaTransaccion;
	private String variosCertificados;
	
	public String getVariosCertificados() {
		return variosCertificados;
	}

	public void setVariosCertificados(String variosCertificados) {
		this.variosCertificados = variosCertificados;
	}

	private Sucursal sucursal;
	private PersUsua persUsua;
	
	private PersClie persClie;
	private PersVend persVend;
	private PersCobr persCobr;

//	Graba todos los permisos de todos los roles al ingresar al sistema
//	Para consultar esta lista al momento de abrir cada una de las paginas del sistema
	private List<RolPerm> rolPerms;

//	Graba todos los documentos de todos los roles al ingresar al sistema
//	Para consultar esta lista al momento de abrir cada una de las paginas del sistema
	private List<RolDocu> rolDocus;
	
	private List<RolPrec> rolPrecs;
	private List<RolSucu> rolSucus;
	private List<RolBode> rolBodes;
	private List<RolFormPago> rolFormPagos;
	
	private List<SucuBode> sucuBodes;
	private List<SucuPrec> sucuPrecs;
	
	@PostConstruct
	public void cargar() {
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

	public List<RolBode> getRolBodes() {
		return rolBodes;
	}

	public void setRolBodes(List<RolBode> rolBodes) {
		this.rolBodes = rolBodes;
	}

	public List<RolFormPago> getRolFormPagos() {
		return rolFormPagos;
	}

	public void setRolFormPagos(List<RolFormPago> rolFormPagos) {
		this.rolFormPagos = rolFormPagos;
	}

	public List<SucuBode> getSucuBodes() {
		return sucuBodes;
	}

	public void setSucuBodes(List<SucuBode> sucuBodes) {
		this.sucuBodes = sucuBodes;
	}

	public List<SucuPrec> getSucuPrecs() {
		return sucuPrecs;
	}

	public void setSucuPrecs(List<SucuPrec> sucuPrecs) {
		this.sucuPrecs = sucuPrecs;
	}

	public PersUsua getPersUsua() {
		return persUsua;
	}

	public void setPersUsua(PersUsua persUsua) {
		this.persUsua = persUsua;
	}

	public BitSet getRolPermiso() {
		return rolPermiso;
	}

	public void setRolPermiso(BitSet rolPermiso) {
		this.rolPermiso = rolPermiso;
	}

	public int getFilasPagina() {
		return filasPagina;
	}

	public void setFilasPagina(int filasPagina) {
		this.filasPagina = filasPagina;
	}

	public PersClie getPersClie() {
		return persClie;
	}

	public void setPersClie(PersClie persClie) {
		this.persClie = persClie;
	}

	public PersVend getPersVend() {
		return persVend;
	}

	public void setPersVend(PersVend persVend) {
		this.persVend = persVend;
	}

	public int getFilasProductosIngreso() {
		return filasProductosIngreso;
	}

	public void setFilasProductosIngreso(int filasProductosIngreso) {
		this.filasProductosIngreso = filasProductosIngreso;
	}

	public int getFilasProductosEgreso() {
		return filasProductosEgreso;
	}

	public void setFilasProductosEgreso(int filasProductosEgreso) {
		this.filasProductosEgreso = filasProductosEgreso;
	}

	public int getFilasClientesEgreso() {
		return filasClientesEgreso;
	}

	public void setFilasClientesEgreso(int filasClientesEgreso) {
		this.filasClientesEgreso = filasClientesEgreso;
	}

	public PersCobr getPersCobr() {
		return persCobr;
	}

	public void setPersCobr(PersCobr persCobr) {
		this.persCobr = persCobr;
	}

	public StringBuilder getParametrosSmtp() {
		return parametrosSmtp;
	}

	public void setParametrosSmtp(StringBuilder parametrosSmtp) {
		this.parametrosSmtp = parametrosSmtp;
	}

	public StringBuilder getUsuarioCorreo() {
		return usuarioCorreo;
	}

	public void setUsuarioCorreo(StringBuilder usuarioCorreo) {
		this.usuarioCorreo = usuarioCorreo;
	}

	public String getRutaCertificado() {
		return rutaCertificado;
	}

	public void setRutaCertificado(String rutaCertificado) {
		this.rutaCertificado = rutaCertificado;
	}

	public String getDireccionesAdicionales() {
		return direccionesAdicionales;
	}

	public void setDireccionesAdicionales(String direccionesAdicionales) {
		this.direccionesAdicionales = direccionesAdicionales;
	}

	public String getRutaImagenCabecera() {
		return rutaImagenCabecera;
	}

	public void setRutaImagenCabecera(String rutaImagenCabecera) {
		this.rutaImagenCabecera = rutaImagenCabecera;
	}

	public String getRutaImagenPie() {
		return rutaImagenPie;
	}

	public void setRutaImagenPie(String rutaImagenPie) {
		this.rutaImagenPie = rutaImagenPie;
	}

	public int getColumnasProductos() {
		return columnasProductos;
	}

	public void setColumnasProductos(int columnasProductos) {
		this.columnasProductos = columnasProductos;
	}

	public String getCodigoIva() {
		return codigoIva;
	}

	public void setCodigoIva(String codigoIva) {
		this.codigoIva = codigoIva;
	}

	public String getCodigoIce() {
		return codigoIce;
	}

	public void setCodigoIce(String codigoIce) {
		this.codigoIce = codigoIce;
	}

	public String getCodigoIrbpnr() {
		return codigoIrbpnr;
	}

	public void setCodigoIrbpnr(String codigoIrbpnr) {
		this.codigoIrbpnr = codigoIrbpnr;
	}

	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	public String getCodigoReteIva() {
		return codigoReteIva;
	}

	public void setCodigoReteIva(String codigoReteIva) {
		this.codigoReteIva = codigoReteIva;
	}

	public String getCodigoReteRenta() {
		return codigoReteRenta;
	}

	public void setCodigoReteRenta(String codigoReteRenta) {
		this.codigoReteRenta = codigoReteRenta;
	}

	public String getCodigoReteIsd() {
		return codigoReteIsd;
	}

	public void setCodigoReteIsd(String codigoReteIsd) {
		this.codigoReteIsd = codigoReteIsd;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getEsperaAuto() {
		return esperaAuto;
	}

	public void setEsperaAuto(Integer esperaAuto) {
		this.esperaAuto = esperaAuto;
	}

	public Boolean getProxyActivar() {
		return proxyActivar;
	}

	public void setProxyActivar(Boolean proxyActivar) {
		this.proxyActivar = proxyActivar;
	}

	public String getProxyIp() {
		return proxyIp;
	}

	public void setProxyIp(String proxyIp) {
		this.proxyIp = proxyIp;
	}

	public String getProxyPuerto() {
		return proxyPuerto;
	}

	public void setProxyPuerto(String proxyPuerto) {
		this.proxyPuerto = proxyPuerto;
	}

	public String getProxyUsuario() {
		return proxyUsuario;
	}

	public void setProxyUsuario(String proxyUsuario) {
		this.proxyUsuario = proxyUsuario;
	}

	public String getProxyClave() {
		return proxyClave;
	}

	public void setProxyClave(String proxyClave) {
		this.proxyClave = proxyClave;
	}

	public String getUrlProduccion() {
		return urlProduccion;
	}

	public void setUrlProduccion(String urlProduccion) {
		this.urlProduccion = urlProduccion;
	}

	public String getUrlPruebas() {
		return urlPruebas;
	}

	public void setUrlPruebas(String urlPruebas) {
		this.urlPruebas = urlPruebas;
	}

	public String getCodiClav() {
		return codiClav;
	}

	public void setCodiClav(String codiClav) {
		this.codiClav = codiClav;
	}

	public String getRutaGenerados() {
		return rutaGenerados;
	}

	public void setRutaGenerados(String rutaGenerados) {
		this.rutaGenerados = rutaGenerados;
	}

	public String getRutaFirmados() {
		return rutaFirmados;
	}

	public void setRutaFirmados(String rutaFirmados) {
		this.rutaFirmados = rutaFirmados;
	}

	public String getRutaEnviados() {
		return rutaEnviados;
	}

	public void setRutaEnviados(String rutaEnviados) {
		this.rutaEnviados = rutaEnviados;
	}

	public String getRutaAutorizados() {
		return rutaAutorizados;
	}

	public void setRutaAutorizados(String rutaAutorizados) {
		this.rutaAutorizados = rutaAutorizados;
	}

	public String getRutaNoAutorizados() {
		return rutaNoAutorizados;
	}

	public void setRutaNoAutorizados(String rutaNoAutorizados) {
		this.rutaNoAutorizados = rutaNoAutorizados;
	}

	public String getRutaDevueltos() {
		return rutaDevueltos;
	}

	public void setRutaDevueltos(String rutaDevueltos) {
		this.rutaDevueltos = rutaDevueltos;
	}

	public String getRutaCertificadoSerWebSri() {
		return rutaCertificadoSerWebSri;
	}

	public void setRutaCertificadoSerWebSri(String rutaCertificadoSerWebSri) {
		this.rutaCertificadoSerWebSri = rutaCertificadoSerWebSri;
	}

	public boolean isActivarCodigoBarra() {
		return activarCodigoBarra;
	}

	public void setActivarCodigoBarra(boolean activarCodigoBarra) {
		this.activarCodigoBarra = activarCodigoBarra;
	}

	public boolean isActivarDescri() {
		return activarDescri;
	}

	public void setActivarDescri(boolean activarDescri) {
		this.activarDescri = activarDescri;
	}

	public boolean isActivarPrecio() {
		return activarPrecio;
	}

	public void setActivarPrecio(boolean activarPrecio) {
		this.activarPrecio = activarPrecio;
	}

	public boolean isActivarImagen() {
		return activarImagen;
	}

	public void setActivarImagen(boolean activarImagen) {
		this.activarImagen = activarImagen;
	}

	public boolean isActivarConsultaPrecio() {
		return activarConsultaPrecio;
	}

	public void setActivarConsultaPrecio(boolean activarConsultaPrecio) {
		this.activarConsultaPrecio = activarConsultaPrecio;
	}

	public String getVentaDetaAlCargar() {
		return ventaDetaAlCargar;
	}

	public void setVentaDetaAlCargar(String ventaDetaAlCargar) {
		this.ventaDetaAlCargar = ventaDetaAlCargar;
	}

	public String getVentaDetaAlSeleProd1() {
		return ventaDetaAlSeleProd1;
	}

	public void setVentaDetaAlSeleProd1(String ventaDetaAlSeleProd1) {
		this.ventaDetaAlSeleProd1 = ventaDetaAlSeleProd1;
	}

	public String getVentaDetaAlSeleProd2() {
		return ventaDetaAlSeleProd2;
	}

	public void setVentaDetaAlSeleProd2(String ventaDetaAlSeleProd2) {
		this.ventaDetaAlSeleProd2 = ventaDetaAlSeleProd2;
	}

	public boolean isActivarIngrDetaCodigoBarra() {
		return activarIngrDetaCodigoBarra;
	}

	public void setActivarIngrDetaCodigoBarra(boolean activarIngrDetaCodigoBarra) {
		this.activarIngrDetaCodigoBarra = activarIngrDetaCodigoBarra;
	}

	public boolean isActivarIngrDetaDescri() {
		return activarIngrDetaDescri;
	}

	public void setActivarIngrDetaDescri(boolean activarIngrDetaDescri) {
		this.activarIngrDetaDescri = activarIngrDetaDescri;
	}

	public boolean isActivarIngrDetaPrecio() {
		return activarIngrDetaPrecio;
	}

	public void setActivarIngrDetaPrecio(boolean activarIngrDetaPrecio) {
		this.activarIngrDetaPrecio = activarIngrDetaPrecio;
	}

	public boolean isActivarMesa() {
		return activarMesa;
	}

	public void setActivarMesa(boolean activarMesa) {
		this.activarMesa = activarMesa;
	}

	public boolean isActivarIngrDetaCostoUlti() {
		return activarIngrDetaCostoUlti;
	}

	public void setActivarIngrDetaCostoUlti(boolean activarIngrDetaCostoUlti) {
		this.activarIngrDetaCostoUlti = activarIngrDetaCostoUlti;
	}

	public boolean isActivarIngrDetaCostoProm() {
		return activarIngrDetaCostoProm;
	}

	public void setActivarIngrDetaCostoProm(boolean activarIngrDetaCostoProm) {
		this.activarIngrDetaCostoProm = activarIngrDetaCostoProm;
	}

	public String getLeyenda1() {
		return leyenda1;
	}

	public void setLeyenda1(String leyenda1) {
		this.leyenda1 = leyenda1;
	}

	public String getLeyenda2() {
		return leyenda2;
	}

	public void setLeyenda2(String leyenda2) {
		this.leyenda2 = leyenda2;
	}


	public boolean isAcumularCantidad() {
		return acumularCantidad;
	}

	public void setAcumularCantidad(boolean acumularCantidad) {
		this.acumularCantidad = acumularCantidad;
	}

	public String getMsgInfoAdicional() {
		return msgInfoAdicional;
	}

	public void setMsgInfoAdicional(String msgInfoAdicional) {
		this.msgInfoAdicional = msgInfoAdicional;
	}

	public boolean isMostrarCodigoIngrDeta() {
		return mostrarCodigoIngrDeta;
	}

	public void setMostrarCodigoIngrDeta(boolean mostrarCodigoIngrDeta) {
		this.mostrarCodigoIngrDeta = mostrarCodigoIngrDeta;
	}

	public boolean isMostrarCodigoEgreDeta() {
		return mostrarCodigoEgreDeta;
	}

	public void setMostrarCodigoEgreDeta(boolean mostrarCodigoEgreDeta) {
		this.mostrarCodigoEgreDeta = mostrarCodigoEgreDeta;
	}

	public String getGeneraTransaccion() {
		return generaTransaccion;
	}

	public void setGeneraTransaccion(String generaTransaccion) {
		this.generaTransaccion = generaTransaccion;
	}

	public String getRutaDescargados() {
		return rutaDescargados;
	}

	public void setRutaDescargados(String rutaDescargados) {
		this.rutaDescargados = rutaDescargados;
	}
	
	public Integer getDimmIdIvaActual() {
		return dimmIdIvaActual;
	}

	public void setDimmIdIvaActual(Integer dimmIdIvaActual) {
		this.dimmIdIvaActual = dimmIdIvaActual;
	}

	public String getFactElecVentaAuto() {
		return factElecVentaAuto;
	}

	public void setFactElecVentaAuto(String factElecVentaAuto) {
		this.factElecVentaAuto = factElecVentaAuto;
	}

	public String getFactElecCompraAuto() {
		return factElecCompraAuto;
	}

	public void setFactElecCompraAuto(String factElecCompraAuto) {
		this.factElecCompraAuto = factElecCompraAuto;
	}

	public int getTiempoEmisionDocuElec() {
		return tiempoEmisionDocuElec;
	}

	public void setTiempoEmisionDocuElec(int tiempoEmisionDocuElec) {
		this.tiempoEmisionDocuElec = tiempoEmisionDocuElec;
	}
	
	public int getDiasAnulacionDocuElec() {
		return diasAnulacionDocuElec;
	}

	public void setDiasAnulacionDocuElec(int diasAnulacionDocuElec) {
		this.diasAnulacionDocuElec = diasAnulacionDocuElec;
	}

	public boolean isAnulaFactConsFina() {
		return anulaFactConsFina;
	}

	public void setAnulaFactConsFina(boolean anulaFactConsFina) {
		this.anulaFactConsFina = anulaFactConsFina;
	}

}
