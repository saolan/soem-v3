package ec.com.tecnointel.soem.documeElec.key;

import java.security.KeyStore;
import java.security.KeyStoreException;

public interface KeyStoreProvider {
	  public abstract KeyStore getKeystore(char[] paramArrayOfChar)
			    throws KeyStoreException;
}
