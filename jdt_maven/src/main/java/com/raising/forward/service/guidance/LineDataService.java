package com.raising.forward.service.guidance;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.entity.GridData;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.constants.Constants;
import com.baosight.common.excel.ExcelLib;
import com.baosight.common.utils.StringUtils;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonAnyFormatVisitor;
import com.raising.forward.entity.CoordinateInfo;
import com.raising.forward.entity.LineData;
import com.raising.forward.service.PropertiesValue;
import com.util.DateUtils;
import com.util.ExcelUtil;
import com.util.UploadUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.baosight.common.utils.DateUtils.String2Date;
import static com.baosight.common.utils.DateUtils.date2String;


@Service("lineDataService")
@Transactional
public class LineDataService extends BaseService {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;



    /**
     * 得到表格显示数据
     * @param projectIdStr
     * @return
     */
    public JSONObject getTableData(String projectIdStr){
        JSONObject returnInfo = new JSONObject();
        int projectId = Integer.parseInt(projectIdStr);

        //查询蓝图表中数据
        String dawingSql = "com.raising.forward.mapper.Drawing.getRows";
        JSONObject paramInfo = new JSONObject();
        paramInfo.put("tenant",request.getSession().getAttribute("tenant"));
        paramInfo.put("projectId",projectId);
        List<JSONObject> drawingList = sqlSessionTemplate.selectList(dawingSql, paramInfo);
        List<JSONObject> LineList = new ArrayList<>();
        List<Integer> drawingIds = new ArrayList<>();
        if(drawingList != null && drawingList.size() > 0){
            for(int i =0;i<drawingList.size();i++){
                JSONObject temp = drawingList.get(i);
                drawingIds.add(temp.getInteger("drawingId"));
                Timestamp creatrTime = temp.getTimestamp("createTime");
                temp.put("createTime", date2String("yyyy-MM-dd HH:mm:ss", new Date(creatrTime.getTime())));
            }
        }
        if(drawingIds.size() > 0){
            JSONObject lineParam = new JSONObject();
            lineParam.put("tenant",request.getSession().getAttribute("tenant"));
            lineParam.put("projectId",projectId);
            lineParam.put("drawingIds",drawingIds);
            //查询线型表中数据
            String LineSql = "com.raising.forward.mapper.LineCheckInfo.getRows2";
            LineList = sqlSessionTemplate.selectList(LineSql, lineParam);
            if(LineList != null && LineList.size() > 0){
                for(int i =0;i<LineList.size();i++){
                    JSONObject temp = LineList.get(i);
                    Timestamp creatrTime = temp.getTimestamp("createTime");
                    temp.put("createTime", date2String("yyyy-MM-dd HH:mm:ss", new Date(creatrTime.getTime())));
                }
            }
        }
        returnInfo.put("status",Constants.EXECUTE_SUCCESS);
        returnInfo.put("drawingList",drawingList);
        returnInfo.put("LineList",LineList);
        return returnInfo;
    }

    public JSONObject getLineTable(String ajaxParam){
        JSONObject ajaxObj = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = new JSONObject();

        Cookie[] cookies = request.getCookies();
        Integer projectId  = null;
        for (Cookie c : cookies) {
            if (c.getName().equals("selected_id")) {
                projectId = Integer.parseInt(c.getValue());
            }
        }
        if(projectId == null){
            returnInfo.put("message","请指定当前项目！");
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            return returnInfo;
        }

        Integer drawingId = ajaxObj.getInteger("drawingId");
        JSONObject paramInfo = new JSONObject();
        paramInfo.put("tenant",request.getSession().getAttribute("tenant"));
        paramInfo.put("projectId",projectId);
        paramInfo.put("drawingId",drawingId);

        //查询线型表中数据
        String LineSql = "com.raising.forward.mapper.LineCheckInfo.getRows";
        List<JSONObject> LineList = sqlSessionTemplate.selectList(LineSql, paramInfo);

        if(LineList != null && LineList.size() > 0){
            for(int i =0;i<LineList.size();i++){
                JSONObject temp = LineList.get(i);
                Timestamp creatrTime = temp.getTimestamp("createTime");
                temp.put("createTime", date2String("yyyy-MM-dd HH:mm:ss", new Date(creatrTime.getTime())));
            }
        }
        returnInfo.put("status",Constants.EXECUTE_SUCCESS);
        returnInfo.put("LineList",LineList);
        return returnInfo;
    }

    public JSONObject compare(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        List<JSONObject>  dataList = null;
        JSONObject ajaxObj = JSONObject.parseObject(ajaxParam);
        JSONObject paramJson = setQueryParam(ajaxObj);
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        if(paramJson == null){
            return returnInfo;
        }
        String sql = paramJson.getString("sql");
        String countSql = paramJson.getString("countSql");
        dataList = sqlSessionTemplate.selectList(sql,paramJson);
        int count = sqlSessionTemplate.selectOne(countSql,paramJson);

        for(int i =0;i<dataList.size();i++){
            JSONObject temp = dataList.get(i);
            temp.put("x",decimalFormat.format(temp.getDoubleValue("x")));
            temp.put("y",decimalFormat.format(temp.getDoubleValue("y")));
            temp.put("z",decimalFormat.format(temp.getDoubleValue("z")));
            temp.put("designX",decimalFormat.format(temp.getDoubleValue("designX")));
            temp.put("designY",decimalFormat.format(temp.getDoubleValue("designY")));
            temp.put("designZ",decimalFormat.format(temp.getDoubleValue("designZ")));
            temp.put("dx",decimalFormat.format(temp.getDoubleValue("dx")));
            temp.put("dy",decimalFormat.format(temp.getDoubleValue("dy")));
            temp.put("dz",decimalFormat.format(temp.getDoubleValue("dz")));
        }

        Integer pageSize = ajaxObj.getInteger("curRowNum");
        if(pageSize == null){
            pageSize = 10;
        }
        int total = count% pageSize == 0 ? count/pageSize : count/pageSize +1;

        Integer projectId = paramJson.getInteger("projectId");
        JSONObject checkResult = checkDesignData(projectId);
        if(checkResult.getBoolean("result") == false){
            returnInfo.put("returnMsg",checkResult.getString("message")+"查询成功！本次返回" + dataList.size() + "条记录，总共" + count+ "条记录！");
            returnInfo.put("status",Constants.EXECUTE_FAIL);
        }else{
            returnInfo.put("returnMsg", "查询成功！本次返回" + dataList.size() + "条记录，总共" + count+ "条记录！");
            returnInfo.put("status",Constants.EXECUTE_SUCCESS);
        }

        returnInfo.put("page",ajaxObj.getInteger("curPage"));
        returnInfo.put("repeatitems","false");
        returnInfo.put("total", total);


        returnInfo.put("rows", dataList);
        returnInfo.put("records", count);
        return returnInfo;
    }

    public JSONObject saveBlueprint(MultipartFile file,String info,String projectName) throws Exception {
        JSONObject returnInfo = new JSONObject();
        Cookie[] cookies = request.getCookies();
        Integer projectId  = null;
        for (Cookie c : cookies) {
            if (c.getName().equals("selected_id")) {
                projectId = Integer.parseInt(c.getValue());
            }
        }
        if(projectId == null){
            returnInfo.put("message","请指定当前项目！");
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            return returnInfo;
        }
        String path = PropertiesValue.UPLOAD_FILE_PATH+File.separator+"line_data"+File.separator+"blue_print"+File.separator+projectName;
        //String path =request.getSession().getServletContext().getRealPath("WEB-INF/uploadfiles/lineData/"+projectName);
        returnInfo = UploadUtil.resolveCompressUploadFile(file,path,50);
        if(Constants.EXECUTE_SUCCESS.equals(returnInfo.getString("status"))){
            String sql = "com.raising.forward.mapper.Drawing.insert";
            JSONObject paramInfo = new JSONObject();
            paramInfo.put("tenant",request.getSession().getAttribute("tenant"));
            paramInfo.put("projectId",projectId);
            paramInfo.put("drawingName", file.getOriginalFilename());//文件原名);
            paramInfo.put("info",info);
            paramInfo.put("filePath",returnInfo.getString("filePath"));
            Date date = new java.util.Date(System.currentTimeMillis());
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            paramInfo.put("createTime",String2Date(new String(simpleDateFormat.format(date)), "yyyy-MM-dd HH:mm:ss"));
            paramInfo.put("updateTime",String2Date(new String(simpleDateFormat.format(date)), "yyyy-MM-dd HH:mm:ss"));
            int insert = sqlSessionTemplate.insert(sql, paramInfo);
            if(insert < 0){
                throw new Exception("项目上传失败");
            }
            returnInfo.put("message","文件上传成功");
            returnInfo.put("status",Constants.EXECUTE_SUCCESS);
        }
        return  returnInfo;
    }

    public JSONObject saveLine(MultipartFile lineFile, LineData lineData) throws Exception {
        JSONObject returnInfo = new JSONObject();
        JSONObject mileageInterval = getMileageInterval(lineData.getProjectId());//获取有效里程区间
        JSONObject checkJson = checkLineExcel(lineFile, mileageInterval);//如果值不为null。则将此集合插入coordinatelist表中
        if(Constants.EXECUTE_FAIL.equals(checkJson.getString("status"))){
            return checkJson;
        }
        List<JSONObject> excelList = checkJson.getObject("excelList",List.class);


        String sql = "com.raising.forward.mapper.LineCheckInfo.insert";
        Date date = new java.util.Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        lineData.setReview("未复核");
        lineData.setTenant(request.getSession().getAttribute("tenant").toString());
        lineData.setCreateTime(String2Date(new String(simpleDateFormat.format(date)), "yyyy-MM-dd HH:mm:ss"));
        lineData.setUpdateTime(String2Date(new String(simpleDateFormat.format(date)), "yyyy-MM-dd HH:mm:ss"));
        int insert = sqlSessionTemplate.insert(sql, lineData);//增加线性表记录
        Integer lineId = lineData.getLineId();
        if(lineId < 0){
            throw new Exception();
        }
        excelList = fillExcelList(excelList,lineData.getProjectId(),mileageInterval.getFloatValue("startMileage"));
        String coordinatelistSql = "com.raising.forward.mapper.CoordinateInfo.addRows";
        JSONObject coordJson = new JSONObject();
        coordJson.put("tenant",request.getSession().getAttribute("tenant"));
        coordJson.put("lineId",lineId);
        coordJson.put("dataList",excelList);
        insert = sqlSessionTemplate.insert(coordinatelistSql, coordJson);//增加线性表记录
        if(insert < 0){
            throw new Exception();
        }
        returnInfo.put("message","文件上传成功");
        returnInfo.put("status",Constants.EXECUTE_SUCCESS);

        return  returnInfo;
    }

    public void download(String fileName,String filePath,HttpServletResponse response) throws IOException {
        UploadUtil.download(response,fileName,filePath);
    }

    public void downloadLineInfo(Integer lineId,HttpServletResponse response) throws UnsupportedEncodingException {
        JSONObject paramJson = new JSONObject();
        CoordinateInfo obj = new CoordinateInfo();
        obj.setLineId(lineId);
        obj.setTenant(request.getSession().getAttribute("tenant").toString());
        String sql = "com.raising.forward.mapper.CoordinateInfo.getRows";
        List<JSONObject> dataList = sqlSessionTemplate.selectList(sql, obj);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/x-download");

        String fileName = "表格.xlsx";
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        // 第一步：定义一个新的工作簿
        XSSFWorkbook wb = new XSSFWorkbook();
        // 第二步：创建一个Sheet页
        XSSFSheet sheet = wb.createSheet("sheet1");
        sheet.setDefaultRowHeight((short) (2 * 256));//设置行高
        XSSFRow row = sheet.createRow(0); //设置第一行
        XSSFFont font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 16);

        XSSFCell cell;
        String[] columnName = {"里程","x","y","z"};
        for(int i =0 ;i<columnName.length;i++){
            sheet.setColumnWidth(i, 5500);//设置列宽
            cell = row.createCell(i);
            cell.setCellValue(columnName[i]);
        }

        XSSFRow rows;//行
        XSSFCell cells;//单元格
        for (int i = 0; i < dataList.size(); i++) {
            // 第三步：在这个sheet页里创建一行
            rows = sheet.createRow(i+1);
            JSONObject temp = dataList.get(i);
            // 第四步：在该行创建一个单元格
            cells = rows.createCell(0);
            // 第五步：在该单元格里设置值
            cells.setCellValue(dataList.get(i).getString("mileage"));

            cells = rows.createCell(1);
            cells.setCellValue(dataList.get(i).getString("x"));
            cells = rows.createCell(2);
            cells.setCellValue(dataList.get(i).getString("y"));
            cells = rows.createCell(3);
            cells.setCellValue(dataList.get(i).getString("z"));
        }

        try {
            OutputStream out = response.getOutputStream();
            wb.write(out);
            out.close();
            wb.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public JSONObject delete(String ajaxParam){
        JSONObject returnInfo = new JSONObject();
        JSONObject paramJson = JSONObject.parseObject(ajaxParam);
        if(paramJson.containsKey("drawingIds")){
            JSONArray arr = paramJson.getJSONArray("drawingIds");
            for(int i=0;i<arr.size();i++){
                Integer tempId = arr.getInteger(i);
                String sql = "com.raising.forward.mapper.Drawing.delete";
                JSONObject paramInfo = new JSONObject();
                paramInfo.put("tenant",request.getSession().getAttribute("tenant"));
                paramInfo.put("drawingId",tempId);
                sqlSessionTemplate.delete(sql,paramInfo);
            }
        }else if(paramJson.containsKey("lineIds")){
            JSONArray arr = paramJson.getJSONArray("lineIds");
            for(int i=0;i<arr.size();i++){
                Integer tempId = arr.getInteger(i);
                String sql = "com.raising.forward.mapper.LineCheckInfo.delete";
                JSONObject paramInfo = new JSONObject();
                paramInfo.put("tenant",request.getSession().getAttribute("tenant"));
                paramInfo.put("lineId",tempId);
                sqlSessionTemplate.delete(sql,paramInfo);
            }
        }
        returnInfo.put("status",Constants.EXECUTE_SUCCESS);
        returnInfo.put("message","删除记录成功！");
        return returnInfo;
    }

    public List<JSONObject> getDrawing(String projectIdStr){
        List<JSONObject> returnInfo = new ArrayList<>();
        Integer projectId = Integer.parseInt(projectIdStr);
        if(projectId == null){
            return returnInfo;
        }
        String DrawingSql = "com.raising.forward.mapper.Drawing.getRows";
        JSONObject paramInfo = new JSONObject();
        paramInfo.put("tenant",request.getSession().getAttribute("tenant"));
        paramInfo.put("projectId",projectId);
        returnInfo = sqlSessionTemplate.selectList(DrawingSql, paramInfo);
        return returnInfo;
    }

    public String review(String ajaxParam){
        JSONObject paramJson = JSONObject.parseObject(ajaxParam);
        paramJson.put("tenant",request.getSession().getAttribute("tenant").toString());
        paramJson.put("lineId",paramJson.getInteger("lineId"));
        String sql = "com.raising.forward.mapper.LineCheckInfo.update";
        int result = sqlSessionTemplate.update(sql, paramJson);
        if(result > 0){
            return Constants.EXECUTE_SUCCESS;
        }
        return Constants.EXECUTE_FAIL;
    }

    public JSONArray downloadCompareResult(Integer lineId){
        JSONArray returnInfo = null;
        JSONObject paramJson = new JSONObject();
        paramJson.put("tenant",request.getSession().getAttribute("tenant"));
        paramJson.put("lineId",lineId);
        String sql = "com.raising.forward.mapper.LineCheckInfo.compare";
        List<JSONObject> data = sqlSessionTemplate.selectList(sql, paramJson);
        returnInfo = JSON.parseArray(JSONObject.toJSONString(data));
        return returnInfo;
    }

    //检查线性表
    private JSONObject checkLineExcel(MultipartFile lineFile,JSONObject mileageInterval){
        JSONObject returnInfo = new JSONObject();
        //里程间隔。作为校验上传线性表的基数。
        List<JSONObject> excelList = getExcelList(lineFile);//上传数据表中的数据
        if(excelList == null || excelList.size() < 1){
            return null;
        }
        float startMileage = mileageInterval.getFloat("startMileage");
        float endMileage = mileageInterval.getFloat("endMileage");
        float small = excelList.get(0).getFloat("mileage");
        float big = excelList.get(excelList.size()-1).getFloat("mileage");
        if(small > startMileage ){
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            returnInfo.put("message","最小里程大于起始里程不可上传。当前项目里程区间为["+startMileage+","+endMileage+"]");
            return returnInfo;
        }
        if(endMileage > big){
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            returnInfo.put("message","最大里程小于结束里程，不可上传。当前项目里程区间为["+startMileage+","+endMileage+"]");
            return returnInfo;
        }
        returnInfo.put("status",Constants.EXECUTE_SUCCESS);
        returnInfo.put("excelList",excelList);
        return returnInfo;
    }


    private List<JSONObject> getExcelList(MultipartFile lineFile){
        String oldFileName = lineFile.getOriginalFilename();//文件原名
        String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀

        Workbook wb = null;
        InputStream is = null;
        try {
            is = lineFile.getInputStream();
            if("xls".equals(prefix)){
                wb = new HSSFWorkbook(is);
            }else if("xlsx".equals(prefix)){
                wb = new XSSFWorkbook(is);
            }else{
                return null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //得到Workbook 结束
        if(wb == null){
            return null;
        }
        String columns[] = {"mileage","x","y","z"};
        boolean isStart = false;//是否是开始
        boolean isEnd = false ;//是否是结束
        List<JSONObject> dataList = new ArrayList<JSONObject>();
        //获取第一个sheet
        Sheet sheet = wb.getSheetAt(0);
        //获取最大行数
        int rownum = sheet.getPhysicalNumberOfRows();
        //获取第一行
        Row row = sheet.getRow(0);
        //获取最大列数
        int colnum = row.getPhysicalNumberOfCells();
        for (int i = 0; i<rownum; i++) {
            JSONObject dataJson = new JSONObject();
            row = sheet.getRow(i);
            if(isEnd || row == null){
                break;
            }
            for (int j=0;j<colnum;j++){
                String cellData = (String) ExcelUtil.getCellFormatValue(row.getCell(j));
                if("里程".equals(cellData) ){
                    isStart = true;
                    break;
                }
                if(j == 0 && StringUtils.isNullOrEmpty(cellData)){
                    String cellData2 = (String) ExcelUtil.getCellFormatValue(row.getCell(1));
                    String cellData3 = (String) ExcelUtil.getCellFormatValue(row.getCell(2));
                    String cellData4 = (String) ExcelUtil.getCellFormatValue(row.getCell(3));
                    if(StringUtils.isNullOrEmpty(cellData2) && StringUtils.isNullOrEmpty(cellData3) && StringUtils.isNullOrEmpty(cellData4)){
                        isEnd = true;
                        break;
                    }
                }
                if(isStart){
                    dataJson.put(columns[j], Double.parseDouble(cellData));
                }
            }

            if(isStart&& !StringUtils.isNullOrEmpty(dataJson.getString("mileage"))){
                dataList.add(dataJson);
            }

        }

        return dataList;
    }


    private JSONObject setQueryParam(JSONObject ajaxJson){
        JSONObject returnInfo = new JSONObject();

        Integer projectId = ajaxJson.getInteger("cookieId");
        JSONArray array = ajaxJson.getJSONArray("lineArray");
        Boolean display = ajaxJson.getBoolean("display");
        Integer errorSelect = ajaxJson.getInteger("errorSelect");
        Integer currentPage = ajaxJson.getInteger("curPage");
        Integer pageSize = ajaxJson.getInteger("curRowNum");
        Integer pageIndex = (currentPage - 1) * pageSize;

        if(array == null || array.size() < 1){
            return returnInfo;
        }
        returnInfo.put("tenant",request.getSession().getAttribute("tenant"));
        returnInfo.put("projectId",projectId);
        returnInfo.put("display",display);
        returnInfo.put("error",errorSelect);
        returnInfo.put("pageIndex",pageIndex);
        returnInfo.put("pageSize",pageSize);

        Integer lineId = array.getInteger(0);

        returnInfo.put("lineId",lineId);
        returnInfo.put("sql","com.raising.forward.mapper.LineCheckInfo.compare");
        returnInfo.put("countSql","com.raising.forward.mapper.LineCheckInfo.compareCount");
        return  returnInfo;
    }

    private JSONObject getMileageInterval(Integer projectId){
        JSONObject returnInfo = new JSONObject();
        String sql = "com.raising.forward.mapper.ProjectInfoMapper.getRowsForBackstage";
        JSONObject paramJson = new JSONObject();
        paramJson.put("tenant",request.getSession().getAttribute("tenant").toString());
        paramJson.put("projectId",projectId);
        JSONObject project = sqlSessionTemplate.selectOne(sql, paramJson);
        String startMileage = project.getString("startMileage");
        String endMileage = project.getString("endMileage");
        returnInfo.put("startMileage",startMileage);
        returnInfo.put("endMileage",endMileage);
        return returnInfo;
    }

    private List<JSONObject> fillExcelList(List<JSONObject> excelList,Integer projectId,float startMileage){
        String sql = "DesignLine.query";
        JSONObject paramJson  = new JSONObject();
        paramJson.put("tenant",request.getSession().getAttribute("tenant"));
        paramJson.put("cookieId",projectId);
        List<JSONObject> designList = sqlSessionTemplate.selectList(sql, paramJson);

        int designStartIndex = 0;//设计List，起始里程记录所在的下标
        int excelMileageStartIndex = 0;//excel，起始里程记录所在的下标。

        for(int i = 0;i<excelList.size();i++){
            JSONObject excelTemp = excelList.get(i);
            double tempFloat = excelTemp.getDoubleValue("mileage");
            if(tempFloat >= startMileage){
                excelMileageStartIndex = i;
                break;
            }
        }
        for(int i =0;i<designList.size();i++){
            JSONObject design = designList.get(i);
            double designTemp =  design.getDoubleValue("designmileage");
            if(designTemp >= startMileage){
                designStartIndex = i;
                break;
            }
        }
        int designZero = designStartIndex - excelMileageStartIndex;
        //for循环填充记录
        for(int i = 0;i<excelList.size();i++){
            JSONObject excelTemp = excelList.get(i);
            JSONObject designTemp = null;
            if(designZero < 0 || designZero >= designList.size()){
                excelTemp.put("designMileage",null);
                excelTemp.put("designX",null);
                excelTemp.put("designY",null);
                excelTemp.put("designZ",null);
            }else if(designZero < designList.size()){
                excelTemp.put("designMileage",designList.get(designZero).getDoubleValue("designmileage"));
                excelTemp.put("designX",designList.get(designZero).getDoubleValue("x"));
                excelTemp.put("designY",designList.get(designZero).getDoubleValue("y"));
                excelTemp.put("designZ",designList.get(designZero).getDoubleValue("z"));
            }
            designZero++;
        }
        return excelList;
    }

    private JSONObject checkDesignData(Integer projectId){
        JSONObject returnInfo = new JSONObject();
        String sql = "DesignLine.getMaxAndMinWithProjectId";
        JSONObject paramJson  = new JSONObject();
        paramJson.put("tenant",request.getSession().getAttribute("tenant"));
        paramJson.put("projectId",projectId);
        JSONObject design = sqlSessionTemplate.selectOne(sql, paramJson);
        JSONObject projectMileageInterval = getMileageInterval(projectId);
        float designMax = design.getFloatValue("max");
        float designMin = design.getFloatValue("min");
        float startMileage = projectMileageInterval.getFloatValue("startMileage");
        float endMileage = projectMileageInterval.getFloatValue("endMileage");
        if(startMileage < designMin || endMileage > designMax){
            returnInfo.put("result",false);
            returnInfo.put("message","注意:力信导向系统数据["+designMin+","+designMax+"]不能涵盖当前项目有效区间["+startMileage+","+endMileage+"];");
            return returnInfo;
        }
        returnInfo.put("result",true);
        return returnInfo;
    }




}
