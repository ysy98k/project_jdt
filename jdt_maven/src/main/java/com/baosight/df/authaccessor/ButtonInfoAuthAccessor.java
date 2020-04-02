package com.baosight.df.authaccessor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.aas.auth.Constants;
import com.baosight.aas.auth.accessor.AuthInfoAccessor;
import com.baosight.aas.auth.accessor.AuthContents;
import com.baosight.aas.auth.accessor.AuthProvider;
import com.baosight.aas.auth.accessor.button.ButtonNoPermissionContents;
import com.baosight.aas.auth.mgr.AuthManager;
import com.baosight.df.metamanage.dao.ButtonManageDao;
import com.baosight.df.metamanage.entity.ButtonManage;
import com.baosight.df.metamanage.entity.ButtonManageExample;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hujinhua
 *         Histroy:
 *         2016/12/21 hujinhua Create
 */
public class ButtonInfoAuthAccessor implements AuthInfoAccessor<String, List<String>> {

    @Autowired
    private ButtonManageDao buttonManageDao;

    @Autowired
    private AuthManager authManager;

    @Value("${service.name}")
    private String serviceName;

    @Override
    public AuthContents<List<String>> getInfos(AuthProvider<String> provider, HttpServletRequest request, HttpServletResponse response) {

        String path = provider.get();

        ButtonNoPermissionContents authInfo = new ButtonNoPermissionContents();
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            ButtonManageExample example = new ButtonManageExample();
            String tenant = SecurityUtils.getSubject().getSession().getAttribute(Constants.SESSION_TENANT_KEY).toString();
            example.setTenant(tenant);
            example.createCriteria().andPagePathEqualTo(getPagePath(path, request));
            List<ButtonManage> list = buttonManageDao.selectPageButtonByExample(example);

            Map<String, ButtonResource> resourceList = new HashMap<>();

            for (ButtonManage record : list) {
                ButtonResource info = getResourceInfo(record);
                resourceList.put(info.toResourceName(), info);
            }

            checkPermission(authInfo, resourceList);
        }

        return authInfo;
    }

    private ButtonResource getResourceInfo(ButtonManage record) {
        ButtonResource info = new ButtonResource();
        info.setPageId(record.getPageId());
        info.setButtonId(record.getButtonId());
        info.setPageName(record.getPageEname());
        info.setButtonName(record.getButtonName());
        return info;
    }

    private String getPagePath(String path, HttpServletRequest request) {
        String contextPath = request.getContextPath();

        return path.replace(contextPath, "");
    }

    private void checkPermission(ButtonNoPermissionContents autoInfo, Map<String, ButtonResource> resourceList) {
        StringBuilder sb = new StringBuilder();
        for (String key : resourceList.keySet()) {
            sb.append(key).append(",");
        }
        if (sb.length() > 1)
            sb.setLength(sb.length() - 1);
        JSONObject obj = new JSONObject();
        obj.put("name", sb.toString());
        obj.put("action", "get");
        JSONObject mapOut = (JSONObject) authManager.invoke("GET", "/permission/" + serviceName, obj);
        JSONArray permission = mapOut.getJSONArray("permission");
        for (int i = 0; i < permission.size(); i++) {
            JSONObject p = permission.getJSONObject(i);
            String name = p.getString("name");
            if (!p.getBooleanValue("get")) {
                autoInfo.add(resourceList.get(name).getButtonName());
            }
            resourceList.remove(name);
        }
        for (String key : resourceList.keySet()) {
            autoInfo.add(resourceList.get(key).getButtonName());
        }
    }
}
