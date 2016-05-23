package cmpe275.order.model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="OrdersPlaced")
public class OrdersPlaced {

	
	@Id 
	@GeneratedValue
	private int orderId;
	private int chefId;
	private Date prepDate;
	private Date pickUpDate;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	private Time startTime;
	private Time endTime;
	private String email;
	private Time pickupTime;
	private Timestamp orderTime;
	private float totalPrice;
	private String status;
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getChefId() {
		return chefId;
	}
	public void setChefId(int chefId) {
		this.chefId = chefId;
	}
	public Date getPrepDate() {
		return prepDate;
	}
	public void setPrepDate(Date prepDate) {
		this.prepDate = prepDate;
	}
	public Time getStartTime() {
		return startTime;
	}
	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}
	public Time getEndTime() {
		return endTime;
	}
	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public float getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Time getPickupTime() {
		return pickupTime;
	}
	public void setPickupTime(Time pickupTime) {
		this.pickupTime = pickupTime;
	}
	public Timestamp getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Timestamp orderTime) {
		this.orderTime = orderTime;
	}
	public Date getPickUpDate() {
		return pickUpDate;
	}
	public void setPickUpDate(Date pickUpDate) {
		this.pickUpDate = pickUpDate;
	}
	
}
