package ec.com.tecnointel.soem.firmaElec.registroImp;

import java.security.cert.X509Certificate;
import java.util.List;

import ec.com.tecnointel.soem.parametro.modelo.Sucursal;
import xades4j.providers.KeyingDataProvider;
import xades4j.providers.SigningCertChainException;
import xades4j.providers.impl.DirectPasswordProvider;
import xades4j.providers.impl.FileSystemKeyStoreKeyingDataProvider;
import xades4j.providers.impl.SeleccionarCertificado;
import xades4j.verification.UnexpectedJCAException;

public class RecuperarDatosCertEmis {

	Sucursal sucursal;

	public RecuperarDatosCertEmis() {
	}

	public RecuperarDatosCertEmis(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	public X509Certificate recuperarDatos() throws SigningCertChainException, UnexpectedJCAException {
		
		KeyingDataProvider kdp = FileSystemKeyStoreKeyingDataProvider
				.builder("pkcs12", sucursal.getSucuCertEmis().getRuta() + sucursal.getSucuCertEmis().getDescri(), new SeleccionarCertificado())
				.storePassword(new DirectPasswordProvider(sucursal.getSucuCertEmis().getClave()))
				.entryPassword(new DirectPasswordProvider(sucursal.getSucuCertEmis().getClave()))
				.fullChain(false)
				.build();
		
		List<java.security.cert.X509Certificate> signingCertificateChain = kdp.getSigningCertificateChain();
//		if (null == signingCertificateChain || signingCertificateChain.isEmpty()) {
//			mensaje = "Firma electrónica no registrada";
//		}
		
		return signingCertificateChain.get(0);
	}
}
