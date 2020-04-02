package com.raising.forward.mapper;


import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("codeItemDao")
public interface CodeItemDao {
    /**
     * 根据fdLevelCode。查询fdItemName
     * @param fdLevelCode
     * @return
     */
    public String getFdItemName(@Param("fdLevelCode") String fdLevelCode);

    public List<JSONObject> getCCSIds(@Param("ccsIds") List<String> ccsIds);

    /**
     * 获得城市树的方法
     * 根据选择的节点，获得其下的子节点
     * @param jsonObject
     * @return
     */
    public List<JSONObject> getLineTree(JSONObject jsonObject);


    List<JSONObject> getStartManageSubTree(JSONObject jsonObject);


    List<JSONObject> getCodeListFromSystemCode(@Param("fdSystemCode")String fdSystemCode);

    List<JSONObject> getCodeNameFromSystemCode(@Param("fdSystemCode")String fdSystemCode);


}
