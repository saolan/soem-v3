package ec.com.tecnointel.soem.firmaElec.registroImp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.cert.X509Certificate;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.IETFUtils;

import ec.com.tecnointel.soem.firmaElec.registroInt.ValidarRucFirmaInt;
import ec.com.tecnointel.soem.parametro.modelo.SucuCertEmis;
import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import xades4j.providers.SigningCertChainException;
import xades4j.verification.UnexpectedJCAException;

public class validarM3 implements ValidarRucFirmaInt {

	/**
	 * CORPNEWBEST - Certificados sin OID 1.3.6.1.4.1.34380.3.11
	 *
	 * El ruc se obtiene del atributo del sujeto indicado en cert_emis.cert_organi_iden (organizationIdentifier 2.5.4.97)
	 * Formato ETSI EN 319 412-1: TINEC-1500818016001 (tipo + pais + "-" + ruc)
	 */

	private static final Pattern PATRON_IDENTIFICADOR = Pattern.compile("^[A-Z]{3}[A-Z]{2}-(.+)$");

	@Override
	public boolean validarRucFirma(Sucursal sucursal) throws SigningCertChainException, UnexpectedJCAException, IOException {

		boolean rucValido = false;

		String rucCertificado = null;

		Optional<SucuCertEmis> sucuCertEmisOptional = Optional.ofNullable(sucursal.getSucuCertEmis());
		if (!sucuCertEmisOptional.isPresent()) {
			return false;
		}

		Optional<String> certOrganiIdenOptional = Optional
				.ofNullable(sucursal.getSucuCertEmis().getCertEmis().getCertOrganiIden())
				.map(String::trim)
				.filter(valor -> !valor.isEmpty());
		if (!certOrganiIdenOptional.isPresent()) {
			return false;
		}

		Path path = Paths.get(sucursal.getSucuCertEmis().getRuta(), sucursal.getSucuCertEmis().getDescri());

		if (!Files.exists(path)) {
			return false;
		}

		RecuperarDatosCertEmis recuperarDatosCertEmis = new RecuperarDatosCertEmis(sucursal);
		X509Certificate signingCertificate = recuperarDatosCertEmis.recuperarDatos();

//		Solo se lee el sujeto, el emisor tambien tiene 2.5.4.97 pero es el ruc de la entidad certificadora
		X500Name x500Name = X500Name.getInstance(signingCertificate.getSubjectX500Principal().getEncoded());
		RDN[] rdns = x500Name.getRDNs(new ASN1ObjectIdentifier(certOrganiIdenOptional.get()));

		for (RDN rdn : rdns) {
			String valor = IETFUtils.valueToString(rdn.getFirst().getValue());
			rucCertificado = extraerRuc(valor);
		}

		Optional<String> rucOptional = Optional.ofNullable(rucCertificado);

		if (rucOptional.isPresent() && rucCertificado.equals(sucursal.getRuc())) {
			rucValido = true;
		}

		return rucValido;
	}

	private String extraerRuc(String valor) {

		String valorLimpio = valor.trim();

		Matcher matcher = PATRON_IDENTIFICADOR.matcher(valorLimpio);
		if (matcher.matches()) {
			return matcher.group(1).trim();
		}

		return valorLimpio;
	}
}
