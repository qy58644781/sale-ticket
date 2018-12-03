package com.yadan.saleticket.base.tools;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.math.BigDecimal;

public class ExcelUtils {

    public static final int INTEGER = 0;
    public static final int STRING = 1;
    public static final int DECEMAL = 2;

    public static Object getValue(XSSFCell cell) {
        if (cell == null) return null;
        CellType cellTypeEnum = cell.getCellTypeEnum();
        switch (cellTypeEnum) {
            case _NONE:
            case ERROR:
            case FORMULA:
                return null;
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                return cell.getStringCellValue();
            case BLANK:
                return null;
            case BOOLEAN:
                return cell.getBooleanCellValue();
            default:
                return null;
        }
    }

    public static Object getValue(XSSFCell cell, int type) {
        Object value = getValue(cell);
        if (type == ExcelUtils.STRING) {
            return value.toString();
        } else if (type == ExcelUtils.INTEGER) {
            if (value.toString().contains(".")) {
                value = value.toString().split("\\.")[0];
            }
            return Integer.valueOf(value.toString());
        } else if (type == ExcelUtils.DECEMAL) {
            return new BigDecimal(value.toString());
        } else {
            return value;
        }
    }
}
