package ec.com.tecnointel.soem.egreso.modelo;
// Generated 26-jul-2016 22:29:48 by Hibernate Tools 5.1.0.Alpha2

import java.util.Objects;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "egre_nota")
public class EgreNota implements java.io.Serializable {

	private static final long serialVersionUID = -7128622261254615233L;
	
	private Integer egreNotaId;
	private Egreso egreso;
	private String codigo;
	private String descri;

	public EgreNota() {
	}

	public EgreNota(Egreso egreso) {
		this.egreso = egreso;
	}

	
	public EgreNota(Integer egreNotaId, Egreso egreso, String codigo, String descri) {
		this.egreso = egreso;
		this.codigo = codigo;
		this.descri = descri;
	}

	@Id
	@TableGenerator(name = "idEgreNota", table = "secuencial", pkColumnName = "tabla", pkColumnValue = "egre_nota", valueColumnName = "numero", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idEgreNota")
	@Column(name = "EGRE_NOTA_ID", unique = true, nullable = false)
	public Integer getEgreNotaId() {
		return egreNotaId;
	}

	public void setEgreNotaId(Integer egreNotaId) {
		this.egreNotaId = egreNotaId;
	}

	@Length(min = 1, max = 32, message = "El numero de caracteres no debe sobrepasar de 32")
	@NotNull
	@Column(name = "CODIGO", nullable = false, length = 32)
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@NotNull
	@Length(min = 1, max = 255, message = "El numero de caracteres no debe sobrepasar de 255")
	@Column(name = "DESCRI", nullable = false, length = 255)
	public String getDescri() {
		return descri;
	}

	public void setDescri(String descri) {
		this.descri = descri;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EGRESO_ID", nullable = false)
	public Egreso getEgreso() {
		return egreso;
	}

	public void setEgreso(Egreso egreso) {
		this.egreso = egreso;
	}

	@Override
	public int hashCode() {
		return Objects.hash(egreNotaId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EgreNota other = (EgreNota) obj;
		return Objects.equals(egreNotaId, other.egreNotaId);
	}

}
