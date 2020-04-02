package com.raising.backstage.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.raising.backstage.service.TbmService;
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

import java.util.List;
import java.util.Map;

@Controller("tbmController")
@RequestMapping("/raising/backstage/tbm")
public class TbmController {

    private static final Logger logger = LoggerFactory.getLogger(TbmService.class);

    @Autowired
    private CodeItemService codeItemService;

    @Autowired
    private TbmService tbmService;

    @RequestMapping(value="getTbmInfos.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getTbmInfos(String ajaxParam){
        JSONObject returnInfo = tbmService.getTbmInfos(ajaxParam);
        JSONArray detailsArray = (JSONArray)returnInfo.get("rows");
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            if(detailsArray.size() < 1){
                return returnInfo;
            }
            JSONObject one = detailsArray.getJSONObject(0);
            Map<String,String> tbmCCSId = codeItemService.getCodeNameFromSystemCode(one.getString("ccsType"));
            for(int i = 0;i<detailsArray.size();i++){
                JSONObject tempObj = detailsArray.getJSONObject(i);
                //String tbmType = codeItemService.getCodeNameFromSystemAndItem(tempObj.getString("ccsType"));
                String tbmType = tbmCCSId.get(tempObj.getString("ccsType"));
                tempObj.put("tbmType",tbmType);
            }
        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        return  returnInfo;
    }
    /**
     * 新增
     * @param ajaxParam
     * @return
     */
    @RequestMapping(value="addTbmInfos.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addTbmInfos(String ajaxParam){

        JSONObject returnInfo = null;
        try {
            returnInfo = tbmService.addTbmInfos(ajaxParam);
        } catch (Exception e) {
            logger.error(e.getMessage());
            returnInfo = new JSONObject();
            returnInfo.put("status", Constants.EXECUTE_FAIL);
            returnInfo.put("returnMsg", e.getMessage());
            return  returnInfo;
        }
        return returnInfo;
    }

    /**
     * 修改
     * @param ajaxParam
     * @return
     */
    @RequestMapping(value="updateTbmInfos.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject updateTbmInfos(String ajaxParam){
        JSONObject returnInfo = null;
        try {
            returnInfo = tbmService.updateTbmInfos(ajaxParam);
        } catch (Exception e) {
            logger.error(e.getMessage());
            returnInfo = new JSONObject();
            returnInfo.put("status", Constants.EXECUTE_FAIL);
            returnInfo.put("returnMsg", e.getMessage());
        }
        return returnInfo;
    }

    /**
     * 删除
     * @param ajaxParam
     * @return
     */
    @RequestMapping(value="deleteTbmInfos.do",method=RequestMethod.POST)
    @ResponseBody
    public JSONObject deleteTbmInfos(String ajaxParam){

        JSONObject returnInfo = null;
        try {
            returnInfo = tbmService.deleteTbmInfos(ajaxParam);
        } catch (Exception e) {
            logger.error(e.getMessage());
            returnInfo = new JSONObject();
            returnInfo.put("status", Constants.EXECUTE_FAIL);
            returnInfo.put("returnMsg", e.getMessage());
        }
        return returnInfo;
    }

    @RequestMapping(value="getTbmType.do",method = RequestMethod.POST)
    @ResponseBody
    public List<JSONObject> getTbmType(String code){

        List<JSONObject> statusList = null;
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            statusList = codeItemService.getCodeListFromSystemCode(code);
        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        return  statusList;
    }

    @RequestMapping(value="initQuery.do",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject initQuery(String tbmCode){
        JSONObject returnInfo = new JSONObject();
        //得到项目状态的systemcode。
        returnInfo = tbmService.initQuery();

        List<JSONObject> tbmTypesList = null;
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            tbmTypesList = codeItemService.getCodeListFromSystemCode(tbmCode);
        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        returnInfo.put("tbmTypesList",tbmTypesList);
        return  returnInfo;
    }
}
