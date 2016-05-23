package cmpe275.order.model;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;


@Entity
@Table(name="OrderDetail")
public class OrderDetail {

	@EmbeddedId
	private OrderDetailId oId;
	
	@MapsId(value="menuId")
	@ManyToOne
	@JoinColumn(name="menuId", referencedColumnName="menuId")
	private MenuItem menuItem;
	
	@MapsId(value="orderId")
	@ManyToOne
	@JoinColumn(name="orderId", referencedColumnName="orderId")
	private OrdersPlaced foodOrder;
	
	private int quantity;

	public MenuItem getMenuItem() {
		return menuItem;
	}

	public void setMenuItem(MenuItem menuItem) {
		this.menuItem = menuItem;
	}

	public OrdersPlaced getFoodOrder() {
		return foodOrder;
	}

	public void setFoodOrder(OrdersPlaced foodOrder) {
		this.foodOrder = foodOrder;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}
