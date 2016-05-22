package cmpe275.order.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

import com.google.gson.JsonObject;

import cmpe275.order.model.MenuItem;
import cmpe275.order.model.OrderDetail;
import cmpe275.order.model.OrdersPlaced;
import cmpe275.order.model.Popular;
import cmpe275.order.model.ShoppingCart;
import cmpe275.order.service.DatabaseService;
import cmpe275.order.service.JdbcDatabaseService;
import cmpe275.order.service.OrderAlgo;

@Controller
public class OrderController {
	private DatabaseService databaseService;
	boolean sessionFlag;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getDefault() {
		return "additem";
	}
	
	@RequestMapping(value="/orders",method=RequestMethod.GET)
	public ModelAndView getMyOrders(HttpServletRequest request) {
		String email = (String) request.getSession().getAttribute("user");
		DatabaseService database = new DatabaseService();
		List<OrdersPlaced> list = database.getMyOrders(email);
		ModelAndView mav = new ModelAndView();
		mav.addObject("myOrders",list);
		mav.setViewName("myorders");
		return mav;
		
	}

	@RequestMapping(value = "/items/add", method = RequestMethod.GET)
	public String getItemPage() {
		return "additem";
	}

	@RequestMapping(value = "/items/add", method = RequestMethod.POST)
	public ModelAndView addItem(@RequestParam("category") String category, @RequestParam("name") String name,
			@RequestParam("picture") String picture, @RequestParam("unitPrice") float unitPrice,
			@RequestParam("calories") float calories, @RequestParam("prepTime") int prepTime)
					throws SerialException, SQLException {

		DatabaseService database = new DatabaseService();
		ModelAndView mav = new ModelAndView();
		
		MenuItem menu = new MenuItem();
		long menuCount=database.getMenuCount();
		if(menuCount==999)
		{
			mav.addObject("msg","Maximum items reached(999)");
			mav.setViewName("errorPage");
			return mav;
		}
		else 
			menu.setMenuId((int) (menuCount));
		
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
		database.addItem(menu);

		
		mav.setViewName("additem");
		return mav;
	}

	@RequestMapping(value = "/items/delete/{id}", method = RequestMethod.POST)
	public String deleteItem(@PathVariable("id") int id) {
		DatabaseService ds = new DatabaseService();
		ds.deleteItem(id);
		return "redirect:/items/viewall";
	}

	@RequestMapping(value = "/items/showOrders", method = RequestMethod.GET)
	public String getCustomerOrderDetails() {
		return "OrdersFilter";
	}

	@RequestMapping(value = "/items/getCustomerDetails", method = RequestMethod.GET)
	public ModelAndView getCustomerOrderDetails(@RequestParam("startDate") Date startDate,
			@RequestParam("endDate") Date endDate, @RequestParam("sortBy") String sort,
			HttpServletRequest request) {

		DatabaseService ds = new DatabaseService();

		int sortBy = 0;
		if (sort.equals("orderTime")) {
			sortBy = 1;
		} else {
			sortBy = 2;
		}
		@SuppressWarnings("unchecked")
		List<OrdersPlaced> resultSet = ds.getCustomerOrderDetails(startDate, endDate,sortBy);

		ModelAndView mav = new ModelAndView();
		mav.addObject("ordersList", resultSet);
		// mav.addObject("startDate", attributeValue)
		mav.addObject("startDate",startDate);
		mav.addObject("endDate",endDate);
		mav.setViewName("orders");
		return mav;

	}
	

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/items/getCartdetails", method = RequestMethod.GET)

	public ModelAndView getCartDetails(HttpServletRequest request) {

		HttpSession session = request.getSession();
		HashMap<String, Integer> cart = new HashMap<>();
		ModelAndView mav = new ModelAndView();
		if (session.getAttribute("cart") != null) {
			cart = (HashMap<String, Integer>) session.getAttribute("cart");
			List<ShoppingCart> arr = new ArrayList<ShoppingCart>();
			for (String key : cart.keySet()) {
				ShoppingCart s = new ShoppingCart();
				s.setMenuName(key);
				s.setQuantity(cart.get(key));
				arr.add(s);

			}

			mav.addObject("cart", arr);
			
		}
		mav.setViewName("shoppingcart");
		return mav;
	}

	@RequestMapping(value = "/items/orderNow", method = RequestMethod.POST)

	public @ResponseBody JsonObject orderNow(@RequestParam(value = "dop", required = false) Date dop,
			@RequestParam(value = "pickupTime", required = false) Time pickupTime, HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		System.out.println(dop + " " + pickupTime);

		// Date dop = new Date(2016 - 1900, 4, 11);

		int chefId = 1;
		boolean orderCreated = false;

		// Time pickupTime2=new Time(16,0,0);
		// int prepTime = 25;

		boolean manualInput = false;
		////////
		OrderAlgo obj = new OrderAlgo(session);
		JsonObject jObj = new JsonObject();

		if ((int) session.getAttribute("totalPrepTime") > 60) {
			jObj.addProperty("success", "cannot place");
			jObj.addProperty("msg", "Order not possible, please revise the Items/Quantities");
			return jObj;

		}

		if (dop != null) {
			// 6AM and 9PM check
			if (pickupTime.getHours() < 6 || (pickupTime.getHours() == 21 && pickupTime.getMinutes() > 0)
					|| (pickupTime.getHours() > 21 && pickupTime.getMinutes() >= 0)) {
				jObj.addProperty("success", "cannot place");
				jObj.addProperty("msg", "Order should be placed between 6AM and 9PM");
				return jObj;

			}

			boolean result = obj.userProvidedTimeSlot(dop, (int) session.getAttribute("totalPrepTime"), pickupTime);
			if (result) {
				jObj.addProperty("success", "manual order placed");
				jObj.addProperty("msg", "order created");
				session.removeAttribute("cart");
				session.setAttribute("totalPrepTime", 0);
				session.removeAttribute("price");
			}

			else {
				
				Calendar calendar = Calendar.getInstance();
				Time result2 = obj.earliestAvailableTimeSlot(new Date(calendar.getTime().getYear(),
						calendar.getTime().getMonth(), calendar.getTime().getDate()),
						(int) session.getAttribute("totalPrepTime"));
				if (result2 != null) {
					jObj.addProperty("success", "cannot place");
					jObj.addProperty("msg", "Order not possible at given time, earliest possible time is " + result2
							+ ", please revise the order");

				} else {
					jObj.addProperty("success", "cannot place");
					jObj.addProperty("msg", "Order not possible, please revise the Items/Quantities/Pickup time/Date");
				}

			}

		} else {
			Calendar calendar = Calendar.getInstance();
			//String email = (String) session.getAttribute("user");
			Time result = obj.earliestAvailableTimeSlot(
					new Date(calendar.getTime().getYear(), calendar.getTime().getMonth(), calendar.getTime().getDate()),
					(int) session.getAttribute("totalPrepTime"));
			if (result != null) {
				jObj.addProperty("success", "auto order placed");
				// System.out.println("total prep time
				// "+(int)session.getAttribute("totalPrepTime"));
				System.out
						.println("Order created " + obj.userProvidedTimeSlot(
								new Date(calendar.getTime().getYear(), calendar.getTime().getMonth(),
										calendar.getTime().getDate()),
								(int) session.getAttribute("totalPrepTime"), result));
				session.removeAttribute("cart");
				session.removeAttribute("totalPrepTime");
				session.removeAttribute("price");
				jObj.addProperty("msg", "Order created ");// +obj.userProvidedTimeSlot(dop,
															// (int)session.getAttribute("totalPrepTime"),
															// result));

			} else {
				jObj.addProperty("success", "cannot place");
				jObj.addProperty("msg", "Order not possible, please revise the Items/Quantities/Pickup time/Date");
			}

		}

		System.out.println(jObj);
		return jObj;

	}

	@RequestMapping(value = "/items/shoppingCart", method = RequestMethod.POST)

	public String addToShoppingCart(@RequestParam("menuid") int menuid, @RequestParam("menuName") String name,
			@RequestParam("quantity") int quantity, @RequestParam("prepTime") int prepTime,
			@RequestParam("price") float price,
			HttpServletRequest request) {
		// Get HTTPSession from the user

		HttpSession session = request.getSession(false);
		if (session.getAttribute("totalPrepTime") != null) {
			session.setAttribute("totalPrepTime", (int) session.getAttribute("totalPrepTime") + (prepTime * quantity));

		} else
			session.setAttribute("totalPrepTime", prepTime * quantity);

		System.out.println("totalPrepTime " + session.getAttribute("totalPrepTime"));

		System.out.println("price"+session.getAttribute("price"));
		
		if (session.getAttribute("price") == null) {
			session.setAttribute("price",price * quantity);
		} else {
			session.setAttribute("price",(float) session.getAttribute("price") + (price * quantity));
		}
		System.out.println("price"+session.getAttribute("price"));
		
		HashMap cart;
		if (session.getAttribute("cart") != null)
			cart = (HashMap) session.getAttribute("cart");

		else
			cart = new HashMap();

		if (!cart.containsKey(name))
			cart.put(name, quantity);
		else
			cart.put(name, (int) (cart.get(name)) + quantity);
		session.setAttribute("cart", cart);

		Set set = cart.entrySet();
		// Get an iterator
		Iterator i = set.iterator();
		// Display elements
		while (i.hasNext()) {
			Map.Entry me = (Map.Entry) i.next();
			System.out.print(me.getKey() + ": ");
			System.out.println(me.getValue());
		}
		return "redirect:/items/viewall";
	}

	@RequestMapping(value = "/items/enable/{id}", method = RequestMethod.POST)
	public String enableItem(@PathVariable("id") int id) {
		DatabaseService ds = new DatabaseService();
		ds.enableItem(id);
		return "redirect:/items/viewall";
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
	public String updateMenuItem(@PathVariable("itemId") int itemId, @RequestParam("category") String category,
			@RequestParam("name") String name, @RequestParam("picture") String picture,
			@RequestParam("unitPrice") float unitPrice, @RequestParam("calories") float calories,
			@RequestParam("prepTime") int prepTime) throws SerialException, SQLException {
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
		// database.updatePicture(itemId, blob);
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

	@RequestMapping(value = "/orders/deleteall", method = RequestMethod.DELETE)
	public String getAllOrders() {
		DatabaseService database = new DatabaseService();
		database.deleteOrders();
		return "redirect:/items/viewall";
	}
	
	@RequestMapping(value="/orders/{orderId}",method=RequestMethod.DELETE)
	public String deleteOrder(@PathVariable("orderId") int orderId) {
		DatabaseService database = new DatabaseService();
		boolean isDeleted = database.deleteOrder(orderId);
		return "redirect:/orders";
	}
	
	@RequestMapping(value="/orders/{orderId}",method=RequestMethod.GET)
	public ModelAndView getOrder(@PathVariable("orderId") int orderId) {
	
		ModelAndView mav = new ModelAndView();
		DatabaseService database = new DatabaseService();
		List<ShoppingCart> list = database.getOrder(orderId);
		mav.addObject("orderDetail",list);
		mav.setViewName("orderdetail");
		return mav;
	}
	
	@RequestMapping(value="/orders/popularorders",method=RequestMethod.GET)
	public ModelAndView getOrder(@RequestParam("startDate") Date startDate,
			@RequestParam("endDate") Date endDate) {
		ModelAndView mav = new ModelAndView();
		List<Popular> list = new DatabaseService().getPopular(startDate, endDate);
		mav.addObject("popular",list);
		System.out.println(list.toString());
		mav.setViewName("popularorders");
		return mav;
		
	}

	@RequestMapping(value="/orders/popular",method=RequestMethod.GET)
	public String returnPopularPage() {
		return "popular";
	}
	
	@RequestMapping(value="/items/cart/{itemName}",method=RequestMethod.GET)
	public String removeCartItem(@PathVariable("itemName") String itemName,HttpServletRequest request) {
		HttpSession session = request.getSession();
		HashMap hash = (HashMap) session.getAttribute("cart");
		float price = new DatabaseService().getPrice(itemName);
		session.setAttribute("price",(float) session.getAttribute("price") - (price *(int) hash.get(itemName)));
		if ((float) session.getAttribute("price") == 0) {
			session.removeAttribute("price");
		}
		
		hash.remove(itemName);
		session.setAttribute("cart",hash);
		return "redirect:/items/getCartdetails";
	}
	
	@RequestMapping(value="/items/cart",method=RequestMethod.POST)
	public String editCart(@RequestParam("menuName") String menuName,@RequestParam("quantity") int quantity,
						   HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		HashMap hash = (HashMap) session.getAttribute("cart");
		float price = new DatabaseService().getPrice(menuName);
		session.setAttribute("price",(float) session.getAttribute("price") - (price *(int) hash.get(menuName)));
		session.setAttribute("price",(float) session.getAttribute("price") + (price * quantity));
		
		hash.put(menuName, quantity);
		System.out.println(hash);
		session.setAttribute("cart",hash);
		
		return "redirect:/items/getCartdetails";
	}
	
	@RequestMapping(value="/items/cart/cancel",method=RequestMethod.POST)
	public String cancelOrder(HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		session.removeAttribute("cart");
		session.removeAttribute("price");
		session.removeAttribute("totalPrepTime");
		return "redirect:/items/getCartdetails";
	}
	
	public DatabaseService getDatabaseService() {
		return databaseService;
	}

	public void setDatabaseService(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}
}