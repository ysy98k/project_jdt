package com.baosight.df.designer.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.aas.beans.ErrorInfo;
import com.baosight.common.basic.dao.RestDao;
import com.baosight.common.constants.Constants;
import com.baosight.common.oldrest.RestClient;
import com.baosight.common.oldrest.RestError;
import com.baosight.df.designer.entity.DesignPageBean;
import com.baosight.df.designer.service.LoadSaveServiceImpl;
import com.baosight.df.metamanage.controller.PageManageController;
import com.baosight.xinsight.constant.AasKeyConstants;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/df/designer/loadsave.do")
public class LoadSaveController {
    private static final Logger logger = LoggerFactory
        .getLogger(PageManageController.class);
    @Autowired
    private HttpServletRequest request;
    private String api = "/api/resource";
    @Value("${aas.host}")
    private String aasAddress;

    @Value("${aas.rest_service_name}")
    private String aasAppPath;
    @Value("${service.name}")
    private String serviceName;

    @Value("${designer.delivery_pkg}")
    private String delivery_pkg;

    @Autowired
    private RestDao dao;

    @Autowired
    LoadSaveServiceImpl loadSaveService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView init(HttpServletRequest request,
                             HttpServletResponse response) {
        dao.setHost(aasAddress);
        dao.setServiceName(aasAppPath);
        logger.info("成功读取权限rest配置文件，host：" + aasAddress + " 服务名：" + aasAppPath);
        return new ModelAndView();
    }

    private static final ErrorInfo defaultErrorInfo = new ErrorInfo(-1,
        "请求参数错误");


    /*新建页面的时候调用*/
    @RequestMapping(params = "method=saveas", method = RequestMethod.POST)
    @ResponseBody
    public ErrorInfo saveas(String ajaxParam) {
        dao.setHost(aasAddress);
        dao.setServiceName(aasAppPath);
        DesignPageBean designPage = RestClient.getJavaBean(ajaxParam,
            DesignPageBean.class);

        ErrorInfo errorInfo = defaultErrorInfo;

        if (designPage != null) {
            errorInfo = loadSaveService.NewDesignPage(designPage);
            if (errorInfo.getErrcode() == ErrorInfo.SUCCESS) {// {"htmlstring":"","jsstring":"","cssstring":"","page_desc":"iii","page_cname":"u",
                // "pagename":"uuuuu8888"}
                // JSONObject
                // ajaxParamObj
                // =
                // JSONObject.parseObject(ajaxParam);
                /* 根据状态判断是否需要新增资源 */
                HttpSession session = this.request.getSession();
                String token = session.getAttribute("token").toString();
                JSONObject mapIn = new JSONObject();
                mapIn.put("token", token);
                JSONArray resourceArray = new JSONArray();
                // JSONArray resourceArray =
                // ajaxParamObj.getJSONObject("detail")
                // .getJSONArray("resultRow");
                // for (int i = 0; i < resourceArray.size(); i++) {
                JSONObject resource = new JSONObject();// resourceArray.getJSONObject(i);
                resource.put("name", designPage.getPagename());// resource.getString("pageEname"));
                resource.put("description", designPage.getPage_desc());// resource.getString("pageDesc"));
                resource.put("service", serviceName);
                resourceArray.add(resource);
                // }
                mapIn.put("resources", resourceArray);
                JSONObject restInfo = dao.invoke("post", api, mapIn);
                if (Constants.EXECUTE_SUCCESS.equals(restInfo
                    .getString(AasKeyConstants.KEY_ERRCODE))) {
                    errorInfo.setErrinfo("插入了1条记录，资源新增成功");
                } else {
                    errorInfo.setErrcode(-1);
                    errorInfo.setErrinfo("插入了1条记录！" + "资源新增失败，错误码为："
                        + restInfo.getString(AasKeyConstants.KEY_ERRCODE));
                }

            }
        }

        return errorInfo;
    }

    @RequestMapping(params = "method=upgrade", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject upgrade(String ajaxParam) {
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = loadSaveService.upgrade(ajaxParamObj);
        return returnInfo;
    }

    /*保存页面的时候调用*/
    @RequestMapping(params = "method=save", method = RequestMethod.POST)
    @ResponseBody
    public ErrorInfo save(String ajaxParam) {
        DesignPageBean designPage = RestClient.getJavaBean(ajaxParam,
                DesignPageBean.class);

        ErrorInfo errorInfo = defaultErrorInfo;

        if (designPage != null) {
            errorInfo = loadSaveService.SaveDesignPage(designPage);
        }

        return errorInfo;
    }

    @RequestMapping(params = "method=getRequireJsArray", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getRequireJsArray(String ajaxParam) {
        JSONObject ajaxParamObj = JSONObject.parseObject(ajaxParam);
        JSONObject returnObj = new JSONObject();
        String pageName = ajaxParamObj.getString("pageId");
        DesignPageBean designPage = new DesignPageBean();
        designPage.setPagename(pageName);
        DesignPageBean retDesignPageBean = null;
        if (designPage != null) {
            try {
                retDesignPageBean = loadSaveService.LoadDesignPage(designPage
                        .getPagename());
            } catch (RestError e) {
                e.printStackTrace();
                returnObj.put("status", Constants.EXECUTE_FAIL);
                return returnObj;
            }
        }
        String importJs = getImportJs(retDesignPageBean.getHtmlstring());
        returnObj.put("status", Constants.EXECUTE_SUCCESS);
        returnObj.put("importJs",importJs);
        return returnObj;
    }

    @RequestMapping(params = "method=load", method = RequestMethod.GET)
    @ResponseBody
    public ErrorInfo load(String ajaxParam) {
        DesignPageBean designPage = RestClient.getJavaBean(ajaxParam,
                DesignPageBean.class);

        DesignPageBean retDesignPageBean = null;

        if (designPage != null) {
            try {
                retDesignPageBean = loadSaveService.LoadDesignPage(designPage
                    .getPagename());
                if (retDesignPageBean == null) {
                    return new ErrorInfo(-1, "您查询的pagename\""
                        + designPage.getPagename() + "\"不存在");
                }
                if (null == retDesignPageBean.getDelivery_pkg() || delivery_pkg.compareTo(retDesignPageBean.getDelivery_pkg()) > 0) {
                    return new ErrorInfo(1, "当前页面：" + designPage.getPagename() + "版本过低，需要升级！");
                }
            } catch (RestError e) {

                e.printStackTrace();
                return new ErrorInfo(e);
            }
        }
        String importJs = getImportJs(retDesignPageBean.getHtmlstring());
        retDesignPageBean.setImportJs(importJs);
        return retDesignPageBean;
    }

    private String getImportJs(String htmlString) {
        Document htmlDoc = Jsoup.parse(htmlString);
        Elements widgetDivLst = htmlDoc.getElementsByAttribute("data-id");
        List<String> requireLst = new ArrayList<String>();
        for (int i = 0; i < widgetDivLst.size(); i++) {
            Element curElement = widgetDivLst.get(i);
            String widgetBaseType = curElement.attr("data-type");
            if ((widgetBaseType.equals("bxtimepicker") || widgetBaseType.equals("bxtree") || widgetBaseType.equals("bxgrid") ||
                widgetBaseType.equals("bxcombobox")) && !requireLst.contains(widgetBaseType)) {
                requireLst.add(widgetBaseType);
            } else if ((widgetBaseType.equals("chartCol") || widgetBaseType.equals("chartColT") || widgetBaseType.equals("chartBar") ||
                widgetBaseType.equals("chartBarT")) && !requireLst.contains("bxchartsbar")) {
                if(!requireLst.contains("bxcharts")){
                    requireLst.add("bxcharts");
                }
                requireLst.add("bxchartsbar");
            } else if ((widgetBaseType.equals("chartLine") || widgetBaseType.equals("chartLineT") || widgetBaseType.equals("chartLineS") ||
                widgetBaseType.equals("chartArea") || widgetBaseType.equals("chartAreaT")) && !requireLst.contains("bxchartsline")) {
                if(!requireLst.contains("bxcharts")){
                    requireLst.add("bxcharts");
                }
                requireLst.add("bxchartsline");
            } else if ((widgetBaseType.equals("chartPie") || widgetBaseType.equals("chartPieC")) && !requireLst.contains("bxchartspie")) {
                if(!requireLst.contains("bxcharts")){
                    requireLst.add("bxcharts");
                }
                requireLst.add("bxchartspie");
            } else if (widgetBaseType.equals("chartGauge") && !requireLst.contains("bxchartsgauge")) {
                if(!requireLst.contains("bxcharts")){
                    requireLst.add("bxcharts");
                }
                requireLst.add("bxchartsgauge");
            } else if (widgetBaseType.equals("chartMap") && !requireLst.contains("bxchartsmap")) {
                if(!requireLst.contains("bxcharts")){
                    requireLst.add("bxcharts");
                }
                requireLst.add("bxchartsmap");
            } else if ((widgetBaseType.equals("chartRadar") || widgetBaseType.equals("chartRadarF")) && !requireLst.contains("bxchartsradar")) {
                if(!requireLst.contains("bxcharts")){
                    requireLst.add("bxcharts");
                }
                requireLst.add("bxchartsradar");
            } else if (widgetBaseType.equals("chartScat") && !requireLst.contains("bxchartsscatter")) {
                if(!requireLst.contains("bxcharts")){
                    requireLst.add("bxcharts");
                }
                requireLst.add("bxchartsscatter");
            }
        }
        return StringUtils.join(requireLst.toArray(), ",");
    }

    @RequestMapping(params = "method=preview", method = RequestMethod.POST)
    @ResponseBody
    public String preview(@ModelAttribute DesignPageBean designPage,
                          BindingResult result) {
        String previewPage = "the htmlstring parameter is null or empty";

        if ((designPage != null) && (designPage.getHtmlstring() != null)) {
            previewPage = loadSaveService.LoadPreviewPage(designPage);
        }

        return previewPage;
    }

    @RequestMapping(params = "method=publish", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String publish(@ModelAttribute DesignPageBean designPage,
                          BindingResult result) {
        String publishPage = "the pagename parameter is null or empty";

        String pagename = designPage.getPagename();
        if ((pagename != null) && (!pagename.isEmpty())) {
            publishPage = loadSaveService.LoadPublishPage(pagename);
        }

        return publishPage;
    }

    @RequestMapping(params = "method=publishjs", method = RequestMethod.GET, produces = "application/javascript;charset=UTF-8")
    @ResponseBody
    public String publishjs(@ModelAttribute DesignPageBean parameter,
                            BindingResult result) {

        String publishPage = "the pagename parameter is null or empty";

        String pagename = parameter.getPagename();
        if ((pagename != null) && (!pagename.isEmpty())) {
            try {
                DesignPageBean designPage = loadSaveService
                    .LoadDesignPage(pagename);
                publishPage = designPage.getJsstring();
            } catch (RestError e) {

                e.printStackTrace();
                return "the javascript page load error";
            }
        }

        return publishPage;
    }

    @RequestMapping(params = "method=publishcss", method = RequestMethod.GET, produces = "text/css;charset=UTF-8")
    @ResponseBody
    public String publishcss(@ModelAttribute DesignPageBean parameter,
                             BindingResult result) {
        String publishPage = "the pagename parameter is null or empty";

        String pagename = parameter.getPagename();
        if ((pagename != null) && (!pagename.isEmpty())) {
            try {
                DesignPageBean designPage = loadSaveService
                    .LoadDesignPage(pagename);
                publishPage = "@CHARSET \"UTF-8\";\r\n";
                String cssstring = designPage.getCssstring();
                if ((cssstring != null) && (!cssstring.isEmpty())) {
                    publishPage += cssstring;
                }
            } catch (RestError e) {

                e.printStackTrace();
                return "the css page load error";
            }
        }

        return publishPage;
    }

}
