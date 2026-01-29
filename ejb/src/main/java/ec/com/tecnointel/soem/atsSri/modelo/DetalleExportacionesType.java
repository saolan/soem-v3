//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantaci�n de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perder�n si se vuelve a compilar el esquema de origen. 
// Generado el: 2023.12.05 a las 03:49:23 PM COT 
//


package ec.com.tecnointel.soem.atsSri.modelo;

import java.math.BigDecimal;
import java.math.BigInteger;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para detalleExportacionesType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="detalleExportacionesType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tpIdClienteEx" type="{}tpIdClienteExType" minOccurs="0"/>
 *         &lt;element name="idClienteEx" type="{}idClienteType" minOccurs="0"/>
 *         &lt;element name="parteRelExp" type="{}parteRelType" minOccurs="0"/>
 *         &lt;element name="tipoCli" type="{}tipoProvType" minOccurs="0"/>
 *         &lt;element name="denoExpCli" type="{}denoProvType" minOccurs="0"/>
 *         &lt;element name="tipoRegi" type="{}tipoRegiType" minOccurs="0"/>
 *         &lt;element name="paisEfecPagoGen" type="{}paisEfecPagoType" minOccurs="0"/>
 *         &lt;element name="paisEfecPagoParFis" type="{}paraisoFiscalType" minOccurs="0"/>
 *         &lt;element name="denopagoRegFis" type="{}razonSocialType" minOccurs="0"/>
 *         &lt;element name="paisEfecExp" type="{}paisEfecPagoType" minOccurs="0"/>
 *         &lt;element name="pagoRegFis" type="{}parteRelType" minOccurs="0"/>
 *         &lt;element name="exportacionDe" type="{}exportacionDeType"/>
 *         &lt;element name="tipIngExt" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{}tipIngExtType">
 *               &lt;pattern value="\d{3}"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ingExtGravOtroPais" type="{}parteRelType" minOccurs="0"/>
 *         &lt;element name="impuestoOtroPais" type="{}monedaType" minOccurs="0"/>
 *         &lt;element name="tipoComprobante" type="{}tipoComprobanteType"/>
 *         &lt;element name="distAduanero" type="{}distAduaneroType" minOccurs="0"/>
 *         &lt;element name="anio" type="{}anioType" minOccurs="0"/>
 *         &lt;element name="regimen" type="{}regimenType" minOccurs="0"/>
 *         &lt;element name="correlativo" type="{}correlativoType" minOccurs="0"/>
 *         &lt;element name="verificador" type="{}verificadorType" minOccurs="0"/>
 *         &lt;element name="docTransp" type="{}docTranspType" minOccurs="0"/>
 *         &lt;element name="fechaEmbarque" type="{}fechaType"/>
 *         &lt;element name="fue" type="{}fueType" minOccurs="0"/>
 *         &lt;element name="valorFOB" type="{}valorFOBType"/>
 *         &lt;element name="valorFOBComprobante" type="{}valorFOBComprobanteType"/>
 *         &lt;element name="establecimiento" type="{}establecimientoType"/>
 *         &lt;element name="puntoEmision" type="{}ptoEmisionType"/>
 *         &lt;element name="secuencial" type="{}secuencialType"/>
 *         &lt;element name="autorizacion" type="{}autorizacionType"/>
 *         &lt;element name="fechaEmision" type="{}fechaType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "detalleExportacionesType", propOrder = {
    "tpIdClienteEx",
    "idClienteEx",
    "parteRelExp",
    "tipoCli",
    "denoExpCli",
    "tipoRegi",
    "paisEfecPagoGen",
    "paisEfecPagoParFis",
    "denopagoRegFis",
    "paisEfecExp",
    "pagoRegFis",
    "exportacionDe",
    "tipIngExt",
    "ingExtGravOtroPais",
    "impuestoOtroPais",
    "tipoComprobante",
    "distAduanero",
    "anio",
    "regimen",
    "correlativo",
    "verificador",
    "docTransp",
    "fechaEmbarque",
    "fue",
    "valorFOB",
    "valorFOBComprobante",
    "establecimiento",
    "puntoEmision",
    "secuencial",
    "autorizacion",
    "fechaEmision"
})
public class DetalleExportacionesType {

    protected BigInteger tpIdClienteEx;
    protected String idClienteEx;
    @XmlSchemaType(name = "string")
    protected ParteRelType parteRelExp;
    protected String tipoCli;
    protected String denoExpCli;
    protected String tipoRegi;
    protected String paisEfecPagoGen;
    protected String paisEfecPagoParFis;
    protected String denopagoRegFis;
    protected String paisEfecExp;
    @XmlSchemaType(name = "string")
    protected ParteRelType pagoRegFis;
    @XmlElement(required = true)
    protected BigInteger exportacionDe;
    protected String tipIngExt;
    @XmlSchemaType(name = "string")
    protected ParteRelType ingExtGravOtroPais;
    protected BigDecimal impuestoOtroPais;
    @XmlElement(required = true)
    protected String tipoComprobante;
    protected String distAduanero;
    @XmlSchemaType(name = "integer")
    protected Integer anio;
    protected String regimen;
    protected String correlativo;
    protected String verificador;
    protected String docTransp;
    @XmlElement(required = true)
    protected String fechaEmbarque;
    protected String fue;
    @XmlElement(required = true)
    protected BigDecimal valorFOB;
    @XmlElement(required = true)
    protected BigDecimal valorFOBComprobante;
    @XmlElement(required = true)
    protected String establecimiento;
    @XmlElement(required = true)
    protected String puntoEmision;
    @XmlSchemaType(name = "integer")
    protected int secuencial;
    @XmlElement(required = true)
    protected String autorizacion;
    @XmlElement(required = true)
    protected String fechaEmision;

    /**
     * Obtiene el valor de la propiedad tpIdClienteEx.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTpIdClienteEx() {
        return tpIdClienteEx;
    }

    /**
     * Define el valor de la propiedad tpIdClienteEx.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTpIdClienteEx(BigInteger value) {
        this.tpIdClienteEx = value;
    }

    /**
     * Obtiene el valor de la propiedad idClienteEx.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdClienteEx() {
        return idClienteEx;
    }

    /**
     * Define el valor de la propiedad idClienteEx.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdClienteEx(String value) {
        this.idClienteEx = value;
    }

    /**
     * Obtiene el valor de la propiedad parteRelExp.
     * 
     * @return
     *     possible object is
     *     {@link ParteRelType }
     *     
     */
    public ParteRelType getParteRelExp() {
        return parteRelExp;
    }

    /**
     * Define el valor de la propiedad parteRelExp.
     * 
     * @param value
     *     allowed object is
     *     {@link ParteRelType }
     *     
     */
    public void setParteRelExp(ParteRelType value) {
        this.parteRelExp = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoCli.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoCli() {
        return tipoCli;
    }

    /**
     * Define el valor de la propiedad tipoCli.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoCli(String value) {
        this.tipoCli = value;
    }

    /**
     * Obtiene el valor de la propiedad denoExpCli.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenoExpCli() {
        return denoExpCli;
    }

    /**
     * Define el valor de la propiedad denoExpCli.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenoExpCli(String value) {
        this.denoExpCli = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoRegi.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoRegi() {
        return tipoRegi;
    }

    /**
     * Define el valor de la propiedad tipoRegi.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoRegi(String value) {
        this.tipoRegi = value;
    }

    /**
     * Obtiene el valor de la propiedad paisEfecPagoGen.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaisEfecPagoGen() {
        return paisEfecPagoGen;
    }

    /**
     * Define el valor de la propiedad paisEfecPagoGen.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaisEfecPagoGen(String value) {
        this.paisEfecPagoGen = value;
    }

    /**
     * Obtiene el valor de la propiedad paisEfecPagoParFis.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaisEfecPagoParFis() {
        return paisEfecPagoParFis;
    }

    /**
     * Define el valor de la propiedad paisEfecPagoParFis.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaisEfecPagoParFis(String value) {
        this.paisEfecPagoParFis = value;
    }

    /**
     * Obtiene el valor de la propiedad denopagoRegFis.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenopagoRegFis() {
        return denopagoRegFis;
    }

    /**
     * Define el valor de la propiedad denopagoRegFis.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenopagoRegFis(String value) {
        this.denopagoRegFis = value;
    }

    /**
     * Obtiene el valor de la propiedad paisEfecExp.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaisEfecExp() {
        return paisEfecExp;
    }

    /**
     * Define el valor de la propiedad paisEfecExp.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaisEfecExp(String value) {
        this.paisEfecExp = value;
    }

    /**
     * Obtiene el valor de la propiedad pagoRegFis.
     * 
     * @return
     *     possible object is
     *     {@link ParteRelType }
     *     
     */
    public ParteRelType getPagoRegFis() {
        return pagoRegFis;
    }

    /**
     * Define el valor de la propiedad pagoRegFis.
     * 
     * @param value
     *     allowed object is
     *     {@link ParteRelType }
     *     
     */
    public void setPagoRegFis(ParteRelType value) {
        this.pagoRegFis = value;
    }

    /**
     * Obtiene el valor de la propiedad exportacionDe.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getExportacionDe() {
        return exportacionDe;
    }

    /**
     * Define el valor de la propiedad exportacionDe.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setExportacionDe(BigInteger value) {
        this.exportacionDe = value;
    }

    /**
     * Obtiene el valor de la propiedad tipIngExt.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipIngExt() {
        return tipIngExt;
    }

    /**
     * Define el valor de la propiedad tipIngExt.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipIngExt(String value) {
        this.tipIngExt = value;
    }

    /**
     * Obtiene el valor de la propiedad ingExtGravOtroPais.
     * 
     * @return
     *     possible object is
     *     {@link ParteRelType }
     *     
     */
    public ParteRelType getIngExtGravOtroPais() {
        return ingExtGravOtroPais;
    }

    /**
     * Define el valor de la propiedad ingExtGravOtroPais.
     * 
     * @param value
     *     allowed object is
     *     {@link ParteRelType }
     *     
     */
    public void setIngExtGravOtroPais(ParteRelType value) {
        this.ingExtGravOtroPais = value;
    }

    /**
     * Obtiene el valor de la propiedad impuestoOtroPais.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImpuestoOtroPais() {
        return impuestoOtroPais;
    }

    /**
     * Define el valor de la propiedad impuestoOtroPais.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImpuestoOtroPais(BigDecimal value) {
        this.impuestoOtroPais = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoComprobante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoComprobante() {
        return tipoComprobante;
    }

    /**
     * Define el valor de la propiedad tipoComprobante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoComprobante(String value) {
        this.tipoComprobante = value;
    }

    /**
     * Obtiene el valor de la propiedad distAduanero.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDistAduanero() {
        return distAduanero;
    }

    /**
     * Define el valor de la propiedad distAduanero.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDistAduanero(String value) {
        this.distAduanero = value;
    }

    /**
     * Obtiene el valor de la propiedad anio.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAnio() {
        return anio;
    }

    /**
     * Define el valor de la propiedad anio.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAnio(Integer value) {
        this.anio = value;
    }

    /**
     * Obtiene el valor de la propiedad regimen.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegimen() {
        return regimen;
    }

    /**
     * Define el valor de la propiedad regimen.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegimen(String value) {
        this.regimen = value;
    }

    /**
     * Obtiene el valor de la propiedad correlativo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCorrelativo() {
        return correlativo;
    }

    /**
     * Define el valor de la propiedad correlativo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCorrelativo(String value) {
        this.correlativo = value;
    }

    /**
     * Obtiene el valor de la propiedad verificador.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVerificador() {
        return verificador;
    }

    /**
     * Define el valor de la propiedad verificador.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVerificador(String value) {
        this.verificador = value;
    }

    /**
     * Obtiene el valor de la propiedad docTransp.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocTransp() {
        return docTransp;
    }

    /**
     * Define el valor de la propiedad docTransp.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocTransp(String value) {
        this.docTransp = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaEmbarque.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaEmbarque() {
        return fechaEmbarque;
    }

    /**
     * Define el valor de la propiedad fechaEmbarque.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaEmbarque(String value) {
        this.fechaEmbarque = value;
    }

    /**
     * Obtiene el valor de la propiedad fue.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFue() {
        return fue;
    }

    /**
     * Define el valor de la propiedad fue.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFue(String value) {
        this.fue = value;
    }

    /**
     * Obtiene el valor de la propiedad valorFOB.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorFOB() {
        return valorFOB;
    }

    /**
     * Define el valor de la propiedad valorFOB.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorFOB(BigDecimal value) {
        this.valorFOB = value;
    }

    /**
     * Obtiene el valor de la propiedad valorFOBComprobante.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorFOBComprobante() {
        return valorFOBComprobante;
    }

    /**
     * Define el valor de la propiedad valorFOBComprobante.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorFOBComprobante(BigDecimal value) {
        this.valorFOBComprobante = value;
    }

    /**
     * Obtiene el valor de la propiedad establecimiento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstablecimiento() {
        return establecimiento;
    }

    /**
     * Define el valor de la propiedad establecimiento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstablecimiento(String value) {
        this.establecimiento = value;
    }

    /**
     * Obtiene el valor de la propiedad puntoEmision.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPuntoEmision() {
        return puntoEmision;
    }

    /**
     * Define el valor de la propiedad puntoEmision.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPuntoEmision(String value) {
        this.puntoEmision = value;
    }

    /**
     * Obtiene el valor de la propiedad secuencial.
     * 
     */
    public int getSecuencial() {
        return secuencial;
    }

    /**
     * Define el valor de la propiedad secuencial.
     * 
     */
    public void setSecuencial(int value) {
        this.secuencial = value;
    }

    /**
     * Obtiene el valor de la propiedad autorizacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAutorizacion() {
        return autorizacion;
    }

    /**
     * Define el valor de la propiedad autorizacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAutorizacion(String value) {
        this.autorizacion = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaEmision.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaEmision() {
        return fechaEmision;
    }

    /**
     * Define el valor de la propiedad fechaEmision.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaEmision(String value) {
        this.fechaEmision = value;
    }

}
