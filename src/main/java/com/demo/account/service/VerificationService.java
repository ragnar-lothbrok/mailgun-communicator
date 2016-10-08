package com.demo.account.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.demo.models.MailModel;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

@Service
public class VerificationService {

	final static Logger logger = LoggerFactory.getLogger(VerificationService.class);

	@Value("${mailgun.fromEmail.address}")
	private String fromEmail;

	@Value("${mailgun.subject}")
	private String subject;

	@Value("${mailgun.apikey}")
	private String apiKey;

	@Value("${mailgun.mailhost}")
	private String mailHost;

	@Value("${mailgun.mail.template.filepath}")
	private String mailTemplatePath;

	@Value("${mailgun.mail.receivers.filepath}")
	private String mailReceiverList;

	@Value("${mailgun.mail.received.filepath}")
	private String mailSentList;
	
	final static List<String> sentMails = new ArrayList<String>();

	private void init() {
		final MailModel mailModel = new MailModel();
		StringBuilder sb = new StringBuilder();
		BufferedReader bufferedReader = null;
		if (mailTemplatePath != null) {
			try {
				bufferedReader = new BufferedReader(new FileReader(mailTemplatePath));
				String line = bufferedReader.readLine();
				while (line != null) {
					sb.append(line);
					line = bufferedReader.readLine();
				}
				if (sb.toString().length() > 0) {
					mailModel.setMailTemplate(sb.toString());
				}
			} catch (Exception e) {
				logger.error("File not found : ", e.getMessage());
			} finally {
				if (bufferedReader != null) {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						logger.error("File not found : ", e.getMessage());
					}
				}
			}
		}

		if (mailReceiverList != null) {
			try {
				bufferedReader = new BufferedReader(new FileReader(mailReceiverList));
				String line = bufferedReader.readLine();
				while (line != null) {
					String split[] = line.split(",");
					if (split[0].trim().length() > 0)
						mailModel.getReceiverList().add(split[0].trim());
					line = bufferedReader.readLine();
				}
			} catch (Exception e) {
				logger.error("File not found : ", e.getMessage());
			} finally {
				if (bufferedReader != null) {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						logger.error("File not found : ", e.getMessage());
					}
				}
			}
		}

		System.out.println(mailModel.getReceiverList());
		if (mailModel.getReceiverList().size() > 0) {
			mailModel.getReceiverList().stream().forEach(new Consumer<String>() {
				@Override
				public void accept(String emailId) {
					if(!sentMails.contains(emailId)){
						sentMails.add(emailId); 
						sendMailByMailGun(emailId, mailModel.getMailTemplate());
					}
				}
			});
		}
	}

	public void sendMailByMailGun(String emailId, String mailTemplate) {
		try {
			Client client = Client.create();
			client.addFilter(new HTTPBasicAuthFilter("api", apiKey));
			WebResource webResource = client.resource(mailHost);

			MultivaluedMapImpl formData = new MultivaluedMapImpl();
			formData.add("from", fromEmail);
			formData.add("to", emailId);
			formData.add("subject", subject);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("url", mailTemplate);
			formData.add("html", mailTemplate);

			formData.add("o:tracking", true);
			formData.add("o:tracking-opens", true);
			formData.add("o:tracking-clicks", "htmlonly");

			logger.info("Send Mail details : " + emailId+" "+ webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData));
		} catch (Exception e) {
			logger.error("Exception occured : {}", "s");
		} finally {

		}
	}

	public void sendMailByMailGun() {
		init();
	}

}
