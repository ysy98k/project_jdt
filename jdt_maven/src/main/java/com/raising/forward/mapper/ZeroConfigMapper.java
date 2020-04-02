package com.raising.forward.mapper;

import com.alibaba.fastjson.JSONObject;
import com.raising.forward.entity.ZeroConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ysy
 * @date 2018/3/26 19:05
 * @description
 */

@Component
public interface ZeroConfigMapper {
    public List<ZeroConfig> query(@Param("cookieId") Integer cookieId);
    public void update(@Param("zeroConfig") ZeroConfig zeroConfig);
    public void insert(@Param("zeroConfig") ZeroConfig zeroConfig);
    List<JSONObject> queryJson(JSONObject jsonObject);
}
