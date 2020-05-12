package com.smc.service;

import com.smc.domain.CompanyStockExchange;
import com.smc.domain.StockPrice;
import com.smc.repository.CompanyStockExchangeRepository;
import com.smc.repository.StockPriceRepository;
import com.smc.util.ExcelUtil;
import com.smc.util.ReadExcelUtil;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StockPriceServiceImpl implements StockPriceService {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

    private static SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private StockPriceRepository stockPriceRepo;

    @Autowired
    private CompanyStockExchangeRepository cseRepo;

    private void validateStockPrice(StockPrice stockPrice, List<Integer> exchangeList, List<String> stockCodeList) throws Exception {
        if (!exchangeList.contains(stockPrice.getStockExchangeId())) {
            throw new Exception("The stock exchange ID \"" + stockPrice.getStockExchangeId() + "\" does not exist.");
        }
        if (!stockCodeList.contains(stockPrice.getStockCode())) {
            throw new Exception("The stock code \"" + stockPrice.getStockCode() + "\" does not exist.");
        }
    }

    private List<StockPrice> parsePrice(List<ArrayList<String>> priceList) throws Exception {
        List<CompanyStockExchange> cseList = cseRepo.findAll();
        List<Integer> exchangeList = new ArrayList<>();
        List<String> stockCodeList = new ArrayList<>();
        for (CompanyStockExchange cse : cseList) {
            if (!exchangeList.contains(cse.getStockExchangeId())) {
                exchangeList.add(cse.getStockExchangeId());
            }
            stockCodeList.add(cse.getStockCode());
        }
        List<StockPrice> stockPriceList = new ArrayList<>();
        int stockExchangeId;
        String stockCode;
        BigDecimal price;
        Date date;
        Date time;
        for (List<String> list : priceList) {
            if (!list.isEmpty() && ExcelUtil.END_OF_SHEET_SYMBOL.endsWith(list.get(0))) {
                System.err.println(ExcelUtil.END_OF_SHEET_SYMBOL);
                continue;
            } else {
                for (String s : list) {
                    if (s == null || s.length() < 1) {
                        throw new Exception("Invalid input, no empty input is allowed.");
                    }
                }
                stockExchangeId = Integer.valueOf(list.get(0));
                stockCode = list.get(1);
                price = BigDecimal.valueOf(Double.valueOf(list.get(2)));
                try {
                    date = sdf.parse(list.get(3));
                    time = sdfTime.parse(list.get(4));
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw e;
                }
                StockPrice stockPrice = new StockPrice();
                stockPrice.setStockExchangeId(stockExchangeId);
                stockPrice.setStockCode(stockCode);
                stockPrice.setPrice(price);
                stockPrice.setDate(new java.sql.Date(date.getTime()));
                stockPrice.setTime(new java.sql.Time(time.getTime()));
                validateStockPrice(stockPrice, exchangeList, stockCodeList);
                stockPriceList.add(stockPrice);
            }
        }
        return stockPriceList;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public String importPrice(MultipartFile file) throws Exception {
        List<ArrayList<String>> priceList;
        try {
            priceList = new ReadExcelUtil().readExcel(file);
        } catch (IOException e) {
            throw e;
        }
        List<StockPrice> stockPriceList = parsePrice(priceList);
        int count = stockPriceRepo.saveAll(stockPriceList).size();
        return count + " records were imported successfully.";
    }

    private Date plusOneDay(Date fromDate) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(fromDate);
        calendar.add(Calendar.DATE,1);
        return calendar.getTime();
    }

    private Date minusOneDay(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,-1);
        return calendar.getTime();
    }

    @Override
    public List<Date> getMissingData(int companyId, String fromDate, String toDate) throws Exception {
        Date from = sdf.parse(fromDate);
        Date to = sdf.parse(toDate);
        List<CompanyStockExchange> cseList = cseRepo.findByCompanyId(companyId);
        if (cseList.isEmpty()) {
            throw new Exception("Company with IDv\"" + companyId + "\" does not exist.");
        }
        CompanyStockExchange cse = cseList.get(0);
        int stockExchangeId = cse.getStockExchangeId();
        String stockCode = cse.getStockCode();
        List<StockPrice> stockPriceList = stockPriceRepo.getMissingData(stockExchangeId, stockCode, from, to);
        List<Date> datesWithMissingData = new ArrayList<>();
        StockPrice price = stockPriceList.get(0);
        Date previousDate = price.getDate();
        while (previousDate.after(from)) {
            datesWithMissingData.add(from);
            from = plusOneDay(from);
        }
        for (int i = 1; i < stockPriceList.size() - 1; i++) {
            price = stockPriceList.get(i);
            Date currentDate = price.getDate();
            if (!minusOneDay(currentDate).equals(previousDate)) {
                while (currentDate.after(previousDate)) {
                    previousDate = plusOneDay(previousDate);
                    if (!previousDate.equals(currentDate)) {
                        datesWithMissingData.add(previousDate);
                    }
                }
            }
            previousDate = currentDate;
        }
        price = stockPriceList.get(stockPriceList.size() - 1);
        Date currentDate = price.getDate();
        if (!minusOneDay(currentDate).equals(previousDate)) {
            while (currentDate.after(previousDate)) {
                previousDate = plusOneDay(previousDate);
                if (!previousDate.equals(currentDate)) {
                    datesWithMissingData.add(previousDate);
                }
            }
        }
        while (currentDate.before(to)) {
            currentDate = plusOneDay(currentDate);
            datesWithMissingData.add(currentDate);
        }

        return datesWithMissingData;
    }

    @Override
    public List<List<StockPrice>> compareBetweenCompanies(int stockExchangeId, int companyId1, int companyId2,
                                                      String fromDate, String toDate) throws Exception {
        List<CompanyStockExchange> cseList = cseRepo.findByStockExchangeIdAndCompanyIds(stockExchangeId, companyId1, companyId2);
        if (cseList.isEmpty()) {
            throw new Exception("At least 1 of the companies is not registered in the given stock exchange.");
        }
        Date from  = sdf.parse(fromDate);
        Date to = sdf.parse(toDate);
        List<StockPrice> priceList1 = stockPriceRepo.findPriceByStockCodeAndDate(cseList.get(0).getStockCode(), from, to);
        List<StockPrice> priceList2 = stockPriceRepo.findPriceByStockCodeAndDate(cseList.get(1).getStockCode(), from, to);
        List<List<StockPrice>> result = new ArrayList<>();
        result.add(priceList1);
        result.add(priceList2);
        return result;
    }

    @Override
    public Workbook excelExportFile(int stockExchangeId, int companyId1, int companyId2, String fromDate, String toDate) throws Exception {
        List<List<StockPrice>> result = compareBetweenCompanies(stockExchangeId, companyId1, companyId2, fromDate, toDate);
        Workbook book = new XSSFWorkbook();
        for (List<StockPrice> list : result) {
            Sheet sheet = book.createSheet(list.get(0).getStockCode());
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("Stock Exchange ID");
            row.createCell(1).setCellValue("Stock Code");
            row.createCell(2).setCellValue("Price");
            row.createCell(3).setCellValue("Date");
            row.createCell(4).setCellValue("Time");
            int count = 1;
            for(StockPrice stockPrice : list) {
                Row rows = sheet.createRow(count);
                rows.createCell(0).setCellValue(stockPrice.getStockExchangeId());
                rows.createCell(1).setCellValue(stockPrice.getStockCode());
                rows.createCell(2).setCellValue(stockPrice.getPrice() + "");
                rows.createCell(3).setCellValue(sdf.format(stockPrice.getDate()));
                rows.createCell(4).setCellValue(sdfTime.format(stockPrice.getTime()));
                count++;
            }
        }
        return book;
    }

}
