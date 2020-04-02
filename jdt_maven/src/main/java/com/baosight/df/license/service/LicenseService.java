package com.baosight.df.license.service;

import com.alibaba.fastjson.JSONObject;
import com.baosight.df.license.dao.LicenseDao;
import com.baosight.df.license.entity.LicenseEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by xutingting on 2017/6/13.
 */
@Service
public class LicenseService {
    @Autowired
    private LicenseDao LicenseDao;
    /**
     * 查询
     * @param mapIn
     * @return
     */
    public JSONObject queryLicense(JSONObject mapIn){
        JSONObject result = new JSONObject();
        List<LicenseEntry> licenseVersion = LicenseDao.query(mapIn);
        if(licenseVersion.size()>=1){
            LicenseEntry  licenseEntry = licenseVersion.get(0);
            result.put("version",licenseEntry.getVersion());
            result.put("baseLine",licenseEntry.getBaseLine());
            result.put("deliveryPkg",licenseEntry.getDeliveryPkg());
            try {
                result.put("buildDate",strToDateFormat(licenseEntry.getBuildDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     *将字符串格式yyyyMMdd的字符串转为日期，格式"yyyy-MM-dd"
     *
     * @param date 日期字符串
     * @return 返回格式化的日期
     * @throws ParseException 分析时意外地出现了错误异常
     */
    public static String strToDateFormat(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        formatter.setLenient(false);
        Date newDate= formatter.parse(date);
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(newDate);
    }
}
