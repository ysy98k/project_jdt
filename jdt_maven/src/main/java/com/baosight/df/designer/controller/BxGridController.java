package com.baosight.df.designer.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.dao.RestDao;
import com.baosight.common.basic.entity.GridData;
import com.baosight.common.constants.Constants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Set;

@Controller
@RequestMapping("/df/designer/controllers/bxgrid.do")
public class BxGridController {

	private String apiExecute = "/api/execute";

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private RestDao dao;

	@Value("${ccs.host}")
	private String serviceAddress;

	@Value("${ccs.rest_service_name}")
	private String appPath;

	@RequestMapping(params = "method=query", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject query(String ajaxParam) {
        dao.setHost(serviceAddress);
        dao.setServiceName(appPath);
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        Set<String> keySet = ajaxParamObj.keySet();
        JSONObject params = ajaxParamObj.getJSONObject("params");
        for (String key : keySet) {
            if (!"code".equals(key) && !"params".equals(key)) {
                if("curPage".equals(key)){
                    params.put("offset", (ajaxParamObj.getInteger("curPage") - 1) * ajaxParamObj.getInteger("curRowNum"));
                }else if("curRowNum".equals(key)){
                    params.put("limit", ajaxParamObj.get(key));
                } else{
                    params.put(key, ajaxParamObj.get(key));
                }
            }
        }
        if(!StringUtils.isBlank(ajaxParamObj.getString("fieldName"))){
            params.put("orderby",ajaxParamObj.getString("fieldName"));
            if(!StringUtils.isBlank(ajaxParamObj.getString("ascDesc"))){
                params.put("ascend",ajaxParamObj.getString("ascDesc"));
            }
        }
        HttpSession session = this.request.getSession();
        String token = session.getAttribute("token").toString();
        ajaxParamObj.put("token", token);
        JSONObject returnInfo = dao.invoke("post", apiExecute, ajaxParamObj);
        JSONObject newReturn = new JSONObject();
        if(returnInfo.getInteger("errcode") == 0){
            GridData outGridSource = new GridData(returnInfo.getInteger("total_count"), returnInfo.getJSONArray("result"));
            outGridSource.fillGridAttribute(ajaxParamObj);
            JSONObject outJSONObj = JSONObject.parseObject(JSON
                    .toJSONString(outGridSource));
            outJSONObj.put("status", Constants.EXECUTE_SUCCESS);
            outJSONObj.put("returnMsg", "查询成功！本次返回" + returnInfo.getJSONArray("result").size() + "条记录，总共"
                    + returnInfo.getInteger("total_count") + "条记录！");
            return outJSONObj;
        }else{
            newReturn.put("status",Constants.EXECUTE_FAIL);
            newReturn.put("returnMsg", returnInfo.getString("errinfo"));
            return newReturn;
        }
	}
}
