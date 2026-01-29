package ec.com.tecnointel.soem.serWebClientSri.general;

public enum EstadoAutorizacion {
	
	AUT("AUTORIZADO"), NAU("NO AUTORIZADO"), PRO("EN PROCESO"), NPR("NO PROCESADO");

	private String descripcion;

	private EstadoAutorizacion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public static EstadoAutorizacion getEstadoAutorizacion(String descripcion) {
		EstadoAutorizacion[] listaEstadoAutorizaciones = values();
		for (EstadoAutorizacion estadoAutorizacion : listaEstadoAutorizaciones) {
			if (descripcion.equals(estadoAutorizacion.getDescripcion())) {
				return estadoAutorizacion;
			}
		}
		return null;
	}
}
