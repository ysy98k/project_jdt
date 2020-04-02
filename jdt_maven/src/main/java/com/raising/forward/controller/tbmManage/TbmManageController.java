package com.raising.forward.controller.tbmManage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.StringUtils;
import com.common.BaseController;
import com.raising.backstage.service.TbmService;
import com.raising.forward.service.PropertiesValue;
import com.util.CSVUtils;
import com.util.MultipleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;



/**
 * 上侧菜单，盾构管理Controller
 */
@RestController("tbmManageMenu")
@RequestMapping("/raising/forward/tbmManage")
public class TbmManageController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(TbmManageController.class);
    @Autowired
    private TbmService tbmService;


    /**
     * 查询所有盾构机信息
     * @param ajaxParam
     * @return
     */
    @RequestMapping(value="getTbmInfos.do",method = RequestMethod.POST)
    public JSONObject getTbmInfos(String ajaxParam){
        //获取盾构机
        JSONObject data = tbmService.getTbmInfos(ajaxParam);
        if(data == null){
            data = new JSONObject();
            data.put("rows",new JSONArray());
            return data;
        }
        //获取盾构机当前状态，推进，还是闲置
        JSONArray detailsArray = (JSONArray)data.get("rows");
        for(int i=0;i<detailsArray.size();i++){
            JSONObject tempObj = detailsArray.getJSONObject(i);
            JSONObject param = new JSONObject();
            param.put("tbmId",tempObj.getInteger("tbmId"));
            JSONObject project = projectForwardService.getProjectStatusByTbm(param);
            String projectStatus = project.getString("status");
            if(StringUtils.isNullOrEmpty(projectStatus)){
                tempObj.put("tbmStatus","");
            }else  if("prostatus.building".equals(projectStatus)){
                tempObj.put("tbmStatus","推进");
            }else{
                tempObj.put("tbmStatus","闲置");
            }
        }

        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            if(detailsArray.size() < 1){
                return data;
            }
            JSONObject one = detailsArray.getJSONObject(0);
            Map<String,String> tbmCCSId = codeItemService.getCodeNameFromSystemCode(one.getString("ccsType"));

            for(int i = 0;i<detailsArray.size();i++){
                JSONObject tempObj = detailsArray.getJSONObject(i);
                //String tbmType = codeItemService.getCodeNameFromSystemAndItem(tempObj.getString("ccsType"));
                String tbmType = tbmCCSId.get(tempObj.getString("tbmCCSId"));
                tempObj.remove("ccsType");
                tempObj.put("tbmType",tbmType);
            }

        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        return  data;
    }


    @RequestMapping(value = "getTbmInfosOption.do",method = RequestMethod.GET)
    public JSONObject getTbmInfosOption(){
        JSONObject returnInfo = new JSONObject();

        JSONObject data = tbmService.getTbmInfos("{inqu_status:{}}");
        if(data == null ){
            returnInfo.put("haveData",Constants.EXECUTE_FAIL);
            return returnInfo;
        }
        JSONArray detailsArray = (JSONArray)data.get("rows");
        if(detailsArray.size() < 1){
            returnInfo.put("haveData",Constants.EXECUTE_FAIL);
            return returnInfo;
        }
        //获取盾构机当前状态，推进，还是闲置
        for(int i=0;i<detailsArray.size();i++){
            JSONObject tempObj = detailsArray.getJSONObject(i);
            JSONObject param = new JSONObject();
            param.put("tbmId",tempObj.getInteger("tbmId"));
            JSONObject project = projectForwardService.getProjectStatusByTbm(param);
            String projectStatus = project.getString("status");
            if(StringUtils.isNullOrEmpty(projectStatus)){
                tempObj.put("tbmStatus","");
            }else  if("prostatus.building".equals(projectStatus)){
                tempObj.put("tbmStatus","推进");
            }else{
                tempObj.put("tbmStatus","闲置");
            }
        }
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            List<String> factorysList = new ArrayList<>();
            List<String> ownerList = new ArrayList<>();
            List<String> statusList = new ArrayList<>();
            for(int i = 0;i<detailsArray.size();i++){
                JSONObject tempObj = detailsArray.getJSONObject(i);
                //填充前台条件
                String factory = tempObj.getString("factory");
                String owner = tempObj.getString("owner");
                String tbmStatus = tempObj.getString("tbmStatus");
                if(!StringUtils.isNullOrEmpty(factory) && factorysList.contains(factory) == false){
                    factorysList.add(factory);
                }
                if(!StringUtils.isNullOrEmpty(owner) && ownerList.contains(owner) == false){
                    ownerList.add(owner);
                }
                if(!StringUtils.isNullOrEmpty(tbmStatus) && statusList.contains(tbmStatus) == false){
                    statusList.add(tbmStatus);
                }
            }
            returnInfo.put("rows",detailsArray);
            returnInfo.put("factorysList",factorysList);
            returnInfo.put("ownersList",ownerList);
            returnInfo.put("statusList",statusList);
        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        returnInfo.put("haveData",Constants.EXECUTE_SUCCESS);
        return returnInfo;
    }

    @RequestMapping(value = "getTbmInfosSelect.do",method = RequestMethod.GET)
    public JSONArray getTbmInfosSelect(){
        JSONArray returnInfo = new JSONArray();

        JSONObject data = tbmService.getTbmInfos("{inqu_status:{}}");
        if(data == null || data.getJSONArray("rows").size() < 1 ){
            return returnInfo;
        }
        returnInfo = data.getJSONArray("rows");
        return returnInfo;
    }


    @RequestMapping(value = "addTbm.do",method = RequestMethod.POST)
    public JSONObject addTbm(String ajaxParam){
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

    @RequestMapping(value = "updateTbm.do",method = RequestMethod.POST)
    public JSONObject updateTbm(String ajaxParam){

        JSONObject returnInfo = null;
        try {
            returnInfo = tbmService.updateTbmInfos(ajaxParam);
        } catch (Exception e) {
            logger.error(e.getMessage());
            returnInfo = new JSONObject();
            returnInfo.put("status", Constants.EXECUTE_FAIL);
            returnInfo.put("returnMsg", e.getMessage());
            return  returnInfo;
        }
        return returnInfo;
    }

    @RequestMapping(value = "jumpPage.do", method = RequestMethod.POST)
    public JSONObject queryFrame(String ajaxParam) {
        restDao.setHost(PropertiesValue.AAS_ADRESS);
        restDao.setServiceName(PropertiesValue.AAS_APP_PATH);
        frameSettingService.dao = restDao;

        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = frameSettingService.queryFrame(ajaxParamObj);

        if (returnInfo.getString("skinName") != null) {
            request.getSession().setAttribute("skinName", returnInfo.getString("skinName"));
        } else {
            request.getSession().setAttribute("skinName", "blue-skin");
            frameSettingService.insertFrameSetting(ajaxParamObj);
        }
        returnInfo.put("pageCode","factory");
        returnInfo.put("pagePath", "/raising/forward/tbmManage/factory.jsp");
        return returnInfo;
    }













}
