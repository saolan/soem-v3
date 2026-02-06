package ec.com.tecnointel.soem.tesoreria.modelo;

import java.io.Serializable;

import ec.com.tecnointel.soem.ingreso.modelo.Retencion;
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
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "FPME_FORM_PAGO_RETE", uniqueConstraints = {
		@UniqueConstraint(name = "UQ_FPME_FORM_PAGO_RETENCION", columnNames = { "FPME_FORM_PAGO_ID",
				"RETENCION_ID" }) })
public class FpmeFormPagoRete implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer ffprId;
	private FpmeFormPago fpmeFormaPago;
	private Retencion retencion;

	public FpmeFormPagoRete() {
	}

	public FpmeFormPagoRete(FpmeFormPago fpmeFormaPago, Retencion retencion) {
		this.fpmeFormaPago = fpmeFormaPago;
		this.retencion = retencion;
	}

	@Id
	@TableGenerator(name = "idFpmeFormPagoRete", table = "secuencial", pkColumnName = "tabla", pkColumnValue = "fpme_form_pago_rete", valueColumnName = "numero", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idFpmeFormPagoRete")
	@Column(name = "FFPR_ID", unique = true, nullable = false)
	public Integer getFfprId() {
		return ffprId;
	}

	public void setFfprId(Integer ffprId) {
		this.ffprId = ffprId;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "FPME_FORM_PAGO_ID", nullable = false)
	public FpmeFormPago getFpmeFormaPago() {
		return fpmeFormaPago;
	}

	public void setFpmeFormaPago(FpmeFormPago fpmeFormaPago) {
		this.fpmeFormaPago = fpmeFormaPago;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "RETENCION_ID", nullable = false)
	public Retencion getRetencion() {
		return retencion;
	}

	public void setRetencion(Retencion retencion) {
		this.retencion = retencion;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof FpmeFormPagoRete other))
			return false;

		return ffprId != null && ffprId.equals(other.ffprId);
	}

	@Override
	public int hashCode() {
		if (ffprId != null)
			return ffprId.hashCode();
		return System.identityHashCode(this);
	}
}
