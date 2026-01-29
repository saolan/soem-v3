package ec.com.tecnointel.soem.firmaElec.registroInt;

import java.io.IOException;
import java.security.cert.CertificateParsingException;

import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import xades4j.providers.SigningCertChainException;
import xades4j.verification.UnexpectedJCAException;

public interface ValidarRucFirmaInt {
	
	boolean validarRucFirma(Sucursal sucursal) throws SigningCertChainException, UnexpectedJCAException, IOException, CertificateParsingException;
	
}
