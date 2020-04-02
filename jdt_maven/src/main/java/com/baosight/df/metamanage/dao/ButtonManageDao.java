package com.baosight.df.metamanage.dao;

import com.alibaba.fastjson.JSONObject;
import com.baosight.df.metamanage.entity.ButtonManage;
import com.baosight.df.metamanage.entity.ButtonManageExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ButtonManageDao {
    long countByExample(ButtonManageExample example);

    int delete(ButtonManage record);

    int deleteByExample(ButtonManageExample example);

    int deleteByPrimaryKey(Integer buttonId);

//    int insert(ButtonManage record);

    int insertSelective(ButtonManage record);

//    int insertByPageName(ButtonManage record);

    List<ButtonManage> selectByExample(ButtonManageExample example);

    List<ButtonManage> selectPageButtonByExample(ButtonManageExample example);

    ButtonManage selectByPrimaryKey(Integer buttonId);

    int updateByExampleSelective(@Param("record") ButtonManage record, @Param("example") ButtonManageExample example);

    int updateByExample(@Param("record") ButtonManage record, @Param("example") ButtonManageExample example);

    int updateByPrimaryKeySelective(ButtonManage record);

    int updateByPrimaryKey(ButtonManage record);

    List<JSONObject> query(ButtonManage record);

    int count(ButtonManage record);
}