package edu.utd.sa.kwicwebapp.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import edu.utd.sa.kwic.AlphabeticShift;
import edu.utd.sa.kwic.CircularShiftInterface;
import edu.utd.sa.kwic.Master;
import edu.utd.sa.kwic.NoiseEliminator;
import edu.utd.sa.kwicwebapp.common.LoginBean;
import edu.utd.sa.kwicwebapp.service.InputBean;
import edu.utd.sa.kwicwebapp.service.SearchServiceImpl;

@Controller
public class HomeController {

	@Autowired
	private SearchServiceImpl searchService = null;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView home(ModelMap model, HttpServletRequest request, @RequestParam String indexURL) {
		String message = "";
		if (request.getSession().getAttribute("user") != null) {
			if (indexURL != null && indexURL.trim().length() != 0) {

				String dataRead = null;
				InputStream in = null;
				try {
					in = new URL(indexURL).openStream();

					dataRead = IOUtils.toString(in);
					String[] lines = dataRead.split("\n");
					Master master = new Master();
					for (String line : lines) {
						boolean delete = false;

						if (line.contains("(EXPIRED)")) {
							delete = true;
							line = line.replaceAll("\\(EXPIRED\\)", "");
						}
						String[] inputs = line.split("=");
						String[] url = inputs[0].split(":");
						// validation
						String[] arr = { inputs[1] };
						List<CircularShiftInterface> circularShifts = master.getCircularShifts(arr);

						AlphabeticShift as = new AlphabeticShift();
						List<String> circularShiftsStr = new LinkedList<String>();
						for (CircularShiftInterface circularShiftInterface : circularShifts) {
							circularShiftsStr.addAll(circularShiftInterface.getCircularShifts());
						}
						NoiseEliminator nse = new NoiseEliminator();
						as.alpha(nse.getNoiseLessShifts(circularShiftsStr));
						if (url[0].trim().length() > 0 && url[0].trim().replaceFirst("\\.","").replaceFirst("\\.","").contains(".")) {
							searchService.populateIndex(Master.output(as), inputs[1], url[0], Integer.parseInt(url[1]),
									delete);
						}
					}
					request.setAttribute("message", "Kwic Index has been generated and stored for given url");
					request.getSession().setAttribute("indexPresent", "true");
 

				} catch (IOException e) {
					message = "Error occured while downloading data, please retry later.";
					e.printStackTrace();
				} finally {
					IOUtils.closeQuietly(in);
				}
				request.setAttribute("message", message);
			}
		}
		return new ModelAndView("home");
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ModelAndView search(ModelMap model, HttpServletRequest request, @RequestParam String prefix) {

		System.out.println(":Request received"+prefix);
		Set<InputBean> results =searchService.select(prefix);
		if(results.size()>0)
		{
			request.setAttribute("searchResults", results);	
		}
		else
		{
			request.setAttribute("message", "* No Search Results found");
		}
			
		
		return new ModelAndView("home");
	}

	@RequestMapping(value = "/autocomplete", method = RequestMethod.GET, headers = "Accept=*/*")
	public @ResponseBody List<String> autocomplete(ModelMap model, HttpServletRequest request,
			@RequestParam String term) {

		return searchService.autocomplete(term);
	}

	@RequestMapping(value = "/deleteIndex", method = RequestMethod.GET)
	public ModelAndView deleteIndex(ModelMap model, HttpServletRequest request) {

		request.getSession().removeAttribute("indexPresent");
		searchService.deleteAll();
		request.setAttribute("message", "Kwic Index was removed from the system");
		request.removeAttribute("searchResults");
		return new ModelAndView("home");
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView home(ModelMap model, HttpServletRequest request, @ModelAttribute LoginBean loginBean) {
		return new ModelAndView("home");

	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(ModelMap model, HttpServletRequest request, @ModelAttribute LoginBean loginBean) {
		System.out.println("loginBean= " + loginBean);

		if (loginBean.getUserName().equals("kwicuser") && loginBean.getPassword().equalsIgnoreCase("utd123")) {
			request.getSession().setAttribute("user", loginBean);
			model.addAttribute("userName", loginBean.getUserName());

			if (searchService.select("").size() != 0) {
				request.getSession().setAttribute("indexPresent", "true");

			}
			return new ModelAndView("home");

		} else {
			model.addAttribute("message", "Please check credentials entered.");
			return new ModelAndView("home");

		}

	}

	/**
	 * This method logs user out
	 * 
	 * @param request
	 * @return
	 */

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logUserOut(HttpServletRequest request) {
		request.getSession().invalidate();
		System.out.println("loggingout");
		return "redirect:/";
	}

}
