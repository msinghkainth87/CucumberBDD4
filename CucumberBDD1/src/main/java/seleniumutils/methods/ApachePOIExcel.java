package seleniumutils.methods;

import com.github.javafaker.Faker;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import env.Log4j;

public class ApachePOIExcel implements Log4j{

    private static XSSFWorkbook workbook;
    //Nested Hash of excel sheet
    private static HashMap<String,HashMap<String,HashMap<String, String>>> xlData;

    public static HashMap<String, PageObject> getFlatData() {
        return flatData;
    }

    public static void setFlatData(HashMap<String, PageObject> flatData) {
        ApachePOIExcel.flatData = flatData;
    }

    //flattened Hashmap of the excel sheet
    private static HashMap<String,PageObject> flatData;

    public static HashMap<String, HashMap<String, HashMap<String, String>>> getXlData() {
        return xlData;
    }

    public static void setXlData(String filename) {
        ApachePOIExcel.xlData = ApachePOIExcel.readXL2Hash(filename);
    }


    public static XSSFWorkbook getXLHandler(String fileName)
    {
       // if(workbook==null)
        workbook=readXL(fileName);
        return workbook;
    }

    /**
     *
     * @param fileName : String : Excel Workbook name to read
     * @throws Throwable
     */
    public static XSSFWorkbook readXL(String fileName){
        try{
            workbook = new XSSFWorkbook(new FileInputStream(fileName));
        }catch(Exception e){
            e.printStackTrace();
            log.error(e.getMessage());

        }
        return workbook;
    }

    /**
     * write contents to an xl file
     * @param fileName : String : Excel workbook name
     * @throws Throwable
     */
    public static void write2XL(String fileName){
       try {
           workbook = new XSSFWorkbook();
           //Add a worksheet
           XSSFSheet sheet = workbook.createSheet("Sheet1");
           //Create two rows and add content in first 3 cells of each row
           for (int i = 0; i < 2; i++) {
               XSSFRow row = sheet.createRow(i);
               row.createCell(0).setCellValue(new Faker().name().firstName());
               row.createCell(1).setCellValue(new Faker().address().streetAddressNumber());
               row.createCell(2).setCellValue(new Faker().phoneNumber().phoneNumber());
           }
           FileOutputStream fout = new FileOutputStream(fileName);
           workbook.write(fout);
           fout.flush();
           log.info("Write operation performed on Workbook "+fileName);
           fout.close();
       }catch (Exception e)
        {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    /**
     *
     * @param sheetName : String : sheet name
     * @throws Throwable
     */
    public static void printSheet(String sheetName){
        try
        {

            XSSFSheet sheet = workbook.getSheet(sheetName);
            for(int i=0;i<sheet.getPhysicalNumberOfRows();i++)
            {
                Row currentRow = sheet.getRow(i);
                for(int j=0;j<currentRow.getPhysicalNumberOfCells();j++)
                {
                    Cell currentCell = currentRow.getCell(j);
                    System.out.print(currentCell.getStringCellValue() + "\t");

                }
                System.out.println("\n");

            }

            }

        catch (Exception e)
        {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    /**
     * perform delete operations on a workbook
     * @param sheetName : String : sheet name
     * @throws Throwable
     */

    public static void deleteFromXL(String sheetName){
        System.out.println("Deleting data from Random.xlsx.....");
        XSSFSheet sheet = workbook.getSheet(sheetName);
        System.out.println("removing Row 1....");
        sheet.removeRow(sheet.getRow(1));
        System.out.println("Removing column 2 from Row 0....");
        Row firstRow = sheet.getRow(0);
        firstRow.removeCell(firstRow.getCell(2));
        System.out.println("Printing the contents of Random.xlsx now.....");

        for(int i=0;i<sheet.getPhysicalNumberOfRows();i++)
        {
            Row currentRow = sheet.getRow(i);
            for(int j=0;j<currentRow.getPhysicalNumberOfCells();j++)
            {
                Cell currentCell = currentRow.getCell(j);
                System.out.print(currentCell.getStringCellValue() + "\t");

            }

        }
    }


    /**
     *
     * @param sheetName : String : sheet name
     * @throws Throwable
     */
    public static void getWorksheet(String sheetName){
        XSSFSheet sheet = workbook.getSheet(sheetName);
    }

    /**
     *access the rows and cells
     * @param sheetName : String : sheet name
     * @throws Throwable
     */
    public static String getCell(String sheetName,int row, int column){
        XSSFSheet sheet = workbook.getSheet(sheetName);
        String cellValue="";
        Row currentRow = sheet.getRow(row);
        Cell currentCell = currentRow.getCell(column);
        cellValue = currentCell.getStringCellValue();
        return cellValue;
    }
    /** convert the sheet data into hash with keys as column headers
     * @param filename : String : excel workbook name
     */
    public static HashMap<String,HashMap<String,HashMap<String, String>>> readXL2Hash(String filename){
            getXLHandler(filename);
            PageObject pObj = new PageObject();
            xlData=new HashMap<>();
            for(int k=0;k<workbook.getNumberOfSheets();k++) {
                XSSFSheet sheet = workbook.getSheetAt(k);
                Row HeaderRow = sheet.getRow(0);
                HashMap<String,HashMap<String, String>> sheetData=new HashMap<String,HashMap<String, String>>();
                for (int i = 1; i < sheet.getPhysicalNumberOfRows()-1; i++) {
                    Row currentRow = sheet.getRow(i);
                    HashMap<String, String> currentHash = new HashMap<String, String>();
                    for (int j = 2; j < 11; j++) {
                        Cell currentCell = currentRow.getCell(j);
                        if (currentCell == null)
                            continue;
                        String x = currentCell.getStringCellValue();
                        switch (currentCell.getCellType()) {
                            case Cell.CELL_TYPE_BLANK:
                                continue;
                            case Cell.CELL_TYPE_STRING:
                                String key=HeaderRow.getCell(j).getStringCellValue().toLowerCase();
                                String value=currentCell.getStringCellValue();
                                currentHash.put(key, value);
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                key=HeaderRow.getCell(j).getStringCellValue().toLowerCase();
                                currentHash.put(key, String.valueOf(currentCell.getNumericCellValue()));
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                key=HeaderRow.getCell(j).getStringCellValue().toLowerCase();
                                currentHash.put(key, String.valueOf(currentCell.getBooleanCellValue()));
                                break;
                        }

                    }
                    sheetData.put(currentRow.getCell(1).getStringCellValue().toLowerCase(), currentHash);
                }

                xlData.put(sheet.getSheetName().toLowerCase(),sheetData);
            }
            return xlData;
        }

    /** convert the sheet data into hash with keys as column headers
     * @param filename : String : excel workbook name
     */
    public static HashMap<String, PageObject> readXL2FlatHash(String filename){
        getXLHandler(filename);
        HashMap<String, PageObject> flatData=new HashMap<>();
        for(int k=0;k<workbook.getNumberOfSheets();k++) {
            XSSFSheet sheet = workbook.getSheetAt(k);
            String sheetname=sheet.getSheetName();
            Row HeaderRow = sheet.getRow(0);
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row currentRow = sheet.getRow(i);
                for (int j = 2; j < 11; j++) {
                    Cell currentCell = currentRow.getCell(j);
                    if (currentCell == null)
                        continue;
                    String x = currentCell.getStringCellValue();
                    switch (currentCell.getCellType()) {
                        case Cell.CELL_TYPE_BLANK:
                            continue;
                        case Cell.CELL_TYPE_STRING:
                            String key=HeaderRow.getCell(j).getStringCellValue().toLowerCase();
                            String value=currentCell.getStringCellValue();
                            PageObject pObjString = new PageObject();
                            pObjString.setType(key);
                            pObjString.setAccess_name(value);
                            flatData.put(sheet.getSheetName().toLowerCase()+"."+currentRow.getCell(1).getStringCellValue().toLowerCase(),pObjString);
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            key=HeaderRow.getCell(j).getStringCellValue().toLowerCase();
                            PageObject pObjnum = new PageObject();
                            pObjnum.setType(key);
                            pObjnum.setAccess_name(String.valueOf(currentCell.getNumericCellValue()));
                            flatData.put(sheet.getSheetName().toLowerCase()+"."+currentRow.getCell(1).getStringCellValue().toLowerCase(),pObjnum);
                            break;
                    }

                }
            }

        }
        return flatData;
    }

    /*@When("^I fill the form with data from excel$")
    public static void XLFillForm(){
        WebDriver driver = DriverUtil.driver;

        XSSFSheet sheet = workbook.getSheet("Sheet1");

        driver.findElement(By.name("firstname")).sendKeys(sheet.getRow(1).getCell(0).getStringCellValue());
        driver.findElement(By.name("lastname")).sendKeys(sheet.getRow(1).getCell(1).getStringCellValue());
        driver.findElement(By.id("sex-1")).click();
        driver.findElement(By.id("exp-2")).click();
        driver.findElement(By.id("datepicker")).sendKeys(sheet.getRow(1).getCell(4).getStringCellValue());
        driver.findElement(By.id("tea3")).click();
        driver.findElement(By.id("tool-1")).click();
        Select continents_select = new Select(driver.findElement(By.id("continents")));
        continents_select.selectByVisibleText(sheet.getRow(1).getCell(7).getStringCellValue());
        Select another_select_list = new Select(driver.findElement(By.id("selenium_commands")));
        another_select_list.selectByVisibleText(sheet.getRow(1).getCell(8).getStringCellValue());
    }*/


}
