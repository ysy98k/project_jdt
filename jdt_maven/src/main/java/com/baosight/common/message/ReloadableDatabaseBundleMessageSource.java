package com.baosight.common.message;

import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author hujinhua
 *         Histroy:
 *         2016/8/2 hujinhua Create
 */
public class ReloadableDatabaseBundleMessageSource extends DatabaseBundleMessageSource {

    private int cacheMillis;

    public void setCacheSeconds(int cacheSeconds) {
        this.cacheMillis = (cacheSeconds * 1000);
    }

    private ConcurrentMap<String, Long> refreshMap = new ConcurrentHashMap<>();

    @Override
    protected ConcurrentMap<String, String> resolveMessage(String tenantName, Locale locale) {
        if (this.refreshMap.containsKey(tenantName)) {
            Long originalTimestamp = this.refreshMap.get(tenantName);
            if (originalTimestamp == -1 || originalTimestamp > System.currentTimeMillis() - this.cacheMillis) {
                return super.resolveMessage(tenantName, locale);
            } else {
                getMessageCache().remove(tenantName);
                return resolveNewMessage(tenantName, locale);
            }
        } else {
            return resolveNewMessage(tenantName, locale);
        }
    }

    private ConcurrentMap<String, String> resolveNewMessage(String tenantName, Locale locale) {
        ConcurrentMap<String, String> retMap = super.resolveMessage(tenantName, locale);
        Long refreshTimestamp = (this.cacheMillis < 0 ? -1 : System.currentTimeMillis());
        this.refreshMap.put(tenantName, refreshTimestamp);
        return retMap;
    }
}
