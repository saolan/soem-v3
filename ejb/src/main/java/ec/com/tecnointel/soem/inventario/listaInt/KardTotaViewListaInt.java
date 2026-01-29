package ec.com.tecnointel.soem.inventario.listaInt;

import java.util.List;

import ec.com.tecnointel.soem.inventario.modelo.KardTotaView;
import jakarta.ejb.Local;

@Local
public interface KardTotaViewListaInt {

	public List<KardTotaView> buscar(KardTotaView kardTotaView, Integer pagina) throws Exception;

	public long contarRegistros(KardTotaView kardTotaView) throws Exception;

	public List<KardTotaView> buscar(List<Integer> sucursals, KardTotaView kardTotaView) throws Exception;

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< METODOS ADICIONALES
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}
