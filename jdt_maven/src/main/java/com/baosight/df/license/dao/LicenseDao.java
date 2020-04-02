package com.baosight.df.license.dao;

import com.baosight.df.license.MyBatisRepository;
import com.baosight.df.license.entity.LicenseEntry;

import java.util.List;
import java.util.Map;

/**
 * Created by xutingting on 2017/6/13.
 */

@MyBatisRepository
public interface LicenseDao {
    /**
     * 模糊查找
     * @param record
     * @return
     */
    List<LicenseEntry> query(Map<String, Object> record);
}
