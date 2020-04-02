package com.raising.backstage.controller;
import com.alibaba.fastjson.JSONObject;
import com.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.raising.backstage.service.DrawLineService;

import java.util.List;


@Controller
@RequestMapping("/raising/backstage/drawLine.do")
public class DrawLineController extends BaseController {

    @Autowired
    private DrawLineService drawLineService;

    @RequestMapping(params = "method=save",method = RequestMethod.POST)
    public @ResponseBody
    JSONObject saveLine(String ajaxParam) {

        JSONObject jsonObject = drawLineService.saveLine(ajaxParam);
        return jsonObject;
    }

    @RequestMapping(params = "method=query",method = RequestMethod.POST)
    public @ResponseBody
    JSONObject queryLine(String ajaxParam){
        JSONObject jsonObject = drawLineService.queryLine(ajaxParam);

        return jsonObject;

    }

    @RequestMapping(params = "method=queryAll",method = RequestMethod.POST)
    public @ResponseBody
    JSONObject queryAllLine(String ajaxParam){
        String groupNames = request.getSession().getAttribute("groupNames").toString();
        List<String> collectionNames = resourceService.getCollectionNames(groupNames);
        JSONObject jsonObject = drawLineService.queryAllLine(collectionNames);

        return jsonObject;

    }

}

