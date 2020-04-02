package com.baosight.df.designer.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baosight.aas.beans.ErrorInfo;
import com.baosight.common.basic.dao.IBaseDao;
import com.baosight.common.constants.Constants;
import com.baosight.common.oldrest.RestError;
import com.baosight.common.utils.FileUtils;
import com.baosight.common.utils.JsonUtils;
import com.baosight.common.utils.RedisUtils;
import com.baosight.df.designer.entity.DesignPageBean;
import com.baosight.df.designer.entity.PageBean;
import com.baosight.df.designer.entity.PageBean.PageType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Service("loadSaveService")
public class LoadSaveServiceImpl {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private HttpServletRequest request;

    @Resource(name = "designPageDao")
    protected IBaseDao<DesignPageBean> designPageDao;

    @Resource(name = "pageDao")
    protected IBaseDao<PageBean> pageDao;

    @Value("${designer.delivery_pkg}")
    private String delivery_pkg;

    @Value("${jdt.wls.cache}")
    private String wlsCache;

    @Transactional
    public ErrorInfo NewDesignPage(DesignPageBean designPage) {

        PageBean page = new PageBean(designPage.getPagename(),
                designPage.getPage_cname(), designPage.getPage_desc(),
                PageType.designPage);
        try {
            this.pageDao.insert("insertPage", page);
            String pkg = designPage.getDelivery_pkg();
            designPage.setDelivery_pkg((pkg == null) ? delivery_pkg : pkg);
            this.designPageDao.insert("insertDesignPage", designPage);
            String pagename = designPage.getPagename();
            String wlsKey = com.baosight.aas.auth.Constants.JDT_REDIS_ROOT + ":" + com.baosight.aas.auth.Constants.JDT_REDIS_WLS_ROOT + ":" + request.getSession().getAttribute(
                    Constants.SESSION_TENANT_KEY) + ":" + pagename;
            redisUtils.set(wlsKey, JSON.toJSONString(designPage));
            redisUtils.expire(wlsKey, 604800);//一周7天
        } catch (Exception e) {
            e.printStackTrace();
            // throw new RuntimeException(e.getMessage());
            return new ErrorInfo(-1, "数据库插入异常，请查看服务器Log获取详细信息。");// e.getMessage());
        }

        return new ErrorInfo(ErrorInfo.SUCCESS);
    }

    @Transactional
    public ErrorInfo SaveDesignPage(DesignPageBean designPage) {

        int rows = 0;
        try {
            designPage.setDelivery_pkg(delivery_pkg);
            rows = this.designPageDao.update("updateDesignPage", designPage);
            String pagename = designPage.getPagename();
            String wlsKey = com.baosight.aas.auth.Constants.JDT_REDIS_ROOT + ":" + com.baosight.aas.auth.Constants.JDT_REDIS_WLS_ROOT + ":" + request.getSession().getAttribute(
                    Constants.SESSION_TENANT_KEY) + ":" + pagename;
            redisUtils.set(wlsKey, JSON.toJSONString(designPage));
            redisUtils.expire(wlsKey, 604800);//一周7天
            if (rows <= 0) {
                int insertRow = this.designPageDao.insert("insertDesignPage", designPage);
                if(insertRow <= 0){
                    return new ErrorInfo(-1, "未更新任何数据，该页面名在设计器表中可能不存在！");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            // throw new RuntimeException(e.getMessage());
            return new ErrorInfo(-1, e.getMessage());
        }

        return new ErrorInfo(ErrorInfo.SUCCESS);
    }

    public DesignPageBean LoadDesignPage(String pagename) throws RestError {

        DesignPageBean designPage = new DesignPageBean();
        String wlsKey = com.baosight.aas.auth.Constants.JDT_REDIS_ROOT + ":" + com.baosight.aas.auth.Constants.JDT_REDIS_WLS_ROOT + ":" + request.getSession().getAttribute(
                Constants.SESSION_TENANT_KEY) + ":" + pagename;
        if("true".equals(wlsCache)){
            String pageContentFromRedis = redisUtils.get(wlsKey);
            if (pageContentFromRedis != null) {
                DesignPageBean retDesignPageBean = JSON.parseObject(pageContentFromRedis, new TypeReference<DesignPageBean>() {
                });
                return retDesignPageBean;
            }
        }
        designPage.setPagename(pagename);
        DesignPageBean retDesignPageBean = (DesignPageBean) this.designPageDao
                .queryOne("getDesignPageByName", designPage);
        redisUtils.set(wlsKey, JSON.toJSONString(retDesignPageBean));
        redisUtils.expire(wlsKey, 604800);//一周7天
        return retDesignPageBean;
    }

    public String LoadPreviewPage(DesignPageBean designPage) {
        String ret = new String(designPage.getHtmlstring());
        int index = ret.indexOf("</head>");
        if (index != -1) {
            String head = ret.substring(0, index);
            String js = designPage.getJsstring();
            String css = designPage.getCssstring();
            String tail = ret.substring(index);
            String retString = head;

            // CSS Begin
            retString += "<style type=\"text/css\">\n";
            retString += ((css != null) ? css : "");
            retString += "\n</style>\n";
            // CSS End

            // JavaScript Begin
            retString += "<script type=\"text/javascript\">\n";
            retString += ((js != null) ? js : "");
            retString += "\n</script>\n";
            // JavaScript End

            retString += tail;
            return retString;
        }

        return designPage.getHtmlstring();
    }

    public String LoadPublishPage(String pagename) {

        DesignPageBean designPage = null;
        try {
            designPage = LoadDesignPage(pagename);
        } catch (RestError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "the html page load error";
        }

        String ret = new String(designPage.getHtmlstring());
        int index = ret.indexOf("</head>");
        if (index != -1) {
            String head = ret.substring(0, index);

            String tail = ret.substring(index);
            String retString = head;

            // CSS Begin
            retString += "<link rel=\"stylesheet\" href=\"loadsave.do?method=publishcss&pagename=";
            retString += pagename;
            retString += "\"></link>\n";
            // CSS End

            // JavaScript Begin
            retString += "<script type=\"text/javascript\" charset=\"utf-8\" src=\"loadsave.do?method=publishjs&pagename=";
            retString += pagename;
            retString += "\"></script>\n";
            // JavaScript End

            retString += tail;
            return retString;
        }

        return designPage.getHtmlstring();

    }

    /**
     * 查询
     *
     * @param paramInfo
     * @return returnInfo
     */
    public List<DesignPageBean> query(DesignPageBean paramInfo) {
        List<DesignPageBean> returnInfo = this.designPageDao.query(
                "getDesignPages", paramInfo);
        return returnInfo;
    }

    public JSONObject upgrade(JSONObject paramInfo) {
        JSONObject resultObj = new JSONObject();
        DesignPageBean param = new DesignPageBean();
        param.setPagename(paramInfo.getString("pageName"));
        DesignPageBean retDesignPageBean = (DesignPageBean) this.designPageDao
                .queryOne("getDesignPageByName", param);
        if (null != retDesignPageBean) {
            String htmlString = retDesignPageBean.getHtmlstring();
            JSONObject widgetObj = null;
            try {
                widgetObj = JsonUtils
                        .convertStream2Json(FileUtils.getFileInput(
                                "df/designer/controllers", "widgets.json"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                resultObj.put("status", Constants.EXECUTE_FAIL);
                resultObj.put("returnMsg", e.toString());
            }
            JSONArray groupObjLst = widgetObj.getJSONArray("groups");
            /* 以下代码开始处理升级 */
            Document doc = Jsoup.parse(htmlString);// 解析html
            Elements docChildren = doc.body().children();
            if (docChildren.size() <= 0) {
                resultObj.put("status", Constants.EXECUTE_SUCCESS);
                resultObj.put("htmlstring", "");
                return resultObj;
            }
            Elements widgetDivLst = doc.getElementsByAttribute("data-id");
            for (int i = 0; i < widgetDivLst.size(); i++) {
                Element curElement = widgetDivLst.get(i);
                String widgetBaseId = curElement.attr("data-id");
                for (int j = 0; j < groupObjLst.size(); j++) {
                    JSONObject groupObj = groupObjLst.getJSONObject(j);
                    JSONArray widgets = groupObj.getJSONArray("widgets");
                    for (int k = 0; k < widgets.size(); k++) {
                        JSONObject oneWidget = widgets.getJSONObject(k);
                        if (oneWidget.getString("type").equals(
                                getChartsTypeForUpdate(curElement
                                        .attr("data-type")))) {// type类型匹配一致，开始替换
                            try {
                                upgradeElement(curElement, oneWidget,
                                        widgetBaseId);// 执行元素升级替换
                            } catch (IOException e) {
                                e.printStackTrace();
                                resultObj.put("status", Constants.EXECUTE_FAIL);
                                resultObj.put("returnMsg", e.toString());
                            }
                        }
                    }
                }
            }
            resultObj.put("status", Constants.EXECUTE_SUCCESS);
            StringBuffer htmlStr = new StringBuffer();
            Elements children = doc.body().children();
            for (Element child : children) {
                htmlStr.append(child.toString());
            }
            resultObj.put("htmlstring", htmlStr.toString());
            resultObj.put("jsstring", retDesignPageBean.getJsstring());
            resultObj.put("cssstring", retDesignPageBean.getCssstring());
        }
        return resultObj;
    }

    private String getChartsTypeForUpdate(String oldType) {// 由于charts的type名字更换过，所以升级时需要考虑替换
        if ("bxchartsbarstand".equals(oldType)) {
            return "chartCol";
        } else if ("bxchartsbarstack".equals(oldType)) {
            return "chartColT";
        } else if ("bxchartsbarstandhorizontal".equals(oldType)) {
            return "chartBar";
        } else if ("bxchartsbarstandhorizontalstack".equals(oldType)) {
            return "chartBarT";
        } else if ("bxchartslinestand".equals(oldType)) {
            return "chartLine";
        } else if ("bxchartslinestack".equals(oldType)) {
            return "chartLineT";
        } else if ("bxchartslinesmooth".equals(oldType)) {
            return "chartLineS";
        } else if ("bxchartslinearea".equals(oldType)) {
            return "chartArea";
        } else if ("bxchartslineareastack".equals(oldType)) {
            return "chartAreaT";
        } else if ("bxchartspiestand".equals(oldType)) {
            return "chartPie";
        } else if ("bxchartspiecircular".equals(oldType)) {
            return "chartPieC";
        } else if ("bxchartsgaugestand".equals(oldType)) {
            return "chartGauge";
        } else if ("bxchartsmapstand".equals(oldType)) {
            return "chartMap";
        } else if ("bxchartsradarstand".equals(oldType)) {
            return "chartRadar";
        } else if ("bxchartsradarfill".equals(oldType)) {
            return "chartRadarF";
        } else if ("bxchartsscatterstand".equals(oldType)) {
            return "chartScat";
        } else {
            return oldType;
        }
    }

    private void upgradeElement(Element curElement, JSONObject oneWidget,
                                String widgetBaseId) throws IOException {
        String htmlPath = oneWidget.getString("html");
        Document widgetHtml = Jsoup.parse(new File(FileUtils.getWebappsPath()
                + "df/designer/" + htmlPath), "UTF-8");
        String[] upgradeViewName = new String[]{"view", "configView",
                "dataConfigView"};
        for (int i = 0; i < upgradeViewName.length; i++) {
            Element view = widgetHtml.select("." + upgradeViewName[i]).first();
            Element viewBeReplaced = curElement
                    .select("." + upgradeViewName[i]).first();
            if ("dataConfigView".equals(upgradeViewName[i])) {// 由于目前组件只有dataConfigView涉及增减，所以只需要处理dataConfigView
                if (view == null && viewBeReplaced != null) {// 删除dataConfigView元素
                    viewBeReplaced.remove();
                } else if (view != null && viewBeReplaced == null) {// 将新增的dataConfigView元素增加到configView后面
                    Element configViewEle = curElement.select(".configView")
                            .first();
                    configViewEle.after(view);
                    Elements elesInView = view.select("*[id]");
                    for (int j = 0; j < elesInView.size(); j++) {// 替换dataConfigView下面id的值
                        Element oneEle = elesInView.get(j);
                        String oldId = oneEle.attr("id");
                        oneEle.attr("id", widgetBaseId + "$$" + oldId
                                + "$ready");
                    }
                    view.attr("style", "display:none");
                } else if (view != null && viewBeReplaced != null) {// 进行元素替换
                    replaceContentInView(view, viewBeReplaced, widgetBaseId);
                }
            } else {// 进行元素替换
                replaceContentInView(view, viewBeReplaced, widgetBaseId);
            }
        }
    }

    private void replaceContentInView(Element view, Element viewBeReplaced,
                                      String widgetBaseId) {
        String className = view.attr("class");
        viewBeReplaced.html(view.html());// 先替换html内容
        Elements replacedInViewNew = viewBeReplaced.select("*[id]");
        for (int i = 0; i < replacedInViewNew.size(); i++) {// 替换id的值
            Element oneElementInReplaced = replacedInViewNew.get(i);
            String oldId = oneElementInReplaced.attr("id");
            if ("view".equals(className)) {
                oneElementInReplaced.attr("id", "Store" + widgetBaseId);
            } else {
                oneElementInReplaced.attr("id", widgetBaseId + "$$" + oldId
                        + "$ready");
            }
        }
    }
}
