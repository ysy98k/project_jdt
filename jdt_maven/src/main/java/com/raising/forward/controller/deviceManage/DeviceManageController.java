package com.raising.forward.controller.deviceManage;

import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.dao.RestDao;
import com.baosight.df.metamanage.service.FrameSettingService;
import com.common.BaseController;
import com.raising.forward.service.PropertiesValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("deviceManageController")
@RequestMapping("/raising/forward/deviceManage")
public class DeviceManageController extends BaseController {

    @Autowired
    private RestDao dao;

    @Autowired
    private FrameSettingService frameSettingService;


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
        /*returnInfo.put("pageCode","factory");*/
        returnInfo.put("pagePath", "/raising/forward/deviceManage/tbmAssetManage.jsp");
        return returnInfo;
    }
}
