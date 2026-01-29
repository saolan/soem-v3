package ec.com.tecnointel.soem.firmaElec.registroImp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;

import ec.com.tecnointel.soem.firmaElec.registroInt.ValidarRucFirmaInt;
import ec.com.tecnointel.soem.parametro.modelo.SucuCertEmis;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import xades4j.providers.SigningCertChainException;
import xades4j.verification.UnexpectedJCAException;

public class validarM2 implements ValidarRucFirmaInt {

	/**
	 * CONSEJO DE LA JUDICATURA - UANATACA - ECLIPSOFT
	 * 
	 * @throws CertificateParsingException
	 */

	@Override
	public boolean validarRucFirma(Sucursal sucursal)
			throws SigningCertChainException, UnexpectedJCAException, IOException, CertificateParsingException {

		boolean rucValido = false;

		String rucCertificado = null;

		Optional<SucuCertEmis> sucuCertEmisOptional = Optional.ofNullable(sucursal.getSucuCertEmis());
		if (!sucuCertEmisOptional.isPresent()) {
			return false;
		}

		Path path = Paths.get(sucursal.getSucuCertEmis().getRuta(), sucursal.getSucuCertEmis().getDescri());

		if (!Files.exists(path)) {
			return false;
		}

		RecuperarDatosCertEmis recuperarDatosCertEmis = new RecuperarDatosCertEmis(sucursal);
		X509Certificate signingCertificate = recuperarDatosCertEmis.recuperarDatos();

		Collection<List<?>> coleccionDatosAlternativos = null;
		coleccionDatosAlternativos = JcaX509ExtensionUtils.getSubjectAlternativeNames(signingCertificate);

		for (List<?> datoAlternativo : coleccionDatosAlternativos) {

			if (datoAlternativo.get(1) instanceof byte[]) {

				ASN1InputStream aSN1InputStream = new ASN1InputStream((byte[]) datoAlternativo.toArray()[1]);

				ASN1Primitive asn1Primitive = aSN1InputStream.readObject();
				ASN1Encodable aSN1Encodable = ((ASN1TaggedObject) asn1Primitive).getBaseObject();

				ASN1ObjectIdentifier derObjectIdentifier = new ASN1ObjectIdentifier(
						sucursal.getSucuCertEmis().getCertEmis().getCertOid());

				if (aSN1Encodable instanceof ASN1Sequence) {

					ASN1Sequence sequence = (ASN1Sequence) aSN1Encodable;

					for (int i = 0; i < sequence.size(); i++) {

						ASN1Encodable element = sequence.getObjectAt(i);

						if (element.equals(derObjectIdentifier)) {

							rucCertificado = sequence.getObjectAt(i + 1).toString().replace("[CONTEXT 0]", "");
						}

//						Tambien se pueee usar asi esta validacion
//						if (sequence.getObjectAt(i).toString()
//								.equals(sucursal.getSucuCertEmis().getCertEmis().getCertOid())) {
//
//							rucCertificado = sequence.getObjectAt(i + 1).toString().replace("[CONTEXT 0]", "");
//						}
					}
				}
			}
		}

		Optional<String> rucOptional = Optional.ofNullable(rucCertificado);

		if (rucOptional.isPresent() && rucCertificado.equals(sucursal.getRuc())) {
			rucValido = true;
		}

		return rucValido;
	}

//	public boolean validarRucFirmaM1(Sucursal sucursal) throws SigningCertChainException, UnexpectedJCAException, IOException {
//		
//		Optional<SucuCertEmis> sucuCertEmisOptional = Optional.fromNullable(sucursal.getSucuCertEmis());
//		if (!sucuCertEmisOptional.isPresent()) {
//			return false;
//		}
//		
//		Path path = Paths.get(sucursal.getSucuCertEmis().getRuta(), sucursal.getSucuCertEmis().getDescri());
//
//		if (!Files.exists(path)) {
//			return false;
//		}
//
//		RecuperarDatosCertEmis recuperarDatosCertEmis = new RecuperarDatosCertEmis(sucursal);
//		X509Certificate signingCertificate = recuperarDatosCertEmis.recuperarDatos();
//		
//		System.out.println("validarRucFirmaM2");
//		boolean rucValido = false;
//
////		CONSEJO DE LA JUDICATURA - UANATACA - ECLIPSOFT
//		Collection<List<?>> coleccionDatosAlternativos = null;
//		try {
////			coleccionDatosAlternativos = X509ExtensionUtil.getSubjectAlternativeNames(signingCertificate);
//			coleccionDatosAlternativos = JcaX509ExtensionUtils.getSubjectAlternativeNames(signingCertificate);
//		} catch (CertificateParsingException e) {
//			e.printStackTrace();
//		}
//
//		Iterator iteradorColeccion = coleccionDatosAlternativos.iterator();
//		while (iteradorColeccion.hasNext()) {
//			
//			List<Object> listaDatosAlternativo = (List) iteradorColeccion.next();
//			
//			for (Object datoAlternativo : listaDatosAlternativo) {
//
//				System.out.println(datoAlternativo.toString());
//				
//				if ((datoAlternativo instanceof DLSequence)) {
//
//					DLSequence datoDERSequence = (DLSequence) datoAlternativo;
//					ASN1ObjectIdentifier derObjectIdentifier = (ASN1ObjectIdentifier) datoDERSequence.getObjectAt(0);
//
//					System.out.println(derObjectIdentifier.toString());
//					
//					if (derObjectIdentifier.toString().equals(sucursal.getSucuCertEmis().getCertEmis().getCertOid())) {
//
//						DLTaggedObject derTaggedObject = (DLTaggedObject) datoDERSequence.getObjectAt(1);
//						
//						ASN1Primitive asn1Primitive = derTaggedObject.getBaseObject().toASN1Primitive();
//
//						String rucCertificado = asn1Primitive.toString();
//
//						System.out.println(asn1Primitive.toString());
//
//						Optional<String> rucOptional = Optional.fromNullable(rucCertificado);
//						
//						if (rucOptional.isPresent() && rucCertificado.equals(sucursal.getRuc())) {
//							rucValido = true;
//							System.out.println("Valido con metodo 2");
//						}
//					}
//				}
//			}
//		}
//		return rucValido;
//	}

}
