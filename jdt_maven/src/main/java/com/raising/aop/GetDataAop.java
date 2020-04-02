package com.raising.aop;

import com.alibaba.fastjson.JSONObject;
import com.baosight.aas.auth.invoker.DefaultServiceInvoker;
import com.baosight.common.utils.StringUtils;
import com.raising.ccs.ResourceService;
import com.raising.forward.service.ProjectForwardService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class GetDataAop {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ProjectForwardService projectForwardService;



    /**
     * 声明切面 应用在，所有 Controller下的， 以Controller结尾的类中的，所有 public 方法
     */
    @Pointcut("execution(public * com.raising.rest.getdata.Data.data(..))")
    public void basicAuthentication() {
    }

    /**
     * 切入点执行前方法
     *
     * @param point 切入点
     */
    @Before("basicAuthentication()")
    public void checkParameter(JoinPoint point)  {
        JSONObject data = getNameAndValue(point);
        //JSONObject data = (JSONObject)param.get("ajaxParam");
        String username =  (String)data.get("username");
        String password =  (String)data.get("password");
        TreeMap<String,String> key = new TreeMap();
        key.put("username",username);
        key.put("password",password);
        Map map = (Map)redisTemplate.opsForValue().get(key.toString());
        if(map == null){//如果token失效
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            Subject subject = SecurityUtils.getSubject(); // 获取Subject单例对象
            subject.login(token); // 登陆
            Session session = subject.getSession();
            System.out.println("mts:----"+session.getId());
            System.out.println("mts-----token:"+session.getAttribute("token"));
            System.out.println("mts-----token:"+session.getAttribute("token"));
            System.out.println("mts-----org.apache.shiro.subject.support.DefaultSubjectContext_AUTHENTICATED_SESSION_KEY:"+session.getAttribute("org.apache.shiro.subject.support.DefaultSubjectContext_AUTHENTICATED_SESSION_KEY"));
            Collection<Object> attributeKeys = session.getAttributeKeys();
            for(Object a : attributeKeys){
                System.out.println("mts--:"+a.toString()+":"+session.getAttribute(a));
            }


            String groupNames = (String)session.getAttribute("groupNames");
            if(StringUtils.isNullOrEmpty(groupNames)){//如果用户校验不通过。直
                return;
            }
            List<String> collectionNames = resourceService.getCollectionNames(groupNames);
            List<JSONObject> resourcesProject = projectForwardService.getResourcesProject(collectionNames);
            List<Integer> projectIds = new ArrayList<>();
            for(JSONObject project : resourcesProject){
                Integer projectId = project.getInteger("projectId");
                projectIds.add(projectId);
            }
            List<String> disAndMileageTableNames = new ArrayList<>();
            for(String collection : collectionNames){
                String disTableName = "j_disdata_"+collection;
                String mileageTableName = "j_mileagedata_"+collection;
                disAndMileageTableNames.add(disTableName);
                disAndMileageTableNames.add(mileageTableName);
            }
            Map<String,Object> dataMap = new HashMap<>();

            dataMap.put("disAndMileageTableNames",disAndMileageTableNames);
            dataMap.put("projectIds",projectIds);
            redisTemplate.opsForValue().set(key.toString(),dataMap,24, TimeUnit.HOURS);
        }
    }

    /**
     * 获取参数Map集合
     * @param joinPoint
     * @return
     */
    JSONObject getNameAndValue(JoinPoint joinPoint) {
        JSONObject param = new JSONObject();

        Object[] paramValues = joinPoint.getArgs();
        String[] paramNames = ((CodeSignature)joinPoint.getSignature()).getParameterNames();
        if(paramValues.length > 0){
            String value = (String)paramValues[0];
            param = JSONObject.parseObject(value);
        }
        return param;
        /*for (int i = 0; i < paramNames.length; i++) {
            param.put(paramNames[i], JSONObject.parseObject((String)paramValues[i]));
        }


        return param;*/
    }
}
