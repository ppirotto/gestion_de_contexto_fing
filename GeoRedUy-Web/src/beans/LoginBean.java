package beans;



import javax.ejb.EJB;

import pojos.Administrador;
import pojos.AdministradorApp;
import pojos.AdministradorEmpresa;




import controladores.IControladorAdministrador;


public class LoginBean {

	private String usuario;
	private String contrasenia;
	private SesionBean sesion;
	
	public void setSesion(SesionBean sesion) {
		this.sesion = sesion;
	}
	
	@EJB
	private IControladorAdministrador icadmin;

	

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		/*MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        md.update(contrasena.getBytes());
        
      //convert the byte to hex format method 1
        byte byteData[] = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
       
		
		
		this.contrasena = sb.toString();*/
		this.contrasenia = contrasenia;
	}


	public String iniciarSesion() {
		Administrador ad = icadmin.loguearAdmin(usuario, contrasenia);

		

		

		//(ad instanceof AdministradorApp)
		if (ad instanceof AdministradorEmpresa){
			System.out.println("empreasa");
			sesion.setMail(ad.getId());
			String nombreempresa =((AdministradorEmpresa) ad).getEmpresa().getNombre();
			sesion.setNombreEmpresa(nombreempresa);
			sesion.setTipoUsuario("Empresa");
			return "homeAdmin";
		}else if(ad instanceof AdministradorApp){
			sesion.setMail(ad.getId());
			System.out.println("app");
			sesion.setTipoUsuario("Aplicacion");
			return "homeAdminApp";
		}else
			return "Mal";
	}
	

	
	
}