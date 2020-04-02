package com.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.utils.DateUtils;
import com.baosight.common.utils.FileUtils;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baosight.common.utils.RedisUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import redis.clients.jedis.Jedis;

/**
 * 新的Excel下载Controller。
 * 提供进度条功能
 * 提供是否缓存下载数据功能。
 */
public class NewDownloadExcelController {

    private static final Logger logger = LoggerFactory.getLogger(NewDownloadExcelController.class);



    @Autowired
    private RedisUtils redisUtils;

    public NewDownloadExcelController() {
    }

    /**
     *
     * @param request
     * @param response
     * @param ajaxParam
     * @param cache 是否缓存下载数据。如果缓存。
     *              存储Key为(username+_+downloadColumn).toBytes()
     * @throws IOException
     */
    public void download(HttpServletRequest request, HttpServletResponse response, String ajaxParam,boolean cache) throws IOException {
        try {
            JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
            String downloadColumnDesc = ajaxParamObj.getString("downloadColumnDesc");
            String downloadColumn = ajaxParamObj.getString("downloadColumn");
            JSONArray downloadDataLst = ajaxParamObj.getJSONArray("downloadData");
            JSONObject columnSelectMapObj = ajaxParamObj.getJSONObject("columnSelectMap");
            JSONObject columnImageMapObj = ajaxParamObj.getJSONObject("columnImageMap");
            String[] downloadColumnDescLst = downloadColumnDesc.split(",");
            String[] downloadColumnLst = downloadColumn.split(",");
            FileInputStream fileInput = FileUtils.getFileInput("df", "download.xls");
            new HSSFWorkbook();
            HSSFWorkbook workbook = (HSSFWorkbook)WorkbookFactory.create(fileInput);
            CellStyle style = workbook.createCellStyle();
            style.setAlignment((short)2);
            style.setFillPattern((short)1);
            style.setFillForegroundColor((short)22);
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setAlignment((short)2);
            HSSFSheet sheet = workbook.getSheet("data");
            HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
            fileInput.close();
            Row row = sheet.createRow(0);

            int i;
            for(i = 0; i < downloadColumnDescLst.length; ++i) {
                Cell cell = row.createCell(i);
                cell.setCellType(1);
                cell.setCellValue(downloadColumnDescLst[i]);
                cell.setCellStyle(style);
                sheet.autoSizeColumn(i, true);
            }

            for(i = 0; i < downloadDataLst.size(); ++i) {
                JSONObject dData = downloadDataLst.getJSONObject(i);
                Row addRow = sheet.createRow(i + 1);

                for(int j = 0; j < downloadColumnLst.length; ++j) {
                    Cell addCell = addRow.createCell(j);
                    addCell.setCellStyle(dataStyle);
                    String cellValue;
                    if (!columnSelectMapObj.containsKey(downloadColumnLst[j])) {
                        if (dData.get(downloadColumnLst[j]) == null) {
                            addCell.setCellValue("");
                        } else if (columnImageMapObj.containsKey(downloadColumnLst[j])) {
                            cellValue = dData.get(downloadColumnLst[j]) + "";
                            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();

                            try {
                                BufferedImage bufferImg = ImageIO.read(new URL(cellValue));
                                ImageIO.write(bufferImg, "png", byteArrayOut);
                                HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short)j, i + 1, (short)j, i + 1);
                                patriarch.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), 6));
                                sheet.autoSizeColumn(i, true);
                            } catch (MalformedURLException var40) {
                                ;
                            } catch (Exception var41) {
                                var41.printStackTrace();
                            }
                        } else {
                            addCell.setCellValue(dData.get(downloadColumnLst[j]) + "");
                        }
                    } else {
                        cellValue = dData.get(downloadColumnLst[j]) + "";
                        JSONObject selectLabelValue = columnSelectMapObj.getJSONObject(downloadColumnLst[j]);
                        Set selectSet = selectLabelValue.entrySet();
                        Iterator selectIterator = selectSet.iterator();

                        while(selectIterator.hasNext()) {
                            Entry<String, Object> selectEnter = (Entry)selectIterator.next();
                            if (cellValue.equals(selectEnter.getKey())) {
                                cellValue = selectEnter.getValue() + "";
                                break;
                            }
                        }

                        addCell.setCellValue(cellValue);
                    }

                    sheet.autoSizeColumn(j, true);
                }
            }

            String fileName = "download" + DateUtils.date2String("yyyyMMddHHmmss", new Date());
            ByteArrayOutputStream os = new ByteArrayOutputStream();

            try {
                workbook.write(os);
            } catch (IOException var39) {
                var39.printStackTrace();
                logger.error(var39.toString());
            }

            byte[] content = os.toByteArray();
            if(cache){//如果缓存。将byte数组写入redisUtil中。
                Jedis jedis = redisUtils.getJedis();
                String username = request.getSession().getAttribute("username").toString();
                jedis.set((username+"_"+downloadColumn).getBytes(),content);
            }
            InputStream is = new ByteArrayInputStream(content);
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xls").getBytes(), "utf-8"));
            ServletOutputStream out = response.getOutputStream();
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;

            try {


                bis = new BufferedInputStream(is);
                bos = new BufferedOutputStream(out);
                byte[] buff = new byte[5120];

                //缓存
                int total = bis.available();
                redisUtils.set(request.getSession().getAttribute("username").toString()+"_"+"downloadTotal",bis.available()+"");
                Integer len = 0;
                int bytesRead;
                while(-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    len += bytesRead;
                    redisUtils.set(request.getSession().getAttribute("username").toString()+"_"+"downloadCurrent",len.toString());
                    redisUtils.set(request.getSession().getAttribute("username").toString()+"_"+"downloadProgress",Math.round((len*1.0/total*1.0)*100)+"");
                    bos.write(buff, 0, bytesRead);
                }
            } catch (IOException var42) {
                throw var42;
            } finally {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }

            }
        } catch (FileNotFoundException var44) {
            var44.printStackTrace();
            logger.error(var44.toString());
        } catch (EncryptedDocumentException var45) {
            var45.printStackTrace();
            logger.error(var45.toString());
        } catch (InvalidFormatException var46) {
            var46.printStackTrace();
            logger.error(var46.toString());
        } catch (IOException var47) {
            var47.printStackTrace();
            logger.error(var47.toString());
        }

    }



}
