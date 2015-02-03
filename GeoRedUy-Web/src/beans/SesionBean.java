package beans;

@ManagedBean
@SessionScoped
public class SesionBean {

	private String tokenId;
	private String mail;
	private String tipoUsuario;
	private String nombreEmpresa;

	public String getMail() {
		return this.mail;
	}

	public String getNombreEmpresa() {
		return this.nombreEmpresa;
	}

	public String getTipoUsuario() {
		return this.tipoUsuario;
	}

	public String getTokenId() {
		return this.tokenId;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

}
