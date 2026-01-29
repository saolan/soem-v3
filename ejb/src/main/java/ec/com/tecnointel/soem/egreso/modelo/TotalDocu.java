package ec.com.tecnointel.soem.egreso.modelo;

import java.math.BigDecimal;

public class TotalDocu {

	String codigo;
	String Descri;
	String TipoImpu = new String();
	BigDecimal porcen;
	BigDecimal valor;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescri() {
		return Descri;
	}

	public void setDescri(String descri) {
		Descri = descri;
	}

	public String getTipoImpu() {
		return TipoImpu;
	}

	public void setTipoImpu(String tipoImpu) {
		TipoImpu = tipoImpu;
	}

	public BigDecimal getPorcen() {
		return porcen;
	}

	public void setPorcen(BigDecimal porcen) {
		this.porcen = porcen;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Descri == null) ? 0 : Descri.hashCode());
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
		TotalDocu other = (TotalDocu) obj;
		if (Descri == null) {
			if (other.Descri != null)
				return false;
		} else if (!Descri.equals(other.Descri))
			return false;
		return true;
	}

}
