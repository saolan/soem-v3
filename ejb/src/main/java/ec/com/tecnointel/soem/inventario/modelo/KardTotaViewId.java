package ec.com.tecnointel.soem.inventario.modelo;

import java.io.Serializable;

public class KardTotaViewId implements Serializable {

	private Integer sucursalId;
	private Integer bodegaId;
	private Integer productoId;

	private static final long serialVersionUID = 422404956556554026L;

	public KardTotaViewId(Integer sucursalId, Integer bodegaId, Integer productoId) {
		super();
		this.sucursalId = sucursalId;
		this.bodegaId = bodegaId;
		this.productoId = productoId;
	}

	public Integer getSucursalId() {
		return sucursalId;
	}

	public void setSucursalId(Integer sucursalId) {
		this.sucursalId = sucursalId;
	}

	public Integer getBodegaId() {
		return bodegaId;
	}

	public void setBodegaId(Integer bodegaId) {
		this.bodegaId = bodegaId;
	}

	public Integer getProductoId() {
		return productoId;
	}

	public void setProductoId(Integer productoId) {
		this.productoId = productoId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bodegaId == null) ? 0 : bodegaId.hashCode());
		result = prime * result + ((productoId == null) ? 0 : productoId.hashCode());
		result = prime * result + ((sucursalId == null) ? 0 : sucursalId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KardTotaViewId other = (KardTotaViewId) obj;
		if (bodegaId == null) {
			if (other.bodegaId != null)
				return false;
		} else if (!bodegaId.equals(other.bodegaId))
			return false;
		if (productoId == null) {
			if (other.productoId != null)
				return false;
		} else if (!productoId.equals(other.productoId))
			return false;
		if (sucursalId == null) {
			if (other.sucursalId != null)
				return false;
		} else if (!sucursalId.equals(other.sucursalId))
			return false;
		return true;
	}
}
