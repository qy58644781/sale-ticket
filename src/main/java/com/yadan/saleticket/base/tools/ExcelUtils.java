package com.yadan.saleticket.base.tools;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

@Slf4j
public class ExcelUtils {

    public static final int INTEGER = 0;
    public static final int STRING = 1;
    public static final int DECIMAL = 2;

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
        } else if (type == ExcelUtils.DECIMAL) {
            return new BigDecimal(value.toString());
        } else {
            return value;
        }
    }

    public static void close(Workbook wb) {
        if (wb != null) {
            try {
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
    }

    public static void close(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
    }

    public static CellStyle genCellStyle(Workbook wb) {
        // 设置边框
        CellStyle style = wb.createCellStyle();
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);

        return style;
    }
}
