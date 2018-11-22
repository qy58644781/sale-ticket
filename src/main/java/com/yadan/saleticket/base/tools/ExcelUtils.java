package com.yadan.saleticket.base.tools;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class ExcelUtils {

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
}
