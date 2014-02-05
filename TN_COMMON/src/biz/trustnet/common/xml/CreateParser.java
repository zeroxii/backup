/* 
 * Project Name : TN_COMMON
 * File Name	: CreateParser.java
 * Date			: 2007. 08. 16
 * History		: 2007. 8. 08
 * Version		: 2.0
 * Author		: {ginaida@ginaida.net} ¿”¡÷º∑
 * Comment      : Create SAX Parser
 */

package biz.trustnet.common.xml;

import java.io.File;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import biz.trustnet.common.log.Log;

public class CreateParser {

	private DefaultHandler handler;
	private SAXParser saxParser;


	public CreateParser(DefaultHandler handler) {
		this.handler = handler;
		create();
	}

	/**
	 * Create the SAX parser
	 */
	private void create() {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(true);
			factory.setValidating(true);
			saxParser = factory.newSAXParser();
		} catch (SAXException e) {
            Log.debug("log.root","SAXException ="+e.getMessage(),this);
        } catch (ParserConfigurationException e) {
        	Log.debug("log.root","ParserConfigurationException ="+e.getMessage(),this);
        }
	}

	public void parse(File file)throws Exception{
		try{
			saxParser.parse(file,handler);
		}catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	
	public void parse(String uri)throws Exception{
		try{
			saxParser.parse(uri,handler);
		}catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	public void parse(InputStream stream)throws Exception{
		try{
			 saxParser.parse(stream,handler);
		}catch (Exception e) {
			throw new Exception(e);
		}
	}
	
}