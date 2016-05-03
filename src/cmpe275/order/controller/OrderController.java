package cmpe275.order.controller;

import java.sql.SQLException;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cmpe275.order.model.MenuItem;
import cmpe275.order.service.DatabaseService;

@Controller
public class OrderController {

	private DatabaseService databaseService;
	
	@RequestMapping(value="/",method=RequestMethod.GET)
	public String getDefault() {
		return "additem";
	}
	
	@RequestMapping(value="/items/add", method=RequestMethod.POST)
	public void addItem(@RequestParam("category") String category,
						  @RequestParam("name") String name, 
						  @RequestParam("picture") String picture,
						  @RequestParam("unitPrice") float unitPrice,
						  @RequestParam("calories") float calories,
						  @RequestParam("prepTime") int prepTime) throws SerialException, SQLException {
		
		MenuItem menu = new MenuItem();
		menu.setCalories(calories);
		menu.setCategory(category);
		menu.setEnabled(true);
		menu.setName(name);
		menu.setPrepTime(prepTime);
		menu.setUnitPrice(unitPrice);
		
		if (picture != null) {
			byte[] decodedBytes = Base64.decodeBase64(picture);
			menu.setPicture(new SerialBlob(decodedBytes));
		}
		
	}
	@RequestMapping(value="/items/delete/{id}", method=RequestMethod.DELETE)
	public void deleteItem(@PathVariable("id") int id)
	{
		System.out.println("Deleting");
		DatabaseService ds=new DatabaseService();
		ds.deleteItem(id);
	}
	public DatabaseService getDatabaseService() {
		return databaseService;
	}

	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}
}