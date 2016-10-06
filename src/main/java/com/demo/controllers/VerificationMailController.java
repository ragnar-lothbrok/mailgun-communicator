package com.demo.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.account.service.VerificationService;

@Controller
public class VerificationMailController {

	final static Logger logger = LoggerFactory.getLogger(VerificationMailController.class);

	@Autowired
	private VerificationService verificationService;

	@RequestMapping(
		value = { "/send" },
		method = { RequestMethod.GET })
	@ResponseBody
	public Map<String, String> verify(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> responseMap = new HashMap<String, String>();
		Boolean result = false;
		try {
			verificationService.sendMailByMailGun();
		} catch (Exception e) {
			logger.error("Exception Occured while signUp : ", e);
			throw e;
		}
		responseMap.put("status", result ? "Mail Sent." : "Mail Sent.");
		return responseMap;
	}

}
