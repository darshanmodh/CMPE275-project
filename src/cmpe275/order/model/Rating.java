package cmpe275.order.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.IdValidation;

@Entity
@Table(name="Rating")
public class Rating {

	@EmbeddedId
	private RatingId rating;
	
	@MapsId(value="menuId")
	@ManyToOne
	@JoinColumn(name="menuId", referencedColumnName="menuId")
	private MenuItem menuItem;
	
	@MapsId(value="email")
	@ManyToOne
	@JoinColumn(name="email",referencedColumnName="email")
	private User user;
	
	private int rate;
	private int total;

	public RatingId getRating() {
		return rating;
	}

	public void setRating(RatingId rating) {
		this.rating = rating;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public MenuItem getMenuItem() {
		return menuItem;
	}

	public void setMenuItem(MenuItem menuItem) {
		this.menuItem = menuItem;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}
	
}
