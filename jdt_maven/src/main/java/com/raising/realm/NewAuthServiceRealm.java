package com.raising.realm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baosight.aas.auth.BgAuthException;
import com.baosight.aas.auth.credentials.TokenAuthenticationInfo;
import com.baosight.aas.auth.credentials.UsernameAuthenticationInfo;
import com.baosight.aas.auth.credentials.token.TokenAuthenticationToken;
import com.baosight.aas.auth.mgr.AuthManager;
import com.baosight.aas.auth.mgr.UserInfo;
import com.baosight.xinsight.constant.Operation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

public class NewAuthServiceRealm extends AuthorizingRealm {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private AuthManager authManager;
    @Value("${service.name}")
    private String serviceName;
    private String permissionRealmName = "PermissionRealm";

    public NewAuthServiceRealm() {
    }

    public boolean supports(AuthenticationToken token) {
        return token instanceof TokenAuthenticationToken || token instanceof UsernamePasswordToken;
    }

    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        JSONObject obj = new JSONObject();
        obj.put("name", this.getResourceName(principalCollection));
        Map<String, Object> mapOut = this.authManager.invoke("GET", "/permission/" + this.serviceName, obj);
        obj = (JSONObject)mapOut;
        if (obj.getIntValue("errcode") != 0) {
            this.logger.warn("err_code: {}, err_info{}: {}", obj.getString("errcode"), obj.getString("errinfo"));
            return info;
        } else {
            JSONArray listPermissin = obj.getJSONArray("permission");

            for(int i = 0; i < listPermissin.size(); ++i) {
                JSONObject tmpPerm = listPermissin.getJSONObject(i);
                boolean isAll = true;

                for(int j = 1; j < Operation.values().length + 1; ++j) {
                    Operation operation = Operation.valueOf(j);
                    if (tmpPerm.containsKey(operation.name())) {
                        isAll = isAll && tmpPerm.getBooleanValue(operation.name());
                        if (tmpPerm.getBooleanValue(operation.name())) {
                            info.addStringPermission(tmpPerm.getString("name") + ":" + operation.name());
                        }
                    }
                }

                if (isAll) {
                    info.addStringPermission(tmpPerm.getString("name") + ":*");
                }
            }

            return info;
        }
    }

    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        Subject subject = SecurityUtils.getSubject();
        Iterator i$ = subject.getSession().getAttributeKeys().iterator();

        while(i$.hasNext()) {
            Object key = i$.next();
            subject.getSession().removeAttribute(key);
        }

        if (authenticationToken instanceof UsernamePasswordToken) {
            return this.getUsernameInfo((UsernamePasswordToken)authenticationToken);
        } else if (authenticationToken instanceof TokenAuthenticationToken) {
            return this.getTokenInfo((TokenAuthenticationToken)authenticationToken);
        } else {
            throw new BgAuthException(180012);
        }
    }

    private AuthenticationInfo getUsernameInfo(UsernamePasswordToken authenticationToken) throws AuthenticationException {
        String username = authenticationToken.getUsername();
        String password = new String(authenticationToken.getPassword());

        String[] s = username.split("@");
        String loginSignal = new String();
        String tenant = new String();
        if(s.length == 2){
            String nameOrNumber = s[0];
            //定义正则表达式判断username是否是 11位数字
            String regExp = "^1[\\d]{10}";
            boolean isPhoneNumber = Pattern.matches(regExp,nameOrNumber);
            if(isPhoneNumber){//如果是电话登录
                loginSignal = s[0]+"@"+"byPhone";
                tenant = s[1];
            }else{//如果是用户名登录
                loginSignal = s[0];
                tenant = s[1];
            }
        }else{
            loginSignal = "";
            tenant = "";
        }
        String svrToken = this.authManager.login(loginSignal, password, tenant);
        UserInfo userInfo = this.authManager.getUserInfo(svrToken);
        UsernameAuthenticationInfo info = new UsernameAuthenticationInfo(username, password, this.getName(), userInfo);
        return info;
    }

    private AuthenticationInfo getTokenInfo(TokenAuthenticationToken authenticationToken) throws AuthenticationException {
        String svrToken = (String)authenticationToken.getPrincipal();
        UserInfo userInfo = this.authManager.getUserInfo(svrToken);
        TokenAuthenticationInfo info = new TokenAuthenticationInfo(svrToken, userInfo, this.getName());
        return info;
    }

    public String getPermissionRealmName() {
        return this.permissionRealmName;
    }

    public void setPermissionRealmName(String permissionRealmName) {
        this.permissionRealmName = permissionRealmName;
    }

    private String getResourceName(PrincipalCollection principalCollection) {
        if (!(principalCollection instanceof SimplePrincipalCollection)) {
            return null;
        } else {
            String retStr = "";
            SimplePrincipalCollection principalMap = (SimplePrincipalCollection)principalCollection;
            Collection collection = principalMap.fromRealm(this.getPermissionRealmName());
            String[] permissions = (String[])((String[])collection.iterator().next());
            String[] arr$ = permissions;
            int len$ = permissions.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String permission = arr$[i$];
                String[] tmp = permission.split(":");
                retStr = retStr + tmp[0] + ",";
            }

            retStr = retStr.substring(0, retStr.length() - 1);
            return retStr;
        }
    }


}
