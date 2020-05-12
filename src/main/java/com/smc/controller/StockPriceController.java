package com.smc.controller;

import com.smc.domain.StockPrice;
import com.smc.service.StockPriceService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

@RestController
public class StockPriceController {

    @Autowired
    private StockPriceService stockPriceService;

    @PostMapping("/import-price")
    public String importPrice(@RequestParam("file") MultipartFile file) throws Exception {
        return stockPriceService.importPrice(file);
    }

    @GetMapping("/missing-data")
    public List<Date> getMissingData(@RequestParam("companyId") int companyId,
                                     @RequestParam("fromDate") String fromDate,
                                     @RequestParam("toDate") String toDate) throws Exception {
        return stockPriceService.getMissingData(companyId, fromDate, toDate);
    }

    @GetMapping("/compare-between-companies")
    public List<List<StockPrice>> compareBetweenCompanies(@RequestParam("stockExchangeId") int stockExchangeId,
                                                          @RequestParam("companyId1") int companyId1,
                                                          @RequestParam("companyId2") int companyId2,
                                                          @RequestParam("fromDate") String fromDate,
                                                          @RequestParam("toDate") String toDate) throws Exception {
        return stockPriceService.compareBetweenCompanies(stockExchangeId, companyId1, companyId2, fromDate, toDate);
    }

    @GetMapping(value="/exportReport")
    public void excelExportFile(@RequestParam("stockExchangeId") int stockExchangeId,
                                @RequestParam("companyId1") int companyId1,
                                @RequestParam("companyId2") int companyId2,
                                @RequestParam("fromDate") String fromDate,
                                @RequestParam("toDate") String toDate,
                                HttpServletResponse response) throws Exception {
        Workbook wb = stockPriceService.excelExportFile(stockExchangeId, companyId1, companyId2, fromDate, toDate);
        try {
            String fileName = "Compare-Report-" + System.currentTimeMillis() + ".xlsx";
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            wb.write(outputStream);
            ByteArrayInputStream tempIn = new ByteArrayInputStream(outputStream.toByteArray());
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Length", String.valueOf(tempIn.available()));
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Content-type", "application-download");
            response.setContentType("application/octet-stream");
            ServletOutputStream out = response.getOutputStream();
            BufferedOutputStream toOut = new BufferedOutputStream(out);
            byte[] buffer = new byte[tempIn.available()];
            int a;
            while ((a = tempIn.read(buffer)) != -1) {
                out.write(buffer, 0, a);
            }
            toOut.write(buffer);
            toOut.flush();
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
