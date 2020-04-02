package com.baosight.common.message;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.df.metamanage.entity.GlobalMessage;
import com.baosight.df.metamanage.service.GlobalMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author hujinhua
 *         Histroy:
 *         2016/7/29 hujinhua Create
 */
@Component
public class DatabaseBundleMessageSource extends AbstractBundleMessageSource {
    private static final int TENANT_SIZE = 100;

    private static final int MESSAGE_SIZE = 1000;

    @Autowired
    private GlobalMessageService globalMessageService;

    private ConcurrentMap<String, ConcurrentMap<String, ConcurrentMap<String, String>>> messageCache = new ConcurrentHashMap<>(TENANT_SIZE);

    @Override
    protected ConcurrentMap<String, String> resolveMessage(String tenantName, Locale locale) {

        ConcurrentMap<String, ConcurrentMap<String, String>> map;
        if (messageCache.containsKey(tenantName)) {
            map = messageCache.get(tenantName);
        } else {
            map = queryFromDatabase(tenantName);
            ConcurrentMap<String, ConcurrentMap<String, String>> tempMap = messageCache.putIfAbsent(tenantName, map);
            if (tempMap != null) {
                map = tempMap;
            }
        }
        ConcurrentMap<String, String> messages = map.get(locale.getLanguage());
        return messages;
    }

    private ConcurrentMap<String, ConcurrentMap<String, String>> queryFromDatabase(String tenant) {
        JSONObject result = globalMessageService.query(new JSONObject());
        ConcurrentMap<String, ConcurrentMap<String, String>> map = new ConcurrentHashMap<>(MESSAGE_SIZE);
        JSONArray list;
        try {
            list = result.getJSONArray("rows");
        } catch (Exception e) {
            return map;
        }
        for (Object one : list) {
            JSONObject obj = (JSONObject) one;
            GlobalMessage globalMessage = new GlobalMessage();
            globalMessage.fromMap(obj);
            Locale locale = globalMessage.getMessageLocale();
            if (locale != null) {
                ConcurrentMap<String, String> localeMap;
                if (map.containsKey(locale.getLanguage())) {
                    localeMap = map.get(locale.getLanguage());
                } else {
                    localeMap = new ConcurrentHashMap<>();
                    map.putIfAbsent(locale.getLanguage(), localeMap);
                }
                if (globalMessage.getMessageEnable()) {
                    localeMap.putIfAbsent(globalMessage.getMessageKey(), globalMessage.getMessageValue());
                }
            }
        }
        return map;
    }

    protected ConcurrentMap<String, ConcurrentMap<String, ConcurrentMap<String, String>>> getMessageCache() {
        return this.messageCache;
    }
}
