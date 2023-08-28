package com.btk.ordercorner.service;

import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.btk.ordercorner.model.entity.Product;
import com.btk.ordercorner.repository.ProductRepository;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class ReportService {

    @Autowired
    private ProductRepository productRepository;

    public void generateExcel(HttpServletResponse response) throws IOException {
        // Ürünleri veritabanından aldık
        List<Product> products = productRepository.findAll();

        // Yeni bir Excel çalışma kitabı oluşturduk
        HSSFWorkbook workbook = new HSSFWorkbook();

        // Çalışma kitabından bir sayfa oluşturduk
        Sheet sheet = workbook.createSheet("Products Info");

        // Başlık satırını oluşturduk
        Row headerRow = sheet.createRow(0);
        Cell idCell = headerRow.createCell(0);
        idCell.setCellValue("ID");
        Cell nameCell = headerRow.createCell(1);
        nameCell.setCellValue("Name");
        Cell priceCell = headerRow.createCell(2);
        priceCell.setCellValue("Price");

        // Veri satırlarını eklemek için sayaç ekledik
        int dataRowIndex = 1;

        // Ürün verilerini satırlara ekledik
        for (Product product : products) {
            Row dataRow = sheet.createRow(dataRowIndex);
            Cell dataIdCell = dataRow.createCell(0);
            dataIdCell.setCellValue(product.getProductId());
            Cell dataNameCell = dataRow.createCell(1);
            dataNameCell.setCellValue(product.getProductName());
            Cell dataPriceCell = dataRow.createCell(2);
            dataPriceCell.setCellValue(product.getProductPrice());
            dataRowIndex++;
        }

        // HTTP yanıtı için ServletOutputStream aldık
        ServletOutputStream outputStream = response.getOutputStream();

        try {
            // Çalışma kitabını HTTP yanıtına yazdık
            workbook.write(outputStream);
        } catch (IOException e) {

        } finally {

            outputStream.close();
            workbook.close();
        }
    }
}
