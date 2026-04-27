package ec.com.tecnointel.soem.ingreso.registroInt;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.ingreso.modelo.IngrDeta;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDetaImpu;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDetaPrec;
import ec.com.tecnointel.soem.ingreso.modelo.IngrDimm;
import ec.com.tecnointel.soem.ingreso.modelo.Ingreso;
import ec.com.tecnointel.soem.ingreso.modelo.PersProv;
import ec.com.tecnointel.soem.ingreso.modelo.PersProvDimm;
import ec.com.tecnointel.soem.ingreso.modelo.ProvGrup;
import ec.com.tecnointel.soem.ingreso.modelo.Retencion;
import ec.com.tecnointel.soem.inventario.modelo.KardTotaView;
import ec.com.tecnointel.soem.inventario.modelo.Kardex;
import ec.com.tecnointel.soem.inventario.modelo.ProdBode;
import ec.com.tecnointel.soem.inventario.modelo.ProdCost;
import ec.com.tecnointel.soem.inventario.modelo.ProdDimm;
import ec.com.tecnointel.soem.inventario.modelo.ProdPrec;
import ec.com.tecnointel.soem.inventario.modelo.Producto;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import ec.com.tecnointel.soem.parametro.modelo.DocuIngr;
import ec.com.tecnointel.soem.parametro.modelo.DocuMoviIngr;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.FormPago;
import ec.com.tecnointel.soem.parametro.modelo.Precio;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolDocu;
import ec.com.tecnointel.soem.seguridad.modelo.RolFormPago;
import ec.com.tecnointel.soem.seguridad.modelo.RolPersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolPrec;
import ec.com.tecnointel.soem.seguridad.modelo.RolSucu;
import ec.com.tecnointel.soem.tesoreria.modelo.Cxp;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviIngr;
import ec.com.tecnointel.soem.tesoreria.modelo.PagoDeta;
import jakarta.ejb.Local;

@Local
public interface CompraInt {

	public Integer insertarPersProv(PersProv persProv) throws Exception;

	public void modificarPersProv(PersProv persProv) throws Exception;

	public List<ProvGrup> buscarProvGrups(ProvGrup provGrup, Integer pagina) throws Exception;

	public List<PersProv> buscarPersProvs(PersProv persProv, Integer pagina, int filasPagina) throws Exception;

	public long contarRegistrosProv(PersProv persProv) throws Exception;

	public Ingreso buscarIngresoPorId(Integer id) throws Exception;

	public List<Ingreso> buscarIngresos(Ingreso ingreso) throws Exception;

	public Long contarIngresos(Ingreso ingreso) throws Exception;

	public List<Producto> buscarProductos(Producto producto, Integer pagina, Integer filas, String ordenColumna)
			throws Exception;

	public int tamanioProductos() throws Exception;

	public long contarProductos(Producto producto) throws Exception;

	public Producto buscarProductoPorId(Integer productoId) throws Exception;

	public List<RolPrec> buscarRolPrecios(RolPrec rolPrec) throws Exception;

	public void grabar(Ingreso ingreso) throws Exception;

	public void modificar(Ingreso ingreso) throws Exception;

	public void eliminar(Ingreso ingreso) throws Exception;

	public void anularIngreso(Ingreso ingreso) throws Exception;

	public List<IngrDeta> buscarIngrDetas(IngrDeta ingrDeta, Integer pagina) throws Exception;

	public void eliminarIngrDeta(IngrDeta ingrDeta) throws Exception;

	public IngrDeta buscarIngrDetaPorId(IngrDeta ingrDeta) throws Exception;

	public List<ProdDimm> buscarProdDimms(ProdDimm prodDimm) throws Exception;

	public List<Dimm> buscarSustentos(Dimm dimmDesde, Dimm dimmHasta) throws Exception;

	public List<Dimm> buscarDimms(Dimm dimmDesde, Dimm dimmHasta) throws Exception;

	public void modificarDocumento(Documento documento) throws Exception;

	public List<DocuIngr> buscarDocuIngrs(DocuIngr docuIngr) throws Exception;

	public List<DocuIngr> filtrarDocuIngrs(List<DocuIngr> docuIngrs, PersUsua persUsua, List<RolDocu> rolDocus)
			throws Exception;

	public List<Cxp> buscarCxps(Cxp cxp, Integer pagina, int filasPagina) throws Exception;

	public long contarCxps(Cxp cxp) throws Exception;

	public List<Cxp> generarCxp(Ingreso ingreso, BigDecimal total, Boolean estado) throws Exception;

	public Integer insertarCxp(List<Cxp> cxps) throws Exception;

	public void modificarCxp(Cxp cxp) throws Exception;

	public void anularCxp(Cxp cxp) throws Exception;

	public void eliminarCxp(Cxp cxp) throws Exception;

	public List<IngrDetaImpu> buscarIngrDetaImpus(IngrDetaImpu ingrDetaImpu) throws Exception;

	public void insertarIngrDetaImpu(IngrDetaImpu ingrDetaImpu) throws Exception;

	public void eliminarIngrDetaImpu(IngrDetaImpu ingrDetaImpu) throws Exception;

	public void grabarIngrDeta(IngrDeta ingrDeta) throws Exception;

	public void modificarIngrDeta(IngrDeta ingrDeta) throws Exception;

	public Dimm buscarDimmPorId(Integer dimmId) throws Exception;

	public void actualizarProdCost(List<ProdCost> prodCosts) throws Exception;

	public void actualizarProdPrec(List<ProdPrec> prodPrecs) throws Exception;

	public List<ProdCost> buscarProdCosts(ProdCost prodCost) throws Exception;

	public List<ProdBode> buscarProdBodes(ProdBode prodBode) throws Exception;

	public List<ProdPrec> buscarProdPrecs(ProdPrec prodPrec, Integer pagina, Integer filas, String ordenColumna)
			throws Exception;

	public List<ProdPrec> buscarProdPrecs(Set<Sucursal> sucursals, Set<Precio> preciosSet, ProdPrec prodPrec,
			Integer filas, String ordenColumna) throws Exception;

	public int tamanioProdPrecs() throws Exception;

	public long contarProdPrecs(ProdPrec prodPrec) throws Exception;

	public List<ProdPrec> filtrarProdPrecs(List<ProdPrec> ProdPrecs, PersUsua persUsua, List<RolPrec> rolPrecs,
			Sucursal sucursal) throws Exception;

	public List<IngrDetaPrec> buscarIngrDetaPrecs(IngrDetaPrec ingrDetaPrec) throws Exception;

	public List<IngrDetaPrec> filtrarIngrDetaPrecs(List<IngrDetaPrec> ingrDetaPrecs, PersUsua persUsua,
			List<RolPrec> rolPrecs, Sucursal sucursal) throws Exception;

	public List<Kardex> buscarKardexs(Kardex kardex) throws Exception;

//	public List<Kardex> buscarKardexs(IngrDeta ingrDeta) throws Exception;

	public void grabarKardex(Kardex kardex) throws Exception;

	public void modificarKardex(Kardex kardex) throws Exception;

	public void eliminarKardex(Kardex kardex) throws Exception;

	public List<KardTotaView> buscarKardTotaViews(KardTotaView kardTotaView) throws Exception;

	public List<KardTotaView> buscarKardTotaViews(List<Integer> sucursals, KardTotaView kardTotaView) throws Exception;

	public List<RolSucu> buscarRolSucus(Set<RolPersUsua> rolPersUsuas) throws Exception;

	public List<Retencion> buscarRetencions(Retencion retencion) throws Exception;

	public Retencion buscarRetencionPorId(Integer retencionId) throws Exception;

	public Integer insertarRetencion(Retencion retencion) throws Exception;

	public void modificarRetencion(Retencion retencion) throws Exception;

	public void anularRetencion(List<Retencion> retencions) throws Exception;

	public List<DocuMoviIngr> buscarDocuMoviIngrs() throws Exception;

	public DocuMoviIngr buscarDocuMoviIngrPred(List<DocuMoviIngr> docuMoviIngrs, List<RolDocu> rolDocus)
			throws Exception;

	public List<DocuMoviIngr> filtrarDocuMoviIngrs(List<DocuMoviIngr> docuMoviIngrs, PersUsua persUsuaSesion,
			List<RolDocu> rolDocus) throws Exception;

	public List<FormPago> buscarFormPago() throws Exception;

	public List<FormPago> filtrarFormPagos(List<FormPago> formPagos, PersUsua persUsuaSesion,
			List<RolFormPago> rolFormPagos) throws Exception;

	public FormPagoMoviIngr buscarFpmisPorId(Integer fpmiId) throws Exception;

	public List<FormPagoMoviIngr> buscarFpmis(FormPagoMoviIngr fpmi) throws Exception;

	public Integer insertarFpmi(FormPagoMoviIngr formPagoMoviIngr) throws Exception;

	public void eliminarFpmi(FormPagoMoviIngr formPagoMoviIngr) throws Exception;

	public void modificarFpmi(FormPagoMoviIngr formPagoMoviIngr) throws Exception;

	public List<PagoDeta> buscarPagoDetas(PagoDeta pagoDeta) throws Exception;

	public void insertarPagoDeta(PagoDeta pagoDeta) throws Exception;

	public void eliminarPagoDeta(PagoDeta pagoDeta) throws Exception;

	public BigDecimal sumarCxp(Cxp cxp) throws Exception;

	public BigDecimal sumarPagoDeta(PagoDeta pagoDeta) throws Exception;

	public PersProv buscarPersProvPorId(PersProv persProv) throws Exception;

	List<PersProvDimm> buscarPersProvDimms(PersProvDimm persProvDimm, Integer pagina) throws Exception;

	void insertarIngrDimm(IngrDimm ingrDimm) throws Exception;
	
	void eliminarIngrDimm(IngrDimm ingrDimm) throws Exception;

	List<IngrDimm> buscarIngrDimms(IngrDimm ingrDimm, Integer pagina) throws Exception;

	List<Object[]> buscarPagosPorIngresoId(PagoDeta pagoDeta) throws Exception;

	List<Object[]> buscarPagosPorFpmeId(List<Object[]> objs, Ingreso ingreso) throws Exception;

	public boolean existeNumeroAutorizacion(String trim);

}
