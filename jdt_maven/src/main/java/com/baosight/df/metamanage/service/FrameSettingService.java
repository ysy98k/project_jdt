package com.baosight.df.metamanage.service;

import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.dao.RestDao;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class FrameSettingService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(FrameSettingService.class);

    @Autowired
    private HttpServletRequest request;

    public RestDao dao;

    @Autowired
    private TenantMenuService tenantMenuService;




	/**
	 * 更新
	 * 
	 * @param paramInfo
	 * @return 输出JSONObject
	 */

	public JSONObject updateFrameSetting(JSONObject paramInfo) {
		JSONObject returnInfo = new JSONObject();
        paramInfo.put("tenant", request.getSession().getAttribute(Constants.SESSION_TENANT_KEY));
		try {
			sqlSessionTemplate.update("FrameSetting.update", paramInfo);
		} catch (Exception e) {
			returnInfo.put("status", -1);
			returnInfo.put("returnMsg", "传入参数传递有误，请重新尝试！");
			e.printStackTrace();
		}
		returnInfo.put("status", 0);
		returnInfo.put("returnMsg", "修改成功！");
		return returnInfo;
	}

	/**
	 * 查询记录
	 * 
	 * @param paramInfo
	 * @return 输出JSONObject
	 */
	public JSONObject queryFrame(JSONObject paramInfo) {
        String toolkitPath = request.getContextPath();
		JSONObject returnInfo = new JSONObject();
        paramInfo.put("tenant", request.getSession().getAttribute(Constants.SESSION_TENANT_KEY));
		List frameSettingLst = sqlSessionTemplate.selectList(
				"FrameSetting.query", paramInfo);
		if (frameSettingLst.size() > 0) {
			JSONObject frameSetting = (JSONObject) frameSettingLst.get(0);
			returnInfo.put("skinName", frameSetting.get("skinName"));
            returnInfo.put("logoIcon", frameSetting.get("logoIcon"));
            returnInfo.put("logoText", frameSetting.get("logoText"));
            returnInfo.put("pageCode", frameSetting.get("homepage"));

            String api = "/api/permission/JDT";
            JSONObject mapIn = new JSONObject();
            mapIn.put("token", request.getSession().getAttribute(Constants.SESSION_TOKEN_KEY));
            mapIn.put("name", frameSetting.get("homepage"));

            JSONObject homePageAuth=new JSONObject();
            JSONObject result = new JSONObject();
            homePageAuth = dao.invoke("get", api, mapIn);
            if(homePageAuth.get("errcode").equals("0")){
                result.put("status", 0);
                if(homePageAuth.getJSONArray("permission").size()==1){
                    if(homePageAuth.getJSONArray("permission").getJSONObject(0).get("GET").equals(true)){
                        List pageLst = sqlSessionTemplate.selectList("FrameSetting.queryHomepage", paramInfo);
                        if(pageLst.size() > 0){
                            JSONObject page = (JSONObject) pageLst.get(0);
                            if (page.get("pageType")!=null && (page.get("pageType").equals("localLink") || page.get("pageType").equals("designPage"))){
                                returnInfo.put("pagePath", toolkitPath+page.get("pagePath"));
                            }
                            else if (page.get("pageType")!=null && page.get("pageType").equals("cvsLink")){
                                String pageCodeName = (String)homePageAuth.getJSONArray("permission").getJSONObject(0).get("name");
                                JSONObject cvsResult = tenantMenuService.queryCvsRest(pageCodeName);
                                if(cvsResult.getString("cvsErrorMsg")!=null){
                                    //cvs接口返回异常
                                    returnInfo.put("pagePath", toolkitPath + "/bxui/other/jsp/errorinfo" + ".jsp?errCode=181000&detailMessage=" + cvsResult.getString("cvsErrorMsg"));
                                }else{
                                    if(cvsResult.getString("code").equals("0")){
                                        JSONObject cvsMenus = (JSONObject) cvsResult.get("msg");
                                        if(cvsMenus.getString(pageCodeName)==null  || cvsMenus.getString(pageCodeName)
                                                .equals("")){
                                            //cvs接口返回成功，页面不存在为空
                                            returnInfo.put("pagePath",
                                                    toolkitPath + "/bxui/other/jsp/errorinfo" + ".jsp?errCode=181002&detailMessage=");
                                        }else{
                                            //cvs接口返回成功，页面存在返回url
                                            returnInfo.put("pagePath", cvsMenus.getString(pageCodeName));
                                        }
                                    }
                                    else{
                                        //cvs接口返回失败
                                        returnInfo.put("pagePath",
                                                toolkitPath + "/bxui/other/jsp/errorinfo.jsp?errCode=181001&detailMessage=" + cvsResult.get("msg"));
                                    }
                                }
                            }
                            else{
                                returnInfo.put("pagePath", page.get("pagePath"));
                            }
                            returnInfo.put("pageType", page.get("pageType"));
                        }
                    }
                }
            }else{
                result.put("status", -1);
                result.put("returnMsg", homePageAuth.get("errinfo"));
                return result;
            }
		}

		return returnInfo;
	}

    public JSONObject queryFrontFrame(JSONObject paramInfo) {
        String toolkitPath = request.getContextPath();
        JSONObject returnInfo = new JSONObject();
        paramInfo.put("tenant", request.getSession().getAttribute(Constants.SESSION_TENANT_KEY));
        List frameSettingLst = sqlSessionTemplate.selectList(
                "FrameSetting.query", paramInfo);
        if (frameSettingLst.size() > 0) {
            JSONObject frameSetting = (JSONObject) frameSettingLst.get(0);
            returnInfo.put("skinName", frameSetting.get("skinName"));
            returnInfo.put("logoIcon", frameSetting.get("logoIcon"));
            returnInfo.put("logoText", frameSetting.get("logoText"));
            returnInfo.put("bigLogoIcon", frameSetting.get("bigLogoIcon"));
            returnInfo.put("pageCode", frameSetting.get("frontHomePage"));

            String api = "/api/permission/JDT";
            JSONObject mapIn = new JSONObject();
            mapIn.put("token", request.getSession().getAttribute(Constants.SESSION_TOKEN_KEY));
            mapIn.put("name", frameSetting.get("frontHomePage"));

            JSONObject frontHomePageAuth=new JSONObject();
            JSONObject result = new JSONObject();
            frontHomePageAuth = dao.invoke("get", api, mapIn);
            if(frontHomePageAuth.get("errcode").equals("0")){
                result.put("status", 0);
                if(frontHomePageAuth.getJSONArray("permission").size()==1){
                    if(frontHomePageAuth.getJSONArray("permission").getJSONObject(0).get("GET").equals(true)){
                        List pageLst = sqlSessionTemplate.selectList("FrameSetting.queryFontHomePage", paramInfo);
                        if(pageLst.size() > 0){
                            JSONObject page = (JSONObject) pageLst.get(0);
                            if (page.get("pageType")!=null && (page.get("pageType").equals("localLink") || page.get("pageType").equals("designPage"))){
                                returnInfo.put("pagePath", toolkitPath+page.get("pagePath"));
                            }
                            else if (page.get("pageType")!=null && page.get("pageType").equals("cvsLink")){
                                String pageCodeName = (String)frontHomePageAuth.getJSONArray("permission").getJSONObject(0).get("name");
                                JSONObject cvsResult = tenantMenuService.queryCvsRest(pageCodeName);
                                if(cvsResult.getString("cvsErrorMsg")!=null){
                                    //cvs接口返回异常
                                    returnInfo.put("pagePath", toolkitPath + "/bxui/other/jsp/errorinfo" + ".jsp?errCode=181000&detailMessage=" + cvsResult.getString("cvsErrorMsg"));
                                }else{
                                    if(cvsResult.getString("code").equals("0")){
                                        JSONObject cvsMenus = (JSONObject) cvsResult.get("msg");
                                        if(cvsMenus.getString(pageCodeName)==null  || cvsMenus.getString(pageCodeName)
                                                .equals("")){
                                            //cvs接口返回成功，页面不存在为空
                                            returnInfo.put("pagePath",
                                                    toolkitPath + "/bxui/other/jsp/errorinfo" + ".jsp?errCode=181002&detailMessage=");
                                        }else{
                                            //cvs接口返回成功，页面存在返回url
                                            returnInfo.put("pagePath", cvsMenus.getString(pageCodeName));
                                        }
                                    }
                                    else{
                                        //cvs接口返回失败
                                        returnInfo.put("pagePath",
                                                toolkitPath + "/bxui/other/jsp/errorinfo.jsp?errCode=181001&detailMessage=" + cvsResult.get("msg"));
                                    }
                                }
                            }
                            else{
                                returnInfo.put("pagePath", page.get("pagePath"));
                            }
                            returnInfo.put("pageType", page.get("pageType"));
                        }
                    }

                }
            }else{
                result.put("status", -1);
                result.put("returnMsg", frontHomePageAuth.get("errinfo"));
                return result;
            }

        }

        return returnInfo;
    }

    public JSONObject queryFrameSetting(JSONObject paramInfo) {
        JSONObject returnInfo = new JSONObject();
        paramInfo.put("tenant", request.getSession().getAttribute(Constants.SESSION_TENANT_KEY));
        List frameSettingLst = sqlSessionTemplate.selectList(
                "FrameSetting.query", paramInfo);
        List pageLst = sqlSessionTemplate.selectList(
                "FrameSetting.queryPage", paramInfo);
        if (frameSettingLst.size() > 0) {
            JSONObject frameSetting = (JSONObject) frameSettingLst.get(0);
            returnInfo.put("skinName", frameSetting.get("skinName"));
            returnInfo.put("skinDesc", frameSetting.get("skinDesc"));
            returnInfo.put("logoIcon", frameSetting.get("logoIcon"));
            returnInfo.put("bigLogoIcon", frameSetting.get("bigLogoIcon"));
            returnInfo.put("logoText", frameSetting.get("logoText"));
            returnInfo.put("homepage", frameSetting.get("homepage"));
            returnInfo.put("frontHomePage", frameSetting.get("frontHomePage"));
        }
        if(pageLst.size() > 0){
            returnInfo.put("pageLst", pageLst);
        }
        return returnInfo;
    }

	/**
	 * 插入记录
	 * 
	 * @param paramInfo
	 * @return 输出JSONObject
	 */
	public JSONObject insertFrameSetting(JSONObject paramInfo) {
		JSONObject returnInfo = new JSONObject();
        paramInfo.put("tenant", request.getSession().getAttribute(Constants.SESSION_TENANT_KEY));
		try {
			sqlSessionTemplate.insert("FrameSetting.insert", paramInfo);
		} catch (Exception e) {
			returnInfo.put("status", -1);
			returnInfo.put("returnMsg", "操作失败");
			e.printStackTrace();
		}
		returnInfo.put("status", 0);
		returnInfo.put("returnMsg", "插入成功！");
		return returnInfo;
	}



    public JSONObject getLogo(){
        JSONObject returnInfo = new JSONObject();
        JSONObject paramInfo = new JSONObject();
        paramInfo.put("tenant", "raising");
        List frameSettingLst = sqlSessionTemplate.selectList(
                "FrameSetting.query", paramInfo);
        List pageLst = sqlSessionTemplate.selectList(
                "FrameSetting.queryPage", paramInfo);
        if (frameSettingLst.size() > 0) {
            JSONObject frameSetting = (JSONObject) frameSettingLst.get(0);
            returnInfo.put("logoIcon", frameSetting.get("logoIcon"));
            returnInfo.put("bigLogoIcon", frameSetting.get("bigLogoIcon"));
            returnInfo.put("logoText", frameSetting.get("logoText"));
        }
        if(pageLst.size() > 0){
            returnInfo.put("pageLst", pageLst);
        }
        return returnInfo;
    }
}
