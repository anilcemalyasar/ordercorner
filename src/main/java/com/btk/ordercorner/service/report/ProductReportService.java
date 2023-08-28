package com.btk.ordercorner.service.report;

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
public class ProductReportService {

    @Autowired
    private ProductRepository productRepository;

    public void productGenerateExcel(HttpServletResponse response) throws IOException {
        // Ürünleri veritabanından aldık
        List<Product> products = productRepository.findAll();

        // Yeni bir Excel çalışma kitabı oluşturduk
        HSSFWorkbook workbook = new HSSFWorkbook();

        // Çalışma kitabından bir sayfa oluşturduk
        Sheet sheet = workbook.createSheet("Products Info");

        // Başlık satırını oluşturduk
        Row headerRow = sheet.createRow(0);
        Cell idCell = headerRow.createCell(0);
        idCell.setCellValue("Product_ID");
        Cell nameCell = headerRow.createCell(1);
        nameCell.setCellValue("Product_Name");
        Cell descriptionCell = headerRow.createCell(2);
        descriptionCell.setCellValue("Product_Descripton");
        Cell priceCell = headerRow.createCell(3);
        priceCell.setCellValue("Product_Price");
        Cell stockAmountStatusCell = headerRow.createCell(4);
        stockAmountStatusCell.setCellValue("Product_Amount_Status");
        Cell productOfCategory = headerRow.createCell(5);
        productOfCategory.setCellValue("Product_Of_Category");
        // Veri satırlarını eklemek için sayaç ekledik
        int dataRowIndex = 1;

        // Ürün verilerini satırlara ekledik
        for (Product product : products) {
            Row dataRow = sheet.createRow(dataRowIndex);
            Cell dataIdCell = dataRow.createCell(0);
            dataIdCell.setCellValue(product.getProductId());
            Cell dataNameCell = dataRow.createCell(1);
            dataNameCell.setCellValue(product.getProductName());
            Cell dataDescriptionCell = dataRow.createCell(2);
            dataDescriptionCell.setCellValue(product.getProductDescription());
            Cell dataPriceCell = dataRow.createCell(3);
            dataPriceCell.setCellValue(product.getProductPrice());
            Cell dataStockAmountStatusCell = dataRow.createCell(4); // Fix the cell index to 4
            dataStockAmountStatusCell.setCellValue(product.getStockAmount());
            Cell dataProductOfCategory = dataRow.createCell(5); // Fix the cell index to 5
            dataProductOfCategory.setCellValue(product.getCategory().getCategoryName());

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