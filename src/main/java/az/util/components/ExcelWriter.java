package az.util.components;

/**
 * @author Rashad Amirjanov
 */

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.*;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExcelWriter {

    private WritableCellFormat timesBoldUnderline;
    private WritableCellFormat times;
    private String outputFile;
    private WritableWorkbook workbook = null;
    private WorkbookSettings wbSettings = null;

    public ExcelWriter(String outputFile) throws IOException, WriteException {
        this(new File(outputFile));
    }

    public ExcelWriter(File file) throws IOException, WriteException {
        outputFile = file.getName();

        wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));

        workbook = Workbook.createWorkbook(file, wbSettings);

        WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
        // Define the cell format
        times = new WritableCellFormat(times10pt);
        // Lets automatically wrap the cells
        times.setWrap(true);

        // Create create a bold font with unterlines
        WritableFont times10ptBoldUnderline = new WritableFont(
                WritableFont.TIMES, 10, WritableFont.BOLD, false,
                UnderlineStyle.SINGLE);
        timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
        // Lets automatically wrap the cells
        timesBoldUnderline.setWrap(true);
    }

    //    /**
//     *
//     * @param fileName
//     * @param tableModels Each table model
//     */
//    public static void viewAsExcelFile(String fileName, ObjectTableModel[] tableModels, String[] sheetNames) throws IOException, WriteException {
//        if (tableModels == null || sheetNames == null) {
//            return;
//        }
//
//        if (tableModels.length != sheetNames.length) {
//            Logger.getLogger(ExcelWriter.class.getName()).log(Level.SEVERE, "not equal: tableModels.length {0}, sheetNames.length {1}", new Object[]{tableModels.length, sheetNames.length});
//            return;
//        }
//
//        ExcelWriter excel = new ExcelWriter(fileName);
////        System.out.println("1111");
//        for (int i = 0; i < tableModels.length; i++) {
//            ObjectTableModel objectTableModel = tableModels[i];
////            System.out.println("2222");
//            WritableSheet sheet = excel.createSheet(sheetNames[i]);
//            for (int c = 0; c < objectTableModel.getColumnCount(); c++) {
//                String columnName = objectTableModel.getColumnName(c);
////                System.out.println("3333 " + columnName + " " + (c + 1));
//                try {
//                    excel.addHeader(sheet, c, 1, columnName);
//                } catch (WriteException ex) {
//                    ex.printStackTrace();
//                }
//            }
//
//            for (int r = 0; r < objectTableModel.getRowCount(); r++) {
//                for (int c = 0; c < objectTableModel.getColumnCount(); c++) {
////                    System.out.println("4444 " + objectTableModel.getValueAt(r, c).toString());
//                    excel.addLabel(sheet, c, r + 2, objectTableModel.getValueAt(r, c).toString());
//                }
//            }
//        }
//
//        excel.write();
//    }
    public static void viewAsExcelFile(ObjectTableModel[] tableModels, String[] sheetNames) throws IOException, WriteException {
        if (tableModels == null || sheetNames == null) {
            return;
        }

        if (tableModels.length != sheetNames.length) {
            Logger.getLogger(ExcelWriter.class.getName()).log(Level.SEVERE, "not equal: tableModels.length {0}, sheetNames.length {1}", new Object[]{tableModels.length, sheetNames.length});
            return;
        }

        File file = File.createTempFile("store_app_", ".xls");
        file.deleteOnExit();

        ExcelWriter excel = new ExcelWriter(file);
//        System.out.println("1111");
        for (int i = 0; i < tableModels.length; i++) {
            ObjectTableModel objectTableModel = tableModels[i];
//            System.out.println("2222");
            WritableSheet sheet = excel.createSheet(sheetNames[i]);
            for (int c = 0; c < objectTableModel.getColumnCount(); c++) {
                String columnName = objectTableModel.getColumnName(c);
//                System.out.println("3333 " + columnName + " " + (c + 1));
                try {
                    excel.addHeader(sheet, c, 1, columnName);
                } catch (WriteException ex) {
                    ex.printStackTrace();
                }
            }

            for (int r = 0; r < objectTableModel.getRowCount(); r++) {
                for (int c = 0; c < objectTableModel.getColumnCount(); c++) {
//                    System.out.println("4444 " + objectTableModel.getValueAt(r, c).toString());
                    excel.addLabel(sheet, c, r + 2, objectTableModel.getValueAt(r, c).toString());
                }
            }
        }

        excel.write();
        Logger.getLogger(ExcelWriter.class.getName()).log(Level.INFO, file.getCanonicalPath());
        viewFile(file.getCanonicalPath());
    }

    //    public static void viewAsExcelFile(ObjectTableModel[] tableModels, String[] sheetNames,
//            HashMap<Integer, String> extraCells) throws IOException, WriteException {
//        if (tableModels == null || sheetNames == null) {
//            return;
//        }
//
//        if (tableModels.length != sheetNames.length) {
//            Logger.getLogger(ExcelWriter.class.getName()).log(Level.SEVERE, "not equal: tableModels.length {0}, sheetNames.length {1}", new Object[]{tableModels.length, sheetNames.length});
//            return;
//        }
//
//        File file = File.createTempFile("store_app_", ".xls");
//        file.deleteOnExit();
//
//        ExcelWriter excel = new ExcelWriter(file);
////        System.out.println("1111");
//        for (int i = 0; i < tableModels.length; i++) {
//            ObjectTableModel objectTableModel = tableModels[i];
////            System.out.println("2222");
//            WritableSheet sheet = excel.createSheet(sheetNames[i]);
//            for (int c = 0; c < objectTableModel.getColumnCount(); c++) {
//                String columnName = objectTableModel.getColumnName(c);
////                System.out.println("3333 " + columnName + " " + (c + 1));
//                try {
//                    excel.addHeader(sheet, c, 1, columnName);
//                } catch (WriteException ex) {
//                    ex.printStackTrace();
//                }
//            }
//
//            for (int r = 0; r < objectTableModel.getRowCount(); r++) {
//                for (int c = 0; c < objectTableModel.getColumnCount(); c++) {
////                    System.out.println("4444 " + objectTableModel.getValueAt(r, c).toString());
//                    excel.addLabel(sheet, c, r + 2, objectTableModel.getValueAt(r, c).toString());
//                }
//            }
//
//            if (extraCells != null) {
//                Iterator iter = extraCells.entrySet().iterator();
//                while (iter.hasNext()) {
//                    Map.Entry<Integer, String> e = (Map.Entry<Integer, String>) iter.next();
//                    excel.addHeader(sheet, e.getKey(), objectTableModel.getRowCount() + 2 + 1, e.getValue());
//                }
//            }
//        }
//
//        excel.write();
//        System.out.println(file.getCanonicalPath());
//        viewFile(file.getCanonicalPath());
//    }
//
//    public static void viewAsExcelFile(ObjectTableModel[] tableModels, String[] sheetNames,
//            HashMap<Integer, String> headerCells,
//            HashMap<Integer, String> footerCells) throws IOException, WriteException {
//        if (tableModels == null || sheetNames == null) {
//            return;
//        }
//
//        if (tableModels.length != sheetNames.length) {
//            System.err.println("tableModels and sheetNames does't appropriate!");
//            return;
//        }
//
//        File file = File.createTempFile("store_app_", ".xls");
//        file.deleteOnExit();
//
//        ExcelWriter excel = new ExcelWriter(file);
////        System.out.println("1111");
//        for (int i = 0; i < tableModels.length; i++) {
//            ObjectTableModel objectTableModel = tableModels[i];
////            System.out.println("2222");
//            WritableSheet sheet = excel.createSheet(sheetNames[i]);
//
//            if (headerCells != null) {
//                Iterator iter = headerCells.entrySet().iterator();
//                while (iter.hasNext()) {
//                    Map.Entry<Integer, String> e = (Map.Entry<Integer, String>) iter.next();
//                    excel.addHeader(sheet, e.getKey(), 0, e.getValue());
//                }
//            }
//
//            for (int c = 0; c < objectTableModel.getColumnCount(); c++) {
//                String columnName = objectTableModel.getColumnName(c);
////                System.out.println("3333 " + columnName + " " + (c + 1));
//                try {
//                    excel.addHeader(sheet, c, 1 + 1, columnName);
//                } catch (WriteException ex) {
//                    ex.printStackTrace();
//                }
//            }
//
//            for (int r = 0; r < objectTableModel.getRowCount(); r++) {
//                for (int c = 0; c < objectTableModel.getColumnCount(); c++) {
////                    System.out.println("4444 " + objectTableModel.getValueAt(r, c).toString());
//                    excel.addLabel(sheet, c, r + 2 + 1, objectTableModel.getValueAt(r, c).toString());
//                }
//            }
//
//            if (footerCells != null) {
//                Iterator iter = footerCells.entrySet().iterator();
//                while (iter.hasNext()) {
//                    Map.Entry<Integer, String> e = (Map.Entry<Integer, String>) iter.next();
//                    excel.addHeader(sheet, e.getKey(), objectTableModel.getRowCount() + 2 + 1 + 1, e.getValue());
//                }
//            }
//        }
//
//        excel.write();
//        System.out.println(file.getCanonicalPath());
//        viewFile(file.getCanonicalPath());
//    }
    public static void viewFile(String fileName) {
        try {
            String[] commands = {"cmd", "/c", "start", "\"DummyTitle\"", fileName};
            Process p = Runtime.getRuntime().exec(commands);
            p.waitFor();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(ExcelWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public WritableSheet createSheet(String sheetName) {
        return workbook.createSheet(sheetName, workbook.getNumberOfSheets());
    }

    //    public void createHeader(WritableSheet sheet)
//            throws WriteException {
//        // Lets create a times font
//        WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
//        // Define the cell format
//        times = new WritableCellFormat(times10pt);
//        // Lets automatically wrap the cells
//        times.setWrap(true);
//
//        // Create create a bold font with unterlines
//        WritableFont times10ptBoldUnderline = new WritableFont(
//                WritableFont.TIMES, 10, WritableFont.BOLD, false,
//                UnderlineStyle.SINGLE);
//        timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
//        // Lets automatically wrap the cells
//        timesBoldUnderline.setWrap(true);
//
//        CellView cv = new CellView();
//        cv.setFormat(times);
//        cv.setFormat(timesBoldUnderline);
//        cv.setAutosize(true);
//
//        // Write a few headers
//        addHeader(sheet, 0, 0, "Header 1Əhmöğü");
//        addHeader(sheet, 1, 0, "This is another header");
//
//
//    }
//    public void createContent(WritableSheet sheet) throws WriteException,
//            RowsExceededException {
//        // Write a few number
//        for (int i = 1; i < 10; i++) {
//            // First column
//            addNumber(sheet, 0, i, i + 10);
//            // Second column
//            addNumber(sheet, 1, i, i * i);
//        }
//        // Lets calculate the sum of it
//        StringBuffer buf = new StringBuffer();
//        buf.append("SUM(A2:A10)");
//        Formula f = new Formula(0, 10, buf.toString());
//        sheet.addCell(f);
//        buf = new StringBuffer();
//        buf.append("SUM(B2:B10)");
//        f = new Formula(1, 10, buf.toString());
//        sheet.addCell(f);
//
//        // Now a bit of text
//        for (int i = 12; i < 20; i++) {
//            // First column
//            addLabel(sheet, 0, i, "Boring text " + i);
//            // Second column
//            addLabel(sheet, 1, i, "Another text");
//        }
//    }
    public void addHeader(WritableSheet sheet, int column, int row, String headerName)
            throws WriteException {
        Label label;
        label = new Label(column, row, headerName, timesBoldUnderline);
        sheet.addCell(label);
        sheet.setColumnView(column, 20);
    }

    public void addNumber(WritableSheet sheet, int column, int row,
                          Integer integer) throws WriteException {
        jxl.write.Number number;
        number = new jxl.write.Number(column, row, integer, times);
        sheet.addCell(number);
    }

    public void addLabel(WritableSheet sheet, int column, int row, String cellText)
            throws WriteException {
        Label label;
        label = new Label(column, row, cellText, times);
        sheet.addCell(label);
    }

    public void write() throws IOException, WriteException {
        workbook.write();
        workbook.close();
    }
//    public static void main(String[] args) throws WriteException, IOException {
//        WriteExcel test = new WriteExcel("");
//        test.setOutputFile("c:/temp/lars.xls");
//        test.write();
//        System.out.println("Please check the result file under c:/temp/lars.xls ");
//    }
}
