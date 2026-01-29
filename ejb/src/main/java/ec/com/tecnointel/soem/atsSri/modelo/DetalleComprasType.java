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
 * <p>Clase Java para detalleComprasType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="detalleComprasType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codSustento" type="{}codSustentoType"/>
 *         &lt;element name="tpIdProv" type="{}tpIdProvType"/>
 *         &lt;element name="idProv" type="{}idProvType"/>
 *         &lt;element name="tipoComprobante" type="{}tipoComprobanteCompraAnuType"/>
 *         &lt;element name="tipoProv" type="{}tipoProvType" minOccurs="0"/>
 *         &lt;element name="denoProv" type="{}denoProvType" minOccurs="0"/>
 *         &lt;element name="parteRel" type="{}parteRelType" minOccurs="0"/>
 *         &lt;element name="fechaRegistro" type="{}fechaType"/>
 *         &lt;element name="establecimiento" type="{}establecimientoType"/>
 *         &lt;element name="puntoEmision" type="{}ptoEmisionType"/>
 *         &lt;element name="secuencial" type="{}secuencialType"/>
 *         &lt;element name="fechaEmision" type="{}fechaType"/>
 *         &lt;element name="autorizacion" type="{}autorizacionType"/>
 *         &lt;element name="baseNoGraIva" type="{}monedaType"/>
 *         &lt;element name="baseImponible" type="{}monedaType"/>
 *         &lt;element name="baseImpGrav" type="{}monedaType"/>
 *         &lt;element name="baseImpExe" type="{}monedaType"/>
 *         &lt;element name="montoIce" type="{}monedaType"/>
 *         &lt;element name="montoIva" type="{}monedaType"/>
 *         &lt;element name="valRetBien10" type="{}monedaType" minOccurs="0"/>
 *         &lt;element name="valRetServ20" type="{}monedaType" minOccurs="0"/>
 *         &lt;element name="valorRetBienes" type="{}monedaType"/>
 *         &lt;element name="valRetServ50" type="{}monedaType" minOccurs="0"/>
 *         &lt;element name="valorRetServicios" type="{}monedaType"/>
 *         &lt;element name="valRetServ100" type="{}monedaType"/>
 *         &lt;element name="valorRetencionNc" type="{}monedaType" minOccurs="0"/>
 *         &lt;element name="totbasesImpReemb" type="{}monedaType" minOccurs="0"/>
 *         &lt;element name="pagoExterior" type="{}pagoExteriorType" minOccurs="0"/>
 *         &lt;element name="formasDePago" type="{}formasDePagoType" minOccurs="0"/>
 *         &lt;element name="air" type="{}airType" minOccurs="0"/>
 *         &lt;element name="estabRetencion1" type="{}establecimientoType" minOccurs="0"/>
 *         &lt;element name="ptoEmiRetencion1" type="{}ptoEmisionType" minOccurs="0"/>
 *         &lt;element name="secRetencion1" type="{}secRetencionType" minOccurs="0"/>
 *         &lt;element name="autRetencion1" type="{}autRetencionType" minOccurs="0"/>
 *         &lt;element name="fechaEmiRet1" type="{}fechaType" minOccurs="0"/>
 *         &lt;element name="estabRetencion2" type="{}establecimientoType" minOccurs="0"/>
 *         &lt;element name="ptoEmiRetencion2" type="{}ptoEmisionType" minOccurs="0"/>
 *         &lt;element name="secRetencion2" type="{}secRetencionType" minOccurs="0"/>
 *         &lt;element name="autRetencion2" type="{}autRetencionType" minOccurs="0"/>
 *         &lt;element name="fechaEmiRet2" type="{}fechaType" minOccurs="0"/>
 *         &lt;element name="docModificado" type="{}docModificadoType" minOccurs="0"/>
 *         &lt;element name="estabModificado" type="{}establecimientoType" minOccurs="0"/>
 *         &lt;element name="ptoEmiModificado" type="{}ptoEmisionType" minOccurs="0"/>
 *         &lt;element name="secModificado" type="{}secModType" minOccurs="0"/>
 *         &lt;element name="autModificado" type="{}autModificadoType" minOccurs="0"/>
 *         &lt;element name="reembolsos" type="{}reembolsosType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "detalleComprasType", propOrder = {
    "codSustento",
    "tpIdProv",
    "idProv",
    "tipoComprobante",
    "tipoProv",
    "denoProv",
    "parteRel",
    "fechaRegistro",
    "establecimiento",
    "puntoEmision",
    "secuencial",
    "fechaEmision",
    "autorizacion",
    "baseNoGraIva",
    "baseImponible",
    "baseImpGrav",
    "baseImpExe",
    "montoIce",
    "montoIva",
    "valRetBien10",
    "valRetServ20",
    "valorRetBienes",
    "valRetServ50",
    "valorRetServicios",
    "valRetServ100",
    "valorRetencionNc",
    "totbasesImpReemb",
    "pagoExterior",
    "formasDePago",
    "air",
    "estabRetencion1",
    "ptoEmiRetencion1",
    "secRetencion1",
    "autRetencion1",
    "fechaEmiRet1",
    "estabRetencion2",
    "ptoEmiRetencion2",
    "secRetencion2",
    "autRetencion2",
    "fechaEmiRet2",
    "docModificado",
    "estabModificado",
    "ptoEmiModificado",
    "secModificado",
    "autModificado",
    "reembolsos"
})
public class DetalleComprasType {

    @XmlElement(required = true)
    protected String codSustento;
    @XmlElement(required = true)
    protected String tpIdProv;
    @XmlElement(required = true)
    protected String idProv;
    @XmlElement(required = true)
    protected String tipoComprobante;
    protected String tipoProv;
    protected String denoProv;
    @XmlSchemaType(name = "string")
    protected ParteRelType parteRel;
    @XmlElement(required = true)
    protected String fechaRegistro;
    @XmlElement(required = true)
    protected String establecimiento;
    @XmlElement(required = true)
    protected String puntoEmision;
    @XmlSchemaType(name = "integer")
    protected int secuencial;
    @XmlElement(required = true)
    protected String fechaEmision;
    @XmlElement(required = true)
    protected String autorizacion;
    @XmlElement(required = true)
    protected BigDecimal baseNoGraIva;
    @XmlElement(required = true)
    protected BigDecimal baseImponible;
    @XmlElement(required = true)
    protected BigDecimal baseImpGrav;
    @XmlElement(required = true)
    protected BigDecimal baseImpExe;
    @XmlElement(required = true)
    protected BigDecimal montoIce;
    @XmlElement(required = true)
    protected BigDecimal montoIva;
    protected BigDecimal valRetBien10;
    protected BigDecimal valRetServ20;
    @XmlElement(required = true)
    protected BigDecimal valorRetBienes;
    protected BigDecimal valRetServ50;
    @XmlElement(required = true)
    protected BigDecimal valorRetServicios;
    @XmlElement(required = true)
    protected BigDecimal valRetServ100;
    protected BigDecimal valorRetencionNc;
    protected BigDecimal totbasesImpReemb;
    protected PagoExteriorType pagoExterior;
    protected FormasDePagoType formasDePago;
    protected AirType air;
    protected String estabRetencion1;
    protected String ptoEmiRetencion1;
    @XmlSchemaType(name = "integer")
    protected Integer secRetencion1;
    protected String autRetencion1;
    protected String fechaEmiRet1;
    protected String estabRetencion2;
    protected String ptoEmiRetencion2;
    @XmlSchemaType(name = "integer")
    protected Integer secRetencion2;
    protected String autRetencion2;
    protected String fechaEmiRet2;
    protected String docModificado;
    protected String estabModificado;
    protected String ptoEmiModificado;
    @XmlSchemaType(name = "integer")
    protected Integer secModificado;
    protected String autModificado;
    protected ReembolsosType reembolsos;

    /**
     * Obtiene el valor de la propiedad codSustento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodSustento() {
        return codSustento;
    }

    /**
     * Define el valor de la propiedad codSustento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodSustento(String value) {
        this.codSustento = value;
    }

    /**
     * Obtiene el valor de la propiedad tpIdProv.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTpIdProv() {
        return tpIdProv;
    }

    /**
     * Define el valor de la propiedad tpIdProv.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTpIdProv(String value) {
        this.tpIdProv = value;
    }

    /**
     * Obtiene el valor de la propiedad idProv.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdProv() {
        return idProv;
    }

    /**
     * Define el valor de la propiedad idProv.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdProv(String value) {
        this.idProv = value;
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
     * Obtiene el valor de la propiedad tipoProv.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoProv() {
        return tipoProv;
    }

    /**
     * Define el valor de la propiedad tipoProv.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoProv(String value) {
        this.tipoProv = value;
    }

    /**
     * Obtiene el valor de la propiedad denoProv.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenoProv() {
        return denoProv;
    }

    /**
     * Define el valor de la propiedad denoProv.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenoProv(String value) {
        this.denoProv = value;
    }

    /**
     * Obtiene el valor de la propiedad parteRel.
     * 
     * @return
     *     possible object is
     *     {@link ParteRelType }
     *     
     */
    public ParteRelType getParteRel() {
        return parteRel;
    }

    /**
     * Define el valor de la propiedad parteRel.
     * 
     * @param value
     *     allowed object is
     *     {@link ParteRelType }
     *     
     */
    public void setParteRel(ParteRelType value) {
        this.parteRel = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaRegistro.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaRegistro() {
        return fechaRegistro;
    }

    /**
     * Define el valor de la propiedad fechaRegistro.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaRegistro(String value) {
        this.fechaRegistro = value;
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
     * Obtiene el valor de la propiedad baseNoGraIva.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBaseNoGraIva() {
        return baseNoGraIva;
    }

    /**
     * Define el valor de la propiedad baseNoGraIva.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBaseNoGraIva(BigDecimal value) {
        this.baseNoGraIva = value;
    }

    /**
     * Obtiene el valor de la propiedad baseImponible.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBaseImponible() {
        return baseImponible;
    }

    /**
     * Define el valor de la propiedad baseImponible.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBaseImponible(BigDecimal value) {
        this.baseImponible = value;
    }

    /**
     * Obtiene el valor de la propiedad baseImpGrav.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBaseImpGrav() {
        return baseImpGrav;
    }

    /**
     * Define el valor de la propiedad baseImpGrav.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBaseImpGrav(BigDecimal value) {
        this.baseImpGrav = value;
    }

    /**
     * Obtiene el valor de la propiedad baseImpExe.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBaseImpExe() {
        return baseImpExe;
    }

    /**
     * Define el valor de la propiedad baseImpExe.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBaseImpExe(BigDecimal value) {
        this.baseImpExe = value;
    }

    /**
     * Obtiene el valor de la propiedad montoIce.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontoIce() {
        return montoIce;
    }

    /**
     * Define el valor de la propiedad montoIce.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontoIce(BigDecimal value) {
        this.montoIce = value;
    }

    /**
     * Obtiene el valor de la propiedad montoIva.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMontoIva() {
        return montoIva;
    }

    /**
     * Define el valor de la propiedad montoIva.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMontoIva(BigDecimal value) {
        this.montoIva = value;
    }

    /**
     * Obtiene el valor de la propiedad valRetBien10.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValRetBien10() {
        return valRetBien10;
    }

    /**
     * Define el valor de la propiedad valRetBien10.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValRetBien10(BigDecimal value) {
        this.valRetBien10 = value;
    }

    /**
     * Obtiene el valor de la propiedad valRetServ20.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValRetServ20() {
        return valRetServ20;
    }

    /**
     * Define el valor de la propiedad valRetServ20.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValRetServ20(BigDecimal value) {
        this.valRetServ20 = value;
    }

    /**
     * Obtiene el valor de la propiedad valorRetBienes.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorRetBienes() {
        return valorRetBienes;
    }

    /**
     * Define el valor de la propiedad valorRetBienes.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorRetBienes(BigDecimal value) {
        this.valorRetBienes = value;
    }

    /**
     * Obtiene el valor de la propiedad valRetServ50.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValRetServ50() {
        return valRetServ50;
    }

    /**
     * Define el valor de la propiedad valRetServ50.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValRetServ50(BigDecimal value) {
        this.valRetServ50 = value;
    }

    /**
     * Obtiene el valor de la propiedad valorRetServicios.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorRetServicios() {
        return valorRetServicios;
    }

    /**
     * Define el valor de la propiedad valorRetServicios.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorRetServicios(BigDecimal value) {
        this.valorRetServicios = value;
    }

    /**
     * Obtiene el valor de la propiedad valRetServ100.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValRetServ100() {
        return valRetServ100;
    }

    /**
     * Define el valor de la propiedad valRetServ100.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValRetServ100(BigDecimal value) {
        this.valRetServ100 = value;
    }

    /**
     * Obtiene el valor de la propiedad valorRetencionNc.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorRetencionNc() {
        return valorRetencionNc;
    }

    /**
     * Define el valor de la propiedad valorRetencionNc.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorRetencionNc(BigDecimal value) {
        this.valorRetencionNc = value;
    }

    /**
     * Obtiene el valor de la propiedad totbasesImpReemb.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotbasesImpReemb() {
        return totbasesImpReemb;
    }

    /**
     * Define el valor de la propiedad totbasesImpReemb.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotbasesImpReemb(BigDecimal value) {
        this.totbasesImpReemb = value;
    }

    /**
     * Obtiene el valor de la propiedad pagoExterior.
     * 
     * @return
     *     possible object is
     *     {@link PagoExteriorType }
     *     
     */
    public PagoExteriorType getPagoExterior() {
        return pagoExterior;
    }

    /**
     * Define el valor de la propiedad pagoExterior.
     * 
     * @param value
     *     allowed object is
     *     {@link PagoExteriorType }
     *     
     */
    public void setPagoExterior(PagoExteriorType value) {
        this.pagoExterior = value;
    }

    /**
     * Obtiene el valor de la propiedad formasDePago.
     * 
     * @return
     *     possible object is
     *     {@link FormasDePagoType }
     *     
     */
    public FormasDePagoType getFormasDePago() {
        return formasDePago;
    }

    /**
     * Define el valor de la propiedad formasDePago.
     * 
     * @param value
     *     allowed object is
     *     {@link FormasDePagoType }
     *     
     */
    public void setFormasDePago(FormasDePagoType value) {
        this.formasDePago = value;
    }

    /**
     * Obtiene el valor de la propiedad air.
     * 
     * @return
     *     possible object is
     *     {@link AirType }
     *     
     */
    public AirType getAir() {
        return air;
    }

    /**
     * Define el valor de la propiedad air.
     * 
     * @param value
     *     allowed object is
     *     {@link AirType }
     *     
     */
    public void setAir(AirType value) {
        this.air = value;
    }

    /**
     * Obtiene el valor de la propiedad estabRetencion1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstabRetencion1() {
        return estabRetencion1;
    }

    /**
     * Define el valor de la propiedad estabRetencion1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstabRetencion1(String value) {
        this.estabRetencion1 = value;
    }

    /**
     * Obtiene el valor de la propiedad ptoEmiRetencion1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPtoEmiRetencion1() {
        return ptoEmiRetencion1;
    }

    /**
     * Define el valor de la propiedad ptoEmiRetencion1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPtoEmiRetencion1(String value) {
        this.ptoEmiRetencion1 = value;
    }

    /**
     * Obtiene el valor de la propiedad secRetencion1.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSecRetencion1() {
        return secRetencion1;
    }

    /**
     * Define el valor de la propiedad secRetencion1.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSecRetencion1(Integer value) {
        this.secRetencion1 = value;
    }

    /**
     * Obtiene el valor de la propiedad autRetencion1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAutRetencion1() {
        return autRetencion1;
    }

    /**
     * Define el valor de la propiedad autRetencion1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAutRetencion1(String value) {
        this.autRetencion1 = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaEmiRet1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaEmiRet1() {
        return fechaEmiRet1;
    }

    /**
     * Define el valor de la propiedad fechaEmiRet1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaEmiRet1(String value) {
        this.fechaEmiRet1 = value;
    }

    /**
     * Obtiene el valor de la propiedad estabRetencion2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstabRetencion2() {
        return estabRetencion2;
    }

    /**
     * Define el valor de la propiedad estabRetencion2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstabRetencion2(String value) {
        this.estabRetencion2 = value;
    }

    /**
     * Obtiene el valor de la propiedad ptoEmiRetencion2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPtoEmiRetencion2() {
        return ptoEmiRetencion2;
    }

    /**
     * Define el valor de la propiedad ptoEmiRetencion2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPtoEmiRetencion2(String value) {
        this.ptoEmiRetencion2 = value;
    }

    /**
     * Obtiene el valor de la propiedad secRetencion2.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSecRetencion2() {
        return secRetencion2;
    }

    /**
     * Define el valor de la propiedad secRetencion2.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSecRetencion2(Integer value) {
        this.secRetencion2 = value;
    }

    /**
     * Obtiene el valor de la propiedad autRetencion2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAutRetencion2() {
        return autRetencion2;
    }

    /**
     * Define el valor de la propiedad autRetencion2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAutRetencion2(String value) {
        this.autRetencion2 = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaEmiRet2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaEmiRet2() {
        return fechaEmiRet2;
    }

    /**
     * Define el valor de la propiedad fechaEmiRet2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaEmiRet2(String value) {
        this.fechaEmiRet2 = value;
    }

    /**
     * Obtiene el valor de la propiedad docModificado.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public String getDocModificado() {
        return docModificado;
    }

    /**
     * Define el valor de la propiedad docModificado.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setDocModificado(String value) {
        this.docModificado = value;
    }

    /**
     * Obtiene el valor de la propiedad estabModificado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstabModificado() {
        return estabModificado;
    }

    /**
     * Define el valor de la propiedad estabModificado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstabModificado(String value) {
        this.estabModificado = value;
    }

    /**
     * Obtiene el valor de la propiedad ptoEmiModificado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPtoEmiModificado() {
        return ptoEmiModificado;
    }

    /**
     * Define el valor de la propiedad ptoEmiModificado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPtoEmiModificado(String value) {
        this.ptoEmiModificado = value;
    }

    /**
     * Obtiene el valor de la propiedad secModificado.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSecModificado() {
        return secModificado;
    }

    /**
     * Define el valor de la propiedad secModificado.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSecModificado(Integer value) {
        this.secModificado = value;
    }

    /**
     * Obtiene el valor de la propiedad autModificado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAutModificado() {
        return autModificado;
    }

    /**
     * Define el valor de la propiedad autModificado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAutModificado(String value) {
        this.autModificado = value;
    }

    /**
     * Obtiene el valor de la propiedad reembolsos.
     * 
     * @return
     *     possible object is
     *     {@link ReembolsosType }
     *     
     */
    public ReembolsosType getReembolsos() {
        return reembolsos;
    }

    /**
     * Define el valor de la propiedad reembolsos.
     * 
     * @param value
     *     allowed object is
     *     {@link ReembolsosType }
     *     
     */
    public void setReembolsos(ReembolsosType value) {
        this.reembolsos = value;
    }

}
