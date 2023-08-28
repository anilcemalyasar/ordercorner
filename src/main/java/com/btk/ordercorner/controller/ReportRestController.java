package com.btk.ordercorner.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.btk.ordercorner.exception.report.ReportGenerationException;
import com.btk.ordercorner.service.report.CategoryReportService;
import com.btk.ordercorner.service.report.CustomerReportService;
import com.btk.ordercorner.service.report.ProductReportService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class ReportRestController {
    private static final Logger logger = LoggerFactory.getLogger(ReportRestController.class);

    @Autowired
    private ProductReportService productReportService;
    @Autowired
    private CustomerReportService customerReportService;
    @Autowired
    private CategoryReportService categoryReportService;

    @GetMapping("/products/report")
    public void productExcelReport(HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/vnd.ms-excel");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=products.xls";
            response.setHeader(headerKey, headerValue);
            productReportService.productGenerateExcel(response);
            logger.info("Ürün raporu başarıyla oluşturuldu.");
        } catch (Exception e) {
            logger.error("Ürün raporu oluşturulurken hata oluştu");
            throw new ReportGenerationException("Ürün raporu oluşturulurken hata oluştu", e);
        }
    }

    @GetMapping("/customers/report")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void customerExcelReport(HttpServletResponse response) throws IOException {
        try {

            response.setContentType("application/vnd.ms-excel");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=customers.xls";
            response.setHeader(headerKey, headerValue);
            customerReportService.customerGenerateExcel(response);
            logger.info("Müşteri raporu başarıyla oluşturuldu.");
        } catch (Exception e) {
            logger.error("Müşteri raporu oluşturulurken hata oluştu");
            throw new ReportGenerationException("Müşteri raporu oluşturulurken hata oluştu", e);
        }
    }

    @GetMapping("/categories/report")
    // @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void categoryExcelReport(HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/vnd.ms-excel");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=categories.xls";
            response.setHeader(headerKey, headerValue);
            categoryReportService.categoryGenerateExcel(response);
            logger.info("Kategori raporu başarıyla oluşturuldu");
        } catch (Exception e) {
            logger.error("Katagori raporu oluşturulurken hata oluştu");
            throw new ReportGenerationException("Kategori raporu oluşturulurken hata oluştu ", e);
        }
    }

}
