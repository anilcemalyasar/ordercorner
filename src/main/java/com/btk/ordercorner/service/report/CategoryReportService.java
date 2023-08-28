package com.btk.ordercorner.service.report;

import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.btk.ordercorner.model.entity.Category;
import com.btk.ordercorner.repository.CategoryRepository;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class CategoryReportService {

    @Autowired
    private CategoryRepository categoryRepository;

    public void categoryGenerateExcel(HttpServletResponse response) throws IOException {

        List<Category> categories = categoryRepository.findAll();

        HSSFWorkbook workbook = new HSSFWorkbook();

        Sheet sheet = workbook.createSheet("Categories Info");

        Row headerRow = sheet.createRow(0);
        Cell idCell = headerRow.createCell(0);
        idCell.setCellValue("Category_ID");
        Cell nameCell = headerRow.createCell(1);
        nameCell.setCellValue("Category_Name");

        int dataRowIndex = 1;

        for (Category category : categories) {
            Row dataRow = sheet.createRow(dataRowIndex);
            Cell dataIdCell = dataRow.createCell(0);
            dataIdCell.setCellValue(category.getCategoryId());
            Cell dataNameCell = dataRow.createCell(1);
            dataNameCell.setCellValue(category.getCategoryName());
            dataRowIndex++;
        }

        ServletOutputStream outputStream = response.getOutputStream();

        try {
            workbook.write(outputStream);
        } catch (IOException e) {

        } finally {

            outputStream.close();
            workbook.close();
        }
    }

}
