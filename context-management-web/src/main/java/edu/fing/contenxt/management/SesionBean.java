package edu.fing.contenxt.management;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class SesionBean {

	private String tokenId;
	private String mail;

	public String getMail() {
		return this.mail;
	}

	public String getTokenId() {
		return this.tokenId;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

}
