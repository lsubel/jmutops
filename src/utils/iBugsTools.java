package utils;

import java.io.File;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class iBugsTools {
	
	protected static final String PATH_TO_IBUGS_REPOSITORY = "C:\\Users\\sheak\\Desktop\\Bachelorarbeit\\repository.xml";

	public static String generateReport_iBugs(int index_iBugs){
		// generate StringBuffer where we store the output string
		StringBuffer buffer = new StringBuffer();
		buffer.append("\n");
		
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
						
		
			buffer.append("iBugs id:"+ "\n");
			buffer.append("\t" + index_iBugs+ "\n");
					
			for (int i = 0; i < nodelistProperties.getLength(); i++){
				NamedNodeMap attr = nodelistProperties.item(i).getAttributes();
				try {
					if(attr.getNamedItem("name").getNodeValue().equals("lines-added")){
						buffer.append("Number of added lines:" + "\n");
						buffer.append("\t" + attr.getNamedItem("value").getNodeValue()+ "\n");
					}
					if(attr.getNamedItem("name").getNodeValue().equals("lines-deleted")){
						buffer.append("Number of deleted lines:"+ "\n");
						buffer.append("\t" + attr.getNamedItem("value").getNodeValue()+ "\n");
					}
					if(attr.getNamedItem("name").getNodeValue().equals("lines-modified")){
						buffer.append("Number of modified lines:"+ "\n");
						buffer.append("\t" + attr.getNamedItem("value").getNodeValue()+ "\n");
					}
					if(attr.getNamedItem("name").getNodeValue().equals("lines-churned")){
						buffer.append("Number of churned lines:"+ "\n");
						buffer.append("\t" + attr.getNamedItem("value").getNodeValue()+ "\n");
					}
				} catch (Exception e) {
				}
			}
			buffer.append("Bugreport:"+ "\n");
			buffer.append("\t" + strBugreport+ "\n");


			buffer.append("Fingerprint:"+ "\n");
			buffer.append("\t" + strFingerprint+ "\n");
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return buffer.toString();
	}
	
	public static File getFileFromiBugsRepository(int id_bug, File pathToModule, String strFilename, File repository) {
		try {
			// load in repository.xml
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true); 
			DocumentBuilder builder = domFactory.newDocumentBuilder();
			Document doc = builder.parse(repository);
			
			// search the entry id_bug
			XPath xpathGeneral = XPathFactory.newInstance().newXPath();
			XPathExpression exprAllChangedFiles = xpathGeneral.compile("/bugrepository/bug[@id = " + id_bug + "]/fixedFiles/file");
			NodeList nodelistChangedFiles = (NodeList) exprAllChangedFiles.evaluate(doc, XPathConstants.NODESET);	
			
			//  now check for each subnode
			for(int i=0; i<nodelistChangedFiles.getLength(); i++) {
				// extract the corresponding file entry
				Node nodeFile = nodelistChangedFiles.item(i);
				NamedNodeMap attributeFile = nodeFile.getAttributes();
				
				// extract the filepath related to the entry
				String strExtractedFilepath = attributeFile.getNamedItem("name").getNodeValue();
				
				// extract the file name out of it
				String[] subentries = strExtractedFilepath.split("/");
				String strExtractedFilename = subentries[(subentries.length - 1)];
				
				if(strExtractedFilename.equals(strFilename)) {
					return new File(pathToModule, strExtractedFilepath.replace("/", File.separator));
				}
			}
			
			// return the detected file, otherwise null
			return null;
		} catch (Exception e) {
			return null;
		}
	}
}
