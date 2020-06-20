package com.cczu.librarymanagementserver.common;

import com.cczu.librarymanagementserver.entity.Book;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel 工具类
 */
public class POIUtils {

	public static ResponseEntity<byte[]> book2Excel(List<Book> list) {
		// 创建一个 Excel 文档
		HSSFWorkbook workbook = new HSSFWorkbook();
		//创建标题行的样式
		HSSFCellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		HSSFSheet sheet = workbook.createSheet("图书信息表");
		//设置列的宽度
		sheet.setColumnWidth(0, 10 * 256);
		sheet.setColumnWidth(1, 10 * 256);
		sheet.setColumnWidth(2, 10 * 256);
		sheet.setColumnWidth(3, 10 * 256);
		sheet.setColumnWidth(4, 10 * 256);
		sheet.setColumnWidth(5, 10 * 256);
		sheet.setColumnWidth(6, 10 * 256);
		sheet.setColumnWidth(7, 10 * 256);
		sheet.setColumnWidth(8, 10 * 256);
		//创建标题行
		HSSFRow r0 = sheet.createRow(0);
		HSSFCell c0 = r0.createCell(0);
		c0.setCellValue("图书卡号");
		c0.setCellStyle(headerStyle);
		HSSFCell c1 = r0.createCell(1);
		c1.setCellStyle(headerStyle);
		c1.setCellValue("图书名");
		HSSFCell c2 = r0.createCell(2);
		c2.setCellStyle(headerStyle);
		c2.setCellValue("作者");
		HSSFCell c3 = r0.createCell(3);
		c3.setCellStyle(headerStyle);
		c3.setCellValue("出版社");
		HSSFCell c4 = r0.createCell(4);
		c4.setCellStyle(headerStyle);
		c4.setCellValue("类别ID");
		HSSFCell c5 = r0.createCell(5);
		c5.setCellStyle(headerStyle);
		c5.setCellValue("价格");
		HSSFCell c6 = r0.createCell(6);
		c6.setCellStyle(headerStyle);
		c6.setCellValue("出版日期");
		HSSFCell c7 = r0.createCell(7);
		c7.setCellStyle(headerStyle);
		c7.setCellValue("状态");
		HSSFCell c8 = r0.createCell(8);
		c8.setCellStyle(headerStyle);
		c8.setCellValue("位置");

		for (int i = 0; i < list.size(); i++) {
			Book book = list.get(i);
			HSSFRow row = sheet.createRow(i + 1);
			row.createCell(0).setCellValue(book.getBookId());
			row.createCell(1).setCellValue(book.getBName());
			row.createCell(2).setCellValue(book.getWriter());
			row.createCell(3).setCellValue(book.getPress());
			row.createCell(4).setCellValue(book.getCId());
			row.createCell(5).setCellValue(book.getPrice());
			row.createCell(6).setCellValue(book.getIssue());
			row.createCell(7).setCellValue(book.getStatus());
			row.createCell(8).setCellValue(book.getPosition());
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.setContentDispositionFormData("attachment", new String("图书信息表.xls".getBytes("UTF-8"), "ISO-8859-1"));
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<byte[]>(out.toByteArray(), headers, HttpStatus.CREATED);
	}

	/**
	 * Excel 解析成图书数据集合
	 *
	 * @param file
	 * @return
	 */
	public static List<Book> excel2book(MultipartFile file) {
		List<Book> list = new ArrayList<>();
		Book book = null;
		try {
			//1. 创建一个 workbook 对象
			HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());

			//2. 获取 workbook 表单的数量
			int numberOfSheets = workbook.getNumberOfSheets();
			for (int i = 0; i < numberOfSheets; i++) {
				//3. 获取表单
				HSSFSheet sheet = workbook.getSheetAt(i);
				//4. 获取表单中的行数
				int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
				for (int j = 0; j < physicalNumberOfRows; j++) {
					//5. 跳过标题行
					if (j == 0) {
						continue;//跳过标题行
					}
					//6. 获取行
					HSSFRow row = sheet.getRow(j);
					if (row == null) {
						continue;//防止数据中间有空行
					}
					//7. 获取列数
					int physicalNumberOfCells = row.getPhysicalNumberOfCells();
					book = new Book();
					for (int k = 0; k < physicalNumberOfCells; k++) {
						HSSFCell cell = row.getCell(k);
						switch (cell.getCellType()) {
							case STRING:
								String cellValue = cell.getStringCellValue();
								switch (k) {
									case 1:
										book.setBName(cellValue);
										break;
									case 2:
										book.setWriter(cellValue);
										break;
									case 3:
										book.setPress(cellValue);
										break;
									case 6:
										book.setIssue(cellValue);
										break;
									case 8:
										book.setPosition(cellValue);
								}
								break;

							default: {
								switch (k) {
									case 0:
										book.setBookId((int) cell.getNumericCellValue());
										break;
									case 4:
										book.setCId((int) cell.getNumericCellValue());
										break;
									case 5:
										book.setPrice(cell.getNumericCellValue());
										break;
									case 7:
										book.setStatus((int) cell.getNumericCellValue());
										break;
								}
								break;
							}
						}
					}
					list.add(book);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}


}
