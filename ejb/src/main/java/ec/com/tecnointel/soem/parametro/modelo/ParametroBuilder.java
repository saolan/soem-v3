package ec.com.tecnointel.soem.parametro.modelo;

public class ParametroBuilder {

	private Integer parametroId;
	private String codigo;
	private String descri;
	private boolean estado;

//	Hace un atributo obligatorio en esta caso de deberia quitar el with estado
//	public ParametroBuilder(boolean estado) {
//		this.estado =  estado;
//	}

	public ParametroBuilder() {
	}

	public ParametroBuilder withParametroId(Integer parametroId) {
		this.parametroId = parametroId;
		return this;
	}

	public ParametroBuilder withCodigo(String codigo) {
		this.codigo = codigo;
		return this;
	}

	public ParametroBuilder withDescri(String descri) {
		this.descri = descri;
		return this;
	}

	public ParametroBuilder withEstado(boolean estado) {
		this.estado = estado;
		return this;
	}

	public Parametro build() {
		Parametro parametro = new Parametro();
		parametro.setParametroId(parametroId);
		parametro.setCodigo(codigo);
		parametro.setDescri(descri);
		parametro.setEstado(estado);
		return parametro;
	}
}
