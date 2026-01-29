package ec.com.tecnointel.soem.documeElec.modelo.retencion;

import java.math.BigDecimal;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "docSustento", propOrder = {
    "codSustento",
    "codDocSustento",
    "numDocSustento",
    "fechaEmisionDocSustento",
    "fechaRegistroContable",
    "numAutDocSustento",
    "pagoLocExt",
    "tipoRegi",
    "paisEfecPago",
    "aplicConvDobTrib",
    "pagExtSujRetNorLeg",
    "pagoRegFis",
    "totalComprobantesReembolso",
    "totalBaseImponibleReembolso",
    "totalImpuestoReembolso",
    "totalSinImpuestos",
    "importeTotal",
    "impuestosDocSustento",
    "retenciones",
    "reembolsos",
    "pagos"
})
public class DocSustento {
    @XmlElement(required = true)
    protected String codSustento;
    @XmlElement(required = true)
    protected String codDocSustento;
    @XmlElement(required = true)
    protected String numDocSustento;
    @XmlElement(required = true)
    protected String fechaEmisionDocSustento;
    protected String fechaRegistroContable;
    protected String numAutDocSustento;
    @XmlElement(required = true)
    protected String pagoLocExt;
    protected String tipoRegi;
    protected String paisEfecPago;
    protected String aplicConvDobTrib;
    protected String pagExtSujRetNorLeg;
    protected String pagoRegFis;
    protected BigDecimal totalComprobantesReembolso;
    protected BigDecimal totalBaseImponibleReembolso;
    protected BigDecimal totalImpuestoReembolso;
    @XmlElement(required = true)
    protected BigDecimal totalSinImpuestos;
    @XmlElement(required = true)
    protected BigDecimal importeTotal;
    @XmlElement(required = true)
    protected ImpuestosDocSustento impuestosDocSustento;
    @XmlElement(required = true)
    protected Retenciones retenciones;
    protected Reembolsos reembolsos;
    @XmlElement(required = true)
    protected Pagos pagos;

    public String getCodSustento() {
        return codSustento;
    }

    public void setCodSustento(String value) {
        this.codSustento = value;
    }

    public String getCodDocSustento() {
        return codDocSustento;
    }

    public void setCodDocSustento(String value) {
        this.codDocSustento = value;
    }

    public String getNumDocSustento() {
        return numDocSustento;
    }

    public void setNumDocSustento(String value) {
        this.numDocSustento = value;
    }

    public String getFechaEmisionDocSustento() {
        return fechaEmisionDocSustento;
    }

    public void setFechaEmisionDocSustento(String value) {
        this.fechaEmisionDocSustento = value;
    }

    public String getFechaRegistroContable() {
        return fechaRegistroContable;
    }

    public void setFechaRegistroContable(String value) {
        this.fechaRegistroContable = value;
    }

    public String getNumAutDocSustento() {
        return numAutDocSustento;
    }

    public void setNumAutDocSustento(String value) {
        this.numAutDocSustento = value;
    }

    public String getPagoLocExt() {
        return pagoLocExt;
    }

    public void setPagoLocExt(String value) {
        this.pagoLocExt = value;
    }

    public String getTipoRegi() {
        return tipoRegi;
    }

    public void setTipoRegi(String value) {
        this.tipoRegi = value;
    }

    public String getPaisEfecPago() {
        return paisEfecPago;
    }

    public void setPaisEfecPago(String value) {
        this.paisEfecPago = value;
    }

    public String getAplicConvDobTrib() {
        return aplicConvDobTrib;
    }

    public void setAplicConvDobTrib(String value) {
        this.aplicConvDobTrib = value;
    }

    public String getPagExtSujRetNorLeg() {
        return pagExtSujRetNorLeg;
    }

    public void setPagExtSujRetNorLeg(String value) {
        this.pagExtSujRetNorLeg = value;
    }

    public String getPagoRegFis() {
        return pagoRegFis;
    }

    public void setPagoRegFis(String value) {
        this.pagoRegFis = value;
    }

    public BigDecimal getTotalComprobantesReembolso() {
        return totalComprobantesReembolso;
    }

    public void setTotalComprobantesReembolso(BigDecimal value) {
        this.totalComprobantesReembolso = value;
    }

    public BigDecimal getTotalBaseImponibleReembolso() {
        return totalBaseImponibleReembolso;
    }

    public void setTotalBaseImponibleReembolso(BigDecimal value) {
        this.totalBaseImponibleReembolso = value;
    }

    public BigDecimal getTotalImpuestoReembolso() {
        return totalImpuestoReembolso;
    }

    public void setTotalImpuestoReembolso(BigDecimal value) {
        this.totalImpuestoReembolso = value;
    }

    public BigDecimal getTotalSinImpuestos() {
        return totalSinImpuestos;
    }

    public void setTotalSinImpuestos(BigDecimal value) {
        this.totalSinImpuestos = value;
    }

    public BigDecimal getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(BigDecimal value) {
        this.importeTotal = value;
    }

    public ImpuestosDocSustento getImpuestosDocSustento() {
        return impuestosDocSustento;
    }

    public void setImpuestosDocSustento(ImpuestosDocSustento value) {
        this.impuestosDocSustento = value;
    }

    public Retenciones getRetenciones() {
        return retenciones;
    }

    public void setRetenciones(Retenciones value) {
        this.retenciones = value;
    }

    public Reembolsos getReembolsos() {
        return reembolsos;
    }

    public void setReembolsos(Reembolsos value) {
        this.reembolsos = value;
    }

    public Pagos getPagos() {
        return pagos;
    }

    public void setPagos(Pagos value) {
        this.pagos = value;
    }
}
