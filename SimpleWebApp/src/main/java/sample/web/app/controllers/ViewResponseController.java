package sample.web.app.controllers;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import sample.web.app.models.SimpleStuff;

@Controller
@RequestMapping(value = "/view", method = RequestMethod.GET)
public class ViewResponseController
{
	
	@RequestMapping("/simple")
	public String getStaticView()
	{
		
		return "pages/simple";
		
	}
	
	@RequestMapping("/model")
	public String getModelView(Model model)
	{
		
		model.addAttribute("firstAttr", "I'm String");
		
		SimpleStuff stuff = new SimpleStuff();
		
		stuff.setDateField(new Date());
		stuff.setIntField(666);
		stuff.setStringField("Sejten");
		
		model.addAttribute("object", stuff);
		
		return "pages/model";
		
	}
	
	@RequestMapping("/param/{urlVar}")
	public String getRESTView(Model model, @PathVariable String urlVar,
			@RequestParam(value = "param", required = false) String reqParam)
	{
		
		model.addAttribute("urlAttr", urlVar);
		model.addAttribute("reqParam", reqParam);
		
		return "pages/param";
		
	}
	
}
