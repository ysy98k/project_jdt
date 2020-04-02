package com.baosight.df.designer.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.aas.beans.ErrorInfo;
import com.baosight.common.basic.dao.RestDao;
import com.baosight.common.constants.Constants;
import com.baosight.common.oldrest.RestClient;
import com.baosight.common.utils.DateUtils;
import com.baosight.df.designer.entity.DesignPageBean;
import com.baosight.df.designer.service.LoadSaveServiceImpl;
import com.baosight.df.metamanage.controller.PageManageController;
import com.baosight.xinsight.constant.AasKeyConstants;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/*
* create by chenxi_178996@baosight.com
* 文件服务类
* 提供xml文件的上传、下载，设置器页面文件的操作功能
* */
@Controller
@RequestMapping("/df/designer/fileserver.do")
public class FileServerController {
    private static final String PAGE_ROOT = "desingerPages";
    private static final String PAGE_NODE = "page";
    private static final String PAGE_NODE_NAME = "pagename";
    private static final String PAGE_NODE_CNAME = "page_cname";
    private static final String PAGE_NODE_DESC = "page_desc";
    private static final String PAGE_NODE_HTML = "htmlstring";
    private static final String PAGE_NODE_JS = "jsstring";
    private static final String PAGE_NODE_CSS = "cssstring";
    private static final String PAGE_NODE_PKG = "delivery_pkg";
    private static final String DEFAULT_PKG = "1.0_DP06";

    private String api = "/api/resource";

    @Value("${service.name}")
    private String serviceName;
    @Value("${aas.host}")
    private String aasAddress;
    @Value("${aas.rest_service_name}")
    private String aasAppPath;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    LoadSaveServiceImpl loadSaveService;
    @Autowired
    private RestDao dao;

    private static final Logger logger = LoggerFactory
            .getLogger(PageManageController.class);

    /*
   * 导出设计页面为xml
   * */
    @RequestMapping(value = "exportDesinger.do", method = RequestMethod.GET)
    public void exportDesingerPages(HttpServletRequest request,
                                    HttpServletResponse response) throws IOException {
        try {
            String pageArray[] = request.getParameter("pagelist").split(",");
            List<String> pageList = Arrays.asList(pageArray); //获得导出列表

            Document doc = DocumentHelper.createDocument();
            Element desingerPages = doc.addElement(PAGE_ROOT);
            List<DesignPageBean> designPageBeanList = loadSaveService.query(new DesignPageBean());
            for (int i = 0; i < designPageBeanList.size(); i++) {
                DesignPageBean pageBean = designPageBeanList.get(i);
                if (!pageList.contains(pageBean.getPagename())) continue;

                Element page = desingerPages.addElement(PAGE_NODE);
                page.addElement(PAGE_NODE_NAME).addText(pageBean.getPagename());
                if (null != pageBean.getPage_cname())
                    page.addElement(PAGE_NODE_CNAME).addText(pageBean.getPage_cname());
                if (null != pageBean.getPage_desc())
                    page.addElement(PAGE_NODE_DESC).addText(pageBean.getPage_desc());
                if (null != pageBean.getHtmlstring())
                    page.addElement(PAGE_NODE_HTML).addText(pageBean.getHtmlstring());
                if (null != pageBean.getJsstring())
                    page.addElement(PAGE_NODE_JS).addText(pageBean.getJsstring().replaceAll("\n", "#CRLF#"));
                if (null != pageBean.getCssstring())
                    page.addElement(PAGE_NODE_CSS).addText(pageBean.getCssstring());
                if (null != pageBean.getDelivery_pkg())
                    page.addElement(PAGE_NODE_PKG).addText(pageBean.getDelivery_pkg());
            }
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("utf-8");
            //format.setTrimText(false); //保留换行和多个空格
            StringWriter writer = new StringWriter();
            XMLWriter xmlWriter = new XMLWriter(writer, format);
            xmlWriter.write(doc);
            InputStream is = new ByteArrayInputStream(writer.toString().getBytes("utf-8"));

            String fileName = "desingPages_"
                    + DateUtils.date2String("yyyyMMddHHmmss", new Date());
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename="
                    + new String((fileName + ".xml").getBytes(), "utf-8"));
            ServletOutputStream out = response.getOutputStream();
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                bis = new BufferedInputStream(is);
                bos = new BufferedOutputStream(out);
                byte[] buff = new byte[2048];
                int bytesRead;
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
            } catch (final IOException e) {
                throw e;
            } finally {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
        }
    }


    /*
    * 导入设计页面
    * */
    @RequestMapping(params = "method=importDesinger", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject importDesingerPages(@RequestParam(value = "desingerPages") MultipartFile desingerPages,
                                   HttpServletRequest request, HttpServletResponse response) {
        JSONObject resultObj = new JSONObject();
        resultObj.put("status", Constants.EXECUTE_FAIL);

        if (desingerPages == null) {
            resultObj.put("returnMsg", "未读取到文档，请重新导入！");
            return resultObj;
        }

        //导入成功后新增资源
        JSONArray resourceArray = new JSONArray();
        resourceArray.add(new JSONObject());
        resourceArray.getJSONObject(0).put("service", serviceName);
        //设置接口
        dao.setHost(aasAddress);
        dao.setServiceName(aasAppPath);
        HttpSession session = this.request.getSession();
        String token = session.getAttribute("token").toString();
        JSONObject mapIn = new JSONObject();

        try {
            InputStream inputStream = desingerPages.getInputStream();
            SAXReader saxReader = new SAXReader();
            Document doc = saxReader.read(inputStream);
            Element root = doc.getRootElement();
            List<Element> pageList = root.elements(PAGE_NODE);

            int countSuccess = 0;
            int countError = 0;
            String errorMsg = "";
            String resourcesFailMsg = "";
            for (int i = 0; i < pageList.size(); i++) {
                Element page = pageList.get(i);
                Element nameNode = page.element(PAGE_NODE_NAME);
                if (nameNode == null || nameNode.getText() == "") {
                    countError++;
                    errorMsg += String.format("&nbsp;&nbsp;第%d个页面&emsp;pagename为空<br/>", i + 1);
                    continue;
                }

                String name = nameNode.getText();
                if (loadSaveService.query(new DesignPageBean(name)).size() > 0) {
                    countError++;
                    errorMsg += String.format("&nbsp;&nbsp;第%d个页面&emsp;设计页面%s已存在<br/>", i + 1, name);
                    continue;
                }

                JSONObject pageData = new JSONObject();
                pageData.put(PAGE_NODE_NAME, name);
                for (String param : Arrays.asList(PAGE_NODE_CNAME, PAGE_NODE_DESC, PAGE_NODE_HTML, PAGE_NODE_JS, PAGE_NODE_CSS, PAGE_NODE_PKG)) {
                    Element paramNode = page.element(param);
                    String value = (paramNode != null) ? paramNode.getText() : "";
                    pageData.put(param, value);
                }
                pageData.put(PAGE_NODE_JS, pageData.getString(PAGE_NODE_JS).replaceAll("#CRLF#", "\n"));
                if(pageData.getString(PAGE_NODE_PKG) == ""){
                    pageData.put(PAGE_NODE_PKG, DEFAULT_PKG);
                }

                DesignPageBean designPage = RestClient.getJavaBean(JSONObject.toJSONString(pageData),
                        DesignPageBean.class);
                ErrorInfo errorInfo = loadSaveService.NewDesignPage(designPage);
                if (errorInfo.getErrcode() != ErrorInfo.SUCCESS) {
                    countError++;
                    errorMsg += String.format("&nbsp;&nbsp;第%d个页面&emsp;%s<br/>", i + 1, errorInfo.getErrinfo());
                    continue;
                }
                //新增资源
                resourceArray.getJSONObject(0).put("name", name);
                resourceArray.getJSONObject(0).put("display_name", pageData.getString(PAGE_NODE_CNAME));
                resourceArray.getJSONObject(0).put("discription", pageData.getString(PAGE_NODE_DESC));
                mapIn.put("resources", resourceArray);
                mapIn.put("token", token);
                JSONObject restInfo = dao.invoke("post", api, mapIn);
                if (!Constants.EXECUTE_SUCCESS.equals(restInfo.getString(AasKeyConstants.KEY_ERRCODE))){
                    resourcesFailMsg += String.format("<br/>&nbsp;&nbsp;第%d个页面&emsp;添加资源失败", i + 1);
                }
                countSuccess++;
            }

            resultObj.put("status", Constants.EXECUTE_SUCCESS);
            resultObj.put("returnMsg", "导入成功" + countSuccess + "条页面信息！" + resourcesFailMsg);
            resultObj.put("countError", countError);
            resultObj.put("errorMsg", errorMsg);

        } catch (IOException e) {
            e.printStackTrace();
            resultObj.put("returnMsg", e.toString());
        } catch (DocumentException e) {
            e.printStackTrace();
            resultObj.put("returnMsg", e.toString());
        }

        return resultObj;
    }
}
