package beans;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;


import controladores.IControladorEmpresa;

@ManagedBean
@ViewScoped
public class ModificarEmpresaBean {

	
	private String descripcion;
	private byte[] file;
	private String fileName;
	
	
	@ManagedProperty(value="#{sesionBean}")
	private SesionBean sesion;
	
	@EJB
	private IControladorEmpresa icEmpresa;

	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}



	public void setSesion(SesionBean sesion) {
		this.sesion = sesion;
	}

	public void upload(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Ok! ", event.getFile()
				.getFileName() + " fue subido con éxito.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	

		System.out.println("adjunteeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeEEEEEEEEE");
		file=event.getFile().getContents();
		fileName=event.getFile().getFileName();
		System.out.println(fileName);
		
	}
	
	public String modificarEmpresa(){
		
		icEmpresa.modificarEmpresa(sesion.getNombreEmpresa(), descripcion, file,fileName);
	
		descripcion="";
		file=null;
		fileName="";
		String mensaje = "Datos de la empresa modificados";
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(mensaje, null));
		return "homeAdminEmpresa";
	}
	
}
