package com.btk.ordercorner.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.btk.ordercorner.model.entity.Category;
import com.btk.ordercorner.model.entity.Customer;
import com.btk.ordercorner.model.entity.Product;
import com.btk.ordercorner.repository.CategoryRepository;
import com.btk.ordercorner.repository.CustomerRepository;
import com.btk.ordercorner.repository.ProductRepository;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ReportService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public String exportReportCategory(String reportFormat) throws FileNotFoundException, JRException {
        List<Category> categories = categoryRepository.findAll();
        // Load file and compile it
        String path = "C:\\Users\\msi\\Desktop";
        File file = ResourceUtils.getFile("classpath:categories.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(categories);
        Map<String, Object> map = new HashMap<>();
        map.put("Shown to", "Admin");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, dataSource);
    
        if(reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\categories.html");
        }
        if(reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\categories.pdf");
        }

        return "report generated in path : " + path;
    }

    public String exportReportProduct(String reportFormat) throws FileNotFoundException, JRException {
        List<Product> products = productRepository.findAll();
        // Load file and compile it
        String path = "C:\\Users\\msi\\Desktop";
        File file = ResourceUtils.getFile("classpath:products.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(products);
        Map<String, Object> map = new HashMap<>();
        map.put("Shown to", "Admin");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, dataSource);
    
        if(reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\products.html");
        }
        if(reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\products.pdf");
        }

        return "report generated in path : " + path;
    }

    public String exportReportCustomer(String reportFormat) throws FileNotFoundException, JRException {
        List<Customer> customers = customerRepository.findAll();
        // Load file and compile it
        String path = "C:\\Users\\msi\\Desktop";
        File file = ResourceUtils.getFile("classpath:customers.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(customers);
        Map<String, Object> map = new HashMap<>();
        map.put("Shown to", "Admin");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, dataSource);
    
        if(reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\customers.html");
        }
        if(reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\customers.pdf");
        }

        return "report generated in path : " + path;
    }



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
