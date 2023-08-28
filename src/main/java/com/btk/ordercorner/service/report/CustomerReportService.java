package com.btk.ordercorner.service.report;

import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.btk.ordercorner.model.entity.Customer;
import com.btk.ordercorner.repository.CustomerRepository;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class CustomerReportService {
    @Autowired
    private CustomerRepository customerRepository;

    public void customerGenerateExcel(HttpServletResponse response) throws IOException {

        List<Customer> customers = customerRepository.findAll();

        HSSFWorkbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Customers Info");

        Row headerRow = sheet.createRow(0);
        Cell idCell = headerRow.createCell(0);
        idCell.setCellValue("Customer_ID");
        Cell firstNameCell = headerRow.createCell(1);
        firstNameCell.setCellValue("Customer_Name");
        Cell lastNameCell = headerRow.createCell(2);
        lastNameCell.setCellValue("Customer_LastName");
        Cell epostaCell = headerRow.createCell(3);
        epostaCell.setCellValue("Customer_Eposta");
        Cell userRolesCell = headerRow.createCell(4);
        userRolesCell.setCellValue("User_Role");
        Cell userNameCell = headerRow.createCell(5);
        userNameCell.setCellValue("User_Name");

        int dataRowIndex = 1;

        for (Customer customer : customers) {
            Row dataRow = sheet.createRow(dataRowIndex);
            Cell dataIdCell = dataRow.createCell(0);
            dataIdCell.setCellValue(customer.getCustomerId());
            Cell dataNameCell = dataRow.createCell(1);
            dataNameCell.setCellValue(customer.getCustomerFirstName());
            Cell dataLastNameCell = dataRow.createCell(2);
            dataLastNameCell.setCellValue(customer.getCustomerLastName());
            Cell dataEpostaCell = dataRow.createCell(3);
            dataEpostaCell.setCellValue(customer.getMailAddress());
            Cell dataUserCell = dataRow.createCell(4);
            dataUserCell.setCellValue(customer.getUsername());
            Cell dataUserRole = dataRow.createCell(5);
            dataUserRole.setCellValue(customer.getRoles());
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
