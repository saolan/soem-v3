//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantaci�n de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perder�n si se vuelve a compilar el esquema de origen. 
// Generado el: 2023.12.05 a las 03:49:23 PM COT 
//


package ec.com.tecnointel.soem.atsSri.modelo;

import java.math.BigDecimal;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para ivaType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ivaType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TipoIDInformante">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="R"/>
 *               &lt;length value="1"/>
 *               &lt;whiteSpace value="collapse"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="IdInformante" type="{}numeroRucType"/>
 *         &lt;element name="razonSocial" type="{}razonSocialType"/>
 *         &lt;element name="Anio" type="{}anioType"/>
 *         &lt;element name="Mes" type="{}mesType"/>
 *         &lt;element name="regimenMicroempresa" type="{}regimenSemestralType" minOccurs="0"/>
 *         &lt;element name="numEstabRuc" type="{}numEstabRucType" minOccurs="0"/>
 *         &lt;element name="totalVentas" type="{}totalVentasType" minOccurs="0"/>
 *         &lt;element name="codigoOperativo" type="{}codigoOperativoType"/>
 *         &lt;element name="compras" type="{}comprasType" minOccurs="0"/>
 *         &lt;element name="ventas" type="{}ventasType" minOccurs="0"/>
 *         &lt;element name="ventasEstablecimiento" type="{}ventasEstablecimientoType" minOccurs="0"/>
 *         &lt;element name="exportaciones" type="{}exportacionesType" minOccurs="0"/>
 *         &lt;element name="recap" type="{}recapType" minOccurs="0"/>
 *         &lt;element name="fideicomisos" type="{}fideicomisosType" minOccurs="0"/>
 *         &lt;element name="anulados" type="{}anuladosType" minOccurs="0"/>
 *         &lt;element name="rendFinancieros" type="{}rendFinancierosType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ivaType", propOrder = {
    "tipoIDInformante",
    "idInformante",
    "razonSocial",
    "anio",
    "mes",
    "regimenMicroempresa",
    "numEstabRuc",
    "totalVentas",
    "codigoOperativo",
    "compras",
    "ventas",
    "ventasEstablecimiento",
    "exportaciones",
    "recap",
    "fideicomisos",
    "anulados",
    "rendFinancieros"
})
@XmlRootElement(name = "iva")
public class IvaType {

    @XmlElement(name = "TipoIDInformante", required = true)
    protected String tipoIDInformante;
    @XmlElement(name = "IdInformante", required = true)
    protected String idInformante;
    @XmlElement(required = true)
    protected String razonSocial;
    @XmlElement(name = "Anio")
    @XmlSchemaType(name = "integer")
    protected int anio;
    @XmlElement(name = "Mes", required = true)
    protected String mes;
    @XmlSchemaType(name = "string")
    protected RegimenSemestralType regimenMicroempresa;
    @XmlSchemaType(name = "integer")
    protected String numEstabRuc;
    protected BigDecimal totalVentas;
    @XmlElement(required = true)
    @XmlSchemaType(name = "string")
    protected CodigoOperativoType codigoOperativo;
    protected ComprasType compras;
    protected VentasType ventas;
    protected VentasEstablecimientoType ventasEstablecimiento;
    protected ExportacionesType exportaciones;
    protected RecapType recap;
    protected FideicomisosType fideicomisos;
    protected AnuladosType anulados;
    protected RendFinancierosType rendFinancieros;

    /**
     * Obtiene el valor de la propiedad tipoIDInformante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoIDInformante() {
        return tipoIDInformante;
    }

    /**
     * Define el valor de la propiedad tipoIDInformante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoIDInformante(String value) {
        this.tipoIDInformante = value;
    }

    /**
     * Obtiene el valor de la propiedad idInformante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdInformante() {
        return idInformante;
    }

    /**
     * Define el valor de la propiedad idInformante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdInformante(String value) {
        this.idInformante = value;
    }

    /**
     * Obtiene el valor de la propiedad razonSocial.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRazonSocial() {
        return razonSocial;
    }

    /**
     * Define el valor de la propiedad razonSocial.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRazonSocial(String value) {
        this.razonSocial = value;
    }

    /**
     * Obtiene el valor de la propiedad anio.
     * 
     */
    public int getAnio() {
        return anio;
    }

    /**
     * Define el valor de la propiedad anio.
     * 
     */
    public void setAnio(int value) {
        this.anio = value;
    }

    /**
     * Obtiene el valor de la propiedad mes.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMes() {
        return mes;
    }

    /**
     * Define el valor de la propiedad mes.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMes(String value) {
        this.mes = value;
    }

    /**
     * Obtiene el valor de la propiedad regimenMicroempresa.
     * 
     * @return
     *     possible object is
     *     {@link RegimenSemestralType }
     *     
     */
    public RegimenSemestralType getRegimenMicroempresa() {
        return regimenMicroempresa;
    }

    /**
     * Define el valor de la propiedad regimenMicroempresa.
     * 
     * @param value
     *     allowed object is
     *     {@link RegimenSemestralType }
     *     
     */
    public void setRegimenMicroempresa(RegimenSemestralType value) {
        this.regimenMicroempresa = value;
    }

    /**
     * Obtiene el valor de la propiedad numEstabRuc.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public String getNumEstabRuc() {
        return numEstabRuc;
    }

    /**
     * Define el valor de la propiedad numEstabRuc.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumEstabRuc(String value) {
        this.numEstabRuc = value;
    }

    /**
     * Obtiene el valor de la propiedad totalVentas.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalVentas() {
        return totalVentas;
    }

    /**
     * Define el valor de la propiedad totalVentas.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalVentas(BigDecimal value) {
        this.totalVentas = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoOperativo.
     * 
     * @return
     *     possible object is
     *     {@link CodigoOperativoType }
     *     
     */
    public CodigoOperativoType getCodigoOperativo() {
        return codigoOperativo;
    }

    /**
     * Define el valor de la propiedad codigoOperativo.
     * 
     * @param value
     *     allowed object is
     *     {@link CodigoOperativoType }
     *     
     */
    public void setCodigoOperativo(CodigoOperativoType value) {
        this.codigoOperativo = value;
    }

    /**
     * Obtiene el valor de la propiedad compras.
     * 
     * @return
     *     possible object is
     *     {@link ComprasType }
     *     
     */
    public ComprasType getCompras() {
        return compras;
    }

    /**
     * Define el valor de la propiedad compras.
     * 
     * @param value
     *     allowed object is
     *     {@link ComprasType }
     *     
     */
    public void setCompras(ComprasType value) {
        this.compras = value;
    }

    /**
     * Obtiene el valor de la propiedad ventas.
     * 
     * @return
     *     possible object is
     *     {@link VentasType }
     *     
     */
    public VentasType getVentas() {
        return ventas;
    }

    /**
     * Define el valor de la propiedad ventas.
     * 
     * @param value
     *     allowed object is
     *     {@link VentasType }
     *     
     */
    public void setVentas(VentasType value) {
        this.ventas = value;
    }

    /**
     * Obtiene el valor de la propiedad ventasEstablecimiento.
     * 
     * @return
     *     possible object is
     *     {@link VentasEstablecimientoType }
     *     
     */
    public VentasEstablecimientoType getVentasEstablecimiento() {
        return ventasEstablecimiento;
    }

    /**
     * Define el valor de la propiedad ventasEstablecimiento.
     * 
     * @param value
     *     allowed object is
     *     {@link VentasEstablecimientoType }
     *     
     */
    public void setVentasEstablecimiento(VentasEstablecimientoType value) {
        this.ventasEstablecimiento = value;
    }

    /**
     * Obtiene el valor de la propiedad exportaciones.
     * 
     * @return
     *     possible object is
     *     {@link ExportacionesType }
     *     
     */
    public ExportacionesType getExportaciones() {
        return exportaciones;
    }

    /**
     * Define el valor de la propiedad exportaciones.
     * 
     * @param value
     *     allowed object is
     *     {@link ExportacionesType }
     *     
     */
    public void setExportaciones(ExportacionesType value) {
        this.exportaciones = value;
    }

    /**
     * Obtiene el valor de la propiedad recap.
     * 
     * @return
     *     possible object is
     *     {@link RecapType }
     *     
     */
    public RecapType getRecap() {
        return recap;
    }

    /**
     * Define el valor de la propiedad recap.
     * 
     * @param value
     *     allowed object is
     *     {@link RecapType }
     *     
     */
    public void setRecap(RecapType value) {
        this.recap = value;
    }

    /**
     * Obtiene el valor de la propiedad fideicomisos.
     * 
     * @return
     *     possible object is
     *     {@link FideicomisosType }
     *     
     */
    public FideicomisosType getFideicomisos() {
        return fideicomisos;
    }

    /**
     * Define el valor de la propiedad fideicomisos.
     * 
     * @param value
     *     allowed object is
     *     {@link FideicomisosType }
     *     
     */
    public void setFideicomisos(FideicomisosType value) {
        this.fideicomisos = value;
    }

    /**
     * Obtiene el valor de la propiedad anulados.
     * 
     * @return
     *     possible object is
     *     {@link AnuladosType }
     *     
     */
    public AnuladosType getAnulados() {
        return anulados;
    }

    /**
     * Define el valor de la propiedad anulados.
     * 
     * @param value
     *     allowed object is
     *     {@link AnuladosType }
     *     
     */
    public void setAnulados(AnuladosType value) {
        this.anulados = value;
    }

    /**
     * Obtiene el valor de la propiedad rendFinancieros.
     * 
     * @return
     *     possible object is
     *     {@link RendFinancierosType }
     *     
     */
    public RendFinancierosType getRendFinancieros() {
        return rendFinancieros;
    }

    /**
     * Define el valor de la propiedad rendFinancieros.
     * 
     * @param value
     *     allowed object is
     *     {@link RendFinancierosType }
     *     
     */
    public void setRendFinancieros(RendFinancierosType value) {
        this.rendFinancieros = value;
    }

}
