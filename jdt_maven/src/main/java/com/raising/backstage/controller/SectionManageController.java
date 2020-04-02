package com.raising.backstage.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.baosight.common.controller.DownloadExcelController;
import com.baosight.common.utils.StringUtils;
import com.raising.backstage.service.SectionManageService;
import com.raising.forward.service.CodeItemService;
import com.util.CSVUtils;
import com.util.MultipleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.baosight.common.utils.DateUtils.String2Date;
import static com.baosight.common.utils.DateUtils.date2String;

/**
 * 区间段Controller
 */
@Controller
@RequestMapping("/raising/backstage/sectionManage.do")
public class SectionManageController extends DownloadExcelController {

    private static final Logger logger = LoggerFactory
            .getLogger(SectionManageController.class);



    @Autowired
    private SectionManageService service;

    @Autowired
    private CodeItemService codeItemService;



    @RequestMapping(params = "method=query", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject query(String ajaxParam) {
        JSONObject returnInfo = service.query(JSONObject.parseObject(ajaxParam));

        if(Constants.EXECUTE_SUCCESS.equals(returnInfo.getString("status"))){
            //如果查询成功，则将ccsId转换成中文显示
            JSONArray dataArray = returnInfo.getJSONArray("rows");
            if(dataArray == null || dataArray.size() < 1 ){
                return returnInfo;
            }
            try{
                MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
                //循环修改ccsId值
                JSONObject one = dataArray.getJSONObject(0);
                Map<String,String> ccsSection = codeItemService.getCodeNameFromSystemCode(one.getString("ccsSectionType"));

                for(int i = 0; i<dataArray.size();i++){
                    JSONObject temp = dataArray.getJSONObject(i);
                    String createTimeStr = temp.getString("createTime");
                    if(!StringUtils.isNullOrEmpty(createTimeStr)){
                        temp.put("createTime", date2String("yyyy-MM-dd HH:mm:ss", new Date(new Long(createTimeStr))));
                    }

                    String ccsIdCode = temp.getString("ccsId");
                    String provinceCode = ccsIdCode.substring(0,ccsIdCode.indexOf("."));
                    String cityCode = ccsIdCode.substring(0,ccsIdCode.lastIndexOf("."));
                    String provinceStr = codeItemService.getCodeName(provinceCode);
                    String cityStr = codeItemService.getCodeName(cityCode);
                    String lineStr = codeItemService.getCodeName(ccsIdCode);
                    //将code和中文名拼装在一起返回前台。前台隐藏code，只显示中文名称
                    String result = ccsIdCode+"_"+provinceStr+cityStr+lineStr;

                    /*String ccsSectionType = temp.getString("ccsSectionType");
                    String sectionType = codeItemService.getCodeNameFromSystemAndItem(ccsSectionType);*/
                    String sectionType = ccsSection.get(temp.getString("ccsSectionType"));
                    temp.put("ccsId",result);
                    temp.put("ccsSectionType",sectionType);
                }
            }finally {
                //数据源切换回来，
                MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
            }
        }
        return returnInfo;
    }

    /**
     * 新增
     * @param ajaxParam
     * @return
     */
    @RequestMapping(params = "method=insert", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject insert(String ajaxParam) {
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject strDateObj = ajaxParamObj.getJSONObject("detail");
        JSONArray strDateAry = strDateObj.getJSONArray("resultRow");
        for (int i = 0; i < strDateAry.size(); i++) {
            JSONObject obj = strDateAry.getJSONObject(i);
            String strDate = obj.getString("createTime");
            obj.put("createTime", String2Date(new String(strDate), "yyyy-MM-dd HH:mm:ss"));
        }
        ajaxParamObj.put("update", false);
        JSONObject returnInfo = service.insert(ajaxParamObj);

        return returnInfo;
    }

    /**
     * 删除
     * @param ajaxParam
     * @return
     */
    @RequestMapping(params = "method=delete", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject delete(String ajaxParam) {
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject strDateObj = ajaxParamObj.getJSONObject("result");
        JSONArray strDateAry = strDateObj.getJSONArray("resultRow");
        ajaxParamObj.put("update", false);
        JSONObject returnInfo = service.delete(ajaxParamObj);
        return returnInfo;
    }

    /**
     * 更新
     * @param ajaxParam
     * @return
     */
    @RequestMapping(params = "method=update", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject update(String ajaxParam) {
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject strDateObj = ajaxParamObj.getJSONObject("detail");
        JSONArray strDateAry = strDateObj.getJSONArray("resultRow");
        for (int i = 0; i < strDateAry.size(); i++) {
            JSONObject obj = strDateAry.getJSONObject(i);
            String strDate = obj.getString("createTime");
            obj.put("createTime", String2Date(new String(strDate), "yyyy-MM-dd HH:mm:ss"));
        }
        //执行更新
        JSONObject returnInfo = null;
        try {
            returnInfo = service.update(ajaxParamObj);
        } catch (Exception e) {
            logger.error(e.getMessage());
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            returnInfo.put("returnMsg","更新出错，记录参数有误，请修改后重新操作！");
            return returnInfo;
        }
        return returnInfo;
    }

    @RequestMapping(params = "method=queryOne", method = RequestMethod.POST)
    public @ResponseBody
    JSONObject queryOne(String ajaxParam) {
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = service.queryOne(ajaxParamObj);
        return returnInfo;
    }

    @RequestMapping(params = "method=getLineTree",method = RequestMethod.GET)
    @ResponseBody
    public JSONArray getLineTree(String ajaxParam,HttpServletRequest request) {
        JSONObject ajaxParamObj = new JSONObject();

        ajaxParamObj.put("fdParentId", request.getParameter("fdParentId"));
        ajaxParamObj.put("treeparentId", request.getParameter("treeparentId"));
        JSONArray returnInfo;
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            returnInfo = codeItemService.querySubTree(ajaxParamObj);
        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }

        return returnInfo;
    }

    @RequestMapping(params = "method=initSelect",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject initSelect(String code){
        JSONObject returnInfo = new JSONObject();
        //得到项目状态的systemcode。
        List<JSONObject> sectionsList = service.initSelect();
        List<JSONObject> sectionTypesList =  null;
        List<JSONObject> linesList = new ArrayList<>();
        List<String> ownersList = new ArrayList<>();

        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            sectionTypesList = codeItemService.getCodeListFromSystemCode(code);
            if(sectionsList == null || sectionsList.size() < 1){
                returnInfo.put("haveData",Constants.EXECUTE_FAIL);

            }else{
                returnInfo.put("haveData",Constants.EXECUTE_SUCCESS);
                List<String> ccsIdsTemp = new ArrayList<>();
                for(int i =0;i<sectionsList.size();i++){
                    JSONObject section = sectionsList.get(i);
                    String owner = section.getString("owner");

                    JSONObject lineTemp = new JSONObject();
                    String ccsId = section.getString("ccsId");
                    String result = null;
                    if(!StringUtils.isNullOrEmpty(ccsId)){
                        String provinceCode = ccsId.substring(0,ccsId.indexOf("."));
                        String cityCode = ccsId.substring(0,ccsId.lastIndexOf("."));
                        String provinceStr = codeItemService.getCodeName(provinceCode);
                        String cityStr = codeItemService.getCodeName(cityCode);
                        String lineStr = codeItemService.getCodeName(ccsId);
                        //将code和中文名拼装在一起返回前台。前台隐藏code，只显示中文名称
                        result = provinceStr+cityStr+lineStr;
                    }

                    if(!StringUtils.isNullOrEmpty(owner) && !ownersList.contains(owner) && !ownersList.contains(owner)){
                        ownersList.add(owner);
                    }
                    if(!StringUtils.isNullOrEmpty(ccsId) && !StringUtils.isNullOrEmpty(result) && !ccsIdsTemp.contains(ccsId)){
                        lineTemp.put("ccsId",ccsId);
                        lineTemp.put("line",result);
                        linesList.add(lineTemp);
                        ccsIdsTemp.add(ccsId);
                    }
                }
            }
        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        Collections.sort(ownersList);
        returnInfo.put("sectionTypesList",sectionTypesList);
        returnInfo.put("linesList",linesList);
        returnInfo.put("ownersList",ownersList);
        return  returnInfo;

    }

    @RequestMapping(params = "method=getSectionType",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getSectionType(String code){
        JSONObject returnInfo = new JSONObject();
        List<JSONObject> sectionTypesList =  null;
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            sectionTypesList = codeItemService.getCodeListFromSystemCode(code);
        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        returnInfo.put("sectionTypesList",sectionTypesList);
        return  returnInfo;

    }

}
