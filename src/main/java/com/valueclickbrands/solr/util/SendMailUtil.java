package com.valueclickbrands.solr.util;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class SendMailUtil {
	private static Logger logger = Logger.getLogger (SendMailUtil.class.getName());
	private static Configuration file_cfg = new Configuration();
	
	public SendMailUtil() {
		setFileCfg();
	}
	private void setFileCfg() {
		try {
			Properties p = new Properties();
			p.load(SendMailUtil.class.getClassLoader().getResourceAsStream("freemarker.properties"));
			
			file_cfg.setDirectoryForTemplateLoading(new File(Configure.template_path));
			file_cfg.setSettings(p);
			
//			ClassTemplateLoader ctl = new ClassTemplateLoader(TemplateFileFreemarkerUtil.class, "/template");
//			TemplateLoader tl = file_cfg.getTemplateLoader();
//			TemplateLoader[] loaders = new TemplateLoader[] { tl, ctl };
//			MultiTemplateLoader mtl = new MultiTemplateLoader(loaders);
//			file_cfg.setTemplateLoader(mtl);
		} catch (IOException e) {
			logger.error(e);
		} catch (TemplateException e) {
			logger.error(e);
		}
		file_cfg.setObjectWrapper(new DefaultObjectWrapper());
	}
	
	public void sendBatchReport(String mail_content_hi, String subject, String mail_content, String mail_content_signature, String attachments, String contentType) {
		HashMap<String, String> rootMap = new HashMap<String,String>();
		rootMap.put("hi", mail_content_hi);
		rootMap.put("content", mail_content);
		rootMap.put("signature", mail_content_signature);
		try {
			String content = getContent(Configure.mail_template, rootMap);
			logger.info("get mail content ---->"+content.length());
			MailUtil mail = MailUtil.newInstance(Configure.mail_smtp_host, Configure.mail_user, Configure.mail_password, Configure.mail_auth);
			mail.send(Configure.mail_from, Configure.mail_to, Configure.mail_cc,null, subject, content, attachments, contentType);
			logger.info("send mail Success");
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		} catch (TemplateException e) {
			logger.error(e.getMessage(),e);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}
	
	public void sendBatchReport(String subject, String mail_content) {
		sendBatchReport("All", subject, mail_content, "", null, "text/html;charset=utf-8");
	}
	
	public void sendBatchReport(String subject, String mail_content, String attachments) {
		if (StringUtils.isNotEmpty(attachments)) {
			sendBatchReport("All", subject, mail_content, "", attachments, null);
		} else {
			sendBatchReport(subject, mail_content);
		}
		
	}
	
	public String getContent(String templateName,Object data) throws IOException, TemplateException{
		StringWriter w = new StringWriter();
		Template temp = file_cfg.getTemplate(templateName);
		temp.process(data, w);
		w.flush();
		return w.toString();
	}
	
	public static void main(String args[]) {
		Configure.init();
		SendMailUtil s = new SendMailUtil();
		HashMap<String, String> rootMap = new HashMap<String,String>();
		rootMap.put("hi", "All");
		rootMap.put("content", "Holle world!");
		rootMap.put("signature", "Jack He");
		try {
			String content = s.getContent(Configure.mail_template, rootMap);
			System.out.println(content);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		s.sendBatchReport("test mial", "mail content", "D:\\test1.txt");
	}
}
