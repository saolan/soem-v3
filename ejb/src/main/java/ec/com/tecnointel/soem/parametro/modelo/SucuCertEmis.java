package ec.com.tecnointel.soem.parametro.modelo;

import java.io.Serializable;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import ec.com.tecnointel.soem.firmaElec.modelo.CertEmis;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "sucu_cert_emis")
@NamedEntityGraph(name = "sucuCertEmis.Graph", attributeNodes = {
@NamedAttributeNode(value = "sucursal"), 
@NamedAttributeNode(value = "certEmis") })
public class SucuCertEmis implements Serializable {

	private Integer sucursalId;
	private Sucursal sucursal;
	private CertEmis certEmis;
	private String ruta;
	private String descri;
	private String clave;

	private static final long serialVersionUID = -2146561116787862392L;

	public SucuCertEmis() {
	}

	public SucuCertEmis(Sucursal sucursal, CertEmis certEmis) {
		this.sucursal = sucursal;
		this.certEmis = certEmis;
	}

	@Id
	@GenericGenerator(name = "idSucuCertId", strategy = "foreign", parameters = @Parameter(name = "property", value = "sucursal"))
	@GeneratedValue(generator = "idSucuCertId")
	@Column(name = "SUCURSAL_ID", unique = true, nullable = false)
	public Integer getSucursalId() {
		return sucursalId;
	}

	public void setSucursalId(Integer sucursalId) {
		this.sucursalId = sucursalId;
	}
	
	@NotBlank
	@Column(name = "RUTA", nullable = false, length = 100)
	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	@NotBlank
	@Column(name = "DESCRI", nullable = false, length = 100)
	public String getDescri() {
		return descri;
	}

	public void setDescri(String descri) {
		this.descri = descri;
	}

	@NotBlank
	@Column(name = "CLAVE", nullable = false, length = 100)
	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@PrimaryKeyJoinColumn
	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CERT_EMIS_ID", nullable = false)
	public CertEmis getCertEmis() {
		return certEmis;
	}

	public void setCertEmis(CertEmis certEmis) {
		this.certEmis = certEmis;
	}
}
