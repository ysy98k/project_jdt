package com.baosight.df.metamanage.entity;

import com.baosight.common.basic.entity.BaseEntity;
import com.baosight.common.utils.NumberUtils;
import com.baosight.common.utils.StringUtils;

import java.util.Map;

/**
 * Created by lusongkai on 2017/12/6.
 */
public class MediaManage extends BaseEntity {

    private Long mediaId;
    private String mediaName;
    private String mediaType;
    private String mediaDesc;
    private String mediaPath;
    private String mediaCreator;
    private String mediaCreateTime;
    private String mediaModifier;
    private String mediaModitime;

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaDesc() {
        return mediaDesc;
    }

    public void setMediaDesc(String mediaDesc) {
        this.mediaDesc = mediaDesc;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public String getMediaCreator() {
        return mediaCreator;
    }

    public void setMediaCreator(String mediaCreator) {
        this.mediaCreator = mediaCreator;
    }

    public String getMediaCreateTime() {
        return mediaCreateTime;
    }

    public void setMediaCreateTime(String mediaCreateTime) {
        this.mediaCreateTime = mediaCreateTime;
    }

    public String getMediaModifier() {
        return mediaModifier;
    }

    public void setMediaModifier(String mediaModifier) {
        this.mediaModifier = mediaModifier;
    }

    public String getMediaModitime() {
        return mediaModitime;
    }

    public void setMediaModitime(String mediaModitime) {
        this.mediaModitime = mediaModitime;
    }

    public void fromMap(Map map) {
        super.fromMap(map);
        setMediaId(NumberUtils.toLong(map.get("mediaId")));
        setMediaName(StringUtils.toString(map.get("mediaName")));
        setMediaType(StringUtils.toString(map.get("mediaType")));
        setMediaDesc(StringUtils.toString(map.get("mediaDesc")));
        setMediaPath(StringUtils.toString(map.get("mediaPath")));
        setMediaCreator(StringUtils.toString(map.get("mediaCreator")));
        setMediaCreateTime(StringUtils.toString(map.get("mediaCreateTime")));
        setMediaModifier(StringUtils.toString(map.get("mediaModifier")));
        setMediaModitime(StringUtils.toString(map.get("mediaModitime")));
    }

    public Map toMap() {
        Map map = super.toMap();
        map.put("mediaId", mediaId);
        map.put("mediaName", mediaName);
        map.put("mediaType", mediaType);
        map.put("mediaDesc", mediaDesc);
        map.put("mediaPath", mediaPath);
        map.put("mediaCreator", mediaCreator);
        map.put("mediaCreateTime", mediaCreateTime);
        map.put("mediaModifier", mediaModifier);
        map.put("mediaModitime", mediaModitime);
        return map;
    }


}
