package com.javasampleapproach.multipartfile.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.javasampleapproach.multipartfile.filestorage.FileStorage;
import com.javasampleapproach.multipartfile.model.Order;

@Controller
public class UploadFileController {

	@Autowired
	FileStorage fileStorage;

	@GetMapping("/")
	public String index() {
		return "multipartfile/uploadform.html";
	}

	@PostMapping("/")
	public String uploadMultipartFile(@RequestParam("uploadfile") MultipartFile multipartfile, Model model) {
		if (multipartfile.isEmpty()) {
			model.addAttribute("message",
					"Fail file is empty! -> uploaded filename: " + multipartfile.getOriginalFilename());
		} else if (multipartfile.getSize() > 50000)
			model.addAttribute("message", "Fail file is tooLarge! -> uploaded filename: "
					+ multipartfile.getOriginalFilename() + " Size " + multipartfile.getSize());
		else {
			try {

				File file = convert(multipartfile);
				String fileExtention = getFileExtension(multipartfile.getOriginalFilename());
				if (fileExtention.equalsIgnoreCase("xlsx") || fileExtention.equalsIgnoreCase("xls")) {
					exportExcelfile(file);
				}

				else if (fileExtention.equalsIgnoreCase("csv")) {
					System.out.println("csv file");
					parseCsv(file).forEach(System.out::println);;
				}

				fileStorage.store(multipartfile);
				model.addAttribute("message",
						"File uploaded successfully! -> filename = " + multipartfile.getOriginalFilename());
			} catch (Exception e) {
				model.addAttribute("message", "Fail! -> uploaded filename: " + multipartfile.getOriginalFilename());
			}
		}
		return "multipartfile/uploadform.html";
	}

	public void exportExcelfile(File file) throws IOException, InvalidFormatException {
		Workbook workbook = WorkbookFactory.create(file);

		List<Order> orders = new ArrayList<Order>();
		for (Sheet sheet : workbook) {
			for (Row row : sheet) {
				if (row.getRowNum() != 0)
					orders.add(mapOrder(row));
			}
		}

		orders.forEach(System.out::println);
		parseCsv(file);
		workbook.close();
	}

	public static File convert(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	public static String getFileExtension(String fileName) {
		if (fileName == null || !fileName.contains("."))
			return "InvalidFileName";
		int dotIndex = fileName.lastIndexOf('.');
		return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
	}

	public Order mapOrder(Row row) {

		DataFormatter dataFormatter = new DataFormatter();
		Order order = new Order();
		for (Cell cell : row) {
			String cellValue = dataFormatter.formatCellValue(cell);
			if (cell.getColumnIndex() == 0)
				order.setId(cellValue);

			else if (cell.getColumnIndex() == 1)
				order.setRegion(cellValue);

			else if (cell.getColumnIndex() == 2)
				order.setName(cellValue);

			else if (cell.getColumnIndex() == 3)
				order.setItem(cellValue);

			else if (cell.getColumnIndex() == 4)
				order.setUnits(cellValue);

			else if (cell.getColumnIndex() == 5)
				order.setUnits(cellValue);

			else if (cell.getColumnIndex() == 6)
				order.setTotal(cellValue);
		}
		return order;
	}

	public List<Order> parseCsv(File file) throws IOException {
		List<Order> orders = new ArrayList<Order>();
		try (Reader reader = Files.newBufferedReader(Paths.get(file.getPath()));
				CSVParser csvParser = new CSVParser(reader,
						CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
			for (CSVRecord csvRecord : csvParser) {
				// Accessing values by Header names

				Order order = new Order();
				order.setId(csvRecord.get("Id"));
				order.setRegion(csvRecord.get("Region"));
				order.setName(csvRecord.get("name"));
				order.setItem(csvRecord.get("Item"));
				order.setUnits(csvRecord.get("Units"));
				order.setUnits(csvRecord.get("Unit Cost"));
				order.setTotal(csvRecord.get("Total"));
				orders.add(order);
			}
		}
		return orders;
	}
}
