package ec.com.tecnointel.soem.firmaElec.registroImp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.cert.X509Certificate;
import java.util.Optional;

import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;

import ec.com.tecnointel.soem.firmaElec.registroInt.ValidarRucFirmaInt;
import ec.com.tecnointel.soem.parametro.modelo.SucuCertEmis;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import xades4j.providers.SigningCertChainException;
import xades4j.verification.UnexpectedJCAException;

public class validarM1 implements ValidarRucFirmaInt {

	/**
	 * SECURITY DATA - BANCO CENTRAL - ANF - LAZZATE
	 */
	
	@Override
	public boolean validarRucFirma(Sucursal sucursal) throws SigningCertChainException, UnexpectedJCAException, IOException {

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
				
		byte[] extensionValue = signingCertificate.getExtensionValue(sucursal.getSucuCertEmis().getCertEmis().getCertOid());
		
//		Metodo 1: Para obtener el numero de ruc
		if (extensionValue != null) {
				ASN1Primitive extensionValue1 = JcaX509ExtensionUtils.parseExtensionValue(extensionValue);
				rucCertificado = extensionValue1.toString();
		}

		Optional<String> rucOptional = Optional.ofNullable(rucCertificado);
				
		if (rucOptional.isPresent() && rucCertificado.equals(sucursal.getRuc())) {
			rucValido = true;
		}

//		Metodo 2: Para obtener el numero de ruc
//		try {
//			ASN1Primitive derObject = toDERObject(extensionValue);
//			if (derObject instanceof DEROctetString) {
//				DEROctetString derOctetString = (DEROctetString) derObject;
//
//				derObject = toDERObject(derOctetString.getOctets());
//				if (derObject instanceof ASN1String) {
//					ASN1String s = (ASN1String) derObject;
//					rucCertificado = s.getString();
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
//		Transforma byte a string en este caso el numero de ruc
//		No funciona coloca caracteres que parecen saltos de linea al inicio
//		String rucCertificado = new String(extensionValue);
//		System.out.println("Extension Value como String: " + rucCertificado);

		return rucValido;
	}
}
