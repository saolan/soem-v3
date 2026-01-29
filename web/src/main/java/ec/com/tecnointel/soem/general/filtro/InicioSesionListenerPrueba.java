package ec.com.tecnointel.soem.general.filtro;

import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;

//@WebListener
public class InicioSesionListenerPrueba implements ServletRequestListener {

	@Override
	public void requestDestroyed(ServletRequestEvent arg0) {
		System.out.println("===================================== requestDestroyed ");
	}

	@Override
	public void requestInitialized(ServletRequestEvent sre) {
		System.out.println("===================================== requestInitialized ");
	}

}
