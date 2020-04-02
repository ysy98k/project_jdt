package com.raising.forward.controller.group;


import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.dao.RestDao;
import com.baosight.df.metamanage.service.FrameSettingService;
import com.common.BaseController;
import com.raising.forward.service.PropertiesValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 统计分析
 */
@RestController("statisticalAnalysisController")
@RequestMapping("/raising/forward/statisticalAnalysis")
public class StatisticalAnalysisController extends BaseController {


    @Autowired
    private RestDao dao;

    @Autowired
    private FrameSettingService frameSettingService;

    /**
     * 定义点击统计分析时。跳转的默认页面
     * @param ajaxParam
     * @return
     */
    @RequestMapping(value = "jumpPage.do", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject queryFrame(String ajaxParam) {
        dao.setHost(PropertiesValue.AAS_ADRESS);
        dao.setServiceName(PropertiesValue.AAS_APP_PATH);
        frameSettingService.dao = dao;

        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = frameSettingService.queryFrame(ajaxParamObj);

        if (returnInfo.getString("skinName") != null) {
            request.getSession().setAttribute("skinName", returnInfo.getString("skinName"));
        } else {
            request.getSession().setAttribute("skinName", "blue-skin");
            frameSettingService.insertFrameSetting(ajaxParamObj);
        }
        returnInfo.put("pageCode","factory");
        returnInfo.put("pagePath", "/raising/forward/group/progress/groupProgress.jsp");
        return returnInfo;
    }

    /**
     * 获得用户权限下的项目信息
     * @return
     */
    public List<JSONObject> getProjects(){
        String groupNames = request.getSession().getAttribute("groupNames").toString();
        List<String> collectionNames = resourceService.getCollectionNames(groupNames);
        List<JSONObject> resourcesProject = projectForwardService.getResourcesProject(collectionNames);
        return resourcesProject;
    }

    /**
     * 获取用户权限下的 项目id
     * @return
     */
    public List<Integer> getProjectIds(){
        List<Integer> result = new ArrayList<>();
        List<JSONObject> resourcesProject = getProjects();
        if(resourcesProject == null || resourcesProject.size() < 1){
            return result;
        }
       for(int i =0;i<resourcesProject.size();i++){
           JSONObject jsonObject = resourcesProject.get(i);
           result.add(jsonObject.getInteger("projectId"));
       }
       return  result;
    }

}
