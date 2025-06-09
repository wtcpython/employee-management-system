package com.company.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.company.pojo.Employee;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ExcelUtil {
    public static void exportToExcel(TableView<?> tableView, File file) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        // 创建表头
        Row headerRow = sheet.createRow(0);
        int columnIndex = 0;
        for (TableColumn<?, ?> column : tableView.getColumns()) {
            Cell headerCell = headerRow.createCell(columnIndex++);
            headerCell.setCellValue(column.getText());
        }

        // 填充数据
        int rowIndex = 1;
        for (int i = 0; i < tableView.getItems().size(); i++) {
            Row dataRow = sheet.createRow(rowIndex++);
            columnIndex = 0;
            for (TableColumn<?, ?> column : tableView.getColumns()) {
                Cell cell = dataRow.createCell(columnIndex++);
                Object cellValue = column.getCellObservableValue(i) != null
                        ? column.getCellObservableValue(i).getValue()
                        : null;

                if (cellValue != null) {
                    if (cellValue instanceof Number) {
                        cell.setCellValue(((Number) cellValue).doubleValue());
                    } else {
                        cell.setCellValue(cellValue.toString());
                    }
                } else {
                    cell.setCellValue("");
                }
            }
        }

        // 自动调整列宽
        for (int i = 0; i < tableView.getColumns().size(); i++) {
            sheet.autoSizeColumn(i);
        }

        // 写入文件
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        } finally {
            workbook.close();
        }
    }

    public static ObservableList<Employee> importFromExcel(File file) {
        ObservableList<Employee> employees = FXCollections.observableArrayList();
        if (file != null) {
            try (FileInputStream fis = new FileInputStream(file);
                    XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

                Sheet sheet = workbook.getSheetAt(0);
                for (Row row : sheet) {
                    if (row.getRowNum() == 0)
                        continue; // 跳过表头
                    Employee emp = new Employee(
                            Integer.parseInt(getCellValue(row.getCell(0))),
                            getCellValue(row.getCell(1)),
                            getCellValue(row.getCell(2)),
                            LocalDate.parse(getCellValue(row.getCell(3))),
                            getCellValue(row.getCell(4)),
                            Integer.parseInt(getCellValue(row.getCell(5))),
                            Double.parseDouble(getCellValue(row.getCell(6))),
                            getCellValue(row.getCell(7))

                    );
                    employees.add(emp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return employees;
    }

    // 全部转成String
    public static String getCellValue(Cell cell) {
        if (cell == null)
            return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) { // 日期处理
                    return new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
                }
                return String.valueOf((int) cell.getNumericCellValue()); // 数字去小数
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return evaluateFormula(cell); // 公式解析
            default:
                return "";
        }
    }

    // 公式解析方法
    private static String evaluateFormula(Cell cell) {
        FormulaEvaluator evaluator = cell.getSheet().getWorkbook()
                .getCreationHelper().createFormulaEvaluator();
        return new DataFormatter().formatCellValue(cell, evaluator);
    }
}
