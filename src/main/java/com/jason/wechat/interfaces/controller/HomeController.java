package com.jason.wechat.interfaces.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jason.framework.web.support.ControllerSupport;

@Controller
public class HomeController extends ControllerSupport{

	@RequestMapping(value="/", method=RequestMethod.GET)
	public String index(){
		return "index";
	}
}

