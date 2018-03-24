package com.menyi.aio.web.favourstyle;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MessageBean {
	/**
	 * ��Ϣ����
	 */
	private int code;
	/**
	 * ��Ϣ����
	 */
	private String message;
	/**
	 * ����
	 */
	private String description;
	
	
	public MessageBean() {
		super();
	}
	
	public MessageBean(int code, String message, String description) {
		super();
		this.code = code;
		this.message = message;
		this.description = description;
	}

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public Element toXml(Document doc)
	{
			Element root = doc.createElement("MessageBean");
			Element tmp = doc.createElement("code");
			tmp.appendChild(doc.createTextNode(String.valueOf(code)));
			root.appendChild(tmp);
			tmp = doc.createElement("message");
			tmp.appendChild(doc.createTextNode(String.valueOf(message)));
			root.appendChild(tmp);
			tmp = doc.createElement("description");
			tmp.appendChild(doc.createTextNode(String.valueOf(description)));
			root.appendChild(tmp);
			return root;
	}
	
	public Element toXml()
	{
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			return toXml(doc);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
}
