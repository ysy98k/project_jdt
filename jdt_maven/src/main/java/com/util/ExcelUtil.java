package com.util;


import com.raising.backstage.controller.SectionManageController;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;



import org.apache.poi.hssf.converter.ExcelToHtmlConverter;


/**
 * excel工具类
 */

public class ExcelUtil {

    private static final Logger logger = LoggerFactory
            .getLogger(SectionManageController.class);

    //读取excel
    public static Workbook readExcel(String filePath) {
        Workbook wb = null;
        if (filePath == null) {
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if (".xls".equals(extString)) {
                return wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(extString)) {
                return wb = new XSSFWorkbook(is);
            } else {
                return wb = null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }

    public static Workbook readExcel(File file) {
        try {
            if (file != null) {
                return new XSSFWorkbook(new FileInputStream(file));
            } else {
                return null;
            }
        } catch (FileNotFoundException e) {
            logger.info("File Not Found, when upload xlsx file.");
        } catch (IOException e) {
            logger.info("IO error, when upload xlsx file.");
        }
        return null;
    }

    /**
     * 得到单元格内容
     * @param cell
     * @return
     */
    public static Object getCellFormatValue(Cell cell) {
        Object cellValue = null;
        if (cell != null) {
            //判断cell类型
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC: {
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case Cell.CELL_TYPE_FORMULA: {
                    //判断cell是否为日期格式
                    if (DateUtil.isCellDateFormatted(cell)) {
                        //转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    } else {
                        //数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING: {
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        } else {
            cellValue = "";
        }
        return cellValue;
    }


    private static String convertExceltoHtmlUtil(String path) throws IOException,ParserConfigurationException, TransformerException,InvalidFormatException {
        HSSFWorkbook workBook = null;
        String content = null;
        StringWriter writer = null;
        File excelFile = new File(path);
        InputStream is = new FileInputStream(excelFile);;
        //判断Excel文件是2003版还是2007版
        String suffix = path.substring(path.lastIndexOf("."));
        if(suffix.equals(".xlsx")){
            //将07版转化为03版
            Xssf2Hssf xlsx2xls = new Xssf2Hssf();
            XSSFWorkbook xSSFWorkbook = new XSSFWorkbook(is);
            workBook = new HSSFWorkbook();
            xlsx2xls.transformXSSF(xSSFWorkbook, workBook);

        }else{
            workBook = new HSSFWorkbook(is);
        }
        try {
            ExcelToHtmlConverter converter = new ExcelToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
            converter.setOutputColumnHeaders(false);// 不显示列的表头
            converter.setOutputRowNumbers(false);// 不显示行的表头
            converter.processWorkbook(workBook);



            writer = new StringWriter();
            Transformer serializer = TransformerFactory.newInstance().newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(new DOMSource(converter.getDocument()),new StreamResult(writer));


            content = writer.toString();
            writer.close();

        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    public static void convertExceltoHtml(String resoucePath,String outPutPath) throws ParserConfigurationException, TransformerException, InvalidFormatException, IOException {
        String htmlContent = convertExceltoHtmlUtil(resoucePath);

        FileOutputStream fos = null;
        BufferedWriter bw = null;
        try {
            File file = new File(outPutPath);
            if(!file.getParentFile().exists()){//如果 父目录不存在，则创建父目录。避免找不到指定路径异常
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
            bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
            bw.write(htmlContent);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fos != null)
                    fos.close();
            } catch (IOException ie) {
            }
        }
    }




}
