package com.xmlController;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReadXMLFile {
	private static String UPLOAD_XML = "src\\main\\resources\\Employees.xml";

	@GetMapping("/")
	public String index() {
		return "upload";
	}

	@PostMapping("/uploadXML")
	public String singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

		try {

			File fXmlFile = new File(UPLOAD_XML);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();

			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("employee");

			System.out.println("----------------------------");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;


					String fName = eElement.getElementsByTagName("firstname").item(0).getTextContent();
					fName.matches("[a-zA-Z]"); // Alpha validation
					
					String lName = eElement.getElementsByTagName("lastname").item(0).getTextContent();
					lName.matches("[a-zA-Z]");
					
					short dob = eElement.getElementsByTagName("dob").item(0).getNodeType();
					Date date = new java.util.Date(dob * 1000L);
					SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd-yyyy");
					String formattedDate = sdf.format(date);
					System.out.println(formattedDate);


				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/uploadStatus";

	}
}