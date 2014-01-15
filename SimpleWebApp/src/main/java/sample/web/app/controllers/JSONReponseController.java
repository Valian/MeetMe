package sample.web.app.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import sample.web.app.components.SimpleComponent;
import sample.web.app.models.SimpleStuff;

/*

Jeszcze sa metody kontrollera put, delete itp ale ich nie bedziemy uzywac

*/
@Controller
@RequestMapping("/json")
public class JSONReponseController
{
	
	@Autowired
	private SimpleComponent simpleComponent;
	
	@RequestMapping(value= "/get", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody SimpleStuff getJSON()
	{
		
		SimpleStuff stuff = new SimpleStuff();
		
		stuff.setDateField(new Date());
		stuff.setIntField(123);
		stuff.setStringField("Stringus maximus");
		
		stuff.getListField().add("pierwszy element listy");
		stuff.getListField().add(simpleComponent.getString());
		stuff.getListField().add("trzeci element listy");
		
		return stuff;
		
	}
	
	/*
	
	Przykladowe request body (koniecznie json)
	
	{
		"dateField": "4245544453452",
		"intField": "666",
		"stringField": "lolo",
		"listField": ["kappa", "keppo" ]
	}
	*/
	@RequestMapping(value= "/post", method = RequestMethod.POST, consumes = "application/json")
	public String postJSON(Model model, @RequestBody SimpleStuff stuff)
	{
		
		model.addAttribute("stuff", stuff);
		
		return "pages/json";
		
	}
	
}
