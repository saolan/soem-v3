package ec.com.tecnointel.soem.egreso.registroInt;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.caja.modelo.CajaDocuEgre;
import ec.com.tecnointel.soem.caja.modelo.CajaMovi;
import ec.com.tecnointel.soem.caja.modelo.CajaPeri;
import ec.com.tecnointel.soem.caja.modelo.SaliArch;
import ec.com.tecnointel.soem.egreso.modelo.ClieGrup;
import ec.com.tecnointel.soem.egreso.modelo.EgreDeta;
import ec.com.tecnointel.soem.egreso.modelo.EgreDetaImpu;
import ec.com.tecnointel.soem.egreso.modelo.Egreso;
import ec.com.tecnointel.soem.egreso.modelo.PersClie;
import ec.com.tecnointel.soem.egreso.modelo.PersCobr;
import ec.com.tecnointel.soem.egreso.modelo.PersVend;
import ec.com.tecnointel.soem.inventario.modelo.KardTotaView;
import ec.com.tecnointel.soem.inventario.modelo.Kardex;
import ec.com.tecnointel.soem.inventario.modelo.ProdBode;
import ec.com.tecnointel.soem.inventario.modelo.ProdCost;
import ec.com.tecnointel.soem.inventario.modelo.ProdDimm;
import ec.com.tecnointel.soem.inventario.modelo.ProdPrec;
import ec.com.tecnointel.soem.inventario.modelo.ProdSubp;
import ec.com.tecnointel.soem.inventario.modelo.Producto;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import ec.com.tecnointel.soem.parametro.modelo.DocuEgre;
import ec.com.tecnointel.soem.parametro.modelo.DocuMoviEgre;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.FormPago;
import ec.com.tecnointel.soem.parametro.modelo.Mesa;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolDocu;
import ec.com.tecnointel.soem.seguridad.modelo.RolFormPago;
import ec.com.tecnointel.soem.seguridad.modelo.RolPersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolPrec;
import ec.com.tecnointel.soem.seguridad.modelo.RolSucu;
import ec.com.tecnointel.soem.tesoreria.modelo.CobrDeta;
import ec.com.tecnointel.soem.tesoreria.modelo.Cxc;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviEgre;
import ec.com.tecnointel.soem.tesoreria.modelo.FpmeFormPago;
import jakarta.ejb.Local;

@Local
public interface VentaInt {
	
	public Integer insertarPersCLie(PersClie persClie) throws Exception;
	
	public void modificarPersClie(PersClie persClie) throws Exception;
		
	public List<ClieGrup> buscarClieGrups(ClieGrup clieGrup, Integer pagina) throws Exception;
	
	public List<PersClie> buscarPersClies(PersClie persClie, Integer pagina, Integer filas) throws Exception;
	
	public long contarRegistrosClie(PersClie persClie) throws Exception;
	
	public List<PersVend> buscarPersVends(PersVend persVend) throws Exception;

	public DocuEgre buscarDocuEgrePorId(Integer id) throws Exception;
	
	public Egreso buscarEgresoPorId(Integer id) throws Exception;
	
	public List<Egreso> buscarEgresos(Egreso egreso, Set<Integer> sucursals) throws Exception;

	public Long contarEgresos(Egreso egreso, Set<Integer> sucursals) throws Exception;
	

	public List<Producto> buscarProductos(Producto producto, Integer pagina, Integer filas, String ordenColumna) throws Exception;

	public int tamanioProductos() throws Exception;

	public long contarProductos(Producto producto) throws Exception;

	public Producto buscarProductoPorId(Integer productoId) throws Exception;
	

	public Integer grabar(Egreso egreso) throws Exception;

	public void modificar(Egreso egreso) throws Exception;

	public void eliminar(Egreso egreso) throws Exception;
	
	public void anularEgreso(Egreso egreso) throws Exception;
	

	public List<EgreDeta> buscarEgreDetas(EgreDeta egreDeta, Integer pagina) throws Exception;

	public void eliminarEgreDeta(EgreDeta egreDeta) throws Exception;

	public EgreDeta buscarEgreDetaPorId(EgreDeta egreDeta) throws Exception;
	

	public Dimm buscarDimmPorId(Integer dimmId) throws Exception;
	
	public List<ProdDimm> buscarProdDimms(ProdDimm prodDimm) throws Exception;

//	public List<Dimm> buscarSustentos(Dimm dimmDesde, Dimm dimmHasta) throws Exception;
	
	public List<Dimm> buscarDimms(Dimm dimmDesde, Dimm dimmHasta) throws Exception;	

	public List<DocuEgre> buscarDocuEgres(DocuEgre docuEgre) throws Exception;
	
	public List<DocuEgre> filtrarDocuEgres(List<DocuEgre> docuEgres, PersUsua persUsua, List<RolDocu> rolDocus) throws Exception;
	

//	public List<Cxc> buscarCxcs(Cxc cxp) throws Exception;
	
	public List<Cxc> buscarCxcs(Cxc cxc, Integer pagina, int filasPagina) throws Exception;
	
	public long contarCxcs(Cxc cxc) throws Exception;
	
	public List<Cxc> generarCxc(Egreso egreso, BigDecimal total, Boolean estado) throws Exception;
	
	public Cxc generarCxc(Egreso egreso, FpmeFormPago fpmeFormPago, Boolean estado) throws Exception;
	
	public Integer insertarCxc(Cxc cxc) throws Exception;
	
	public void modificarCxc(Cxc cxc) throws Exception;
	
	public void anularCxc(Cxc cxc) throws Exception;

	public void eliminarCxc(Cxc cxc) throws Exception;
	
	
	public List<EgreDetaImpu> buscarEgreDetaImpus(EgreDetaImpu egreDetaImpu) throws Exception;

	public void grabarEgreDeta(EgreDeta egreDeta) throws Exception;

	public void modificarEgreDeta(EgreDeta egreDeta) throws Exception;
	
	

	public List<ProdCost> buscarProdCosts(ProdCost prodCost) throws Exception;

	public List<ProdBode> buscarProdBodes(ProdBode prodBode) throws Exception;

	public List<ProdPrec> buscarProdPrecs(ProdPrec prodPrec, Integer pagina, Integer filas, String ordenColumna) throws Exception;

	public int tamanioProdPrecs() throws Exception;

	public long contarProdPrecs(ProdPrec prodPrec) throws Exception;
	
//	public List<Precio> filtrarPrecios(List<Precio> Precios, PersUsua persUsua) throws Exception;
	
	public List<ProdPrec> filtrarProdPrecs(List<ProdPrec> ProdPrecs, PersUsua persUsua, List<RolPrec> rolPrecs,
			Sucursal sucursal) throws Exception;
	
	
	public List<Kardex> buscarKardexs(Kardex kardex) throws Exception;
	
	public void grabarKardex(Kardex kardex) throws Exception;
	
	public void modificarKardex(Kardex kardex) throws Exception;
	
	public void eliminarKardex(Kardex kardex) throws Exception;
	
	
	
	public List<KardTotaView> buscarKardTotaViews(KardTotaView kardTotaView) throws Exception;
	
	public List<KardTotaView> buscarKardTotaViews(List<Integer> sucursals, KardTotaView kardTotaView) throws Exception;

	public List<CajaMovi> buscarSesionVentas(CajaMovi cajaMovi) throws Exception;
	
	
	public List<RolPrec> buscarRolPrecios(RolPrec rolPrec) throws Exception;

	public List<FormPago> buscarFormPago() throws Exception;
	
	public List<FormPago> filtrarFormPagos(List<FormPago> formPagos, PersUsua persUsuaSesion, List<RolFormPago> rolFormPagos)
			throws Exception;
	

	public List<PersCobr> buscarPersCobrs() throws Exception;

	public void modificarDocumento(Documento documento) throws Exception;

	public List<DocuMoviEgre> buscarDocuMoviEgres() throws Exception;
	
	public DocuMoviEgre buscarDocuMoviEgrePred(List<DocuMoviEgre> docuMoviEgres, List<RolDocu> rolDocus) throws Exception;
	
	public List<DocuMoviEgre> filtrarDocuMoviEgres(List<DocuMoviEgre> docuMoviEgres, PersUsua persUsuaSesion,
			List<RolDocu> rolDocus) throws Exception;
	
	public List<FormPagoMoviEgre> buscarFpmes(FormPagoMoviEgre fpme) throws Exception;
	
	public FormPagoMoviEgre buscarFpmesPorId(Integer fpmeId) throws Exception;
	
	public Integer insertarFpme(FormPagoMoviEgre formPagoMoviEgre) throws Exception;
	
	public void modificarFpme(FormPagoMoviEgre fpme) throws Exception;

	public void eliminarFpme(FormPagoMoviEgre formPagoMoviEgre) throws Exception;
	
	
	public List<CobrDeta> buscarCobrDetas(CobrDeta cobrDeta) throws Exception;

	public void insertarCobrDeta(CobrDeta cobrDeta) throws Exception;
	
	public void eliminarCobrDeta(CobrDeta cobrDeta) throws Exception;
	

	public List<CajaDocuEgre> buscarCajaDocuEgre(CajaDocuEgre cajaDocuEgre) throws Exception;
	
	public void modificarCajaDocuEgre(CajaDocuEgre cajaDocuEgre) throws Exception;

	public List<CajaPeri> buscarCajaPeri(CajaPeri cajaPeri) throws Exception;

	public List<RolSucu> buscarRolSucus(Set<RolPersUsua> rolPersUsuas) throws Exception;

	public List<SaliArch> buscarSaliArchs(SaliArch saliArch) throws Exception;

	public BigDecimal sumarCxc(Cxc cxc) throws Exception;

	public BigDecimal sumarCobrDeta(CobrDeta cobrDeta) throws Exception;

	public List<Mesa> buscarMesas(Mesa mesa) throws Exception;


	public Integer insertarFpmeFormPago(FpmeFormPago fpmeFormPago) throws Exception;
	
	public FpmeFormPago buscarFpmeFormPagoPorId(Integer id) throws Exception;

	public void eliminarFpmeFormPago(FpmeFormPago fpmeFormPago) throws Exception;

	public List<FpmeFormPago> buscarFpmeFormPagos(FpmeFormPago fpmeFormPago) throws Exception;

	List<ProdSubp> buscarProdSubps(ProdSubp prodSubp, Integer pagina) throws Exception;


	List<Object[]> buscarCobrosPorEgresoId(CobrDeta cobrDeta) throws Exception;

	List<Object[]> buscarCobrosPorFpmeFpId(List<Object[]> objs, Egreso egreso) throws Exception;

	List<Object[]> buscarFpmeRefere(String refere) throws Exception;

	long contarEgresosNoAutorizados(Egreso egreso, LocalDate fechaEmisDesde,
			LocalDate fechaEmisHasta, Set<Integer> sucursals) throws Exception;


}
