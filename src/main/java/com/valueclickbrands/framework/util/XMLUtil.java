package com.valueclickbrands.framework.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XMLUtil {
	protected static Log log = LogFactory.getLog(XMLUtil.class);
	private Document document;
	private Element root;
	
	public XMLUtil() {
		document = DocumentHelper.createDocument();
	}

	public XMLUtil(String xmlContent) throws DocumentException {
		readXML(xmlContent.trim());
	}
	
	public XMLUtil(File xmlFile) throws DocumentException {
		readXML(xmlFile);
	}
	
	public XMLUtil(InputStream xmlIn) throws DocumentException {
		readXML(xmlIn);
	}
	
	public void readXML(File xmlFile) throws DocumentException {
		SAXReader reader = new SAXReader();
		try {
			document = reader.read(xmlFile);
			root = document.getRootElement();
		} catch (DocumentException e) {
			log.error(e.getMessage(), e);
			throw e;
		}
	}

	public void readXML(String xmlContent) throws DocumentException {
		SAXReader reader = new SAXReader();
		try {
			document = reader.read(new StringReader(xmlContent));
			root = document.getRootElement();
		} catch (DocumentException e) {
			log.error(e.getMessage(), e);
			throw e;
		}
	}
	
	public void readXML(InputStream xmlIn) throws DocumentException {
		SAXReader reader = new SAXReader();
		try {
			document = reader.read(xmlIn);
			root = document.getRootElement();
		} catch (DocumentException e) {
			log.error(e.getMessage(), e);
			throw e;
		}finally{
			try {
				xmlIn.close();
			} catch (IOException e) {
			}
		}
	}
	
	public Element getRoot(){
		return root;
	}
	
	public Element getElement(String elementName){
		return root.element(elementName);
	}
	
	public List getElementsByXPath(String xPath){
		return document.selectNodes(xPath);
	}
	
	public Element getElementByXPath(String xPath){
		List list = document.selectNodes(xPath);
		if(list==null || list.size()==0){
			return null;
		}
		return (Element)list.get(0);
	}
	
	public String getElementValue(String elementName,String attributeName){
		return getElementValue(root.element(elementName),attributeName);
	}
	
	public String getElementValue(Element element,String attributeName){
		return element.attributeValue(attributeName);
	}
	
	public void addAttribute(String element,String name,String value){
		addAttribute(root.element(element),name, value);
	}
	
	public void addAttribute(Element element,String name,String value){
		element.addAttribute(name, value);
	}

	public void createRootElement(String elementName) {
		root = document.addElement(elementName);
	}

	public Element addElement(String parentElement, String elementName) {
		return addElement(root.element(parentElement),elementName);
	}
	
	public Element addElement(Element element, String elementName) {
		return element.addElement(elementName);
	}
	
	public void addElementContent(Element element, String text) {
		element.setText(text);
	}
	
	public void deleteElement(String elementName){
		Element removeElement = root.element(elementName);
		deleteElement(removeElement);
	}
	
	public void deleteElement(Element element){
		Element parent = element.getParent();
		parent.remove(element);
	}
	
	public String toString(){
		return document.asXML();
	}
	
	public static void main(String[] args){
		try {
			XMLUtil xml = new XMLUtil(new File("C:\\Users\\npan\\workspace3\\Invcontent\\src\\main\\java\\com\\valueclickbrands\\invcontent\\test\\search.xml"));
			Element root = xml.getRoot();
			System.out.println(root.getDocument().asXML());
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
	}
}
