package cmpe275.user.controller;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cmpe275.order.service.DatabaseService;
import cmpe275.order.service.MailService;
import cmpe275.user.model.User;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@RequestMapping(value="/",method=RequestMethod.GET)
	public String getDefault() {
		return "registration";
	}
	
	@RequestMapping(value="/",method=RequestMethod.POST)
	public String addUser(@RequestParam("email") String email,
							@RequestParam("password") String password,
							@RequestParam("cpassword") String cpassword, ModelMap model) {
		System.out.println("IN USER REGISTRATION");
		if(password.equals(cpassword)) {
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
			return "registration";
		} else {
			model.addAttribute("message", "Password and confirm password are not matched!!!");
			return "registration";
		}
	}	
	
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public String loginPage() {
		return "login";
	}
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public String loginUser(@RequestParam("inputEmail") String email,
			@RequestParam("inputPassword") String password, ModelMap model) {
		DatabaseService databaseService = new DatabaseService();
		String dbPassword = databaseService.getPassword(email);
		if(password.equals(dbPassword)) {
			boolean isVerified = databaseService.isVerified(email);
			if(isVerified) {
				model.addAttribute("email", email);
				return "customer";
			} else {
				// complete verification process
				model.addAttribute("email", email);
				return "verification";
			}
		} else {
			// wrong password
			model.addAttribute("message", "Wrong email address or password.");
			return "login";
		}
	}
	
	@RequestMapping(value="/verification",method=RequestMethod.POST)
	public void verifyUser() {
		System.out.println("You reached verification process");
	}
}
