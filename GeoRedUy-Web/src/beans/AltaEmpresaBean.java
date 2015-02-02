package beans;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import controladores.IControladorEmpresa;

public class AltaEmpresaBean {

	private String nombreEmpresa;
	private String email;
	private String contrasenia;

	@EJB
	private IControladorEmpresa icEmpresa;

	public String getNombreEmpresa() {
		return nombreEmpresa;
	}

	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	public String crearEmpresa() {

		boolean ret = icEmpresa.altaEmpresa(email, nombreEmpresa, contrasenia);

		if (ret) {
			String mensaje = "Empresa creada con éxito";
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(mensaje, null));
			
			 nombreEmpresa="";
			 email="";
			 contrasenia="";
			 return "homeAdminApp";
			
		} else {
			String mensaje = "Error al crear la empresa";
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(mensaje, null));
			return "";
		}
		
		
	}

}
