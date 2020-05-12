package com.smc.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ReadExcelUtil {

    public int totalRows;
    public static int totalCells;

    public List<ArrayList<String>> readExcel(MultipartFile file) throws Exception {
        if (file == null || ExcelUtil.EMPTY.equals(file.getOriginalFilename().trim())) {
            throw new Exception("File is empty.");
        }
        String suffix = ExcelUtil.getSuffix(file.getOriginalFilename());
        if (!ExcelUtil.EMPTY.equals(suffix)) {
            if (ExcelUtil.OFFICE_EXCEL_2003_SUFFIX.equals(suffix)) {
                return readXls(file);
            } else if (ExcelUtil.OFFICE_EXCEL_2010_SUFFIX.equals(suffix)) {
                return readXlsx(file);
            } else {
                throw new Exception("File type not supported, please provide a .xls or .xlsx file.");
            }
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    public List<ArrayList<String>> readXlsx(MultipartFile file) {
        List<ArrayList<String>> list = new ArrayList<>();
        InputStream input = null;
        XSSFWorkbook wb;
        ArrayList<String> rowList;
        try {
            input = file.getInputStream();
            wb = new XSSFWorkbook(input);
            for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
                XSSFSheet xssfSheet = wb.getSheetAt(numSheet);
                if (xssfSheet == null) {
                    continue;
                }
                totalRows = xssfSheet.getLastRowNum();
                for (int rowNum = 1; rowNum <= totalRows; rowNum++) {
                    XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                    if (xssfRow != null) {
                        rowList = new ArrayList<>();
                        totalCells = xssfRow.getLastCellNum();
                        System.out.println(totalCells);
                        for (int c = 0; c < totalCells; c++){
                            XSSFCell cell = xssfRow.getCell(c);
                            if (cell == null) {
                                rowList.add(ExcelUtil.EMPTY);
                                continue;
                            }
                            rowList.add(ExcelUtil.getXValue(cell).trim());
                        }
                        list.add(rowList);
                    }
                }
                rowList = new ArrayList<>();
                rowList.add(ExcelUtil.END_OF_SHEET_SYMBOL);
                list.add(rowList);
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<ArrayList<String>> readXls(MultipartFile file){
        List<ArrayList<String>> list = new ArrayList<>();
        InputStream input = null;
        HSSFWorkbook wb;
        ArrayList<String> rowList;
        try {
            input = file.getInputStream();
            wb = new HSSFWorkbook(input);
            for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
                HSSFSheet hssfSheet = wb.getSheetAt(numSheet);
                if (hssfSheet == null) {
                    continue;
                }
                totalRows = hssfSheet.getLastRowNum();
                for(int rowNum = 0; rowNum <= totalRows; rowNum++){
                    HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                    if (hssfRow!=null) {
                        rowList = new ArrayList<>();
                        totalCells = hssfRow.getLastCellNum();
                        //读取列，从第一列开始
                        for(short c=0; c<=totalCells + 1; c++){
                            HSSFCell cell = hssfRow.getCell(c);
                            if (cell == null) {
                                rowList.add(ExcelUtil.EMPTY);
                                continue;
                            }
                            rowList.add(ExcelUtil.getHValue(cell).trim());
                        }
                        list.add(rowList);
                    }
                }
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
