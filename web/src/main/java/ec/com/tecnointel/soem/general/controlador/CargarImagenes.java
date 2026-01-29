package ec.com.tecnointel.soem.general.controlador;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import ec.com.tecnointel.soem.parametro.modelo.Parametro;
import ec.com.tecnointel.soem.parametro.registroInt.ParametroRegisInt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.PhaseId;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ApplicationScoped
public class CargarImagenes {

	@Inject
	ParametroRegisInt parametroRegis;

	public Parametro buscarRutaImagenInventario() {

		Parametro parametro = null;

		try {
			parametro = parametroRegis.buscarPorId(Parametro.class, 5020);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, null,
					"Excepcion 5020 - Error al buscar ruta imagenes inventario"));
			e.printStackTrace();
		}

		return parametro;

	}

	public StreamedContent getImagen() throws IOException {

		Parametro parametro = new Parametro();
		String rutaImagen;

		FacesContext context = FacesContext.getCurrentInstance();

		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			return new DefaultStreamedContent();
		} else {

			parametro = this.buscarRutaImagenInventario();
			rutaImagen = parametro.getDescri();

			String archivo = context.getExternalContext().getRequestParameterMap().get("productoId") + ".png";

			InputStream inputStream = new FileInputStream(new File(rutaImagen + archivo));

			return DefaultStreamedContent.builder().name(archivo).contentType("image/png").stream(() -> inputStream)
					.build();
		}
	}
}
