package ec.com.tecnointel.soem.firmaElec.registroImp;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xml.security.algorithms.MessageDigestAlgorithm;
import org.apache.xml.security.signature.XMLSignature;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ec.com.tecnointel.soem.firmaElec.registroInt.FirmarArchivoInt;
import ec.com.tecnointel.soem.parametro.modelo.SucuCertEmis;
import jakarta.ejb.Stateless;
import xades4j.algorithms.EnvelopedSignatureTransform;
import xades4j.production.BasicSignatureOptions;
import xades4j.production.DataObjectReference;
import xades4j.production.SignatureAlgorithms;
import xades4j.production.SignedDataObjects;
import xades4j.production.XadesBesSigningProfileSRI;
import xades4j.production.XadesSigner;
import xades4j.properties.DataObjectDesc;
import xades4j.properties.DataObjectFormatProperty;
import xades4j.providers.KeyingDataProvider;
import xades4j.providers.impl.DirectPasswordProvider;
import xades4j.providers.impl.FileSystemKeyStoreKeyingDataProvider;
import xades4j.providers.impl.SeleccionarCertificado;
//import xades4j.providers.impl.TSAHttpAuthenticationData;
import xades4j.utils.DOMHelper;

@Stateless
public class FirmarArchivoImp implements FirmarArchivoInt {

//	Como hacer llegar datos a sucuCertEmis???
//	Ya que desde venta control se enta inyenctanrdo esta clase
//	entonces este dato debe venir desde el lugar donde se este llamando este metodo
//	que puede ser ventaControl, compraControl, docuElecControl
//	SucuCertEmis sucuCertEmis;
	
	@Override
	public void signBes(SucuCertEmis sucuCertEmis, String nombreArchivo, String rutaGenerados, String rutaFirmado) throws Exception {

		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(rutaGenerados + nombreArchivo));
		Element elem = doc.getDocumentElement();

		DOMHelper.useIdAsXmlId(elem);

//		SeleccionarCertificado seleccionarCertificado = new SeleccionarCertificado();
//		System.out.println(seleccionarCertificado.toString());
				
		KeyingDataProvider kdp = FileSystemKeyStoreKeyingDataProvider
				.builder("pkcs12", sucuCertEmis.getRuta() + sucuCertEmis.getDescri(), new SeleccionarCertificado())
				.storePassword(new DirectPasswordProvider(sucuCertEmis.getClave()))
				.entryPassword(new DirectPasswordProvider(sucuCertEmis.getClave()))
				.fullChain(false)
				.build();
		
		XadesBesSigningProfileSRI p =  null;
        
	    p = (XadesBesSigningProfileSRI) new XadesBesSigningProfileSRI(kdp).withSignatureAlgorithms(new SignatureAlgorithms()
	                    .withDigestAlgorithmForDataObjectReferences(MessageDigestAlgorithm.ALGO_ID_DIGEST_SHA1)
	                    .withDigestAlgorithmForReferenceProperties(MessageDigestAlgorithm.ALGO_ID_DIGEST_SHA1)
	                    .withDigestAlgorithmForTimeStampProperties(MessageDigestAlgorithm.ALGO_ID_DIGEST_SHA1)
	                    .withSignatureAlgorithm("RSA", XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1)
	                    .withSignatureAlgorithm("EC", XMLSignature.ALGO_ID_SIGNATURE_ECDSA_SHA1)
	                    .withSignatureAlgorithm("DSA", XMLSignature.ALGO_ID_SIGNATURE_ECDSA_SHA1)
	                    ).withBasicSignatureOptions(new BasicSignatureOptions().includePublicKey(true).signKeyInfo(true));


//	    Estos dos se usan como para registrar el id
//	    revisar y selecionar cual se va a quedar
//	    elem.setIdAttributeNS(null, "id", true);
	    elem.setIdAttribute("id", true);
	    
	    DataObjectDesc obj = new DataObjectReference("#" + elem.getAttribute("id"))
				.withTransform(new EnvelopedSignatureTransform())
				.withDataObjectFormat(
						new DataObjectFormatProperty("text/xml").withDescription("contenido comprobante"));
	    		
        XadesSigner signer = p.newSigner();
        Element elemToSign = doc.getDocumentElement();
        signer.sign(new SignedDataObjects(obj), elem);
        
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();          
        DOMSource source = new DOMSource(elemToSign);
        StreamResult result = new StreamResult(new File(rutaFirmado + nombreArchivo));
        transformer.transform(source, result);
	}
}
