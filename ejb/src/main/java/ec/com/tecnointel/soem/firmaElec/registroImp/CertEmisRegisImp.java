package ec.com.tecnointel.soem.firmaElec.registroImp;

import java.io.Serializable;

import ec.com.tecnointel.soem.firmaElec.modelo.CertEmis;
import ec.com.tecnointel.soem.firmaElec.registroInt.CertEmisRegisInt;
import ec.com.tecnointel.soem.general.util.GestorRegisSoem;
import jakarta.ejb.Stateless;

@Stateless
public class CertEmisRegisImp extends GestorRegisSoem<CertEmis> implements CertEmisRegisInt, Serializable {

	private static final long serialVersionUID = 2455112918139265700L;

}
