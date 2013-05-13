package utils;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class iBugsTools {
	
	protected static final String PATH_TO_IBUGS_REPOSITORY = "C:\\Users\\sheak\\Desktop\\Bachelorarbeit\\repository.xml";

	public static String generateReport_iBugs(int index_iBugs){
		// generate StringBuffer where we store the output string
		StringBuffer buffer = new StringBuffer();
		
		try {
			// load the document into DOM Document object	
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true); 
			DocumentBuilder builder = domFactory.newDocumentBuilder();
			Document doc = builder.parse(iBugsTools.PATH_TO_IBUGS_REPOSITORY);
						
			// print out general bugfix related informations
			// print out some informations related to this id
			XPath xpathGeneral = XPathFactory.newInstance().newXPath();
						
			XPathExpression exprFilecontentBugreport = xpathGeneral.compile("/bugrepository/bug[@id = " + index_iBugs + "]/bugreport/text()");
			String strBugreport = (String) exprFilecontentBugreport.evaluate(doc, XPathConstants.STRING);
						
			XPathExpression exprFilecontentFingerprint = xpathGeneral.compile("/bugrepository/bug[@id = " + index_iBugs + "]/fullfingerprint/text()");
			String strFingerprint = (String) exprFilecontentFingerprint.evaluate(doc, XPathConstants.STRING);

			XPathExpression exprFilecontentProperty = xpathGeneral.compile("/bugrepository/bug[@id = " + index_iBugs + "]/property");
			NodeList nodelistProperties = (NodeList) exprFilecontentProperty.evaluate(doc, XPathConstants.NODESET);
						
			buffer.append("iBugs id:");
			buffer.append("\t" + index_iBugs);
					
			for (int i = 0; i < nodelistProperties.getLength(); i++){
				NamedNodeMap attr = nodelistProperties.item(i).getAttributes();
				try {
					if(attr.getNamedItem("name").getNodeValue().equals("lines-added")){
						buffer.append("Number of added lines:");
						buffer.append("\t" + attr.getNamedItem("value").getNodeValue());
					}
					if(attr.getNamedItem("name").getNodeValue().equals("lines-deleted")){
						buffer.append("Number of deleted lines:");
						buffer.append("\t" + attr.getNamedItem("value").getNodeValue());
					}
					if(attr.getNamedItem("name").getNodeValue().equals("lines-modified")){
						buffer.append("Number of modified lines:");
						buffer.append("\t" + attr.getNamedItem("value").getNodeValue());
					}
					if(attr.getNamedItem("name").getNodeValue().equals("lines-churned")){
						buffer.append("Number of churned lines:");
						buffer.append("\t" + attr.getNamedItem("value").getNodeValue());
					}
				} catch (Exception e) {
				}
			}
			buffer.append("Bugreport:");
			buffer.append("\t" + strBugreport);


			buffer.append("Fingerprint:");
			buffer.append("\t" + strFingerprint);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return buffer.toString();
	}
}
