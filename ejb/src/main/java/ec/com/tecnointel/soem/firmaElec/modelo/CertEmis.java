package ec.com.tecnointel.soem.firmaElec.modelo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import ec.com.tecnointel.soem.parametro.modelo.SucuCertEmis;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "cert_emis")
public class CertEmis implements Serializable {

	private Integer certEmisId;
	private String descri;
	private String certCn;
	private String certOu;
	private String certO;
	private String certC;
	private String certOid;
	private Boolean estado;
	
	private Set<SucuCertEmis> sucuCertEmiss = new HashSet<SucuCertEmis>(0);

	private static final long serialVersionUID = -5908949525218949196L;

	public CertEmis() {
	}
	
	@Id
	@TableGenerator(name = "idCertEmis", table = "secuencial", pkColumnName = "tabla", pkColumnValue = "cert_emis", valueColumnName = "numero", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idCertEmis")
	@Column(name = "CERT_EMIS_ID", unique = true, nullable = false)
	public Integer getCertEmisId() {
		return certEmisId;
	}

	public void setCertEmisId(Integer certEmisId) {
		this.certEmisId = certEmisId;
	}

	@NotBlank(message = "Ingrese descripción")
	@Column(name = "DESCRI", nullable = false, length = 100)
	public String getDescri() {
		return descri;
	}

	public void setDescri(String descri) {
		this.descri = descri;
	}

	@NotBlank(message = "Ingrese CN")
	@Column(name = "CERT_CN", nullable = false, length = 100)
	public String getCertCn() {
		return certCn;
	}

	public void setCertCn(String certCn) {
		this.certCn = certCn;
	}

	@NotBlank(message = "Ingrese OU")
	@Column(name = "CERT_OU", nullable = false, length = 100)
	public String getCertOu() {
		return certOu;
	}

	public void setCertOu(String certOu) {
		this.certOu = certOu;
	}

	@NotBlank(message = "Ingrese O")
	@Column(name = "CERT_O", nullable = false, length = 100)
	public String getCertO() {
		return certO;
	}

	public void setCertO(String certO) {
		this.certO = certO;
	}

	@NotBlank(message = "Ingrese C")
	@Column(name = "CERT_C", nullable = false, length = 100)
	public String getCertC() {
		return certC;
	}

	public void setCertC(String certC) {
		this.certC = certC;
	}

	@NotBlank(message = "Ingrese Oid")
	@Column(name = "CERT_OID", nullable = false, length = 100)
	public String getCertOid() {
		return certOid;
	}

	public void setCertOid(String certOid) {
		this.certOid = certOid;
	}

	@NotNull
	@Column(name = "ESTADO", nullable = false)
	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "certEmis")
	public Set<SucuCertEmis> getSucuCertEmiss() {
		return sucuCertEmiss;
	}

	public void setSucuCertEmiss(Set<SucuCertEmis> sucuCertEmiss) {
		this.sucuCertEmiss = sucuCertEmiss;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(certEmisId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CertEmis other = (CertEmis) obj;
		return Objects.equals(certEmisId, other.certEmisId);
	}
}
