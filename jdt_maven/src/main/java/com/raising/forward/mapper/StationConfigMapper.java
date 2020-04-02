package com.raising.forward.mapper;


import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ysy
 * @date 2018/6/1 13:28
 * @description
 */
@Component
public interface StationConfigMapper {
    /**
     * 查询分页信息
     * 使用pageHelper分页
     * @param paramJson
     * @return
     */
    List<JSONObject> query(JSONObject paramJson);
}
