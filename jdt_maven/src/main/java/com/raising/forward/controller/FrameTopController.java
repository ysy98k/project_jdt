package com.raising.forward.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.dao.RestDao;
import com.common.BaseController;
import com.raising.forward.service.PropertiesValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 选择默认的项目
 */
@Controller
@RequestMapping("/raising/froward/frameTop")
public class FrameTopController extends BaseController{



    /**
     * 初始化，判断当前用户是否有后台操作权限
     * @return
     */
    @RequestMapping(value = "/init.do",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject init(String groupname){
        JSONObject returnInfo = new JSONObject();

        restDao.setHost(PropertiesValue.AAS_ADRESS);
        restDao.setServiceName(PropertiesValue.AAS_APP_PATH);

        String[] groupnameArr = null;
        if(groupname.indexOf(",")> 0){//如果当前用户所在不止一个群组。
            groupnameArr = groupname.split(",");
        }else{
            groupnameArr = new String[1];
            groupnameArr[0] = groupname;
        }
        //遍历群组集合。判断是否具有后台操作权限
        boolean containFrame = false;
        for(int i=0;i<groupnameArr.length;i++){
            containFrame = checkContainsFrame(restDao,groupnameArr[i]);
            if(containFrame == true){
                break;
            }
        }

        returnInfo.put("containFrame", containFrame);

        return returnInfo;
    }

    private boolean checkContainsFrame(RestDao restDao,String groupname){
        JSONObject mapIn = new JSONObject();
        mapIn.put("token",request.getSession().getAttribute("token").toString());
        JSONObject permission = restDao.invoke("GET", "/api/usergroup/"+groupname+"/resource?service=JDT&name=frame", mapIn);
        boolean containFrame = false;
        if ("0".equals(permission.get("errcode"))) {
            JSONArray resource = permission.getJSONArray("resources");
            for(int i =0;i<resource.size();i++){
                JSONObject temp = resource.getJSONObject(i);
                if("frame".equals(temp.getString("name"))){
                    containFrame =true;
                    break;
                }
            }
        }
        return containFrame;
    }

}
