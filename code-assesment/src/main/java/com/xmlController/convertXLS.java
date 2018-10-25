package com.xmlController;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

public class convertXLS {
	private static Workbook workbook;
	private static int rowNum;

	private final static int FIRST_NAME_COLUMN = 0;
	private final static int LAST_NAME_COLUMN = 1;
	private final static int DATE_OF_BIRTH = 2;

	public static void main(String[] args) throws Exception {
		getAndReadXml();
	}

	/**
	 *
	 * Downloads a XML file, reads the substance and product values and then writes
	 * them to rows on an excel file.
	 *
	 * @throws Exception
	 */
	private static void getAndReadXml() throws Exception {
		System.out.println("getAndReadXml");

		File xmlFile = File.createTempFile("employees", "tmp");
		//String xmlFileUrl = "src\\main\\resources\\Employees.xml";
		File fXmlFile = new File("src\\main\\resources\\Employees.xml");

		System.out.println("downloading file from " + fXmlFile + " ...");
		

		/*
		 * If you have the xml file locally, replace the above code by the following
		 * line: File xmlFile = new File("C:/Temp/Publication1.xml");
		 */

		initXls();

		Sheet sheet = workbook.getSheetAt(0);

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		NodeList nList = doc.getElementsByTagName("employee");
		for (int i = 0; i < nList.getLength(); i++) {
			System.out.println("Processing element " + (i + 1) + "/" + nList.getLength());
			Node node = nList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				String fName = element.getElementsByTagName("firstname").item(0).getTextContent();
				String lName = element.getElementsByTagName("lastname").item(0).getTextContent();
				String dob = element.getElementsByTagName("dob").item(0).getTextContent();

				Row row = sheet.createRow(rowNum++);
				Cell cell = row.createCell(FIRST_NAME_COLUMN);
				cell.setCellValue(fName);

				cell = row.createCell(LAST_NAME_COLUMN);
				cell.setCellValue(lName);

				cell = row.createCell(DATE_OF_BIRTH);
				cell.setCellValue(dob);
			}
		}

		FileOutputStream fileOut = new FileOutputStream("C:/Excel-Out.xlsx");
		workbook.write(fileOut);
		// workbook.close();
		fileOut.close();


		System.out.println("getAndReadXml finished, processed " + nList.getLength() + " employees!");
	}

	/**
	 * Initializes the POI workbook and writes the header row
	 */
	private static void initXls() {
		workbook = new XSSFWorkbook();

		CellStyle style = workbook.createCellStyle();
		Font boldFont = workbook.createFont();
		// boldFont.setBold(true);
		style.setFont(boldFont);
		style.setAlignment(CellStyle.ALIGN_CENTER);

		Sheet sheet = workbook.createSheet();
		rowNum = 0;
		Row row = sheet.createRow(rowNum++);
		Cell cell = row.createCell(FIRST_NAME_COLUMN);
		cell.setCellValue("employee firstname");
		cell.setCellStyle(style);

		cell = row.createCell(LAST_NAME_COLUMN);
		cell.setCellValue("employee lastname");
		cell.setCellStyle(style);

		cell = row.createCell(DATE_OF_BIRTH);
		cell.setCellValue("employee dob");
		cell.setCellStyle(style);

	}
}