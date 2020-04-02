package com.raising.backstage.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baosight.common.basic.entity.BaseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author ysy
 * @date 2018/1/23 10:45
 * @description
 */
public class SectionManage extends BaseEntity {

    private Integer sectionId;

    private String sectionName;

    private String owner;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String collectorName;

    private String ccsId;

    private String ccsSectionType;

    private String mapCoordinateCenter;

    private String mapCoordinateLeft;

    private String mapCoordinateRight;

    private Date updateTime;

    private String sectionNameStrict;

    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Date getCreateTime() {
        if(this.createTime != null){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = df.format(this.createTime);
            try {
                return df.parse(format);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCollectorName() {
        return collectorName;
    }

    public void setCollectorName(String collectorName) {
        this.collectorName = collectorName;
    }

    public String getCcsId() {
        return ccsId;
    }

    public void setCcsId(String ccsId) {
        this.ccsId = ccsId;
    }

    public String getCcsSectionType() {
        return ccsSectionType;
    }

    public void setCcsSectionType(String ccsSectionType) {
        this.ccsSectionType = ccsSectionType;
    }

    public String getMapCoordinateCenter() {
        return mapCoordinateCenter;
    }

    public void setMapCoordinateCenter(String mapCoordinateCenter) {
        this.mapCoordinateCenter = mapCoordinateCenter;
    }

    public String getMapCoordinateLeft() {
        return mapCoordinateLeft;
    }

    public void setMapCoordinateLeft(String mapCoordinateLeft) {
        this.mapCoordinateLeft = mapCoordinateLeft;
    }

    public String getMapCoordinateRight() {
        return mapCoordinateRight;
    }

    public void setMapCoordinateRight(String mapCoordinateRight) {
        this.mapCoordinateRight = mapCoordinateRight;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getSectionNameStrict() {
        return sectionNameStrict;
    }

    public void setSectionNameStrict(String sectionNameStrict) {
        this.sectionNameStrict = sectionNameStrict;
    }
}
