package ec.com.tecnointel.soem.egreso.registroImp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ec.com.tecnointel.soem.caja.listaInt.CajaDocuEgreListaInt;
import ec.com.tecnointel.soem.caja.listaInt.CajaMoviListaInt;
import ec.com.tecnointel.soem.caja.listaInt.CajaPeriListaInt;
import ec.com.tecnointel.soem.caja.listaInt.SaliArchListaInt;
import ec.com.tecnointel.soem.caja.modelo.CajaDocuEgre;
import ec.com.tecnointel.soem.caja.modelo.CajaMovi;
import ec.com.tecnointel.soem.caja.modelo.CajaPeri;
import ec.com.tecnointel.soem.caja.modelo.SaliArch;
import ec.com.tecnointel.soem.caja.registroInt.CajaDocuEgreRegisInt;
import ec.com.tecnointel.soem.egreso.listaInt.ClieGrupListaInt;
import ec.com.tecnointel.soem.egreso.listaInt.EgreDetaImpuListaInt;
import ec.com.tecnointel.soem.egreso.listaInt.EgreDetaListaInt;
import ec.com.tecnointel.soem.egreso.listaInt.EgresoListaInt;
import ec.com.tecnointel.soem.egreso.listaInt.PersClieListaInt;
import ec.com.tecnointel.soem.egreso.listaInt.PersCobrListaInt;
import ec.com.tecnointel.soem.egreso.listaInt.PersVendListaInt;
import ec.com.tecnointel.soem.egreso.modelo.ClieGrup;
import ec.com.tecnointel.soem.egreso.modelo.EgreDeta;
import ec.com.tecnointel.soem.egreso.modelo.EgreDetaImpu;
import ec.com.tecnointel.soem.egreso.modelo.Egreso;
import ec.com.tecnointel.soem.egreso.modelo.PersClie;
import ec.com.tecnointel.soem.egreso.modelo.PersCobr;
import ec.com.tecnointel.soem.egreso.modelo.PersVend;
import ec.com.tecnointel.soem.egreso.registroInt.EgreDetaRegisInt;
import ec.com.tecnointel.soem.egreso.registroInt.EgresoRegisInt;
import ec.com.tecnointel.soem.egreso.registroInt.PersClieRegisInt;
import ec.com.tecnointel.soem.egreso.registroInt.VentaInt;
import ec.com.tecnointel.soem.inventario.listaInt.KardTotaViewListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.KardexListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdBodeListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdCostListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdDimmListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdPrecListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProdSubpListaInt;
import ec.com.tecnointel.soem.inventario.listaInt.ProductoListaInt;
import ec.com.tecnointel.soem.inventario.modelo.KardTotaView;
import ec.com.tecnointel.soem.inventario.modelo.Kardex;
import ec.com.tecnointel.soem.inventario.modelo.ProdBode;
import ec.com.tecnointel.soem.inventario.modelo.ProdCost;
import ec.com.tecnointel.soem.inventario.modelo.ProdDimm;
import ec.com.tecnointel.soem.inventario.modelo.ProdPrec;
import ec.com.tecnointel.soem.inventario.modelo.ProdSubp;
import ec.com.tecnointel.soem.inventario.modelo.Producto;
import ec.com.tecnointel.soem.inventario.registroInt.KardexRegisInt;
import ec.com.tecnointel.soem.inventario.registroInt.ProdBodeRegisInt;
import ec.com.tecnointel.soem.inventario.registroInt.ProdCostRegisInt;
import ec.com.tecnointel.soem.inventario.registroInt.ProdPrecRegisInt;
import ec.com.tecnointel.soem.inventario.registroInt.ProductoRegisInt;
import ec.com.tecnointel.soem.parametro.listaInt.DimmListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.DocuEgreListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.DocuMoviEgreListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.FormPagoListaInt;
import ec.com.tecnointel.soem.parametro.listaInt.MesaListaInt;
import ec.com.tecnointel.soem.parametro.modelo.Dimm;
import ec.com.tecnointel.soem.parametro.modelo.DocuEgre;
import ec.com.tecnointel.soem.parametro.modelo.DocuMoviEgre;
import ec.com.tecnointel.soem.parametro.modelo.Documento;
import ec.com.tecnointel.soem.parametro.modelo.FormPago;
import ec.com.tecnointel.soem.parametro.modelo.Mesa;
import ec.com.tecnointel.soem.parametro.modelo.Persona;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import ec.com.tecnointel.soem.parametro.registroInt.DimmRegisInt;
import ec.com.tecnointel.soem.parametro.registroInt.DocuEgreRegisInt;
import ec.com.tecnointel.soem.parametro.registroInt.DocumentoRegisInt;
import ec.com.tecnointel.soem.parametro.registroInt.ParametroRegisInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolPrecListaInt;
import ec.com.tecnointel.soem.seguridad.listaInt.RolSucuListaInt;
import ec.com.tecnointel.soem.seguridad.modelo.PersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolDocu;
import ec.com.tecnointel.soem.seguridad.modelo.RolFormPago;
import ec.com.tecnointel.soem.seguridad.modelo.RolPersUsua;
import ec.com.tecnointel.soem.seguridad.modelo.RolPrec;
import ec.com.tecnointel.soem.seguridad.modelo.RolSucu;
import ec.com.tecnointel.soem.tesoreria.listaInt.CobrDetaListaInt;
import ec.com.tecnointel.soem.tesoreria.listaInt.CxcListaInt;
import ec.com.tecnointel.soem.tesoreria.listaInt.FormPagoMoviEgreListaInt;
import ec.com.tecnointel.soem.tesoreria.listaInt.FpmeFormPagoListaInt;
import ec.com.tecnointel.soem.tesoreria.modelo.CobrDeta;
import ec.com.tecnointel.soem.tesoreria.modelo.Cxc;
import ec.com.tecnointel.soem.tesoreria.modelo.FormPagoMoviEgre;
import ec.com.tecnointel.soem.tesoreria.modelo.FpmeFormPago;
import ec.com.tecnointel.soem.tesoreria.registroInt.CobrDetaRegisInt;
import ec.com.tecnointel.soem.tesoreria.registroInt.CxcRegisInt;
import ec.com.tecnointel.soem.tesoreria.registroInt.FormPagoMoviEgreRegisInt;
import ec.com.tecnointel.soem.tesoreria.registroInt.FpmeFormPagoRegisInt;
import jakarta.ejb.Stateful;
import jakarta.inject.Inject;

@Stateful
public class VentaImp implements VentaInt {

	private List<Producto> productos;
	private List<ProdPrec> prodPrecs;

	@Inject
	PersClieListaInt persClieLista;

	@Inject
	PersClieRegisInt persClieRegis;

	@Inject
	PersVendListaInt persVendLista;

	@Inject
	DocuEgreRegisInt docuEgreRegis;

	@Inject
	EgresoListaInt egresoLista;

	@Inject
	EgresoRegisInt egresoRegis;

	@Inject
	EgreDetaListaInt egreDetaLista;

	@Inject
	EgreDetaRegisInt egreDetaRegis;

	@Inject
	EgreDetaImpuListaInt egreDetaImpuLista;

	@Inject
	ProductoListaInt productoLista;

	@Inject
	ProductoRegisInt productoRegis;

	@Inject
	ProdCostListaInt prodCostLista;

	@Inject
	ProdCostRegisInt prodCostRegis;

	@Inject
	ProdBodeListaInt prodBodeLista;

	@Inject
	ProdBodeRegisInt prodBodeRegis;

	@Inject
	ProdPrecListaInt prodPrecLista;

	@Inject
	ProdPrecRegisInt prodPrecRegis;

	@Inject
	ProdDimmListaInt prodDimmLista;

	@Inject
	DimmListaInt dimmLista;

	@Inject
	DimmRegisInt dimmRegis;

	@Inject
	DocuEgreListaInt docuEgreLista;

	@Inject
	CajaMoviListaInt cajaMoviLista;

//	@Inject
//	ProdPrecListaInt ProdPrecLista;

	@Inject
	RolPrecListaInt rolPrecLista;

	@Inject
	KardexRegisInt kardexRegis;

	@Inject
	KardexListaInt kardexLista;

	@Inject
	KardTotaViewListaInt kardTotaViewLista;

	@Inject
	ParametroRegisInt parametroRegis;

	@Inject
	FormPagoListaInt formPagoLista;

	@Inject
	PersCobrListaInt persCobrLista;

	@Inject
	CxcListaInt cxcLista;

	@Inject
	CxcRegisInt cxcRegis;

	@Inject
	DocuMoviEgreListaInt docuMoviEgreLista;

	@Inject
	DocumentoRegisInt documentoRegis;

	@Inject
	FormPagoMoviEgreRegisInt formPagoMoviEgreRegis;

	@Inject
	FormPagoMoviEgreListaInt formPagoMoviEgreLista;

	@Inject
	FpmeFormPagoRegisInt fpmeFormPagoRegis;

	@Inject
	FpmeFormPagoListaInt fpmeFormPagoLista;

	@Inject
	CobrDetaListaInt cobrDetaLista;

	@Inject
	CobrDetaRegisInt cobrDetaRegis;

	@Inject
	CajaDocuEgreListaInt cajaDocuEgreLista;

	@Inject
	CajaDocuEgreRegisInt cajaDocuEgreRegis;

	@Inject
	CajaPeriListaInt cajaPeriLista;

	@Inject
	RolSucuListaInt rolSucuLista;

	@Inject
	SaliArchListaInt saliArchLista;

	@Inject
	MesaListaInt mesaLista;

	@Inject
	ClieGrupListaInt clieGrupLista;

	@Inject
	ProdSubpListaInt prodSubpLista;

	@Override
	public Integer insertarPersCLie(PersClie persClie) throws Exception {
		Object id = null;
		id = persClieRegis.insertar(persClie);
		return (Integer) id;
	}

	@Override
	public void modificarPersClie(PersClie persClie) throws Exception {
		persClieRegis.modificar(persClie);
	}

	@Override
	public List<ClieGrup> buscarClieGrups(ClieGrup clieGrup, Integer pagina) throws Exception {
		return clieGrupLista.buscar(clieGrup, pagina);
	}

	@Override
	public List<PersClie> buscarPersClies(PersClie persClie, Integer pagina, Integer filas) throws Exception {
		return persClieLista.buscar(persClie, pagina, filas);
	}

	@Override
	public long contarRegistrosClie(PersClie persClie) throws Exception {
		return persClieLista.contarRegistros(persClie);
	}

	@Override
	public List<PersVend> buscarPersVends(PersVend persVend) throws Exception {
		return persVendLista.buscar(persVend, null);
	}

	@Override
	public DocuEgre buscarDocuEgrePorId(Integer id) throws Exception {
		return docuEgreRegis.buscarPorId(DocuEgre.class, id);
	}

	@Override
	public Egreso buscarEgresoPorId(Integer id) throws Exception {
		return egresoRegis.buscarPorId(Egreso.class, id);
	}

	@Override
	public List<Egreso> buscarEgresos(Egreso egreso, Set<Integer> sucursals) throws Exception {
		return egresoLista.buscar(egreso, null, sucursals);
	}

	@Override
	public Long contarEgresos(Egreso egreso, Set<Integer> sucursals) throws Exception {
		return egresoLista.contarRegistros(egreso, sucursals);
	}

	@Override
	public Integer grabar(Egreso egreso) throws Exception {
		Object id = null;
		id = egresoRegis.insertar(egreso);
		return (Integer) id;
	}

	@Override
	public void modificar(Egreso egreso) throws Exception {
		egresoRegis.modificar(egreso);
	}

	@Override
	public void eliminar(Egreso egreso) throws Exception {
		egresoRegis.eliminar(egreso);
	}

	@Override
	public void anularEgreso(Egreso egreso) throws Exception {

//		Buscar egreDeta sin subProductos
		EgreDeta egreDetaBuscar = new EgreDeta(egreso, new EgreDeta());

		List<EgreDeta> egreDetas = this.buscarEgreDetas(egreDetaBuscar, null);
		for (EgreDeta egreDetaEliminar : egreDetas) {

			EgreDeta egreDetaSubProducto = new EgreDeta(egreso, egreDetaEliminar);

			List<EgreDeta> egreDetaSubProductos = this.buscarEgreDetas(egreDetaSubProducto, null);

//			Puede ir esto para saber que es combo y mejorar el proceso ????
//			if (egreDeta.getProducto().isCombo()) {
//			}

//			Elimina el subproducto
			for (EgreDeta egreDetaSubProductoEliminar : egreDetaSubProductos) {
				this.eliminarEgreDeta(egreDetaSubProductoEliminar);
			}

//			Elimina el producto
			this.eliminarEgreDeta(egreDetaEliminar);
		}
	}

	@Override
	public Producto buscarProductoPorId(Integer productoId) throws Exception {
		return productoRegis.buscarPorId(Producto.class, productoId);
	}

	@Deprecated
	@Override
	public List<Producto> buscarProductos(Producto producto, Integer pagina, Integer filas, String ordenColumna)
			throws Exception {
		productoLista.ordenar(ordenColumna);
		productos = productoLista.buscar(producto, pagina, filas);
		return productos;
	}

	@Override
	public int tamanioProductos() throws Exception {
		return productos.size();
	}

	@Override
	public long contarProductos(Producto producto) throws Exception {
		return productoLista.contarRegistros(producto);
	}

	@Override
	public List<EgreDeta> buscarEgreDetas(EgreDeta egreDeta, Integer pagina) throws Exception {
		return this.egreDetaLista.buscar(egreDeta, null);
	}

	@Override
	public void eliminarEgreDeta(EgreDeta egreDeta) throws Exception {
		this.egreDetaRegis.eliminar(egreDeta);
	}

	@Override
	public EgreDeta buscarEgreDetaPorId(EgreDeta egreDeta) throws Exception {
		return this.egreDetaRegis.buscarPorId(EgreDeta.class, egreDeta.getEgreDetaId());
	}

	@Override
	public void grabarEgreDeta(EgreDeta egreDeta) throws Exception {
		this.egreDetaRegis.insertar(egreDeta);
	}

	@Override
	public void modificarEgreDeta(EgreDeta egreDeta) throws Exception {
		this.egreDetaRegis.modificar(egreDeta);
	}

	@Override
	public List<EgreDetaImpu> buscarEgreDetaImpus(EgreDetaImpu egreDetaImpu) throws Exception {
		return this.egreDetaImpuLista.buscar(egreDetaImpu, null);
	}

	@Override
	public Dimm buscarDimmPorId(Integer dimmId) throws Exception {
		return this.dimmRegis.buscarPorId(Dimm.class, dimmId);
	}

	@Override
	public List<ProdDimm> buscarProdDimms(ProdDimm prodDimm) throws Exception {
		return this.prodDimmLista.buscar(prodDimm, null);
	}

//	@Override
//	public List<Dimm> buscarSustentos(Dimm dimmDesde, Dimm dimmHasta) throws Exception {
//		return this.dimmLista.buscar(dimmDesde, dimmHasta, null);
//	}

	@Override
	public List<Dimm> buscarDimms(Dimm dimmDesde, Dimm dimmHasta) throws Exception {
		return this.dimmLista.buscar(dimmDesde, dimmHasta, null);
	}

	@Override
	public List<DocuEgre> buscarDocuEgres(DocuEgre docuEgre) throws Exception {
		return this.docuEgreLista.buscar(docuEgre, null);
	}

	@Override
	public List<DocuEgre> filtrarDocuEgres(List<DocuEgre> docuEgres, PersUsua persUsua, List<RolDocu> rolDocus)
			throws Exception {
		return this.docuEgreLista.filtrarDocuEgres(docuEgres, persUsua, rolDocus);
	}

//	@Override
//	public List<Cxc> buscarCxcs(Cxc cxc) throws Exception {
//		return this.cxcLista.buscar(cxc, null) ;
//	}

	@Override
	public List<Cxc> buscarCxcs(Cxc cxc, Integer pagina, int filasPagina) throws Exception {

		cxcLista.filasPagina(filasPagina);

		return this.cxcLista.buscar(cxc, pagina);
	}

	@Override
	public long contarCxcs(Cxc cxc) throws Exception {
		return this.cxcLista.contarRegistros(cxc);
	}

	@Override
	public List<Cxc> generarCxc(Egreso egreso, BigDecimal total, Boolean estado) throws Exception {
		return this.cxcRegis.generarCxc(egreso, total, estado);
	}

	@Override
	public Cxc generarCxc(Egreso egreso, FpmeFormPago fpmeFormPago, Boolean estado) throws Exception {
		return this.cxcRegis.generarCxc(egreso, fpmeFormPago, estado);
	}

	@Override
	public Integer insertarCxc(Cxc cxc) throws Exception {
		return (Integer) cxcRegis.insertar(cxc);
	}

	@Override
	public void modificarCxc(Cxc cxc) throws Exception {

		cxcRegis.modificar(cxc);
	}

	@Override
	public List<FormPagoMoviEgre> buscarFpmes(FormPagoMoviEgre fpme) throws Exception {
		return formPagoMoviEgreLista.buscar(fpme, null);
	}

	@Override
	public FormPagoMoviEgre buscarFpmesPorId(Integer fpmeId) throws Exception {
		return formPagoMoviEgreRegis.buscarPorId(FormPagoMoviEgre.class, fpmeId);
	}

	@Override
	public Integer insertarFpme(FormPagoMoviEgre formPagoMoviEgre) throws Exception {
		Object id = formPagoMoviEgreRegis.insertar(formPagoMoviEgre);
		return (Integer) id;
	}

	@Override
	public void modificarFpme(FormPagoMoviEgre fpme) throws Exception {
		formPagoMoviEgreRegis.modificar(fpme);
	}

	@Override
	public void eliminarFpme(FormPagoMoviEgre formPagoMoviEgre) throws Exception {
		formPagoMoviEgreRegis.eliminar(formPagoMoviEgre);
	}

	@Override
	public List<CobrDeta> buscarCobrDetas(CobrDeta cobrDeta) throws Exception {
		return cobrDetaLista.buscar(cobrDeta, null);
	}

	@Override
	public void insertarCobrDeta(CobrDeta cobrDeta) throws Exception {
		cobrDetaRegis.insertar(cobrDeta);
	}

	@Override
	public void eliminarCobrDeta(CobrDeta cobrDeta) throws Exception {
		cobrDetaRegis.eliminar(cobrDeta);
	}

	@Override
	public void anularCxc(Cxc cxc) throws Exception {

		List<Cxc> cxcs = new ArrayList<>();
		cxcs = this.buscarCxcs(cxc, null, 10);
		for (Cxc cxc2 : cxcs) {
			this.eliminarCxc(cxc2);
		}
	}

	@Override
	public List<ProdCost> buscarProdCosts(ProdCost prodCost) throws Exception {
		return prodCostLista.buscar(prodCost, null);
	}

	@Override
	public List<ProdBode> buscarProdBodes(ProdBode prodBode) throws Exception {
		return prodBodeLista.buscar(prodBode, null);
	}

	@Override
	public List<ProdPrec> buscarProdPrecs(ProdPrec prodPrec, Integer pagina, Integer filas, String ordenColumna)
			throws Exception {
		prodPrecLista.ordenar(ordenColumna);
		this.prodPrecs = prodPrecLista.buscar(prodPrec, pagina, filas);
		return this.prodPrecs;
	}

	@Override
	public List<ProdSubp> buscarProdSubps(ProdSubp prodSubp, Integer pagina) throws Exception {
		return prodSubpLista.buscar(prodSubp, pagina);
	}

	@Override
	public int tamanioProdPrecs() throws Exception {
		return this.prodPrecs.size();
	}

	@Override
	public long contarProdPrecs(ProdPrec prodPrec) throws Exception {
		return prodPrecLista.contarRegistros(prodPrec);
	}

	@Override
	public List<Kardex> buscarKardexs(Kardex kardex) throws Exception {
		List<Kardex> kardexs = new ArrayList<>();
		kardexs = kardexLista.buscar(kardex, null);
		return kardexs;
	}

	@Override
	public void grabarKardex(Kardex kardex) throws Exception {
		kardexRegis.insertar(kardex);
	}

	@Override
	public void modificarKardex(Kardex kardex) throws Exception {
		kardexRegis.modificar(kardex);
	}

	@Override
	public void eliminarKardex(Kardex kardex) throws Exception {
		kardexRegis.eliminar(kardex);
	}

	@Override
	public List<KardTotaView> buscarKardTotaViews(KardTotaView kardTotaView) throws Exception {
		return kardTotaViewLista.buscar(kardTotaView, null);
	}

	@Override
	public List<KardTotaView> buscarKardTotaViews(List<Integer> sucursals, KardTotaView kardTotaView) throws Exception {
		return kardTotaViewLista.buscar(sucursals, kardTotaView);
	}

	@Override
	public void eliminarCxc(Cxc cxc) throws Exception {
		cxcRegis.eliminar(cxc);
	}

	@Override
	public List<CajaMovi> buscarSesionVentas(CajaMovi cajaMovi) throws Exception {
		return cajaMoviLista.buscar(cajaMovi, null);
	}

//	@Override
//	public List<Precio> filtrarPrecios(List<Precio> Precios, PersUsua persUsua) throws Exception {
//		return this.precioLista.filtrarPrecios(Precios, persUsua);
//	}

	@Override
	public List<ProdPrec> filtrarProdPrecs(List<ProdPrec> ProdPrecs, PersUsua persUsua, List<RolPrec> rolPrecs,
			Sucursal sucursal) throws Exception {
		return this.prodPrecLista.filtrarProdPrec(ProdPrecs, persUsua, rolPrecs, sucursal);
	}

	@Override
	public List<RolPrec> buscarRolPrecios(RolPrec rolPrec) throws Exception {
		return this.rolPrecLista.buscar(rolPrec, null);
	}

	@Override
	public List<FormPago> buscarFormPago() throws Exception {

		FormPago formPago = new FormPago();
//		Parametro parametro = new Parametro();

//		parametro = parametroRegis.buscarPorId(6051);

//		formPago.setModulo(parametro.getDescri());
		formPago.setEstado(true);

		return this.formPagoLista.buscar(formPago, null);
	}

	@Override
	public List<FormPago> filtrarFormPagos(List<FormPago> formPagos, PersUsua persUsuaSesion,
			List<RolFormPago> rolFormPagos) throws Exception {
		return formPagoLista.filtrarFormPagos(formPagos, persUsuaSesion, rolFormPagos);
	}

	@Override
	public List<PersCobr> buscarPersCobrs() throws Exception {

		PersCobr persCobr = new PersCobr();
		persCobr.setEstado(true);

		Persona persona = new Persona();
		persCobr.setPersona(persona);

		List<PersCobr> persCobrs = new ArrayList<>();
		persCobrs = persCobrLista.buscar(persCobr, null);

		return persCobrs;
	}

	@Override
	public void modificarDocumento(Documento documento) throws Exception {
		this.documentoRegis.modificar(documento);
	}

	@Override
	public List<DocuMoviEgre> buscarDocuMoviEgres() throws Exception {

		Documento documento = new Documento();
		documento.setEstado(true);

		DocuMoviEgre docuMoviEgre = new DocuMoviEgre();
		docuMoviEgre.setDocumento(documento);

		List<DocuMoviEgre> docuMoviEgres = new ArrayList<>();

		docuMoviEgres = docuMoviEgreLista.buscar(docuMoviEgre, null);

		return docuMoviEgres;
	}

	@Override
	public DocuMoviEgre buscarDocuMoviEgrePred(List<DocuMoviEgre> docuMoviEgres, List<RolDocu> rolDocus)
			throws Exception {
		return this.docuMoviEgreLista.buscarDocuMoviEgrePred(docuMoviEgres, rolDocus);
	}

	@Override
	public List<DocuMoviEgre> filtrarDocuMoviEgres(List<DocuMoviEgre> docuMoviEgres, PersUsua persUsuaSesion,
			List<RolDocu> rolDocus) throws Exception {
		return docuMoviEgreLista.filtrarDocuMoviEgres(docuMoviEgres, persUsuaSesion, rolDocus);
	}

	@Override
	public List<CajaDocuEgre> buscarCajaDocuEgre(CajaDocuEgre cajaDocuEgre) throws Exception {
		return cajaDocuEgreLista.buscar(cajaDocuEgre, null);
	}

	@Override
	public void modificarCajaDocuEgre(CajaDocuEgre cajaDocuEgre) throws Exception {
		cajaDocuEgreRegis.modificar(cajaDocuEgre);
	}

	@Override
	public List<CajaPeri> buscarCajaPeri(CajaPeri cajaPeri) throws Exception {
		return cajaPeriLista.buscar(cajaPeri, null);
	}

	@Override
	public List<RolSucu> buscarRolSucus(Set<RolPersUsua> rolPersUsuas) throws Exception {
		return rolSucuLista.buscar(rolPersUsuas);
	}

	@Override
	public List<SaliArch> buscarSaliArchs(SaliArch saliArch) throws Exception {
		return saliArchLista.buscar(saliArch, null);
	}

	@Override
	public BigDecimal sumarCxc(Cxc cxc) throws Exception {
		return this.cxcLista.sumarCxc(cxc);
	}

	@Override
	public BigDecimal sumarCobrDeta(CobrDeta cobrDeta) throws Exception {
		return this.cobrDetaLista.sumarCobrDeta(cobrDeta);
	}

	@Override
	public List<Mesa> buscarMesas(Mesa mesa) throws Exception {
		return mesaLista.buscar(mesa, null);
	}

	@Override
	public Integer insertarFpmeFormPago(FpmeFormPago fpmeFormPago) throws Exception {
		Object id = fpmeFormPagoRegis.insertar(fpmeFormPago);
		return (Integer) id;
	}

	@Override
	public FpmeFormPago buscarFpmeFormPagoPorId(Integer id) throws Exception {
		return fpmeFormPagoRegis.buscarPorId(FpmeFormPago.class, id);
	}

	@Override
	public List<FpmeFormPago> buscarFpmeFormPagos(FpmeFormPago fpmeFormPago) throws Exception {
		return fpmeFormPagoLista.buscar(fpmeFormPago, null);
	}

	@Override
	public void eliminarFpmeFormPago(FpmeFormPago fpmeFormPago) throws Exception {
		fpmeFormPagoRegis.eliminar(fpmeFormPago);
	}

	@Override
	public List<Object[]> buscarCobrosPorEgresoId(CobrDeta cobrDeta) throws Exception {
		return cobrDetaLista.buscarPorEgresoId(cobrDeta);
	}

	@Override
	public List<Object[]> buscarCobrosPorFpmeFpId(List<Object[]> objs, Egreso egreso) throws Exception {
		return cobrDetaLista.buscarPorFpmeFpId(objs, egreso);
	}
	
	@Override
	public List<Object[]> buscarFpmeRefere(String refere) throws Exception {
		return formPagoMoviEgreRegis.buscarFpmeRefere(refere);
	}

	@Override
	public long contarEgresosNoAutorizados(Egreso egreso, LocalDate fechaEmisDesde, LocalDate fechaEmisHasta, Set<Integer> sucursals) throws Exception {
		return egresoLista.contarRegistros2(egreso, fechaEmisDesde, fechaEmisHasta, sucursals);
	}
}
