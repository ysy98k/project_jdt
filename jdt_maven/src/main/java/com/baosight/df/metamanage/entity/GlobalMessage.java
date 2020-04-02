package com.baosight.df.metamanage.entity;

import java.util.Locale;
import java.util.Map;

import com.baosight.common.basic.entity.BaseEntity;
import com.baosight.common.utils.LocaleUtils;
import com.baosight.common.utils.NumberUtils;
import com.baosight.common.utils.StringUtils;

public class GlobalMessage extends BaseEntity {
    private Long messageId;
    private String messageKey;
    private String messageValue;
    private String messageDesc;
    private String messageLan;
    private Boolean messageEnable;
    private Locale locale;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getMessageValue() {
        return messageValue;
    }

    public void setMessageValue(String messageValue) {
        this.messageValue = messageValue;
    }

    public String getMessageDesc() {
        return messageDesc;
    }

    public void setMessageDesc(String messageDesc) {
        this.messageDesc = messageDesc;
    }

    public String getMessageLan() {
        return messageLan;
    }

    public Locale getMessageLocale() {
        if (locale == null) {
            locale = LocaleUtils.toLocale(getMessageLan());
        }
        return locale;
    }

    public void setMessageLan(String messageLan) {
        this.messageLan = messageLan;
    }

    public Boolean getMessageEnable() {
        return messageEnable;
    }

    public void setMessageEnable(Boolean messageEnable) {
        this.messageEnable = messageEnable;
    }

    public void fromMap(Map map) {
        super.fromMap(map);
        setMessageId(NumberUtils.toLong(map.get("messageId")));
        setMessageKey(StringUtils.toString(map.get("messageKey")));
        setMessageValue(StringUtils.toString(map.get("messageValue")));
        setMessageDesc(StringUtils.toString(map.get("messageDesc")));
        setMessageLan(StringUtils.toString(map.get("messageLan")));
        setMessageEnable(NumberUtils.toBoolean(map.get("messageEnable")));
    }

    public Map toMap() {
        Map map = super.toMap();
        map.put("messageId", messageId);
        map.put("messageKey", messageKey);
        map.put("messageValue", messageValue);
        map.put("messageDesc", messageDesc);
        map.put("messageLan", messageLan);
        map.put("messageEnable", messageEnable);
        return map;
    }


}