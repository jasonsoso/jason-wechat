package com.jason.wechat.interfaces.controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jason.framework.web.support.ControllerSupport;

@Controller
public class HomeController extends ControllerSupport{

	@RequestMapping(value="/", method=RequestMethod.GET)
	public String index(Model model){
		model.addAttribute("now", new Date());
		return "index";
	}
	
}

