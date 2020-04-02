package com.raising.forward.controller.deviceManage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.baosight.common.utils.StringUtils;
import com.common.BaseController;
import com.raising.backstage.service.TbmService;
import com.util.CSVUtils;
import com.util.ConstantsUtil;
import com.util.MultipleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/raising/forward/deviceManage/tbmAssetManage")
@RestController
public class TbmAssetManageController extends BaseController {

    @Autowired
    private TbmService tbmService;

    @RequestMapping(value = "getTbmInfos.do",method = RequestMethod.POST)
    public JSONObject updateTbm(String ajaxParam){
        //获取盾构机
        JSONObject data = tbmService.getTbmInfos(ajaxParam);
        if(data == null){
            data = new JSONObject();
            data.put("rows",new JSONArray());
            return data;
        }
        //获取盾构机当前状态，推进，还是闲置
        JSONArray detailsArray = (JSONArray)data.get("rows");
        for(int i=0;i<detailsArray.size();i++){
            JSONObject tempObj = detailsArray.getJSONObject(i);
            JSONObject param = new JSONObject();
            param.put("tbmId",tempObj.getInteger("tbmId"));
            JSONObject project = projectForwardService.getProjectStatusByTbm(param);
            String projectStatus = project.getString("status");
            if(StringUtils.isNullOrEmpty(projectStatus)){
                tempObj.put("tbmStatus","");
            }else  if("prostatus.building".equals(projectStatus)){
                tempObj.put("tbmStatus","推进");
            }else{
                tempObj.put("tbmStatus","闲置");
            }
        }

        try{
            MultipleDataSource.setDataSourceKey(ConstantsUtil.AASCCS_DATA_SOURCE);
            if(detailsArray.size() < 1){
                return data;
            }
            JSONObject one = detailsArray.getJSONObject(0);
            Map<String,String> tbmCCSId = codeItemService.getCodeNameFromSystemCode(one.getString("ccsType"));

            for(int i = 0;i<detailsArray.size();i++){
                JSONObject tempObj = detailsArray.getJSONObject(i);
                //String tbmType = codeItemService.getCodeNameFromSystemAndItem(tempObj.getString("ccsType"));
                String tbmType = tbmCCSId.get(tempObj.getString("tbmCCSId"));
                tempObj.remove("ccsType");
                tempObj.put("tbmType",tbmType);
            }

        }finally {
            //数据源切换回来，
            MultipleDataSource.setDataSourceKey(ConstantsUtil.JDT_DATA_SOURCE);
        }
        return  data;
    }
}
