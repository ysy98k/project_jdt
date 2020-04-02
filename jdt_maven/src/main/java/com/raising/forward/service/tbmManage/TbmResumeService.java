package com.raising.forward.service.tbmManage;

import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.utils.StringUtils;
import com.common.NewBaseService;
import com.raising.forward.entity.tbmManage.TbmServiceInfo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("tbmResumeService")
public class TbmResumeService extends NewBaseService {

    public JSONObject getRows(String ajaxParam){

        JSONObject paramInfo = JSONObject.parseObject(ajaxParam);
        JSONObject returnInfo = new JSONObject();
        JSONObject inquStatus = paramInfo.getJSONObject("inqu_status");
        //数据校验
        String tbmId = inquStatus.getString("tbmId");
        inquStatus.put("tbmId",Integer.parseInt(tbmId));
        String querySql = "com.raising.forward.mapper.TbmServiceInfoMapper.getRows";
        String countSql = "com.raising.forward.mapper.TbmServiceInfoMapper.count";
        String entity = "com.raising.forward.entity.tbmManage.TbmServiceInfo";
        paramInfo.put("inqu_status", inquStatus);
        paramInfo.put(BaseService.QUERY_BLOCK, "inqu_status");
        paramInfo.put(BaseService.QUERY_SQL, querySql);
        paramInfo.put(BaseService.COUNT_SQL, countSql);
        paramInfo.put(BaseService.DAO_ENTITY, entity);
        returnInfo = baseDao.query(paramInfo);
        return returnInfo;
    }

    /**
     * 无则新增，有则修改
     * @param tbmServiceInfo
     */
    public void addRow(TbmServiceInfo tbmServiceInfo){
        String sql = "com.raising.forward.mapper.TbmServiceInfoMapper.getRow";
        tbmServiceInfo.setTenant("raising");
        List<Object> objects = sqlSessionTemplate.selectList(sql, tbmServiceInfo);
        if(objects != null && objects.size() > 0){//有则修改
            String updateSql = "com.raising.forward.mapper.TbmServiceInfoMapper.updateRow";
            int update = sqlSessionTemplate.update(updateSql, tbmServiceInfo);
            int a =1 ;
        }else{//无则新增
            String insertSql = "com.raising.forward.mapper.TbmServiceInfoMapper.addRows";
            int insert = sqlSessionTemplate.insert(insertSql,tbmServiceInfo);
        }

    }

    /**
     * 根据调价查询tbm信息。
     * 返回tbm单条记录
     * @param projectId
     * @return
     */
    public JSONObject getRowWithProjectId(Integer projectId){
        String sql = "com.raising.forward.mapper.TbmInfoMapper.getRowWithProjectId";
        JSONObject paramJson = new JSONObject();
        paramJson.put("tenant",request.getSession().getAttribute("tenant").toString());
        paramJson.put("tbmId",projectId);
        JSONObject one = sqlSessionTemplate.selectOne(sql, paramJson);
        return one;
    }


    public JSONObject getTbmByName(String tbmName){
        String sql = "com.raising.forward.mapper.TbmInfoMapper.getTbm";
        JSONObject param = new JSONObject();
        param.put("tbmNameStrict",tbmName);
        JSONObject tbm = sqlSessionTemplate.selectOne(sql,param);
        return tbm;
    }

    /**
     * 根据项目Id，获取所有数据
     * @param paramJson
     * @return
     */
    public List<JSONObject> getDataByProjectIds(JSONObject paramJson){
        String sql = "com.raising.forward.mapper.TbmInfoMapper.getAllData";
        List<JSONObject> data = sqlSessionTemplate.selectList(sql, paramJson);
        List<Integer> tbmId = new ArrayList<>();
        for(int i=data.size()-1;i>=0;i--){
            JSONObject tbm = data.get(i);
            Integer id = tbm.getInteger("id");
            if(tbmId.contains(id)){
                data.remove(i);
                continue;
            }
            tbmId.add(id);
        }
        return data;
    }

    /**
     * 根据项目id，获取最大Id
     * @param paramJson
     * @return
     */
    public Integer getMaxId(JSONObject paramJson){
        String sql = "com.raising.forward.mapper.TbmInfoMapper.getMaxId";
        Integer max = sqlSessionTemplate.selectOne(sql, paramJson);
        return max;
    }



}
