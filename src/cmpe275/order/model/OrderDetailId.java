package cmpe275.order.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@SuppressWarnings("serial")
@Embeddable
public class OrderDetailId implements Serializable{

	@Column(name="menuId")
	private int menuId;
	
	@Column(name="orderId")
	private int orderId;
	
	public int hashCode() {
		return super.hashCode();
	}
	
	
	public boolean equals(Object obj) {
		if ((!(obj instanceof OrderDetailId)) || obj == null) {
			return false;
		}
		
		if (obj == this) {
			return true;
		}
		
		OrderDetailId oId = (OrderDetailId) obj;
		return (menuId == oId.getMenuId()) && (orderId == oId.getOrderId());
		
	}


	public int getMenuId() {
		return menuId;
	}


	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}


	public int getOrderId() {
		return orderId;
	}


	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

}
