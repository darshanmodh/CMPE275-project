package cmpe275.order.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

@SuppressWarnings("serial")
@Embeddable

public class RatingId implements Serializable{

	@Column(name="menuId",nullable=false,insertable=true,updatable=true)
	private int menuId;
	
	@Column(name="email",updatable=true)
	private String email;
	
	public int hashCode() {
		return super.hashCode();
	}
	
	
	public boolean equals(Object obj) {
		if ((!(obj instanceof RatingId)) || obj == null) {
			return false;
		}
		
		if (obj == this) {
			return true;
		}
		
		RatingId oId = (RatingId) obj;
		return (menuId == oId.getMenuId()) && (email == oId.getEmail());
		
	}


	public int getMenuId() {
		return menuId;
	}


	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}



}
