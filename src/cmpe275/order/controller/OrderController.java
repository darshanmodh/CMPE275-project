package cmpe275.order.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Blob;

import cmpe275.order.model.MenuItem;
import cmpe275.order.service.DatabaseService;

@Controller
public class OrderController {

	private DatabaseService databaseService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getDefault() {
		return "additem";
	}

	@RequestMapping(value = "/items/add", method = RequestMethod.GET)
	public String getItemPage() {
		return "additem";
	}

	@RequestMapping(value = "/items/add", method = RequestMethod.POST)
	public String addItem(@RequestParam("category") String category, @RequestParam("name") String name,
			@RequestParam("picture") String picture, @RequestParam("unitPrice") float unitPrice,
			@RequestParam("calories") float calories, @RequestParam("prepTime") int prepTime)
			throws SerialException, SQLException {
System.out.println("PIC ADD = " + picture);
		MenuItem menu = new MenuItem();
		menu.setCalories(calories);
		menu.setCategory(category);
		menu.setEnabled(true);
		menu.setName(name);
		menu.setPrepTime(prepTime);
		menu.setUnitPrice(unitPrice);

		if (picture != null) {
			byte[] decodedBytes = Base64.decodeBase64(picture);
			Blob blob = new SerialBlob(decodedBytes);
			System.out.println("blob add = " + blob);
			menu.setPicture(blob);
		}
		DatabaseService database = new DatabaseService();
		database.addItem(menu);

		return "additem";
	}

	@RequestMapping(value = "/items/delete/{id}", method = RequestMethod.POST)
	public String deleteItem(@PathVariable("id") int id) {
		DatabaseService ds = new DatabaseService();
		ds.deleteItem(id);
		return "redirect:/items/viewall";
	}

	@RequestMapping(value = "/items/enable/{id}", method = RequestMethod.POST)
	public String enableItem(@PathVariable("id") int id) {
		DatabaseService ds = new DatabaseService();
		ds.enableItem(id);
		return "redirect:/items/viewdisabled";
	}

	@RequestMapping(value = "/items/viewall", method = RequestMethod.GET)
	public ModelAndView viewItems() {
		ModelAndView mav = new ModelAndView();
		DatabaseService database = new DatabaseService();
		List<MenuItem> menuItems = database.viewAllItems();
		mav.addObject("list", menuItems);
		mav.addObject("isDisabled", 0);
		mav.setViewName("viewitem");
		return mav;

	}

	@RequestMapping(value = "/items/viewdisabled", method = RequestMethod.GET)
	public ModelAndView viewDisabledItems() {
		ModelAndView mav = new ModelAndView();
		DatabaseService database = new DatabaseService();
		List<MenuItem> menuItems = database.viewDisabledItems();
		mav.addObject("list", menuItems);
		mav.addObject("isDisabled", 1);
		mav.setViewName("viewitem");
		return mav;
	}

	@RequestMapping(value = "/items/{itemId}", method = RequestMethod.POST)
	public ModelAndView viewItem(@PathVariable("itemId") int menuId) throws SQLException {
		ModelAndView mav = new ModelAndView();
		DatabaseService database = new DatabaseService();
		MenuItem menu = database.viewItem(menuId);
		byte[] image = database.getImage(menuId).getBytes(1, (int) database.getImage(menuId).length());
		menu.setPicture(new SerialBlob(image));
		mav.addObject("menuItem", menu);
		mav.setViewName("subView");
		return mav;
	}

	@RequestMapping(value = "/items/{itemId}/picture", method = RequestMethod.GET)
	public void getImage(@PathVariable("itemId") int itemId, HttpServletResponse response, HttpServletRequest request)
			throws SQLException, IOException {
		DatabaseService database = new DatabaseService();
		byte[] image = database.getImage(itemId).getBytes(1, (int) database.getImage(itemId).length());
		OutputStream outputStream = response.getOutputStream();
		outputStream.write(image);
	}
	
	@RequestMapping(value = "/items/update/{itemId}", method = RequestMethod.POST)
	public String updateMenuItem(@PathVariable("itemId") int itemId, @RequestParam("category") String category, @RequestParam("name") String name,
			@RequestParam("picture") String picture, @RequestParam("unitPrice") float unitPrice,
			@RequestParam("calories") float calories, @RequestParam("prepTime") int prepTime)
			throws SerialException, SQLException {
		MenuItem menu = new MenuItem();
		Blob blob = null;
		menu.setCalories(calories);
		menu.setCategory(category);
		menu.setName(name);
		menu.setPrepTime(prepTime);
		menu.setUnitPrice(unitPrice);
		menu.setEnabled(true);
		if (picture != null) {
			byte[] decodedBytes = Base64.decodeBase64(picture);
			blob = new SerialBlob(decodedBytes);
			menu.setPicture(blob);
		}
		DatabaseService database = new DatabaseService();
		database.updateMenuItem(itemId, menu);
		//database.updatePicture(itemId, blob);
		return "redirect:/items/viewall";
	}

	public DatabaseService getDatabaseService() {
		return databaseService;
	}

	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}
}