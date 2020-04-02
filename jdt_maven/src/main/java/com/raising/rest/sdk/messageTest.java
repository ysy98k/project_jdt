package com.raising.rest.sdk;


import com.alibaba.fastjson.JSONObject;
import com.baosight.common.utils.RedisUtils;
import com.raising.forward.service.UserService;
import com.util.MultipleDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.UUID;

import static com.util.CSVUtils.ConstantsUtil.AASCCS_DATA_SOURCE;
import static com.util.CSVUtils.ConstantsUtil.JDT_DATA_SOURCE;

@Component
@Path("/raising")
public class messageTest {
    @Autowired
    private CCPRestSmsSDK ccpRestSmsSDK;

    @Autowired
    private CodeCreateService codeCreateService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtils redisUtils;

    @GET
    @Path("/message")
    @Produces(MediaType.APPLICATION_JSON)


    public JSONObject messageTestFunc(@QueryParam("telephone") String phone ){
        JSONObject jsb = new JSONObject();
        try {
            MultipleDataSource.setDataSourceKey(AASCCS_DATA_SOURCE);
            JSONObject checkPhone = userService.getPhone(phone);

            if(checkPhone==null)
            {
                String checkError = "此号码未注册，请检查输入是否正确，或联系管理员注册";
                jsb.put("error",checkError);
                return jsb;
            }
            jsb.put("username",checkPhone.get("username"));
        }finally {
            MultipleDataSource.setDataSourceKey(JDT_DATA_SOURCE);
        }

        String token = UUID.randomUUID().toString();
        if(redisUtils.get(token)!=null) {
            String mld = redisUtils.get(token);
        }
        redisUtils.set(token,"1",180);
        jsb.put("token",token);
//        String temp=redisUtils.get("53323025-1ddf-4e58-aaac-b5b48887d91e");

        ccpRestSmsSDK.init("app.cloopen.com", "8883");


        ccpRestSmsSDK.setAccount("8aaf07086715299301672fb853ca136d","e754ad0144724aee8339e6645acff0dc");

        ccpRestSmsSDK.setAppId("8aaf07086715299301672fb854181373");

        HashMap<String, Object> result = null;

        String code = codeCreateService.getNonce_str();
        jsb.put("code",code);
        result = ccpRestSmsSDK.sendTemplateSMS(phone,"385105",new String[] {code} );
        String status = result.get("statusCode").toString();
         if(status.equals("000000"))
            jsb.put("success","验证码已发送,请耐心等候");

        return jsb;
    }
}
