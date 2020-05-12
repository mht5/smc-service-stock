package com.smc.util;

import com.sun.istack.internal.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ExcelUtil {

    private static final Logger logger = Logger.getLogger(ExcelUtil.class);
    public static final String OFFICE_EXCEL_2003_SUFFIX = "xls";
    public static final String OFFICE_EXCEL_2010_SUFFIX = "xlsx";
    public static final String EMPTY = "";
    public static final String POINT = ".";
    public static final String END_OF_SHEET_SYMBOL = "end of the sheet";
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    public static SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");

    public static String getSuffix(String path){
        if (path==null || EMPTY.equals(path.trim())) {
            return EMPTY;
        }
        if(path.contains(POINT)){
            return path.substring(path.lastIndexOf(POINT) + 1);
        }
        return EMPTY;
    }

    public static String getXValue(XSSFCell xssfCell) {
        if (xssfCell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(xssfCell.getBooleanCellValue());
        } else if (xssfCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            String cellValue = "";
            if (XSSFDateUtil.isCellDateFormatted(xssfCell)) {
                Date date = XSSFDateUtil.getJavaDate(xssfCell.getNumericCellValue());
                int defaultYear = -1;
                if (date.getYear() == defaultYear) {
                    cellValue = sdfTime.format(date);
                } else {
                    cellValue = sdf.format(date);
                }
            } else {
                DecimalFormat df = new DecimalFormat("#.##");
                cellValue = df.format(xssfCell.getNumericCellValue());
                String strArr = cellValue.substring(cellValue.lastIndexOf(POINT) + 1);
                if (strArr.equals("00")) {
                    cellValue = cellValue.substring(0, cellValue.lastIndexOf(POINT));
                }
            }
            return cellValue;
        } else {
            return String.valueOf(xssfCell.getStringCellValue());
        }
    }

    @SuppressWarnings({ "static-access", "deprecation" })
    public static String getHValue(HSSFCell hssfCell){
        if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
            String cellValue = "";
            if (HSSFDateUtil.isCellDateFormatted(hssfCell)) {
                Date date = XSSFDateUtil.getJavaDate(hssfCell.getNumericCellValue());
                int defaultYear = -1;
                if (date.getYear() == defaultYear) {
                    cellValue = sdfTime.format(date);
                } else {
                    cellValue = sdf.format(date);
                }
            } else {
                DecimalFormat df = new DecimalFormat("#.##");
                cellValue = df.format(hssfCell.getNumericCellValue());
                String strArr = cellValue.substring(cellValue.lastIndexOf(POINT) + 1);
                if (strArr.equals("00")) {
                    cellValue = cellValue.substring(0, cellValue.lastIndexOf(POINT));
                }
            }
            return cellValue;
        } else {
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }

    static class XSSFDateUtil extends DateUtil {
        protected static int absoluteDay(Calendar cal, boolean use1904windowing) {
            return DateUtil.absoluteDay(cal, use1904windowing);
        }
    }
}
