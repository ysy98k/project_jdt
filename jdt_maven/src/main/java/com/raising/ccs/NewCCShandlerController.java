package com.raising.ccs;

import com.alibaba.fastjson.JSONObject;
import com.common.BaseController;
import com.util.CSVUtils;
import com.util.MultipleDataSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/raising/ccs/newCCShandler")
public class NewCCShandlerController extends BaseController {


    @RequestMapping(value = "combobox.do",method = RequestMethod.GET)
    public List<JSONObject> getComBox(String ajaxParam){
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
}
