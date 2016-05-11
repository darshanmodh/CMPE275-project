package cmpe275.order.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.Blob;
import java.sql.Date;

import javax.servlet.http.HttpSession;

import cmpe275.order.model.MenuItem;
import cmpe275.order.model.ShoppingCart;
import cmpe275.order.service.DatabaseService;
import cmpe275.order.service.OrderAlgo;

@Controller
public class OrderController {
	private DatabaseService databaseService;
	boolean sessionFlag;
	@RequestMapping(value="/",method=RequestMethod.GET)

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

@RequestMapping(value="/items/getCustomerDetails", method=RequestMethod.GET)
	
	public @ResponseBody List<String[]> getCustomerOrderDetails(HttpServletRequest request)
	{
	    List details = new ArrayList();
	    DatabaseService ds = new DatabaseService();    
	    List<Object[]> resultSet = ds.getCustomerOrderDetails();
	    return details;
	}

@SuppressWarnings("unchecked")
@RequestMapping(value="/items/getCartdetails", method=RequestMethod.GET)
	
	public ModelAndView getCartDetails(HttpServletRequest request)
	{
		
		HttpSession session = request.getSession();
		HashMap<String,Integer> cart = new HashMap<>();
		cart = (HashMap<String, Integer>) session.getAttribute("cart");
		List<ShoppingCart> arr = new ArrayList<ShoppingCart>();
		for (String key:cart.keySet()) {
			ShoppingCart s = new ShoppingCart();
			s.setMenuName(key);
			s.setQuantity(cart.get(key));
			arr.add(s);
			
		}
		ModelAndView mav = new ModelAndView();
		mav.addObject("cart",arr);
		System.out.println(arr.get(0).getMenuName());
		mav.setViewName("shoppingcart");
		return mav;
	}
	
@RequestMapping(value="/items/orderNow", method=RequestMethod.POST)
	
	public String orderNow(HttpServletRequest request) 
	  {
	HttpSession session = request.getSession(false);

	Date dop = new Date(2016 - 1900, 4, 10);
	
	int chefId=1;
	boolean orderCreated=false;
	
	
	Time pickupTime2=new Time(16,0,0);
	//int prepTime = 25;
	
	boolean manualInput=false;
	////////
	OrderAlgo obj = new OrderAlgo();

	if(manualInput)
	{
		boolean result=obj.userProvidedTimeSlot(dop, (int)session.getAttribute("totalPrepTime"), pickupTime2);
		if(result)
			System.out.println("order created");
		else 
		{
			Time result2=obj.earliestAvailableTimeSlot(dop, (int)session.getAttribute("totalPrepTime"));
			if(result2!=null)
				System.out.println("Order not possible at given time, earliest possible time is "+result2+", please revise the order");
				else {
					System.out.println("Order not possible, please revise the Items/Quantities/Pickup time/Date");
				}

		}
		
	}
	else 
	{
		Time result=obj.earliestAvailableTimeSlot(dop, (int)session.getAttribute("totalPrepTime"));
		if(result!=null)
		{
			System.out.println("Order created "+obj.userProvidedTimeSlot(dop, (int)session.getAttribute("totalPrepTime"), result));
		}else {
			System.out.println("Order not possible, please revise the Items/Quantities/Pickup time/Date");
		}
		
	}
		
	
	return "redirect:/items/viewall";

	  }
	
	

	
@RequestMapping(value="/items/shoppingCart", method=RequestMethod.POST)
	
	public String addToShoppingCart(@RequestParam("menuid") int menuid,
			                        @RequestParam("menuName") String name, 
			                        @RequestParam("quantity") int quantity,
			                        @RequestParam("prepTime") int prepTime,
			                         HttpServletRequest request) 
	{
		// Get HTTPSession from the user 

		HttpSession session = request.getSession(false);
				if(session.getAttribute("totalPrepTime")!=null)
		{
			session.setAttribute("totalPrepTime", (int)session.getAttribute("totalPrepTime")+(prepTime*quantity));

			
		}
		else
			session.setAttribute("totalPrepTime", prepTime*quantity);

		//System.out.println("totalPrepTime "+session.getAttribute("totalPrepTime"));
		
		HashMap cart;
		if(session.getAttribute("cart")!=null)
			cart=(HashMap)session.getAttribute("cart");

		else	
		 cart=new HashMap();
		
			if(!cart.containsKey(name))
				cart.put(name,quantity);
			else
			cart.put(name,(int)(cart.get(name))+quantity);
			session.setAttribute("cart", cart);
			
			
			Set set = cart.entrySet();
		      // Get an iterator
		      Iterator i = set.iterator();
		      // Display elements
		      while(i.hasNext()) {
		         Map.Entry me = (Map.Entry)i.next();
		         System.out.print(me.getKey() + ": ");
		         System.out.println(me.getValue());
		      }
		return "redirect:/items/viewall";
		
	}
	
	@RequestMapping(value="/items/enable/{id}", method=RequestMethod.POST)
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
	
	@RequestMapping(value = "/items/category/{category}", method = RequestMethod.POST)
	public ModelAndView getItemByCategory(@PathVariable("category") String category) {
		ModelAndView mav = new ModelAndView();
		DatabaseService database = new DatabaseService();
		List<MenuItem> menuItems = database.getItemsByCategory(category);
		mav.addObject("list", menuItems);
		mav.setViewName("viewitem");
		return mav;
	}
	
	@RequestMapping(value="/orders/deleteall",method=RequestMethod.DELETE)
	public String getAllOrders() {
		DatabaseService database = new DatabaseService();
		database.deleteOrders();
		return "redirect:/items/viewall";
	}

	public DatabaseService getDatabaseService() {
		return databaseService;
	}

	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}
}