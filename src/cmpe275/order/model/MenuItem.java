package cmpe275.order.model;

import java.sql.Blob;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name="MenuItem")
public class MenuItem {

	@Id
	private int menuId;
	private String category;
	private String name;
	@Lob
	@Basic(fetch=FetchType.LAZY)
	@Column(name="picture", columnDefinition="BLOB NOT NULL")
	private Blob picture;
	private float unitPrice;
	private float calories;
	private int prepTime;
	private boolean isEnabled;
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Blob getPicture() {
		return picture;
	}
	public void setPicture(Blob picture) {
		this.picture = picture;
	}
	public float getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}
	public int getPrepTime() {
		return prepTime;
	}
	public void setPrepTime(int prepTime) {
		this.prepTime = prepTime;
	}
	public boolean isEnabled() {
		return isEnabled;
	}
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	public float getCalories() {
		return calories;
	}
	public void setCalories(float calories) {
		this.calories = calories;
	}
	
}
