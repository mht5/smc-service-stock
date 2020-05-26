package com.smc.service;

import com.smc.domain.StockPrice;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public interface StockPriceService {

    int importPrice(MultipartFile file) throws Exception;

    List<Date> getMissingData(int companyId, String fromDate, String toDate) throws Exception;

    List<List<StockPrice>> compareBetweenCompanies(int stockExchangeId, int companyId1, int companyId2, String fromDate, String toDate) throws Exception;

    Workbook excelExportFile(int stockExchangeId, int companyId1, int companyId2, String fromDate, String toDate) throws Exception;
}
