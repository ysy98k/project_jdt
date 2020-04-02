package com.raising.backstage.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.StringUtils;
import com.common.BaseController;
import com.raising.backstage.service.ProjectService;
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

import java.util.*;

import static com.baosight.common.utils.DateUtils.String2Date;

/**
 * 后台管理，项目管理Controller
 */
@Controller("projectController")
@RequestMapping("/raising/backstage/project")
public class ProjectController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CodeItemService codeItemService;

    /**
     * 得到城市信息
     * @param
     * @return
     */
    @RequestMapping(value = "getProjects.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getProjects(String ajaxParam){
        JSONObject returnInfo = projectService.getProject(ajaxParam);
        return returnInfo;
    }


    /**
     * 新增一组项目，项目为1至多个
     * @param ajaxParam
     * @return
     */
    @RequestMapping(value="addProjects.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addProjects(String ajaxParam){
        JSONObject returnInfo = null;
        try {
            returnInfo = projectService.addProjects(ajaxParam);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            returnInfo = new JSONObject();
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            returnInfo.put("returnMsg", e.getMessage());
            return  returnInfo;
        }
        return returnInfo;
    }


    @RequestMapping(value="deleteProject.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject deleteProjects(String ajaxParam){

        JSONObject returnInfo = null;
        try {
            returnInfo = projectService.deleteProjects(ajaxParam);
        } catch (Exception e) {
            logger.error(e.getMessage());
            returnInfo = new JSONObject();
            returnInfo.put("status",Constants.EXECUTE_FAIL);
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

    @RequestMapping(value="updateProject.do",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject updateProjects(String ajaxParam){

        //执行更新
        JSONObject returnInfo = null;
        try {
            returnInfo = projectService.updateProjects(ajaxParam);
        } catch (Exception e) {
            logger.error(e.getMessage());
            returnInfo = new JSONObject();
            returnInfo.put("status",Constants.EXECUTE_FAIL);
            returnInfo.put("returnMsg", e.getMessage());
            return  returnInfo;
        }
        return returnInfo;
    }


    @RequestMapping(value="getProjectStatus.do",method = RequestMethod.GET)
    @ResponseBody
    public List<JSONObject> getProjectStatus(String ajaxParam){

        List<JSONObject> statusList = null;
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            statusList = codeItemService.getCodeListFromSystemCode(ajaxParam);
        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        return  statusList;
    }


    @RequestMapping(value="getTbmInfo.do",method = RequestMethod.GET)
    @ResponseBody
    public List<JSONObject> getTbmInfo(){
        List<JSONObject> array = projectService.getTbmInfo();
        return  array;
    }

    @RequestMapping(value = "getTemplate.do",method = RequestMethod.GET)
    @ResponseBody
    public List<JSONObject> getTemplate(String ajaxParam){
        List<JSONObject> templateList = null;
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            templateList = codeItemService.getCodeListFromSystemCode(ajaxParam);
        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        return  templateList;

    }

    @RequestMapping(value="getSections.do",method = RequestMethod.GET)
    @ResponseBody
    public List<JSONObject> getSection(){
        List<JSONObject> array = projectService.getSections();
        return  array;
    }

    @RequestMapping(value = "initSelect.do",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject initSelect(String sectionCode,String tbmCode){
        JSONObject returnInfo = new JSONObject();

        List<JSONObject> list = sectionManageService.initSelect();
        JSONObject tbmJson = bTbmService.initQuery();
        JSONObject projectJson = projectService.initSelect();


        List<JSONObject> tbmTypeList = null;
        List<JSONObject> sectionTypesList = null;
        JSONArray linesList = null;
        JSONArray sectionOwnersList = null;
        JSONArray factorysList = tbmJson.getJSONArray("factorysList");
        JSONArray tbmOwnersList = tbmJson.getJSONArray("ownersList");
        JSONArray rmsVersionsList = tbmJson.getJSONArray("rmsVersionsList");
        JSONArray supervisorList = projectJson.getJSONArray("supervisorList");
        JSONArray buildUnitList = projectJson.getJSONArray("buildUnitList");
        try{
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE);
            sectionTypesList = codeItemService.getCodeListFromSystemCode(sectionCode);
            JSONObject sectionJson = section(list);
            linesList = sectionJson.getJSONArray("linesList");
            sectionOwnersList = sectionJson.getJSONArray("sectionOwnersList");
            tbmTypeList = codeItemService.getCodeListFromSystemCode(tbmCode);

        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(CSVUtils.ConstantsUtil.JDT_DATA_SOURCE);
        }
        returnInfo.put("sectionTypesList",sectionTypesList);
        returnInfo.put("linesList",linesList);
        returnInfo.put("sectionOwnersList",sectionOwnersList);

        returnInfo.put("tbmTypeList",tbmTypeList);
        returnInfo.put("factorysList",factorysList);
        returnInfo.put("tbmOwnersList",tbmOwnersList);
        returnInfo.put("rmsVersionsList",rmsVersionsList);


        returnInfo.put("supervisorList",supervisorList);
        returnInfo.put("buildUnitList",buildUnitList);
        returnInfo.put("status",Constants.EXECUTE_SUCCESS);
        return returnInfo;
    }

    private JSONObject section(List<JSONObject> list){
        JSONObject returnInfo = new JSONObject();

        List<JSONObject> linesList = new ArrayList<>();
        List<String> ownersList = new ArrayList<>();

        if(list == null || list.size() < 1){
        }else{
            linesList = new ArrayList<>();
            ownersList = new ArrayList<>();

            List<String> ccsIdsTemp = new ArrayList<>();
            for(int i =0;i<list.size();i++){
                JSONObject section = list.get(i);
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
        Collections.sort(ownersList);
        returnInfo.put("linesList",linesList);
        returnInfo.put("sectionOwnersList",ownersList);
        returnInfo.put("haveData",Constants.EXECUTE_SUCCESS);
        return returnInfo;
    }

}
