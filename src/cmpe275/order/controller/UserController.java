package cmpe275.order.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cmpe275.order.model.User;
import cmpe275.order.service.DatabaseService;
import cmpe275.order.service.MailService;

@Controller
public class UserController {

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public String getDefault(HttpServletRequest request) {
		try {
			HttpSession session = request.getSession(false);
			if (session.getAttribute("user") != null) {
				return "customer";
			}
		} catch (NullPointerException e) {
			System.out.println("NullPointerException - No session available");
		}
		return "registration";
	}

	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
	public String addUser(@RequestParam("email") String email, @RequestParam("password") String password,
			@RequestParam("cpassword") String cpassword, ModelMap model) {
		System.out.println("IN USER REGISTRATION");
		if (password.equals(cpassword)) {
			SecureRandom random = new SecureRandom();
			String verificationCode = new BigInteger(50, random).toString(32);
			System.out.println("Verification code: " + verificationCode);
			User user = new User();
			user.setEmail(email);
			user.setPassword(password);
			user.setRole('U');
			user.setVerificationCode(verificationCode);
			user.setVerified(false);
			DatabaseService databaseService = new DatabaseService();
			databaseService.addUser(user);
			MailService mailService = new MailService();
			mailService.sendMail(user.getEmail());
			model.addAttribute("message", "User added to DB");
			return "verification";
		} else {
			model.addAttribute("message", "Password and confirm password are not matched!!!");
			return "registration";
		}
	}

	@RequestMapping(value = "/user/login", method = RequestMethod.GET)
	public String loginPage(HttpServletRequest request) {
		try {
			HttpSession session = request.getSession(false);
			if (session.getAttribute("user") != null) {
				return "customer";
			}
		} catch (NullPointerException e) {
			System.out.println("NullPointerException - No session available");
		}
		return "registration";
	}

	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	public String loginUser(@RequestParam("inputEmail") String email, @RequestParam("inputPassword") String password,
			ModelMap model, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		DatabaseService databaseService = new DatabaseService();
		String dbPassword = databaseService.getPassword(email);
		if (password.equals(dbPassword)) {
			boolean isVerified = databaseService.isVerified(email);
			if (isVerified) {
				HttpSession session = request.getSession();
				session.setAttribute("user", email);
				session.setMaxInactiveInterval(30 * 60); // 30min
				Cookie userName = new Cookie("user", email);
				userName.setMaxAge(30 * 60);
				response.addCookie(userName);
				// check admin or customer
				char isAdmin = '\0';
				isAdmin = databaseService.isAdmin(email);
				if (isAdmin == 'A') {
					session.setAttribute("role", isAdmin);
					return "admin";
				} else if (isAdmin == 'U') {
					session.setAttribute("role", isAdmin);
					return "customer";
				} else {
					return "registration";
				}
			} else {
				// complete verification process
				model.addAttribute("email", email);
				return "verification";
			}
		} else {
			// wrong password
			model.addAttribute("message", "Wrong email address or password.");
			return "registration";
		}
	}

	@RequestMapping(value = "/user/verification", method = RequestMethod.GET)
	public String getVerify() {
		System.out.println("Get Request on Verification");
		return "verification";
	}

	@RequestMapping(value = "/user/verification", method = RequestMethod.POST)
	public String verifyUser(@RequestParam("inputEmail") String email,
			@RequestParam("inputVerificationCode") String verficationCode, ModelMap model) {
		System.out.println("You reached verification process");
		String dbVerificationCode = "";
		boolean isVerified = false;
		DatabaseService databaseService = new DatabaseService();
		dbVerificationCode = databaseService.getVerificationCode(email);
		if (verficationCode.equals(dbVerificationCode)) {
			// update isVerify in DB
			isVerified = databaseService.makeUserVerified(email);
			if (isVerified) {
				model.addAttribute("email", email);
				model.addAttribute("message", "Your verification is successfully completed.");
				return "customer";
			} else {
				// error while updating
				model.addAttribute("message", "Verification failed, please try again.");
				return "verification";
			}
		} else {
			// wrong verification code
			model.addAttribute("email", email);
			model.addAttribute("message", "Wrong verification code!!!");
			return "verification";
		}
	}

	@RequestMapping(value = "/user/logout", method = RequestMethod.GET)
	public String getLogoutUser(HttpServletRequest request, ModelMap model) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("user")) {
					System.out.println("USER COOKIE = " + cookie.getValue());
					cookie.setMaxAge(0);
					System.out.println("Cookie of user is deleted");
				}
			}
		}

		HttpSession session = request.getSession(false);
		System.out.println("User session = " + session.getAttribute("user"));
		if (session != null) {
			session.invalidate();
		}
		return "registration";
	}
}
