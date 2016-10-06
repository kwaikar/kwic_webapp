package edu.utd.sa.kwicwebapp.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.utd.sa.kwic.AlphabeticShift;
import edu.utd.sa.kwic.CircularShiftInterface;
import edu.utd.sa.kwic.Master;
import edu.utd.sa.kwicwebapp.common.LoginBean;

@Controller
public class HomeController {

	final static Logger logger = Logger.getLogger(HomeController.class);

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView home(ModelMap model, HttpServletRequest request,
			@RequestParam String indexURL) {
		String message="";
		if (request.getSession().getAttribute("user") != null) {
			if (indexURL != null && indexURL.trim().length() != 0) {

				String dataRead = null;
				InputStream in = null;
				try {
					in = new URL(indexURL).openStream();

					dataRead = IOUtils.toString(in);
					String[] lines = dataRead.split("\n");
					Master master= new Master();
					List<CircularShiftInterface> circularShifts = master.getCircularShifts(lines);

					AlphabeticShift as = new AlphabeticShift();
					List<String> csIndex=as.getUnsortedShifts(circularShifts);
					request.getSession().setAttribute("csIndex", csIndex);
					as.alpha(circularShifts);

					request.getSession().setAttribute("asIndex", Master.output(as));
					
					
				} catch (IOException e) {
					message ="Error occured while downloading data, please retry later.";
					e.printStackTrace();
				} finally {
					IOUtils.closeQuietly(in);
				}
				request.setAttribute("message", message);
			}
		} 
		return new ModelAndView("home");
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView home(ModelMap model, HttpServletRequest request, @ModelAttribute LoginBean loginBean) {
		return new ModelAndView("home");
		
	}
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(ModelMap model, HttpServletRequest request, @ModelAttribute LoginBean loginBean) {
		logger.debug("loginBean= " + loginBean);
		 
		if(loginBean.getUserName().equals("kwicuser") && loginBean.getPassword().equalsIgnoreCase("utd123"))
		{
			request.getSession().setAttribute("user" ,loginBean);
			model.addAttribute("userName", loginBean.getUserName());
			return new ModelAndView("home");
			
		}
		else
		{
			model.addAttribute("message",
					"Please check credentials entered.");
			return new ModelAndView("home");
			
		}
 
	}

 
	/**
	 * This method logs user out
	 * @param request
	 * @return
	 */

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logUserOut(HttpServletRequest request) {
		request.getSession().invalidate();
		logger.debug("loggingout");
		return "redirect:/";
	}

}
